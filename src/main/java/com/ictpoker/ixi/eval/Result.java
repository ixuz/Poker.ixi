package com.ictpoker.ixi.eval;

public class Result implements Comparable<Result> {

    private final Hand hand;
    private final int strength;

    public Result(Hand hand) {
        this.hand = hand;
        this.strength = Evaluator.evaluate(hand);
    }

    public Result(Hand hand, int strength) {
        this.hand = hand;
        this.strength = strength;
    }

    public Hand getHand() {
        return hand;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public int compareTo(Result other) {
        return Integer.compare(-this.strength, -other.strength);
    }

    @Override
    public boolean equals(Object other) { return compareTo((Result)other) == 0; }
}