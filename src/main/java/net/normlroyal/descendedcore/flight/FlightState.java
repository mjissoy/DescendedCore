package net.normlroyal.descendedcore.flight;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class FlightState {
    private boolean active;
    private ResourceLocation controllerId;

    public float forwardSpeed;
    public float verticalSpeed;
    public float flapAmount;

    public boolean isActive() {
        return active;
    }

    @Nullable
    public ResourceLocation controllerId() {
        return controllerId;
    }

    public boolean isControlledBy(ResourceLocation id) {
        return active && id != null && id.equals(controllerId);
    }

    public void activate(ResourceLocation id) {
        this.active = true;
        this.controllerId = id;
        resetMotion();
    }

    public void deactivate() {
        this.active = false;
        this.controllerId = null;
        resetMotion();
    }

    public void resetMotion() {
        forwardSpeed = 0.0F;
        verticalSpeed = 0.0F;
        flapAmount = 0.0F;
    }
}
