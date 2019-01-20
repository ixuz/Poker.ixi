package com.ictpoker.ixi.Table;

import com.ictpoker.ixi.Commons.Card;
import com.ictpoker.ixi.Dealer.DealerEvent.DealerEvent;
import com.ictpoker.ixi.Dealer.DealerEvent.DealerEventException;
import com.ictpoker.ixi.Dealer.DealerEvent.MoveButtonDealerEvent;
import com.ictpoker.ixi.Dealer.Deck;
import com.ictpoker.ixi.Player.IPlayer;
import com.ictpoker.ixi.Player.PlayerEvent.*;
import com.ictpoker.ixi.Table.TableEvent.ITableEvent;
import com.ictpoker.ixi.Table.TableException.*;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Table {

    public final static int MAXIMUM_SEATS = 10;
    public final static int FLOP = 3;
    public final static int TURN = 1;
    public final static int RIVER = 1;
    private final static Logger LOGGER = LogManager.getLogger(Table.class);
    private final Queue<ITableEvent> tableEventQueue = new LinkedList<>();
    private final List<Seat> seats;
    private final List<Card> boardCards = new ArrayList<>();
    private final int minimumBuyIn;
    private final int maximumBuyIn;
    private final int smallBlindAmount;
    private final int bigBlindAmount;
    private final Deck deck;
    private int buttonPosition = 0;
    private int pot = 0;
    private Seat lastRaiser = null;
    private int lastRaiseAmount;
    private Seat seatToAct;

    public Table(@NotNull final int nSeats,
                 @NotNull final int minimumBuyIn,
                 @NotNull final int maximumBuyIn,
                 @NotNull final int smallBlindAmount,
                 @NotNull final int bigBlindAmount)
            throws InvalidSeatCountException, TableException {

        if (nSeats <= 1 || nSeats > MAXIMUM_SEATS) {
            throw new InvalidSeatCountException();
        }

        try {
            this.deck = new Deck();
        } catch (Deck.DuplicateCardException e) {
            throw new TableException("Failed to create deck");
        }

        this.seats = new ArrayList<>();
        for (int i=0; i<nSeats; i++) {
            seats.add(new Seat());
        }

        this.minimumBuyIn = minimumBuyIn;
        this.maximumBuyIn = maximumBuyIn;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;

        setLastRaiseAmount(getBigBlindAmount());

        LOGGER.info(toString());
    }

    public void update()
            throws TableException {

        try {
            handleEventQueue();
        } catch (DealerEventException e) {
            throw new TableException("Failed to handle DealerEvent");
        } catch (PlayerEventException e) {
            throw new TableException("Failed to handle PlayerEvent");
        }
    }

    public void pushEvent(@NotNull final ITableEvent tableEvent) {

        tableEventQueue.add(tableEvent);
    }

    public int getPlayerSeatIndex(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        return seats.indexOf(getSeat(player));
    }

    private void handleEventQueue()
            throws DealerEventException, PlayerEventException, TableException {

        while (!tableEventQueue.isEmpty()) {
            final ITableEvent tableEvent = tableEventQueue.remove();

            if (tableEvent instanceof PlayerEvent) {
                final PlayerEvent playerEvent = (PlayerEvent) tableEvent;
                handlePlayerEvent(playerEvent);
            }
            if (tableEvent instanceof DealerEvent) {
                final DealerEvent dealerEvent = (DealerEvent) tableEvent;
                handleDealerEvent(dealerEvent);
            }
        }
    }

    private void handleDealerEvent(@NotNull final DealerEvent dealerEvent)
            throws DealerEventException, TableException {

        switch (dealerEvent.getDealerAction()) {
            case MOVE_BUTTON:
                try {
                    handleMoveDealerButton((MoveButtonDealerEvent) dealerEvent);
                } catch (BettingRoundFinishedException e) {
                    // TODO: Unhandled exception
                    e.printStackTrace();
                }
                break;
            case DEAL:
                break;
        }
    }

    private void handlePlayerEvent(@NotNull final PlayerEvent playerEvent)
            throws PlayerEventException, TableException {

        switch (playerEvent.getPlayerAction()) {
            case JOIN:
                try {
                    handleJoin((JoinPlayerEvent) playerEvent);
                } catch (InvalidSeatException e) {
                    throw new PlayerEventException("Could not join the table", e);
                } catch (PlayerAlreadySeatedException e) {
                    throw new PlayerEventException("The player is already seated at this table", e);
                } catch (InsufficientBalanceException e) {
                    throw new PlayerEventException("The player has insufficient balance", e);
                } catch (InvalidBuyInException e) {
                    throw new PlayerEventException("Invalid player buy-in to table", e);
                }
                return;
            case LEAVE:
                try {
                    handleLeave((LeavePlayerEvent) playerEvent);
                } catch (PlayerNotSeatedException e) {
                    throw new PlayerEventException("Could not leave the table", e);
                }
                return;
        }

        try {
            switch (playerEvent.getPlayerAction()) {
                case CHECK:
                case CALL:
                case BET:
                case RAISE:
                    if (playerEvent instanceof CommitPlayerEvent) {
                        try {
                            handleCommit((CommitPlayerEvent) playerEvent);
                        } catch (PlayerNotSeatedException e) {
                            throw new PlayerEventException("The player is not seated at this table", e);
                        } catch (TableException e) {
                            throw new PlayerEventException("Failed to call", e);
                        }
                    }
                    break;
                case SMALL_BLIND:
                    try {
                        handleSmallBlind((SmallBlindPlayerEvent) playerEvent);
                    } catch (PlayerNotSeatedException e) {
                        throw new PlayerEventException("The player is not seated at this table", e);
                    } catch (TableException e) {
                        throw new PlayerEventException("Failed to post small blind", e);
                    }
                    break;
                case BIG_BLIND:
                    try {
                        handleBigBlind((BigBlindPlayerEvent) playerEvent);
                    } catch (PlayerNotSeatedException e) {
                        throw new PlayerEventException("The player is not seated at this table", e);
                    } catch (TableException e) {
                        throw new PlayerEventException("Failed to post big blind", e);
                    }
                    break;
                case FOLD:
                    try {
                        handleFold((FoldPlayerEvent) playerEvent);
                    } catch (PlayerNotSeatedException e) {
                        throw new PlayerEventException("The player is not seated at this table", e);
                    } catch (TableException e) {
                        throw new PlayerEventException("Failed to fold", e);
                    }
                    break;
            }
        } catch (BettingRoundFinishedException e) {
            LOGGER.info("Betting round has finished");

            if (getNumberOfActiveSeats() > 1) {
                if (hasAllSeatsActed() && boardCards.size() == 0) {
                    LOGGER.info("Dealing flop");
                    boardCards.add(deck.draw());
                    boardCards.add(deck.draw());
                    boardCards.add(deck.draw());

                    for (Seat seat : seats) {
                        seat.setActed(false);
                    }

                    try {
                        setSeatToAct(getNextSeatToAct(buttonPosition, 0));
                    } catch (InvalidSeatException e1) {
                        throw new TableException("Failed to find next player to act", e1);
                    } catch (BettingRoundFinishedException e1) {
                        LOGGER.info("Betting round has finished");
                    }
                }

                if (hasAllSeatsActed() && boardCards.size() == FLOP) {
                    LOGGER.info("Dealing turn");
                    boardCards.add(deck.draw());

                    for (Seat seat : seats) {
                        seat.setActed(false);
                    }

                    try {
                        setSeatToAct(getNextSeatToAct(buttonPosition, 0));
                    } catch (InvalidSeatException e1) {
                        throw new TableException("Failed to find next player to act", e1);
                    } catch (BettingRoundFinishedException e1) {
                        LOGGER.info("Betting round has finished");
                    }
                }

                if (hasAllSeatsActed() && boardCards.size() == FLOP+TURN) {
                    LOGGER.info("Dealing river");
                    boardCards.add(deck.draw());

                    for (Seat seat : seats) {
                        seat.setActed(false);
                    }

                    try {
                        setSeatToAct(getNextSeatToAct(buttonPosition, 0));
                    } catch (InvalidSeatException e1) {
                        throw new TableException("Failed to find next player to act", e1);
                    } catch (BettingRoundFinishedException e1) {
                        LOGGER.info("Betting round has finished");
                    }
                }

                if (boardCards.size() == FLOP+TURN+RIVER) {
                    LOGGER.info("Hand finished");
                }
            } else {
                LOGGER.info("Hand finished, no contestants for the pot");
            }
        }
    }

    private void handleJoin(@NotNull final JoinPlayerEvent joinPlayerEvent)
            throws InvalidSeatException, PlayerAlreadySeatedException,
            InsufficientBalanceException, InvalidBuyInException {

        try {
            if (getSeat(joinPlayerEvent.getPlayer()) != null) {
                throw new PlayerAlreadySeatedException();
            }
        } catch (PlayerNotSeatedException e) {
            // Intended exception thrown, the player must not already be seated to join a table.
        }

        if (!isSeatOccupied(joinPlayerEvent.getSeatIndex())) {

            if (joinPlayerEvent.getAmount() < getMinimumBuyIn() || joinPlayerEvent.getAmount() > getMaximumBuyIn()) {
                throw new InvalidBuyInException();
            }

            seats.set(joinPlayerEvent.getSeatIndex(),
                    new Seat(joinPlayerEvent.getPlayer(),
                            joinPlayerEvent.getPlayer().deductBalance(joinPlayerEvent.getAmount())));

            LOGGER.info(String.format("%s joined the table at seat #%d with stack %d",
                    joinPlayerEvent.getPlayer().getName(),
                    joinPlayerEvent.getSeatIndex(),
                    joinPlayerEvent.getAmount()));
        } else {
            throw new InvalidSeatException("The seat is already occupied");
        }
    }

    private void handleLeave(@NotNull final LeavePlayerEvent leavePlayerEvent)
            throws PlayerNotSeatedException {

        final Seat seat = getSeat(leavePlayerEvent.getPlayer());
        seats.set(seats.indexOf(seat), new Seat());
    }

    private void handleMoveDealerButton(@NotNull final MoveButtonDealerEvent moveButtonDealerEvent)
            throws DealerEventException, TableException, BettingRoundFinishedException {

        final int seatIndex = moveButtonDealerEvent.getDestinationSeatIndex();
        if (seatIndex < 0 || seatIndex >= seats.size()) {
            throw new DealerEventException("Can't move button to a non-existant seat");
        }

        buttonPosition = seatIndex;
        LOGGER.info(String.format("Moved dealer button to seat #%s", buttonPosition));

        try {
            setSeatToAct(getNextSeatToAct(buttonPosition, 0));
        } catch (InvalidSeatException e) {
            throw new TableException("Failed to find next player to act");
        }
    }

    private void handleSmallBlind(@NotNull final SmallBlindPlayerEvent smallBlindPlayerEvent)
            throws PlayerNotSeatedException, TableException, BettingRoundFinishedException {

        final IPlayer player = smallBlindPlayerEvent.getPlayer();
        final Seat seat = getSeat(player);
        final int committed = Math.min(seat.getStack(), getSmallBlindAmount());
        seat.setStack(seat.getStack()-committed);
        seat.setCommitted(seat.getCommitted()+committed);
        LOGGER.info(String.format("%s posted small blind: %d", player.getName(), committed));

        try {
            setSeatToAct(getNextSeatToAct(getSeatToAct(), 0));
        } catch (InvalidSeatException e) {
            throw new TableException("Failed to find next player to act");
        }
    }

    private void handleBigBlind(@NotNull final BigBlindPlayerEvent bigBlindPlayerEvent)
            throws PlayerNotSeatedException, TableException, BettingRoundFinishedException {

        final IPlayer player = bigBlindPlayerEvent.getPlayer();
        final Seat seat = getSeat(player);
        final int committed = Math.min(seat.getStack(), getBigBlindAmount());
        seat.setStack(seat.getStack()-committed);
        seat.setCommitted(seat.getCommitted()+committed);
        LOGGER.info(String.format("%s posted big blind: %d", player.getName(), committed));

        try {
            setSeatToAct(getNextSeatToAct(getSeatToAct(), 0));
        } catch (InvalidSeatException e) {
            throw new TableException("Failed to find next player to act");
        }
    }

    private void handleCommit(@NotNull final CommitPlayerEvent commitPlayerEvent)
            throws PlayerNotSeatedException, TableException, BettingRoundFinishedException, PlayerEventException {

        if (commitPlayerEvent instanceof CallPlayerEvent) {
            handleCall((CallPlayerEvent) commitPlayerEvent);
        }
        if (commitPlayerEvent instanceof CheckPlayerEvent) {
            handleCheck((CheckPlayerEvent) commitPlayerEvent);
        }
        if (commitPlayerEvent instanceof BetPlayerEvent || commitPlayerEvent instanceof RaisePlayerEvent) {
            handleBetOrRaise(commitPlayerEvent);
        }
    }

    private void handleCall(@NotNull final CallPlayerEvent callPlayerEvent)
            throws PlayerNotSeatedException, TableException, BettingRoundFinishedException {

        final IPlayer player = callPlayerEvent.getPlayer();
        final Seat seat = getSeat(player);
        final int requiredAmount = Math.min(seat.getStack(), getHighestCommitAmount() - seat.getCommitted());

        seat.setStack(seat.getStack()-requiredAmount);
        seat.setCommitted(seat.getCommitted()+requiredAmount);
        seat.setActed(true);

        if (seat.getStack() == 0) {
            LOGGER.info(String.format("%s called %d and is all-in", player.getName(), requiredAmount));
        } else {
            LOGGER.info(String.format("%s called %d", player.getName(), requiredAmount));
        }

        try {
            setSeatToAct(getNextSeatToAct(getSeatToAct(), 0));
        } catch (InvalidSeatException e) {
            throw new BettingRoundFinishedException();
        }
    }

    private void handleBetOrRaise(@NotNull final CommitPlayerEvent betPlayerEvent)
            throws PlayerNotSeatedException, BettingRoundFinishedException, TableException, PlayerEventException {

        final IPlayer player = betPlayerEvent.getPlayer();
        final Seat seat = getSeat(player);

        final int requiredAmountToCall = Math.min(seat.getStack(), getHighestCommitAmount() - seat.getCommitted());
        LOGGER.info("required to call: " + requiredAmountToCall);
        final int requiredAmountToRaise = requiredAmountToCall + getLastRaiseAmount();
        LOGGER.info("required to raise: " + requiredAmountToRaise);

        if (requiredAmountToRaise > seat.getStack()) {
            throw new PlayerEventException("Player does not have sufficient stack to raise");
        }

        if (betPlayerEvent.getAmount() > seat.getStack()) {
            throw new PlayerEventException("Player can't raise more than the stack");
        }

        seat.setStack(seat.getStack()-betPlayerEvent.getAmount());
        seat.setCommitted(seat.getCommitted()+betPlayerEvent.getAmount());
        seat.setActed(true);

        if (seat.getStack() == 0) {
            LOGGER.info(String.format("%s raised %d and is all-in",
                    player.getName(),
                    betPlayerEvent.getAmount()));
        } else {
            LOGGER.info(String.format("%s raised %d",
                    player.getName(),
                    betPlayerEvent.getAmount()));
        }

        try {
            setSeatToAct(getNextSeatToAct(getSeatToAct(), 0));
        } catch (InvalidSeatException e) {
            throw new BettingRoundFinishedException();
        }
    }

    private void handleCheck(@NotNull final CheckPlayerEvent checkPlayerEvent)
            throws PlayerNotSeatedException, TableException, BettingRoundFinishedException {

        final IPlayer player = checkPlayerEvent.getPlayer();
        final Seat seat = getSeat(player);
        final int requiredAmount = Math.min(seat.getStack(), getHighestCommitAmount() - seat.getCommitted());

        if (requiredAmount != 0) {
            throw new TableException(String.format("%s can't check, he must commit at least %d",
                    player.getName(),
                    requiredAmount));
        }

        seat.setActed(true);

        LOGGER.info(String.format("%s checked", player.getName()));

        try {
            setSeatToAct(getNextSeatToAct(getSeatToAct(), 0));
        } catch (InvalidSeatException e) {
            throw new BettingRoundFinishedException();
        }
    }

    private void handleFold(@NotNull final FoldPlayerEvent foldPlayerEvent)
            throws PlayerNotSeatedException, BettingRoundFinishedException, TableException {

        final IPlayer player = foldPlayerEvent.getPlayer();
        final Seat seat = getSeat(player);

        seat.setActed(true);
        seat.setFolded(true);

        LOGGER.info(String.format("%s folded", player.getName()));

        try {
            setSeatToAct(getNextSeatToAct(getSeatToAct(), 0));
        } catch (InvalidSeatException e) {
            throw new BettingRoundFinishedException();
        }
    }

    private boolean isSeatOccupied(@NotNull final int seatIndex)
            throws InvalidSeatException {

        final Seat seat = getSeat(seatIndex);
        return isSeatOccupied(seat);
    }

    private boolean isSeatOccupied(@NotNull final Seat seat) {

        return (seat.getPlayer() != null);
    }

    private Seat getSeat(@NotNull final int seatIndex)
            throws InvalidSeatException {

        if (seatIndex < 0 || seatIndex >= seats.size()) {
            throw new InvalidSeatException("That seat doesn't exist at this table");
        }

        return seats.get(seatIndex);
    }

    private Seat getSeat(@NotNull final IPlayer player)
            throws PlayerNotSeatedException {

        for (final Seat seat : seats) {
            if (seat.getPlayer() == player) {
                return seat;
            }
        }

        throw new PlayerNotSeatedException();
    }

    private List<Seat> getOccupiedSeats() {

        final List<Seat> occupiedSeats = new ArrayList<>();
        for (int seatIndex=0; seatIndex<seats.size(); seatIndex++) {
            try {
                if (isSeatOccupied(seatIndex)) {
                    occupiedSeats.add(getSeat(seatIndex));
                }
            } catch (InvalidSeatException e) {
                e.printStackTrace();
            }
        }
        return occupiedSeats;
    }

    private Seat getNextSeatToAct(@NotNull final int seatIndex, @NotNull int skip)
            throws InvalidSeatException, TableException, BettingRoundFinishedException {

        for (int i=0; i<seats.size(); i++) {
            final Seat seat = getSeat((seatIndex+i+1)%seats.size());
            if (!hasSeatActed(seat)) {
                if (skip > 0) {
                    skip--;
                } else {
                    return seat;
                }
            }
        }

        throw new BettingRoundFinishedException();
    }

    private Seat getNextSeatToAct(@NotNull final Seat seat, @NotNull int skip)
            throws InvalidSeatException, TableException, BettingRoundFinishedException {

        final int seatIndex = seats.indexOf(seat);
        return getNextSeatToAct(seatIndex, skip);
    }

    private boolean hasSeatActed(@NotNull final Seat seat)
            throws TableException {

        // If the seat has already folded, true
        if (seat.hasFolded()) {
            return true;
        }

        // If the seat can't act because no stack remaining, true
        else if (seat.getStack() == 0) {
            return true;
        }

        // If the seat have not acted at all this round, false
        else if (!seat.hasActed()) {
            return false;
        }

        // If the seat has a stack remaining and haven't committed enough, false
        else if (seat.getStack() > 0 && seat.getCommitted() < getHighestCommitAmount()) {
            return false;
        }

        // If the seat has a stack remaining and committed enough, true
        else if (seat.getStack() > 0 && seat.getCommitted() == getHighestCommitAmount()) {
            return true;
        }

        throw new TableException("Could not determine if the player has to act or not");
    }

    private boolean hasAllSeatsActed() {

        int numberOfPlayersLeftToAct = 0;
        for (Seat seat : seats) {
            if (isSeatOccupied(seat) && !seat.hasActed()) {
                if (seat.getStack() > 0) {
                    numberOfPlayersLeftToAct++;
                }
            }
        }

        if (numberOfPlayersLeftToAct > 1) {
            return false;
        }
        return true;
    }

    private int getNumberOfActiveSeats() {
        int numberOfActiveSeats = 0;
        for (Seat seat : seats) {
            if (!isSeatOccupied(seat)) {
                continue;
            }
            if (seat.hasFolded()) {
                continue;
            }
            numberOfActiveSeats++;
        }
        return numberOfActiveSeats;
    }

    private int getHighestCommitAmount() {

        int highestCommitted = 0;
        for (Seat seat : seats) {
            if (seat.getCommitted() > highestCommitted) {
                highestCommitted = seat.getCommitted();
            }
        }
        return highestCommitted;
    }

    private void setLastRaiseAmount(@NotNull final int lastRaiseAmount) {

        this.lastRaiseAmount = lastRaiseAmount;
    }

    private int getLastRaiseAmount() {

        return lastRaiseAmount;
    }

    public Seat getSeatToAct() {

        return seatToAct;
    }

    public void setSeatToAct(@NotNull final Seat seatToAct) {

        this.seatToAct = seatToAct;
        LOGGER.info(String.format("%s is next to act...", seatToAct.getPlayer().getName()));
    }

    public String toString() {

        final StringBuilder sb = new StringBuilder();

        sb.append(String.format("Table (seats: %d, buy-in: %d-%d)",
                seats.size(),
                minimumBuyIn,
                maximumBuyIn));

        for (final Seat seat : getOccupiedSeats()) {
            sb.append(String.format("\n %s, stack: %d",
                    seat.getPlayer().getName(),
                    seat.getStack()));
        }

        return sb.toString();
    }

    public int getMinimumBuyIn() {

        return minimumBuyIn;
    }

    public int getMaximumBuyIn() {

        return maximumBuyIn;
    }

    public int getSmallBlindAmount() {

        return smallBlindAmount;
    }

    public int getBigBlindAmount() {

        return bigBlindAmount;
    }
}
