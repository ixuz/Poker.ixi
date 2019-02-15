package com.ictpoker.ixi.eval;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return strength == result.strength &&
                hand.equals(result.hand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hand, strength);
    }
}