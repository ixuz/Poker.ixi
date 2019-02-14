package com.ictpoker.ixi.Util;

import com.ictpoker.ixi.Table.TableEvent.*;
import com.ictpoker.ixi.Table.TableEvent.Action.*;
import com.ictpoker.ixi.Table.TableEvent.Info.DealEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetHandDetailsEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetSeatEvent;
import com.ictpoker.ixi.Table.TableEvent.Info.SetTableDetailsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandHistoryParser {

    private final static Logger LOGGER = LogManager.getLogger(HandHistoryParser.class);

    private final static Pattern infoNewHandPattern = Pattern.compile(".*#(\\d+):  Hold'em No Limit \\(\\$(\\d*.?\\d*)\\/\\$(\\d*.?\\d*) USD\\)");
    private final static Pattern infoTablePattern = Pattern.compile("Table '([^']+)\\' (\\d+)-max Seat #(\\d+) is the button");
    private final static Pattern infoSeatPattern = Pattern.compile("Seat (\\d+): (.+?(?=\\s\\()) \\(\\$(\\d*.?\\d*) in chips\\)");
    private final static Pattern infoUncalledBetPattern = Pattern.compile("Uncalled bet \\(\\$(\\d*.?\\d*)\\) returned to (.+)");
    private final static Pattern infoFlopPattern = Pattern.compile("\\*\\*\\*\\sFLOP\\s\\*\\*\\*\\s\\[(.)(.)\\s(.)(.)\\s(.)(.)\\]");
    private final static Pattern infoTurnPattern = Pattern.compile("\\*\\*\\*\\sTURN\\s\\*\\*\\*\\s\\[(.)(.)\\s(.)(.)\\s(.)(.)\\] \\[(.)(.)\\]");
    private final static Pattern infoRiverPattern = Pattern.compile("\\*\\*\\*\\sRIVER\\s\\*\\*\\*\\s\\[(.)(.)\\s(.)(.)\\s(.)(.)\\s(.)(.)\\] \\[(.)(.)\\]");
    private final static Pattern infoShowdownPattern = Pattern.compile("\\*\\*\\*\\sSHOW DOWN\\s\\*\\*\\*");
    private final static Pattern infoHoleCardsPattern = Pattern.compile("\\*\\*\\*\\sHOLE CARDS\\s\\*\\*\\*");
    private final static Pattern infoDisconnectedPattern = Pattern.compile("(.+?(?=\\sis disconnected)) is disconnected");
    private final static Pattern infoFailedToPostPattern = Pattern.compile("(.+?(?=\\swas removed from the table for failing to post))");
    private final static Pattern infoTimedOutPattern = Pattern.compile("(.+?(?=\\shas timed out))");

    private final static Pattern actionJoinsPattern = Pattern.compile("(.+?(?=\\sjoins)) joins the table at seat #(\\d+)");
    private final static Pattern actionLeavesPattern = Pattern.compile("(.+?(?=\\sleaves)) leaves the table");
    private final static Pattern actionPostSmallBlindPattern = Pattern.compile("(.+?(?=:)): posts small blind \\$(\\d*.?\\d*)");
    private final static Pattern actionPostBigBlindPattern = Pattern.compile("(.+?(?=:)): posts big blind \\$(\\d*.?\\d*)");
    private final static Pattern actionPostSmallAndBigBlindPattern = Pattern.compile("(.+): posts small & big blinds \\$(\\d*.?\\d*)");
    private final static Pattern actionSitOutPattern = Pattern.compile("(.+?(?=:)): (sits out|is sitting out)");
    private final static Pattern actionFoldPattern = Pattern.compile("(.+?(?=:)): folds");
    private final static Pattern actionRaisePattern = Pattern.compile("(.+?(?=:)): raises \\$(\\d*.?\\d*) to \\$(\\d*.?\\d*)");
    private final static Pattern actionCollectedPattern = Pattern.compile("(.+?(?= collected)) collected \\$(\\d*.?\\d*) from pot");
    private final static Pattern actionCallPattern = Pattern.compile("(.+?(?=:)): calls \\$(\\d*.?\\d*)");
    private final static Pattern actionCheckPattern = Pattern.compile("(.+?(?=:)): checks");
    private final static Pattern actionBetPattern = Pattern.compile("(.+?(?=:)): bets \\$(\\d*.?\\d*)");
    private final static Pattern actionDealtPattern = Pattern.compile("Dealt to (.+?(?=\\s\\[))\\s\\[(.)(.)\\s(.)(.)\\]");
    private final static Pattern actionShowsPattern = Pattern.compile("(.+?(?=:)): shows \\[(.)(.)\\s(.)(.)\\]");
    private final static Pattern actionCollectedFromPattern = Pattern.compile("(.+?(?=collected))collected \\$(\\d*.?\\d*) from");
    private final static Pattern actionMucksPattern = Pattern.compile("(.+?(?=:)): mucks hand");

    private final static Pattern summaryPattern = Pattern.compile("\\*\\*\\*\\sSUMMARY\\s\\*\\*\\*");
    private final static Pattern summaryFoldedBeforeFlopPattern = Pattern.compile("folded before Flop");
    private final static Pattern summaryFoldedOnTheFlopPattern = Pattern.compile("folded on the Flop");
    private final static Pattern summaryFoldedOnTheTurnPattern = Pattern.compile("folded on the Turn");
    private final static Pattern summaryFoldedOnTheRiverPattern = Pattern.compile("folded on the River");
    private final static Pattern summaryMuckedPattern = Pattern.compile("Seat (\\d+): (.+?(?=\\smucked)) mucked \\[(.)(.)\\s(.)(.)\\]");
    private final static Pattern summaryShowedPattern = Pattern.compile("showed");
    private final static Pattern summaryDoesNotShowPattern = Pattern.compile("(.+?(?=:)): doesn't show hand");
    private final static Pattern summaryBoardPattern = Pattern.compile("Board");
    private final static Pattern summaryTotalPotPattern = Pattern.compile("Total pot");
    private final static Pattern summaryCollectedPattern = Pattern.compile("(.+?(?= collected)) collected \\(\\$(\\d*.?\\d*)\\)");

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
