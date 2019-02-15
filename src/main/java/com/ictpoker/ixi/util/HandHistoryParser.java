package com.ictpoker.ixi.util;

import com.ictpoker.ixi.table.event.*;
import com.ictpoker.ixi.table.event.action.*;
import com.ictpoker.ixi.table.event.info.DealEvent;
import com.ictpoker.ixi.table.event.info.SetHandDetailsEvent;
import com.ictpoker.ixi.table.event.info.SetSeatEvent;
import com.ictpoker.ixi.table.event.info.SetTableDetailsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandHistoryParser {

    private static final Logger LOGGER = LogManager.getLogger(HandHistoryParser.class);

    private static final Pattern infoNewHandPattern = Pattern.compile(".*#(\\d+):  Hold'em No Limit \\(\\$(\\d*.?\\d*)\\/\\$(\\d*.?\\d*) USD\\)");
    private static final Pattern infoTablePattern = Pattern.compile("Table '([^']+)\\' (\\d+)-max Seat #(\\d+) is the button");
    private static final Pattern infoSeatPattern = Pattern.compile("Seat (\\d+): (.+?(?=\\s\\()) \\(\\$(\\d*.?\\d*) in chips\\)");
    private static final Pattern infoUncalledBetPattern = Pattern.compile("Uncalled bet \\(\\$(\\d*.?\\d*)\\) returned to (.+)");
    private static final Pattern infoFlopPattern = Pattern.compile("\\*\\*\\*\\sFLOP\\s\\*\\*\\*\\s\\[(.)(.)\\s(.)(.)\\s(.)(.)\\]");
    private static final Pattern infoTurnPattern = Pattern.compile("\\*\\*\\*\\sTURN\\s\\*\\*\\*\\s\\[(.)(.)\\s(.)(.)\\s(.)(.)\\] \\[(.)(.)\\]");
    private static final Pattern infoRiverPattern = Pattern.compile("\\*\\*\\*\\sRIVER\\s\\*\\*\\*\\s\\[(.)(.)\\s(.)(.)\\s(.)(.)\\s(.)(.)\\] \\[(.)(.)\\]");
    private static final Pattern infoShowdownPattern = Pattern.compile("\\*\\*\\*\\sSHOW DOWN\\s\\*\\*\\*");
    private static final Pattern infoHoleCardsPattern = Pattern.compile("\\*\\*\\*\\sHOLE CARDS\\s\\*\\*\\*");
    private static final Pattern infoDisconnectedPattern = Pattern.compile("(.+?(?=\\sis disconnected)) is disconnected");
    private static final Pattern infoFailedToPostPattern = Pattern.compile("(.+?(?=\\swas removed from the table for failing to post))");
    private static final Pattern infoTimedOutPattern = Pattern.compile("(.+?(?=\\shas timed out))");

    private static final Pattern actionJoinsPattern = Pattern.compile("(.+?(?=\\sjoins)) joins the table at seat #(\\d+)");
    private static final Pattern actionLeavesPattern = Pattern.compile("(.+?(?=\\sleaves)) leaves the table");
    private static final Pattern actionPostSmallBlindPattern = Pattern.compile("(.+?(?=:)): posts small blind \\$(\\d*.?\\d*)");
    private static final Pattern actionPostBigBlindPattern = Pattern.compile("(.+?(?=:)): posts big blind \\$(\\d*.?\\d*)");
    private static final Pattern actionPostSmallAndBigBlindPattern = Pattern.compile("(.+): posts small & big blinds \\$(\\d*.?\\d*)");
    private static final Pattern actionSitOutPattern = Pattern.compile("(.+?(?=:)): (sits out|is sitting out)");
    private static final Pattern actionFoldPattern = Pattern.compile("(.+?(?=:)): folds");
    private static final Pattern actionRaisePattern = Pattern.compile("(.+?(?=:)): raises \\$(\\d*.?\\d*) to \\$(\\d*.?\\d*)");
    private static final Pattern actionCollectedPattern = Pattern.compile("(.+?(?= collected)) collected \\$(\\d*.?\\d*) from pot");
    private static final Pattern actionCallPattern = Pattern.compile("(.+?(?=:)): calls \\$(\\d*.?\\d*)");
    private static final Pattern actionCheckPattern = Pattern.compile("(.+?(?=:)): checks");
    private static final Pattern actionBetPattern = Pattern.compile("(.+?(?=:)): bets \\$(\\d*.?\\d*)");
    private static final Pattern actionDealtPattern = Pattern.compile("Dealt to (.+?(?=\\s\\[))\\s\\[(.)(.)\\s(.)(.)\\]");
    private static final Pattern actionShowsPattern = Pattern.compile("(.+?(?=:)): shows \\[(.)(.)\\s(.)(.)\\]");
    private static final Pattern actionCollectedFromPattern = Pattern.compile("(.+?(?=collected))collected \\$(\\d*.?\\d*) from");
    private static final Pattern actionMucksPattern = Pattern.compile("(.+?(?=:)): mucks hand");

    private static final Pattern summaryPattern = Pattern.compile("\\*\\*\\*\\sSUMMARY\\s\\*\\*\\*");
    private static final Pattern summaryFoldedBeforeFlopPattern = Pattern.compile("folded before Flop");
    private static final Pattern summaryFoldedOnTheFlopPattern = Pattern.compile("folded on the Flop");
    private static final Pattern summaryFoldedOnTheTurnPattern = Pattern.compile("folded on the Turn");
    private static final Pattern summaryFoldedOnTheRiverPattern = Pattern.compile("folded on the River");
    private static final Pattern summaryMuckedPattern = Pattern.compile("Seat (\\d+): (.+?(?=\\smucked)) mucked \\[(.)(.)\\s(.)(.)\\]");
    private static final Pattern summaryShowedPattern = Pattern.compile("showed");
    private static final Pattern summaryDoesNotShowPattern = Pattern.compile("(.+?(?=:)): doesn't show hand");
    private static final Pattern summaryBoardPattern = Pattern.compile("Board");
    private static final Pattern summaryTotalPotPattern = Pattern.compile("Total pot");
    private static final Pattern summaryCollectedPattern = Pattern.compile("(.+?(?= collected)) collected \\(\\$(\\d*.?\\d*)\\)");

    private HandHistoryParser() {

    }

    public static final List<TableEvent> parseFile(final String filePath)
            throws ParseException {

        final List<TableEvent> tableEvents = new ArrayList<>();

        final InputStream inputStream = HandHistoryParser.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new ParseException("Failed to open input stream");
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final TableEvent tableEvent = parseLine(line);
                if (tableEvent != null) {
                    tableEvents.add(tableEvent);
                }
            }
        } catch (IOException e) {
            LOGGER.warn(e);
        }
        return tableEvents;
    }

    private static TableEvent parseLine(final String line)
            throws ParseException {
        Matcher m;

        if (line.isEmpty()) {
            return null;
        }

        m = infoNewHandPattern.matcher(line);
        if (m.find()) {
            Long handId = Long.parseLong(m.group(1));
            int smallBlind = Math.round(Float.parseFloat(m.group(2))*100);
            int bigBlind = Math.round(Float.parseFloat(m.group(3))*100);
            return new SetHandDetailsEvent(handId, smallBlind, bigBlind);
        }

        m = infoTablePattern.matcher(line);
        if (m.find()) {
            final String tableName = m.group(1);
            final int seatCount = Integer.parseInt(m.group(2));
            final int buttonPosition = Integer.parseInt(m.group(3)) - 1;
            return new SetTableDetailsEvent(tableName, seatCount, buttonPosition);
        }

        m = infoSeatPattern.matcher(line);
        if (m.find()) {
            final int seatIndex = Integer.parseInt(m.group(1)) - 1;
            final String playerName = m.group(2);
            final int chips = Math.round(Float.parseFloat(m.group(3))*100);
            return new SetSeatEvent(playerName, chips, seatIndex);
        }

        m = actionPostSmallBlindPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int postedSmallBlind = Math.round(Float.parseFloat(m.group(2))*100);
            return new PostSmallBlindEvent(playerName);
        }

        m = actionPostBigBlindPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int postedBigBlind = Math.round(Float.parseFloat(m.group(2))*100);
            return new PostBigBlindEvent(playerName);
        }

        m = actionPostSmallAndBigBlindPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int postedSmallAndBigBlind = Math.round(Float.parseFloat(m.group(2))*100);
            return new PostSmallAndBigBlindEvent(playerName);
        }

        m = actionSitOutPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return new SitOutEvent(playerName, true);
        }

        m = actionFoldPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return new FoldEvent(playerName);
        }

        m = actionRaisePattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int raises = Math.round(Float.parseFloat(m.group(2))*100);
            final int committed = Math.round(Float.parseFloat(m.group(3))*100);
            return new RaiseEvent(playerName, committed);
        }

        m = infoUncalledBetPattern.matcher(line);
        if (m.find()) {
            final int returned = Math.round(Float.parseFloat(m.group(1))*100);
            final String playerName = m.group(2);
            return null;
        }

        m = actionCollectedPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int collected = Math.round(Float.parseFloat(m.group(2))*100);
            return null;
        }

        m = actionCallPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int call = Math.round(Float.parseFloat(m.group(2))*100);
            return new CallEvent(playerName);
        }

        m = actionCheckPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return new CheckEvent(playerName);
        }

        m = actionBetPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int bet = Math.round(Float.parseFloat(m.group(2))*100);
            return new BetEvent(playerName, bet);
        }

        m = infoFlopPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = infoTurnPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = infoRiverPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = infoShowdownPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = infoHoleCardsPattern.matcher(line);
        if (m.find()) {
            return new DealEvent();
        }

        m = actionDealtPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = actionShowsPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = actionCollectedFromPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int collected = Math.round(Float.parseFloat(m.group(2))*100);
            return null;
        }

        m = actionJoinsPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = actionLeavesPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = infoDisconnectedPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = actionMucksPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = summaryMuckedPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = infoFailedToPostPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = infoTimedOutPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = summaryFoldedBeforeFlopPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryFoldedOnTheFlopPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryFoldedOnTheTurnPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryFoldedOnTheRiverPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryShowedPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryDoesNotShowPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            return null;
        }

        m = summaryBoardPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryTotalPotPattern.matcher(line);
        if (m.find()) {
            return null;
        }

        m = summaryCollectedPattern.matcher(line);
        if (m.find()) {
            final String playerName = m.group(1);
            final int collected = Math.round(Float.parseFloat(m.group(2))*100);
            return null;
        }

        throw new ParseException("Failed to parseFile line: " + line);
    }

    public static class ParseException extends Exception {

        public ParseException(final String message) {
            super(message);
        }

        public ParseException(final String message, Exception e) {
            super(message, e);
        }
    }
}
