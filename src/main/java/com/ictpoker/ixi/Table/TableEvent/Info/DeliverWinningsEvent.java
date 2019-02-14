package com.ictpoker.ixi.Table.TableEvent.Info;

import com.google.common.collect.Maps;
import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Commons.SidePot;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;
import com.ictpoker.ixi.eval.Evaluation;
import com.ictpoker.ixi.eval.Evaluator;
import com.ictpoker.ixi.eval.Hand;

import java.util.*;
import java.util.stream.Collectors;

public class DeliverWinningsEvent extends TableEvent {

    @Override
    public TableEvent handle(Table table) throws TableEventException {

        // TODO: Validation that the hand has finished before delivering winnings

        if (table.getNumberOfActiveSeats() == 1) {
            Seat uncontestedWinner = table.getActiveSeats().get(0);

            final int win = table.getSeats().stream()
                    .mapToInt(Seat::getCollected)
                    .reduce(0, (a, b) -> a + b);

            uncontestedWinner.setStack(uncontestedWinner.getStack() + win);
            table.getSeats().forEach(seat -> seat.setCollected(0));

            log(String.format("%s win $%d from uncontested pot", uncontestedWinner.getPlayer().getName(), win));

            return this;
        }

        // Populate a map that contains all hands with corresponding seats
        final Map<Hand, Seat> handSeatMap = new HashMap<>();
        for (Seat activeSeat : table.getActiveSeats()) {
            final ArrayList<Card> cards = new ArrayList<>(table.getBoardCards());
            cards.addAll(activeSeat.getCards());

            try {
                handSeatMap.put(new Hand(cards), activeSeat);
            } catch (Exception e) {
                // TODO: Handle exception
                e.printStackTrace();
            }
        }

        // Evaluate all hands
        final List<List<Hand>> handRankings =
                Evaluator.rankHands(Evaluator.evaluateHands(new ArrayList<>(handSeatMap.keySet())));

        final List<Map.Entry<Seat, Hand>> winners = new ArrayList<>();
        for (List<Hand> handsWithEqualStrength : handRankings) {

            // Collect all winning hands into a list with corresponding winning seat
            for (Hand winningHand : handsWithEqualStrength) {
                Seat winningSeat = handSeatMap.get(winningHand);
                winners.add(Maps.immutableEntry(winningSeat, winningHand));
            }

            // Sort the winners by amount collected, this is to deliver the smallest split pot first
            winners.sort((Map.Entry<Seat, Hand> a, Map.Entry<Seat, Hand> b) -> {
                if (a.getKey().getCollected() > b.getKey().getCollected()) {
                    return 1;
                } else if (a.getKey().getCollected() < b.getKey().getCollected()) {
                    return -1;
                }
                return 0;
            });
        }


        // Create list of side pots
        final List<SidePot> sidePots = new ArrayList<>();
        int lastSidePotSize = 0;

        for (Map.Entry<Seat, Hand> seatHand : winners) {
            final Seat contestant = seatHand.getKey();

            sidePots.forEach(sidePot -> sidePot.addContestant(contestant));

            final int sidePotSize = table.getSeats().stream()
                    .mapToInt(Seat::getCollected)
                    .reduce(0, (a, b) -> a + Math.min(contestant.getCollected(), b)) - lastSidePotSize;

            final Optional<SidePot> sidePot = sidePots.stream()
                    .filter(sp -> sp.getPotSize() == sidePotSize)
                    .findFirst();

            if (sidePot.isPresent()) {
                sidePot.get().addContestant(contestant);
            } else if (sidePotSize != 0) {
                SidePot sidePot1 = new SidePot(sidePotSize);
                sidePot1.addContestant(contestant);
                sidePots.add(sidePot1);
                lastSidePotSize += sidePot1.getPotSize();
            }
        }

        for (int j=0; j<handRankings.size(); j++) {
            for (int i=0; i<sidePots.size(); i++) {
                int[] delivered = {0};
                final SidePot sp = sidePots.get(i);

                final int splitCount = (int)handRankings.get(j).stream()
                        .filter(hand -> sp.getContestants().contains(handSeatMap.get(hand)))
                        .count();

                if (splitCount > 0) {
                    sp.setOddChip(sp.getPotSize() - sp.getPotSize() / splitCount * splitCount);
                }

                final int potIndex = i;
                final List<Seat> contestants = handRankings.get(j).stream()
                        .filter(hand -> sp.getContestants().contains(handSeatMap.get(hand)))
                        .map(handSeatMap::get)
                        .collect(Collectors.toList());

                for (int seatIndex=0; seatIndex<table.getSeats().size(); seatIndex++) {
                    int wrappingSeatIndex = (1+seatIndex+table.getButtonPosition())%table.getSeats().size();
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
                            final Evaluation evaluation = new Evaluation(Evaluator.evaluate(new Hand(cards)));

                            log(String.format("%s win $%d from pot #%d with hand [%s]",
                                    seat.getPlayer().getName(),
                                    win,
                                    potIndex,
                                    evaluation));
                        } catch (Exception e) {
                            // TODO: Unhandled exception
                            e.printStackTrace();
                        }
                    }
                }

                sp.setPotSize(sp.getPotSize()-delivered[0]);
            }
        }

        table.getSeats().forEach(seat -> seat.setCollected(0));

        return this;
    }
}
