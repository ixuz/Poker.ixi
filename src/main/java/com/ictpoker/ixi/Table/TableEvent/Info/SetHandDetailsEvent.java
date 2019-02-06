package com.ictpoker.ixi.Table.TableEvent.Info;

import com.ictpoker.ixi.Table.Table;
import com.ictpoker.ixi.Table.TableEvent.TableEvent;

public class SetHandDetailsEvent extends TableEvent {

    private final Long handId;
    private final int smallBlind;
    private final int bigBlind;

    public SetHandDetailsEvent(Long handId, int smallBlind, int bigBlind) {
        super(null, 0);
        this.handId = handId;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    @Override
    public TableEvent handle(Table table) {

        table.reset();

        table.setHandId(handId);
        table.setSmallBlindAmount(smallBlind);
        table.setBigBlindAmount(bigBlind);
        log(String.format("Poker.ixi Hand #%d:  Hold'em No Limit ($%d/$%d)", handId, smallBlind, bigBlind));

        return this;
    }
}
