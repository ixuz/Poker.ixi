package com.ictpoker.ixi.Player;

import com.ictpoker.ixi.Player.Exception.InsufficientBalanceException;
import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player implements IPlayer {

    private final static Logger LOGGER = LogManager.getLogger(Player.class);
    private final String name;
    private int balance = 0;

    public Player(@NotNull final String name, @NotNull final int balance) {

        this.name = name;
        this.balance = balance;
    }

    public String getName() {

        return name;
    }

    public int getBalance() {

        return balance;
    }

    public synchronized int deductBalance(@NotNull final int amount)
            throws InsufficientBalanceException {

        if (balance-amount < 0) {
            throw new InsufficientBalanceException();
        }

        balance -= amount;
        return amount;
    }

    @Override
    public void onJoinTable(@NotNull final Table table) {

        LOGGER.info("Let's go I'm ready!");
    }
}
