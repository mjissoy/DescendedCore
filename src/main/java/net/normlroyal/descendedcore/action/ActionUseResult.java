package net.normlroyal.descendedcore.action;

public record ActionUseResult(ActionUseStatus status) {
    public static final ActionUseResult SUCCESS = new ActionUseResult(ActionUseStatus.SUCCESS);
    public static final ActionUseResult COOLDOWN = new ActionUseResult(ActionUseStatus.COOLDOWN);
    public static final ActionUseResult DENIED = new ActionUseResult(ActionUseStatus.DENIED);
    public static final ActionUseResult FAILED = new ActionUseResult(ActionUseStatus.FAILED);
    public static final ActionUseResult UNKNOWN_ACTION = new ActionUseResult(ActionUseStatus.UNKNOWN_ACTION);

    public boolean successful() {
        return status == ActionUseStatus.SUCCESS;
    }
}
