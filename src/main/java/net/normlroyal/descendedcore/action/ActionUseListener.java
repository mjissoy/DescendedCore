package net.normlroyal.descendedcore.action;

@FunctionalInterface
public interface ActionUseListener {
    void onActionUsed(ActionContext context, ActionUseResult result);
}
