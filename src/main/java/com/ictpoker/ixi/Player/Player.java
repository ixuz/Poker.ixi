package com.ictpoker.ixi.Player;

import lombok.*;

@Data
@AllArgsConstructor
public class Player {

    private final String name;
    @Setter(AccessLevel.NONE)
    private int balance;

    public synchronized int deductBalance(final int amount)
            throws Exception {
        if (balance-amount < 0) {
            throw new Exception("Insufficient balance");
        }

        balance -= amount;
        return amount;
    }
}
