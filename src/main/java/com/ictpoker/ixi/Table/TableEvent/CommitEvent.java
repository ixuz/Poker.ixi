package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Exception.TableStateException;
import com.ictpoker.ixi.Table.Seat;
import com.ictpoker.ixi.Table.Table;

import java.util.Optional;

public class CommitEvent extends TableEvent {

    public CommitEvent(final Player player,
                       final int amount)
            throws TableEventException {

        super(player, amount);
    }

    @Override
    public TableEvent handle(final Table table)
            throws TableEventException {
        try {
            final Optional<Seat> seat = table.getSeat(getPlayer());
            seat.orElseThrow(() -> new TableStateException(("Player is not seated at the table")));

            final int toCall = table.getRequiredAmountToCall();
            final int toRaise = table.getRequiredAmountToRaise();
            final int actualRaiseAmount = getAmount() - toCall;

            if (seat.get() != table.getSeatToAct()) {
                throw new TableEventException("It's not the player's turn to act");
            }

            if (getAmount() > seat.get().getStack()) {
                throw new TableEventException("Player can't commit more than the available stack");
            }

            if (!table.isSmallBlindPosted() && getAmount() != toCall) {
                throw new TableEventException("The player must post the small blind to play");
            } else if (!table.isBigBlindPosted() && getAmount() != toCall) {
                throw new TableEventException("The player must post the big blind to play");
            }

            if (getAmount() == 0) { // Desired check
                if (toCall != 0) {
                    throw new TableEventException(String.format("%s can't check, he must commit at least %d",
                            getPlayer().getName(),
                            toCall));
                }
                addMessage(String.format("%s checked",
                        getPlayer().getName()));
            } else if (getAmount() > 0 && getAmount() == toCall) { // Desired call
                if (!table.isSmallBlindPosted()) {
                    addMessage(String.format("%s posted small blind %d",
                            getPlayer().getName(),
                            getAmount()));
                } else if (!table.isBigBlindPosted()) {
                    addMessage(String.format("%s posted big blind %d",
                            getPlayer().getName(),
                            getAmount()));
                } else {
                    addMessage(String.format("%s called %d",
                            getPlayer().getName(),
                            getAmount()));
                }
            } else if (getAmount() > toCall) { // Desired raise
                if (getAmount() < toRaise) {
                    throw new TableEventException(String.format("%s he must commit at least %d to raise",
                            getPlayer().getName(),
                            toRaise));
                }
                table.setLastRaiseAmount(actualRaiseAmount);
                addMessage(String.format("%s raised %d by committing %d",
                        getPlayer().getName(),
                        actualRaiseAmount,
                        getAmount()));
            } else {
                throw new TableEventException("Failed to identify desired player action");
            }

            try {
                seat.get().commit(getAmount());
            } catch (Exception e) {
                throw new TableEventException("Failed to commit", e);
            }

            if (!table.isSmallBlindPosted()) {
                table.setSmallBlindPosted(true);
            } else if (!table.isBigBlindPosted()) {
                table.setBigBlindPosted(true);
            } else {
                seat.get().setActed(true);
            }

            if (seat.get().getStack() == 0) {
                addMessage(String.format("%s is all-in",
                        getPlayer().getName()));
            }

            table.moveActionToNextPlayer();
        } catch (TableStateException e) {
            throw new TableEventException("Failed to update table state", e);
        }

        return this;
    }
}
