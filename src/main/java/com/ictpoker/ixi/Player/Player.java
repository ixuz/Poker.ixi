package com.ictpoker.ixi.Player;

public class Player {

    private final String name;
    private int balance;

    public Player(final String name, final int balance) {
        this.name = name;
        this.balance = balance;
    }

    public synchronized int deductBalance(final int amount)
            throws Exception {
        if (balance-amount < 0) {
            throw new Exception("Insufficient balance");
        }

        balance -= amount;
        return amount;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
