package com.ictpoker.ixi.eval;

import com.ictpoker.ixi.Commons.Rank;
import com.ictpoker.ixi.eval.luts.*;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public final class Evaluator {

    // based off of PokerStove (c) Andrew C. Prock
    public static int evaluate(final Hand hand) {

        long bitmask = hand.getBitmask();

        int clubs = getRanks(bitmask, Constants.OFFSET_CLUBS);
        int diamonds = getRanks(bitmask, Constants.OFFSET_DIAMONDS);
        int hearts = getRanks(bitmask, Constants.OFFSET_HEARTS);
        int spades = getRanks(bitmask, Constants.OFFSET_SPADES);

        int ranks = clubs | diamonds | hearts | spades;
        int numRanks = countRanks(ranks);
        int suit = 0;
        int majorRank = Constants.NULL;
        int minorRank = Constants.NULL;
        int kickers = 0;

        if (numRanks >= 5) {
            if (countRanks(clubs) >= 5) {
                suit = clubs;
            } else if (countRanks(diamonds) >= 5) {
                suit = diamonds;
            } else if (countRanks(hearts) >= 5) {
                suit = hearts;
            } else if (countRanks(spades) >= 5) {
                suit = spades;
            } else {
                suit = 0;
            }

            if (suit != 0) {
                majorRank = Straight.at(suit);

                if (majorRank != 0) {
                    if (majorRank == Rank.ACE.ordinal()) {
                        // Royal Flush
                        return encode(Constants.ROYAL_FLUSH, Constants.NULL, Constants.NULL, 0);
                    } else {
                        // Straight Flush
                        return encode(Constants.STRAIGHT_FLUSH, majorRank, Constants.NULL, 0);
                    }
                } else {
                    // Flush
                    return encode(Constants.FLUSH, Constants.NULL, Constants.NULL, Msb5.at(suit));
                }
            } else {
                majorRank = Straight.at(ranks);

                if (majorRank != 0) {
                    // Straight
                    return encode(Constants.STRAIGHT, majorRank, Constants.NULL, 0);
                }
            }
        }

        int numDuplicateRanks = Constants.SIZE_HAND - numRanks;
        switch (numDuplicateRanks) {
            case 0: {
                // Highcard
                return encode(Constants.HIGHCARD, Constants.NULL, Constants.NULL, Msb5.at(ranks));
            }
            case 1: {
                int pairs = ranks ^ (clubs ^ diamonds ^ hearts ^ spades);
                majorRank = TopRank.at(pairs);
                kickers = Msb3.at(ranks ^ Constants.BITMASK_RANK[majorRank]);

                // Pair
                return encode(Constants.PAIR, majorRank, Constants.NULL, kickers);
            }
            case 2: {
                int pairs = ranks ^ (clubs ^ diamonds ^ hearts ^ spades);
                if (pairs != 0) {
                    majorRank = TopRank.at(pairs);
                    minorRank = TopRank.at(pairs ^ Msb1.at(pairs));
                    kickers = Msb1.at(ranks ^ pairs);

                    // TwoPair
                    return encode(Constants.TWO_PAIR, majorRank, minorRank, kickers);
                } else {
                    int trips = ((clubs & diamonds) | (hearts & spades)) & ((clubs & hearts) | (diamonds & spades));
                    majorRank = TopRank.at(trips);
                    int kicker1 = Msb1.at(ranks ^ trips);
                    int kicker2 = Msb1.at((ranks ^ trips) ^ kicker1);
                    kickers = kicker1 | kicker2;

                    // Three-of-a-kind
                    return encode(Constants.TRIPS, majorRank, Constants.NULL, kickers);
                }
            }
            default: {
                int quads = clubs & diamonds & hearts & spades;

                if (quads != 0) {
                    majorRank = TopRank.at(quads);
                    kickers = Msb1.at(ranks ^ quads);

                    // Four-of-a-kind
                    return encode(Constants.QUADS, majorRank, Constants.NULL, kickers);
                } else {
                    int pairs = ranks ^ (clubs ^ diamonds ^ hearts ^ spades);

                    if (countRanks(pairs) != numDuplicateRanks) {
                        int trips = ((clubs & diamonds) | (hearts & spades)) & ((clubs & hearts) | (diamonds & spades));

                        majorRank = TopRank.at(trips);

                        if (pairs != 0) {
                            minorRank = TopRank.at(pairs);

                            // Fullhouse (with 1 triple and 1 pair)
                            return encode(Constants.FULLHOUSE, majorRank, minorRank, 0);
                        } else {
                            minorRank = TopRank.at(trips ^ Constants.BITMASK_RANK[majorRank]);

                            // Fullhouse (with 2 triples)
                            return encode(Constants.FULLHOUSE, majorRank, minorRank, 0);
                        }
                    } else {
                        majorRank = TopRank.at(pairs);
                        minorRank = TopRank.at(pairs ^ Constants.BITMASK_RANK[majorRank]);
                        kickers = Msb1.at((ranks ^ Constants.BITMASK_RANK[majorRank])
                                ^ Constants.BITMASK_RANK[minorRank]);

                        // TwoPair (with 3 pairs)
                        return encode(Constants.TWO_PAIR, majorRank, minorRank, kickers);
                    }
                }
            }
        }
    }

    public static Map<Hand, Integer> evaluateHands(List<Hand> hands) {
        if (hands.size() == 0)
            return new HashMap<>();

        Map<Hand, Integer> evals = new HashMap<>(hands.size());

        for (int i = 0; i < hands.size(); i++) {
            int eval = evaluate(hands.get(i));
            evals.put(hands.get(i), eval);
        }

        return evals;
    }

    public static List<List<Hand>> rankHands(Map<Hand, Integer> evals) {
        if (evals.size() == 0)
            return new ArrayList<>();

        List<Result> sortedByStrength = new ArrayList<>();

        for (Map.Entry<Hand, Integer> entry : evals.entrySet()) {
            Hand hand = entry.getKey();
            int strength = entry.getValue();

            sortedByStrength.add(new Result(hand, strength));
        }

        // sort evaluations (from strongest to weakest)
        sortedByStrength.sort(null);

        List<List<Hand>> outer = new ArrayList<>();
        List<Hand> first = new ArrayList<>();
        first.add(sortedByStrength.get(0).hand);
        outer.add(first);

        for (int i = 1; i < sortedByStrength.size(); i++) {
            if (sortedByStrength.get(i).strength == sortedByStrength.get(i - 1).strength) {
                List<Hand> inner = outer.get(outer.size() - 1);
                inner.add(sortedByStrength.get(i).hand);
            } else {
                List<Hand> inner = new ArrayList<>();
                inner.add(sortedByStrength.get(i).hand);
                outer.add(inner);
            }
        }

        return outer;
    }

    static int getRanks(long bitmask, int suit) {
        return (int) ((bitmask >> suit) & Constants.MASK_SUIT);
    }

    static int countRanks(int ranks) {
        int count = 0;
        for (int i = 0; i < Constants.NUM_RANKS; i++) {
            if (((0x1 << i) & ranks) != 0) {
                count++;
            }
        }
        return count;
    }

    static int encode(int type, int majorRank, int minorRank, int kicker) {
        return (type << Constants.OFFSET_TYPE) ^ (majorRank << Constants.OFFSET_MAJOR)
                ^ (minorRank << Constants.OFFSET_MINOR) ^ (kicker << Constants.OFFSET_KICKER);
    }

}
