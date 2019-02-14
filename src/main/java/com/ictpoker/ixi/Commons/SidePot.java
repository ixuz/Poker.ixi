package com.ictpoker.ixi.Commons;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Seat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SidePot {

    private final List<Seat> contestants = new ArrayList<>();
    private int potSize;
    private int oddChip = 0;

    public SidePot(int potSize) {
        this.potSize = potSize;
    }

    public boolean hasContestant(final Seat contestant) {
        return contestants.contains(contestant);
    }

    public void addContestant(final Seat contestant) {
        if (!contestants.contains(contestant)) {
            contestants.add(contestant);
        }
    }

    public List<Seat> getContestants() {
        return contestants;
    }

    public void setPotSize(int potSize) {
        this.potSize = potSize;
    }

    public int getPotSize() {
        return potSize;
    }

    public int getOddChip() {
        return oddChip;
    }

    public void setOddChip(int oddChip) {
        this.oddChip = oddChip;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Side pot $").append(getPotSize()).append("\n");
        sb.append("Contestants: ").append(Arrays.asList(getContestants().stream()
                .map(Seat::getPlayer)
                .map(Player::getName)
                .toArray()));

        return sb.toString();
    }
}
