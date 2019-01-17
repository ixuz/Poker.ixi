package com.ictpoker.ixi.Player.PlayerEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.TableEvent.ITableEvent;
import com.sun.istack.internal.NotNull;

public class PlayerEvent implements ITableEvent {

    public enum PlayerAction { JOIN, LEAVE, CHECK, BET, CALL, RAISE, FOLD }

    private final Player player;
    private final PlayerAction playerAction;
    private final int amount;

    public PlayerEvent(@NotNull final Player player,
                       @NotNull final PlayerAction playerAction,
                       @NotNull final int amount)
            throws PlayerEventException {

        this.player = player;
        this.playerAction = playerAction;
        this.amount = amount;

        if (amount < 0 || amount > player.getBalance()) {
            throw new PlayerEventException("Invalid amount");
        }
    }

    public Player getPlayer() {

        return player;
    }

    public PlayerAction getPlayerAction() {

        return playerAction;
    }

    public int getAmount() {
        return amount;
    }
}
