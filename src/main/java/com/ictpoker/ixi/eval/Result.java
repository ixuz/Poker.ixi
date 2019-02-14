package com.ictpoker.ixi.eval;

public class Result implements Comparable<Result> {
    Hand hand;
    int strength;

    public Result(Hand hand) {
        this.hand = hand;
        this.strength = Evaluator.evaluate(hand);
    }

    public Result(Hand hand, int strength) {
        this.hand = hand;
        this.strength = strength;
    }

    @Override
    public int compareTo(Result other) {
        return Integer.compare(-this.strength, -other.strength);
    }
}