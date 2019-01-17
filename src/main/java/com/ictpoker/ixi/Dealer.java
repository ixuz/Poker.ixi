package com.ictpoker.ixi;

import com.ictpoker.ixi.DealerEvent.DealerEvent;
import com.ictpoker.ixi.DealerEvent.DealerEventException;
import com.ictpoker.ixi.DealerEvent.MoveButtonDealerEvent;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dealer implements IDealer {

    public final static int CARDS_PER_SEAT = 2;
    public final static int CARDS_FLOP = 3;
    public final static int CARDS_TURN = 1;
    public final static int CARDS_RIVER = 1;
    public final static float DEFAULT_DEALER_SPEED = 1f;
    public final static int DEFAULT_SMALL_BLIND_AMOUNT = 10;
    public final static int DEFAULT_BIG_BLIND_AMOUNT = 20;
    private final static Logger LOGGER = LogManager.getLogger(Dealer.class);
    private final float speed;
    private final Deck deck;
    private Seat seatToAct = null;

    public Dealer(@NotNull final float speed)
            throws PlayerEventException {

        this.speed = speed;

        try {
            deck = new Deck();
        } catch (Deck.DuplicateCardException e) {
            throw new PlayerEventException("Failed to initialize Dealer", e);
        }
    }

    public boolean update(@NotNull final Table.State state) {

        return false;
    }

    public void handleDealerEvent(@NotNull final Table table, @NotNull final DealerEvent dealerEvent)
            throws DealerEventException {

        switch (dealerEvent.getDealerAction()) {
            case MOVE_BUTTON:
                moveButton(table, ((MoveButtonDealerEvent)dealerEvent).getDestinationSeatIndex());
                break;
            case DEAL:
                dealHoleCards(table);
                break;
            case SMALL_BLIND:
                postSmallBlind(table);
                break;
            case BIG_BLIND:
                postBigBlind(table);
                break;
        }

        update(table.getState());
    }

    public void handlePlayerEvent(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        switch (playerEvent.getPlayerAction()) {
            case JOIN:
                handleJoin(table, playerEvent);
                break;
            case LEAVE:
                handleLeave(table, playerEvent);
                break;
            case CHECK:
            case CALL:
            case BET:
            case RAISE:
                handleCommit(table, playerEvent);
                break;
            case FOLD:
                handleFold(table, playerEvent);
                break;
        }

        update(table.getState());
    }

    private void handleFold(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            final Seat seat = table.getPlayerSeat(playerEvent.getPlayer());
            seat.setActed(true);
            seat.setFolded(true);

            LOGGER.info(String.format("%s folded", playerEvent.getPlayer().getName()));

            setNextPlayerToActOrFinishBettingRound(table, seat);
        } catch (Table.PlayerNotSeatedException e) {
            throw new PlayerEventException(String.format("%s is not seated at this table!",
                    playerEvent.getPlayer().getName()), e);
        }
    }

    private void handleCommit(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        try {
            final Seat seat = table.getPlayerSeat(playerEvent.getPlayer());
            if (getSeatToAct() != seat) {
                throw new PlayerEventException(String.format("It's not %s's turn to act!",
                        playerEvent.getPlayer().getName()));
            }

            int amountMinimum = table.getState().getHighestCommittedAmount() - seat.getCommitted();

            if (amountMinimum != 0 && playerEvent.getAmount() == 0) { // Desired check
                throw new PlayerEventException(String.format("%s can't check, he must commit %d more!",
                        playerEvent.getPlayer().getName(),
                        amountMinimum));
            }

            if (playerEvent.getAmount() == amountMinimum) { // Desired check/call

                if (amountMinimum == 0) { // Desired check
                    LOGGER.info(String.format("%s checked", playerEvent.getPlayer().getName()));
                } else { // Desired call
                    seat.commit(playerEvent.getAmount(), true);
                    LOGGER.info(String.format("%s called %d", playerEvent.getPlayer().getName(), amountMinimum));
                }
            }

            if (playerEvent.getAmount() > amountMinimum) { // Desired raise
                final int commit = seat.commit(playerEvent.getAmount(), true);
                final int raiseAmount = commit - amountMinimum;
                LOGGER.info(String.format("%s raised %d, pot is now %d",
                        playerEvent.getPlayer().getName(),
                        raiseAmount,
                        table.getState().getHighestCommittedAmount()));
            }

            if (playerEvent.getAmount() < amountMinimum) { // Invalid
                throw new PlayerEventException(String.format("Invalid action %s, you must at least commit %d to play",
                        playerEvent.getPlayer().getName(),
                        amountMinimum));
            }

            seat.setActed(true);

            setNextPlayerToActOrFinishBettingRound(table, seat);

            // If call amount is zero, the player can check
            // If call amount is more than zero, the player can't check
        } catch (Table.PlayerNotSeatedException e) {
            throw new PlayerEventException(String.format("%s is not seated at this table!",
                    playerEvent.getPlayer().getName()), e);
        } catch (Seat.InsufficientStackException e) {
            throw new PlayerEventException(String.format("%s has insufficient stack and can't commit %d!",
                    playerEvent.getPlayer().getName(),
                    playerEvent.getAmount()
                    ), e);
        }
    }

    protected void handleJoin(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws PlayerEventException {

        if (playerEvent.getAmount() < table.getState().getMinimumBuyIn() ||
                playerEvent.getAmount() > table.getState().getMaximumBuyIn()) {
            throw new PlayerEventException("Invalid buy in amount for this table");
        }

        try {
            playerEvent.getPlayer().deductBalance(playerEvent.getAmount());
        } catch (Player.InsufficientBalanceException e) {
            throw new com.ictpoker.ixi.PlayerEventException("Player has insufficient balance", e);
        }

        try {
            final Seat seat = table.join(playerEvent.getPlayer(), playerEvent.getAmount());
            LOGGER.info(String.format("Welcome to the table %s (stack %d)",
                    playerEvent.getPlayer().getName(),
                    seat.getStack()));
        } catch (Table.NoSeatAvailableException e) {
            throw new com.ictpoker.ixi.PlayerEventException("Sorry Sir, the table is full.", e);
        } catch (Table.PlayerAlreadySeatedException e) {
            throw new com.ictpoker.ixi.PlayerEventException("Sorry Sir, you can't occupy more than one seat.", e);
        }
    }

    protected void handleLeave(@NotNull final Table table, @NotNull final PlayerEvent playerEvent)
            throws com.ictpoker.ixi.PlayerEventException {

        try {
            table.leave(playerEvent.getPlayer());
            LOGGER.info(String.format("Thank you for playing %s. See you next time!", playerEvent.getPlayer().getName()));
        } catch (Table.PlayerNotSeatedException e) {
            throw new com.ictpoker.ixi.PlayerEventException("Sorry Sir, you can't occupy more than one seat.", e);
        }
    }

    public void dealHoleCards(@NotNull final Table table) {

        LOGGER.info("Dealing hole cards to all players");
        shuffleDeck();

        for (int i = 0; i< CARDS_PER_SEAT; i++) {
            for (final Seat seat : table.getSeats()) {
                if (seat != null) {
                    seat.pushCard(getDeck().draw());
                }
            }
        }
    }

    public void dealFlop(@NotNull final Table table) {

        LOGGER.info("Dealing flop...");

        for (int i=0; i<CARDS_FLOP; i++) {
            table.addBoardCard(getDeck().draw());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Card card : table.getBoardCards()) {
            sb.append(String.format("[%s] ", card));
        }
        LOGGER.info(sb.toString());
    }

    public void dealTurn(@NotNull final Table table) {

        LOGGER.info("Dealing turn...");

        for (int i=0; i<CARDS_TURN; i++) {
            table.addBoardCard(getDeck().draw());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Card card : table.getBoardCards()) {
            sb.append(String.format("[%s] ", card));
        }
        LOGGER.info(sb.toString());
    }

    public void dealRiver(@NotNull final Table table) {

        LOGGER.info("Dealing river...");

        for (int i=0; i<CARDS_RIVER; i++) {
            table.addBoardCard(getDeck().draw());
        }

        final StringBuilder sb = new StringBuilder();
        for (final Card card : table.getBoardCards()) {
            sb.append(String.format("[%s] ", card));
        }
        LOGGER.info(sb.toString());
    }

    public void cleanUp(@NotNull final Table table) {

        for (final Seat seat : table.getSeats()) {
            if (seat != null) {
                while (seat.getCards().size() > 0) {
                    try {
                        getDeck().add(seat.popCard());
                    } catch (Deck.DuplicateCardException e) {
                        // TODO: Unhandled exception
                        e.printStackTrace();
                    }
                }
            }
        }

        getDeck().addAll(table.clearBoardCards());
    }

    public void moveButton(@NotNull final Table table, @NotNull final int seatIndex)
            throws DealerEventException {

        table.getState().setButtonPosition(seatIndex);
        LOGGER.info(String.format("Moved button to seat index: %d (%s)",
                seatIndex,
                table.getState().getSeats().get(seatIndex).getPlayer().getName()));

        setSeatToAct(findNextPlayerSeat(table, seatIndex, 0));
    }

    public void pushButton(@NotNull final Table table)
            throws DealerEventException {

        for (int i=0; i<table.getState().getSeats().size(); i++) {
            int wrappingSeatIndex = (table.getState().getButtonPosition() + i) % table.getState().getSeats().size();
            if (table.getState().getSeats().get(wrappingSeatIndex) != null) {
                moveButton(table, wrappingSeatIndex);
                return;
            }
        }
    }

    public void postSmallBlind(@NotNull final Table table)
            throws DealerEventException {

        final Seat seat = findNextPlayerSeat(table, table.getState().getButtonPosition(), 0);

        try {
            final int committedAmount = seat.commit(DEFAULT_SMALL_BLIND_AMOUNT, false);

            LOGGER.info(String.format("Posted small blind %d (%s, stack %d remaining)",
                    committedAmount,
                    seat.getPlayer().getName(),
                    seat.getStack()));
        } catch (Seat.InsufficientStackException e) {
            throw new DealerEventException("Failed to post small blind", e);
        }

        setSeatToAct(findNextPlayerSeat(table, seat, 0));
    }

    public void postBigBlind(@NotNull final Table table)
            throws DealerEventException {

        final Seat seat = findNextPlayerSeat(table, table.getState().getButtonPosition(), 1);

        try {
            final int committedAmount = seat.commit(DEFAULT_BIG_BLIND_AMOUNT, false);

            LOGGER.info(String.format("Posted big blind %d (%s, stack %d remaining)",
                    committedAmount,
                    seat.getPlayer().getName(),
                    seat.getStack()));
        } catch (Seat.InsufficientStackException e) {
            throw new DealerEventException("Failed to post big blind", e);
        }

        setSeatToAct(findNextPlayerSeat(table, seat, 0));
    }

    private Seat findNextPlayerSeat(@NotNull final Table table,
                                    @NotNull final int searchFromSeatIndexExclusive,
                                    @NotNull int skip)
            throws DealerEventException {

        for (int i=1; i<table.getState().getSeats().size()-1; i++) {
            final int wrappingSeatIndex = (searchFromSeatIndexExclusive + i) % table.getState().getSeats().size();
            final Seat seat = table.getState().getSeats().get(wrappingSeatIndex);
            if (seat != null) {

                if (skip-- > 0) {
                    continue;
                }

                return seat;
            }
        }

        throw new DealerEventException("Next player seat not found");
    }

    private Seat findNextPlayerSeat(@NotNull final Table table,
                                    @NotNull final Seat seat,
                                    @NotNull int skip)
            throws DealerEventException {

        try {
            return findNextPlayerSeat(table, table.getPlayerSeatIndex(seat.getPlayer()), skip);
        } catch (Table.PlayerNotSeatedException e) {
            throw new DealerEventException("Player is not seated", e);
        }
    }

    protected Deck getDeck() {

        return deck;
    }

    protected void shuffleDeck() {

        deck.shuffle();
    }

    public void setSeatToAct(@NotNull final Seat seatToAct) {

        this.seatToAct = seatToAct;
    }

    public Seat getSeatToAct() {

        return seatToAct;
    }

    protected boolean hasAllSeatsActed(@NotNull final Table table) {

        for (Seat seat : table.getState().getSeats()) {
            if (seat != null && !seat.hasActed()) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasAllSeatsCommittedEnoughOrAllIn(@NotNull final Table table) {

        for (Seat seat : table.getState().getSeats()) {
            if (seat != null &&
                    seat.getStack() > 0 &&
                    !seat.hasFolded() &&
                    table.getState().getHighestCommittedAmount() != seat.getCommitted()) {
                return false;
            }
        }
        return true;
    }

    protected void setNextPlayerToActOrFinishBettingRound(@NotNull final Table table, @NotNull final Seat seat) {

        if (hasAllSeatsActed(table) && hasAllSeatsCommittedEnoughOrAllIn(table)) {
            finishBettingRound(table);
        } else {
            try {
                setSeatToAct(findNextPlayerSeat(table, seat, 0));
            } catch (DealerEventException e) {
                // TODO: Unhandled exception
                e.printStackTrace();
            }
        }
    }

    protected void setNextPlayerToActOrFinishBettingRound(@NotNull final Table table, @NotNull final int seatIndex) {

        final Seat seat = table.getSeats().get(seatIndex);
        setNextPlayerToActOrFinishBettingRound(table, seat);
    }

    private void finishBettingRound(@NotNull final Table table) {

        for (Seat seat : table.getState().getSeats()) {
            if (seat != null) {
                table.getState().addToPot(seat.collectCommitted());
                seat.setActed(false);
            }
        }

        LOGGER.info(String.format("Betting round finished, pot size %d", table.getState().getPot()));

        if (isHandFinished(table)) {
            finishHand(table);
        } else if (table.getState().getBoardCards().size() == 0) {
            dealFlop(table);
        } else if (table.getState().getBoardCards().size() == Dealer.CARDS_FLOP) {
            dealTurn(table);
        } else if (table.getState().getBoardCards().size() == Dealer.CARDS_FLOP+Dealer.CARDS_TURN) {
            dealRiver(table);
        }

        setNextPlayerToActOrFinishBettingRound(table, table.getState().getButtonPosition());
    }

    private boolean isHandFinished(@NotNull final Table table) {

        int numberOfNonFoldedSeats = 0;
        for (Seat seat : table.getSeats()) {
            if (seat != null && !seat.hasFolded()) {
                numberOfNonFoldedSeats++;
            }
        }

        if (numberOfNonFoldedSeats == 1 ||
                table.getState().getBoardCards().size() == Dealer.CARDS_FLOP+Dealer.CARDS_TURN+Dealer.CARDS_RIVER) {
            return true;
        }

        return false;
    }

    private void finishHand(@NotNull final Table table) {

        int pot = table.getState().collectPot();
        LOGGER.info(String.format("Hand ended, distribute pot(%d) to winners.", pot));
        // TODO: Distribute pot to winners
    }
}
