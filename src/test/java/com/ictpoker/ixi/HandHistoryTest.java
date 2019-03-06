package com.ictpoker.ixi;

import com.ictpoker.ixi.engine.table.exception.TableException;
import com.ictpoker.ixi.engine.table.Table;
import com.ictpoker.ixi.engine.util.HandHistoryParser;
import org.junit.Assert;
import org.junit.Test;

public class HandHistoryTest {

    @Test
    public void testHandHistory1() {

        final Table table = new Table(500, 1000, 5,10);

        try {
            table.addEvents(HandHistoryParser.parseFile("histories/handHistory1.txt"));
            table.handleEventQueue();
        } catch (TableException | HandHistoryParser.ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testHandHistory2() {

        final Table table = new Table(500, 1000, 5,10);

        try {
            table.addEvents(HandHistoryParser.parseFile("histories/handHistory2.txt"));
            table.handleEventQueue();
        } catch (TableException | HandHistoryParser.ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testHandHistory3() {

        final Table table = new Table(500, 1000, 5,10);

        try {
            table.addEvents(HandHistoryParser.parseFile("histories/handHistory3.txt"));
            table.handleEventQueue();
        } catch (TableException | HandHistoryParser.ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
