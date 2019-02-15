package com.ictpoker.ixi.commons;

import com.ictpoker.ixi.eval.Constants;
import com.ictpoker.ixi.eval.Evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Evaluation implements Comparable<Evaluation> {

    private final Hand hand;
    private final int strength;
    private final int handType;
    private final int majorRank;
    private final int minorRank;
    private final int kickers;

    public Evaluation(Hand hand) {
        if (hand.size() != Constants.SIZE_HAND) {
            throw new IllegalStateException("A hand for evaluation must consist of exactly 7 cards.");
        }

        this.hand = hand;
        this.strength = Evaluator.evaluate(hand);
        handType = (strength >> Constants.OFFSET_TYPE) & 0xF;
        majorRank = (strength >> Constants.OFFSET_MAJOR) & 0xF;
        minorRank = (strength >> Constants.OFFSET_MINOR) & 0xF;
        kickers = strength & 0x1FFF;
    }

    public Hand getHand() {
        return hand;
    }

    public int getStrength() {
        return strength;
    }

    public int getType() {
        return handType;
    }

    public Rank getMajor() {
        if (majorRank != Constants.NULL) {
            return Rank.values()[majorRank];
        } else {
            return null;
        }
    }

    public Rank getMinor() {
        if (minorRank != Constants.NULL) {
            return Rank.values()[minorRank];
        } else {
            return null;
        }
    }

    public List<Rank> getKickers() {
        ArrayList<Rank> ranks = new ArrayList<>();
        if (kickers != 0) {
            for (int i = 0; i < Constants.NUM_RANKS; i++) {
                if ((kickers & Constants.bitmaskRank(12 - i)) != 0) {
                    ranks.add(Rank.values()[12 - i]);
                }
            }
        }
        return ranks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.types(getType()));
        final Rank major = getMajor();
        if (major != null) {
            sb.append(" ");
            sb.append(major.toString());
        }
        final Rank minor = getMinor();
        if (minor != null) {
            sb.append(" ");
            sb.append(minor.toString());
        }
        if (!getKickers().isEmpty()) {
            sb.append(" ");
            List<Rank> ranks = getKickers();
            for (Rank rank : ranks) {
                sb.append(rank.toString());
            }

        }
        return sb.toString();
    }

    @Override
    public int compareTo(Evaluation other) {
        return Integer.compare(-this.strength, -other.strength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation that = (Evaluation) o;
        return strength == that.strength &&
                handType == that.handType &&
                majorRank == that.majorRank &&
                minorRank == that.minorRank &&
                kickers == that.kickers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, handType, majorRank, minorRank, kickers);
    }
}