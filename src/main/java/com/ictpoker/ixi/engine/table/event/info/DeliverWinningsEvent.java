package com.ictpoker.ixi.engine.table.event.info;

import com.ictpoker.ixi.engine.commons.Card;
import com.ictpoker.ixi.engine.commons.SidePot;
import com.ictpoker.ixi.engine.table.Seat;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.table.event.TableEvent;
import com.ictpoker.ixi.engine.commons.Evaluation;
import com.ictpoker.ixi.engine.eval.Evaluator;
import com.ictpoker.ixi.engine.commons.Hand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DeliverWinningsEvent extends TableEvent {

    private static final Logger LOGGER = LogManager.getLogger(DeliverWinningsEvent.class);

    @Override
    public TableEvent handle(Table table) {

        // TODO: Validation that the hand has finished before delivering winnings

        if (table.getNumberOfActiveSeats() == 1) {
            Seat uncontestedWinner = table.getActiveSeats().get(0);

            final int win = table.getSeats().stream().mapToInt(Seat::getCollected).sum();

            uncontestedWinner.setStack(uncontestedWinner.getStack() + win);
            table.getSeats().forEach(seat -> seat.setCollected(0));

            LOGGER.info(String.format("%s win $%d from uncontested pot", uncontestedWinner.getPlayer().getName(), win));

            return this;
        }

        // Populate a map that contains all hands with corresponding seats
        final Map<Hand, Seat> handSeatMap = new HashMap<>();
        for (Seat activeSeat : table.getActiveSeats()) {
            final ArrayList<Card> cards = new ArrayList<>(table.getBoardCards());
            cards.addAll(activeSeat.getCards());
            handSeatMap.put(new Hand(cards), activeSeat);
        }

        // Evaluate all hands
        final List<List<Hand>> handRankings = Evaluator.rankHands(new ArrayList<>(handSeatMap.keySet()));

        // Collect all winning hands into a list with corresponding winning seat
        // Sort the winners by amount collected, this is to deliver the smallest split pot first
        final List<SimpleEntry<Seat, Hand>> winners = handRankings.stream()
                .flatMap(handsWithEqualStrength -> handsWithEqualStrength.stream()
                        .map(hand -> new SimpleEntry<>(handSeatMap.get(hand), hand))
                        .sorted(Comparator.comparingInt((SimpleEntry<Seat, Hand> a) -> a.getKey().getCollected())))
                .collect(Collectors.toList());

        // Create list of side pots
        final List<SidePot> sidePots = new ArrayList<>();
        final AtomicInteger lastSidePotSize = new AtomicInteger(0);
        winners.stream()
                .map(SimpleEntry::getKey)
                .forEach(seat -> {
                    sidePots.forEach(sidePot -> sidePot.addContestant(seat));

                    final int seatMaxWin = table.getSeats().stream()
                            .mapToInt(Seat::getCollected)
                            .reduce(0, (a, b) -> a + Math.min(seat.getCollected(), b)) - lastSidePotSize.get();

                    if (seatMaxWin > 0) {
                        final SidePot sidePot = sidePots.stream()
                                .filter(sp -> sp.getPotSize() == seatMaxWin)
                                .findFirst()
                                .orElse(new SidePot(seatMaxWin));

                        sidePot.addContestant(seat);
                        sidePots.add(sidePot);
                        lastSidePotSize.addAndGet(sidePot.getPotSize());
                    }
                }
        );

        // Distribute winnings to players
        for (List<Hand> handRanking : handRankings) {
            for (int potIndex = 0; potIndex < sidePots.size(); potIndex++) {
                final SidePot sp = sidePots.get(potIndex);

                int[] delivered = {0};

                final int splitCount = (int) handRanking.stream()
                        .filter(hand -> sp.getContestants().contains(handSeatMap.get(hand)))
                        .count();

                if (splitCount > 0) {
                    sp.setOddChip(sp.getPotSize() - sp.getPotSize() / splitCount * splitCount);
                }

                final List<Seat> contestants = handRanking.stream()
                        .filter(hand -> sp.getContestants().contains(handSeatMap.get(hand)))
                        .map(handSeatMap::get)
                        .collect(Collectors.toList());

                for (int seatIndex = 0; seatIndex < table.getSeats().size(); seatIndex++) {
                    int wrappingSeatIndex = (1 + seatIndex + table.getButtonPosition()) % table.getSeats().size();
                    Seat seat = table.getSeats().get(wrappingSeatIndex);
                    if (seat.getPlayer() != null && contestants.contains(seat)) {
                        if (sp.getPotSize() == 0) {
                            continue;
                        }

                        int win = sp.getPotSize() / splitCount;
                        delivered[0] += win;

                        if (sp.getOddChip() > 0) {
                            win += sp.getOddChip();
                            sp.setOddChip(0);
                        }

                        seat.setStack(seat.getStack() + win);

                        final ArrayList<Card> cards = new ArrayList<>(table.getBoardCards());
                        cards.addAll(seat.getCards());
                        try {
                            final Evaluation evaluation = new Evaluation(new Hand(cards));

                            LOGGER.info(String.format("%s win $%d from pot #%d with hand [%s]",
                                    seat.getPlayer().getName(),
                                    win,
                                    potIndex,
                                    evaluation));
                        } catch (Exception e) {
                            // TODO: Unhandled exception
                            LOGGER.warn(e);
                        }
                    }
                }

                sp.setPotSize(sp.getPotSize() - delivered[0]);
            }
        }

        table.getSeats().forEach(seat -> seat.setCollected(0));

        return this;
    }
}
