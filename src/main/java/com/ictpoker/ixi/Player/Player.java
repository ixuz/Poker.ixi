package com.ictpoker.ixi.Player;

import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {

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
            throws Exception {

        if (balance-amount < 0) {
            throw new Exception("Insufficient balance");
        }

        balance -= amount;
        return amount;
    }
}
