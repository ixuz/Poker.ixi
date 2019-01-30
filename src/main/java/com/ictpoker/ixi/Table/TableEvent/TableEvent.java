package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class TableEvent {

    private final Player player;
    private final int amount;
    private final List<String> output = new ArrayList<>();

    public TableEvent(final Player player,
                      final int amount)
            throws TableEventException {

        this.player = player;
        this.amount = amount;

        if (player != null) {
            if (amount < 0 || amount > player.getBalance()) {
                throw new TableEventException("Invalid amount");
            }
        }
    }

    public void addMessage(final String message) {
        output.add(message);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (int i=0; i<output.size(); i++) {
            sb.append(output.get(i));
            if (i < output.size()-1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public abstract TableEvent handle(final Table table) throws TableEventException;
}
