package com.ictpoker.ixi.eval;

import com.ictpoker.ixi.Commons.Rank;
import java.util.ArrayList;

public final class Evaluation {
    public int handType = 0;
    public int majorRank = 0;
    public int minorRank = 0;
    public int kickers = 0;

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
        if (getMajor() != null) {
            sb.append(" ");
            sb.append(getMajor().toString());
        }
        if (getMinor() != null) {
            sb.append(" ");
            sb.append(getMinor().toString());
        }
        if (getKickers().size() > 0) {
            sb.append(" ");
            ArrayList<Rank> ranks = getKickers();
            for (int i = 0; i < ranks.size(); i++) {
                sb.append(ranks.get(i).toString());
            }

        }
        return sb.toString();
    }
}