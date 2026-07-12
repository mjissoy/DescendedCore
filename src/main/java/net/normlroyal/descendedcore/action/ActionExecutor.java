package net.normlroyal.descendedcore.action;

@FunctionalInterface
public interface ActionExecutor {
    ActionUseResult execute(ActionContext context);
}
