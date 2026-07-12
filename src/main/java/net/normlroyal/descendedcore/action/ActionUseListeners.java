package net.normlroyal.descendedcore.action;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ActionUseListeners {
    private static final List<ActionUseListener> LISTENERS = new CopyOnWriteArrayList<>();

    private ActionUseListeners() {
    }

    public static void register(ActionUseListener listener) {
        LISTENERS.add(listener);
    }

    static void fire(ActionContext context, ActionUseResult result) {
        for (ActionUseListener listener : LISTENERS) {
            listener.onActionUsed(context, result);
        }
    }
}
