package com.ictpoker.ixi.Table.TableEvent;

import com.ictpoker.ixi.Player.Player;
import com.ictpoker.ixi.Table.Exception.TableEventException;
import com.ictpoker.ixi.Table.Table;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
public abstract class TableEvent {

    private final Player player;
    private final int amount;
    private final List<String> output = new ArrayList<>();

    public TableEvent(@Nullable final Player player,
                      @NotNull final int amount)
            throws TableEventException {

        this.player = player;
        this.amount = amount;

        if (player != null) {
            if (amount < 0 || amount > player.getBalance()) {
                throw new TableEventException("Invalid amount");
            }
        }
    }

    public void addMessage(@NotNull final String message) {
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

    public abstract TableEvent handle(@NotNull final Table table) throws TableEventException;
}
