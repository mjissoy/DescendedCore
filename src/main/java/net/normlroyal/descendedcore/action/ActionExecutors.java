package net.normlroyal.descendedcore.action;

public final class ActionExecutors {
    private ActionExecutors() {
    }

    public static ActionExecutor withCooldown(ActionExecutor executor) {
        return context -> {
            if (!ActionCooldowns.isReady(context.player(), context.action())) {
                return ActionUseResult.COOLDOWN;
            }

            ActionUseResult result = executor.execute(context);
            if (result != null && result.successful()) {
                ActionCooldowns.start(context.player(), context.action());
            }
            return result == null ? ActionUseResult.FAILED : result;
        };
    }

    public static ActionExecutor requiring(ActionRequirement requirement, ActionExecutor executor) {
        return context -> {
            ActionCheck check = requirement.test(context.player(), context.action());
            if (!check.allowed()) {
                if (check.message() != null && !check.message().getString().isEmpty()) {
                    context.player().displayClientMessage(check.message(), true);
                }
                return ActionUseResult.DENIED;
            }
            return executor.execute(context);
        };
    }

    public static ActionExecutor managed(ActionRequirement requirement, ActionExecutor executor) {
        return requiring(requirement, withCooldown(executor));
    }
}
