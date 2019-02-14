package com.ictpoker.ixi.eval;

import com.ictpoker.ixi.commons.Rank;
import java.util.ArrayList;

public final class Evaluation {
    private final int handType;
    private final int majorRank;
    private final int minorRank;
    private final int kickers;

    public Evaluation(int code) {
        handType = (code >> Constants.OFFSET_TYPE) & 0xF;
        majorRank = (code >> Constants.OFFSET_MAJOR) & 0xF;
        minorRank = (code >> Constants.OFFSET_MINOR) & 0xF;
        kickers = code & 0x1FFF;
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

    public ArrayList<Rank> getKickers() {
        ArrayList<Rank> ranks = new ArrayList<Rank>();
        if (kickers != 0) {
            for (int i = 0; i < Constants.NUM_RANKS; i++) {
                if ((kickers & Constants.BITMASK_RANK[12 - i]) != 0) {
                    ranks.add(Rank.values()[12 - i]);
                }
            }
        }
        return ranks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.TYPES[getType()]);
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
        if (getKickers().size() > 0) {
            sb.append(" ");
            ArrayList<Rank> ranks = getKickers();
            for (Rank rank : ranks) {
                sb.append(rank.toString());
            }

        }
        return sb.toString();
    }
}