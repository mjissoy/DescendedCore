package net.normlroyal.descendedcore.action;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class ActionRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<ResourceLocation, RegisteredAction> ACTIONS = new LinkedHashMap<>();

    private ActionRegistry() {
    }

    public static synchronized void register(ActionDefinition definition, ActionExecutor executor) {
        RegisteredAction previous = ACTIONS.putIfAbsent(definition.id(), new RegisteredAction(definition, executor));
        if (previous != null) {
            throw new IllegalStateException("Duplicate action id: " + definition.id());
        }
    }

    public static Optional<RegisteredAction> get(ResourceLocation id) {
        return Optional.ofNullable(ACTIONS.get(id));
    }

    public static Collection<RegisteredAction> entries() {
        return java.util.List.copyOf(ACTIONS.values());
    }

    public static ActionUseResult tryUse(ServerPlayer player, ResourceLocation id) {
        RegisteredAction registered = ACTIONS.get(id);
        if (registered == null) {
            return ActionUseResult.UNKNOWN_ACTION;
        }

        ActionContext context = ActionContext.create(player, registered.definition());
        ActionUseResult result;

        if (!registered.definition().canUse(player)) {
            result = ActionUseResult.DENIED;
        } else {
            try {
                result = registered.executor().execute(context);
                if (result == null) {
                    result = ActionUseResult.FAILED;
                }
            } catch (RuntimeException exception) {
                LOGGER.error("Action {} failed for {}", id, player.getGameProfile().getName(), exception);
                result = ActionUseResult.FAILED;
            }
        }

        ActionUseListeners.fire(context, result);
        return result;
    }

    public record RegisteredAction(ActionDefinition definition, ActionExecutor executor) {
    }

}
