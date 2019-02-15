package com.ictpoker.ixi.eval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.ictpoker.ixi.commons.*;

public class EvaluatorTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\nStarting test: " + description.getMethodName());
        }
    };

    Card ac, ah, as, kc, kh, kd, ks, qc, qh, qd, qs, jc, jh, tc, ts, th, d2, c2, s2, h2, d3, d4, c3, h3, c9, c8, s8, d8,
            c5;

    @Test
    public void testEvaluate() {

        Hand hand;
        Evaluation eval;

        // AcKcQcJcTc2d3d
        // Royal Flush
        hand = new Hand(new ArrayList<>(Arrays.asList(ac, kc, qc, jc, tc, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.ROYAL_FLUSH), eval.toString());

        // KcQcJcTc9c2d3d
        // Straight Flush
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, qc, jc, tc, c9, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.STRAIGHT_FLUSH) + " K", eval.toString());

        // KcKdKsKh9c2d3d
        // Quads (1 quad, 3 singles)
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, kh, c9, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.QUADS) + " K 9", eval.toString());

        // KcKdKsKh2c2d2s
        // Quads (1 quad, 1 trip)
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, kh, c2, d2, s2)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.QUADS) + " K 2", eval.toString());

        // KcKdKsKh9c3d3c
        // Four-of-a-kind K 9 (1 quad, 1 pair, 1 single)
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, kh, c9, d3, c3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.QUADS) + " K 9", eval.toString());

        // KcKdKsQhQc2d3d
        // Fullhouse K Q (1 trip, 1 pair)
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, qh, qc, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.FULLHOUSE) + " K Q", eval.toString());

        // KcKdKsQh3c3h3d
        // Fullhouse K 3 (2 trips)
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, qh, c3, h3, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.FULLHOUSE) + " K 3", eval.toString());

        // KcQcJcTc8c2d3d
        // Flush KQJT8
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, qc, jc, tc, c8, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.FLUSH) + " KQJT8", eval.toString());

        // KcQhJcTs9c2d3d
        // Straight K
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, qh, jc, ts, c9, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.STRAIGHT) + " K", eval.toString());

        // Ac2h5cTs3cQd4d
        // Straight 5 (Ace low)
        hand = new Hand(new ArrayList<>(Arrays.asList(ac, h2, c5, ts, c3, qd, d4)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.STRAIGHT) + " 5", eval.toString());

        // AcAsAhTh8s2d3d
        // Trips A T8
        hand = new Hand(new ArrayList<>(Arrays.asList(ac, as, ah, th, s8, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.TRIPS) + " A T8", eval.toString());

        // AcAsThTc8s2d3d
        // TwoPair A T 8 (2 pairs, 3 singles)
        hand = new Hand(new ArrayList<>(Arrays.asList(ac, as, th, tc, s8, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.TWO_PAIR) + " A T 8", eval.toString());

        // AcAsThTc8s8d3d
        // TwoPair A T 8 (3 pairs, 1 single)
        hand = new Hand(new ArrayList<>(Arrays.asList(ac, as, th, tc, s8, d8, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.TWO_PAIR) + " A T 8", eval.toString());

        // AcAsJhTc8s2d3d
        // Pair A JT8
        hand = new Hand(new ArrayList<>(Arrays.asList(ac, as, jh, tc, s8, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.PAIR) + " A JT8", eval.toString());

        // KcQsJhTh8s2d3d
        // Highcard KQJT8
        hand = new Hand(new ArrayList<>(Arrays.asList(kc, qs, jh, th, s8, d2, d3)));
        eval = new Evaluation(hand);
        System.out.println(hand + " => " + eval);
        Assert.assertEquals(Constants.types(Constants.HIGHCARD) + " KQJT8", eval.toString());
    }

    @Test
    public void testEvaluateHands() throws Exception {
        List<Hand> hands = new ArrayList<>();
        hands.add(new Hand(new ArrayList<>(Arrays.asList(ac, kc, qc, jc, tc, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, qc, jc, tc, c9, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, kh, c9, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, kh, c2, d2, s2))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, kh, c9, d3, c3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, qh, qc, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, kd, ks, qh, c3, h3, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, qc, jc, tc, c8, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, qh, jc, ts, c9, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(ac, h2, c5, ts, c3, qd, d4))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(ac, as, ah, th, s8, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(ac, as, th, tc, s8, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(ac, as, th, tc, s8, d8, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(ac, as, jh, tc, s8, d2, d3))));
        hands.add(new Hand(new ArrayList<>(Arrays.asList(kc, qs, jh, th, s8, d2, d3))));

        List<List<Hand>> handRankings = Evaluator.rankHands(Evaluator.evaluateHands(hands));

        int i = 0;

        for (List<Hand> handsWithEqualStrength : handRankings) {
            int j = 0;
            for (Hand hand : handsWithEqualStrength) {
                if (j != 0) {
                    System.out.print(", " + hand);
                } else {
                    System.out.print(i + ": " + hand);
                }
                j++;
            }
            System.out.println();
            i++;
        }
    }

    @Before
    public void initialize() {
        ac = new Card(Rank.ACE, Suit.CLUBS);
        ah = new Card(Rank.ACE, Suit.HEARTS);
        as = new Card(Rank.ACE, Suit.SPADES);
        kc = new Card(Rank.KING, Suit.CLUBS);
        kh = new Card(Rank.KING, Suit.HEARTS);
        kd = new Card(Rank.KING, Suit.DIAMONDS);
        ks = new Card(Rank.KING, Suit.SPADES);
        qc = new Card(Rank.QUEEN, Suit.CLUBS);
        qh = new Card(Rank.QUEEN, Suit.HEARTS);
        qd = new Card(Rank.QUEEN, Suit.DIAMONDS);
        qs = new Card(Rank.QUEEN, Suit.SPADES);
        jc = new Card(Rank.JACK, Suit.CLUBS);
        jh = new Card(Rank.JACK, Suit.HEARTS);
        tc = new Card(Rank.TEN, Suit.CLUBS);
        ts = new Card(Rank.TEN, Suit.SPADES);
        th = new Card(Rank.TEN, Suit.HEARTS);
        d2 = new Card(Rank.TWO, Suit.DIAMONDS);
        c2 = new Card(Rank.TWO, Suit.CLUBS);
        s2 = new Card(Rank.TWO, Suit.SPADES);
        h2 = new Card(Rank.TWO, Suit.HEARTS);
        d3 = new Card(Rank.THREE, Suit.DIAMONDS);
        d4 = new Card(Rank.FOUR, Suit.DIAMONDS);
        c3 = new Card(Rank.THREE, Suit.CLUBS);
        h3 = new Card(Rank.THREE, Suit.HEARTS);
        c9 = new Card(Rank.NINE, Suit.CLUBS);
        c8 = new Card(Rank.EIGHT, Suit.CLUBS);
        s8 = new Card(Rank.EIGHT, Suit.SPADES);
        d8 = new Card(Rank.EIGHT, Suit.DIAMONDS);
        c5 = new Card(Rank.FIVE, Suit.CLUBS);
    }
}