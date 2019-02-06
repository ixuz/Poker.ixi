package com.ictpoker.ixi;

import com.ictpoker.ixi.Table.Exception.TableException;
import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Util.HandHistoryParser;
import org.junit.Assert;
import org.junit.Test;

public class HandHistoryTest {

    @Test
    public void testHandHistory1() {

        final Table table = new Table(500, 1000, 5,10);

        try {
            table.pushEvents(HandHistoryParser.parseFile("handHistories/handHistory1.txt"));
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
            table.pushEvents(HandHistoryParser.parseFile("handHistories/handHistory2.txt"));
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
            table.pushEvents(HandHistoryParser.parseFile("handHistories/handHistory3.txt"));
            table.handleEventQueue();
        } catch (TableException | HandHistoryParser.ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
