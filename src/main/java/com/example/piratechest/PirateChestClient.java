package com.example.piratechest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class PirateChestClient implements ClientModInitializer {

    public static final Identifier PIRATE_ACCESS_SOUND_ID = new Identifier("piratechest", "pirate_access");
    public static final SoundEvent PIRATE_ACCESS_SOUND = SoundEvent.of(PIRATE_ACCESS_SOUND_ID);

    public static final AnimationOverlay animationOverlay = new AnimationOverlay();

    // Prevent spamming the animation while the same chest screen stays open
    private static boolean triggeredThisScreen = false;

    @Override
    public void onInitializeClient() {
        Registry.register(Registries.SOUND_EVENT, PIRATE_ACCESS_SOUND_ID, PIRATE_ACCESS_SOUND);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            boolean isChestLike = screen instanceof GenericContainerScreen ||
                    (screen instanceof HandledScreen && 
                     (screen.getClass().getSimpleName().contains("Chest") ||
                      screen.getClass().getSimpleName().contains("Shulker") ||
                      screen.getClass().getSimpleName().contains("Barrel")));

            if (isChestLike && !triggeredThisScreen && !animationOverlay.isPlaying()) {
                triggeredThisScreen = true;
                playAnimationAndSound(client);
            }
        });

        // Reset the flag when any screen is closed / changed
        ScreenEvents.REMOVE.register((screen) -> {
            triggeredThisScreen = false;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            animationOverlay.tick();
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            animationOverlay.render(drawContext, tickDelta);
        });
    }

    private void playAnimationAndSound(MinecraftClient client) {
        client.getSoundManager().play(
            PositionedSoundInstance.master(PIRATE_ACCESS_SOUND, 1.0f, 1.0f)
        );
        animationOverlay.start();
    }
}
