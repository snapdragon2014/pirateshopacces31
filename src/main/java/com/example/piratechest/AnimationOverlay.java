package com.example.piratechest;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class AnimationOverlay {

    private static final int TOTAL_FRAMES = 60;
    private static final int TICKS_PER_FRAME = 1;
    private static final Identifier[] FRAMES = new Identifier[TOTAL_FRAMES];

    static {
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String name = String.format("textures/gui/animation/%03d.png", i + 1);
            FRAMES[i] = new Identifier("piratechest", name);
        }
    }

    private boolean playing = false;
    private int currentFrame = 0;
    private int ticks = 0;

    private static final int HOLD_LAST = 28;
    private static final int FADE_OUT = 16;

    public void start() {
        playing = true;
        currentFrame = 0;
        ticks = 0;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void tick() {
        if (!playing) return;
        ticks++;

        int idx = ticks / TICKS_PER_FRAME;
        if (idx < TOTAL_FRAMES) {
            currentFrame = idx;
        } else {
            currentFrame = TOTAL_FRAMES - 1;
            if (ticks > TOTAL_FRAMES * TICKS_PER_FRAME + HOLD_LAST + FADE_OUT) {
                playing = false;
            }
        }
    }

    public void render(DrawContext context, float tickDelta) {
        if (!playing) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) return;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();

        // New texture size
        int texW = 720;
        int texH = 360;

        float scale = Math.min(sw / 850f, sh / 450f);
        scale = MathHelper.clamp(scale, 0.7f, 1.6f);

        int drawW = (int) (texW * scale);
        int drawH = (int) (texH * scale);

        int x = (sw - drawW) / 2;
        int y = (sh - drawH) / 2 - (int)(15 * scale);

        float alpha = 1.0f;
        int animEnd = TOTAL_FRAMES * TICKS_PER_FRAME;

        if (ticks < 4) {
            alpha = ticks / 4.0f;
        } else if (ticks > animEnd + HOLD_LAST) {
            float t = (ticks - animEnd - HOLD_LAST) / (float) FADE_OUT;
            alpha = 1.0f - MathHelper.clamp(t, 0f, 1f);
        }

        if (alpha < 0.02f) return;

        Identifier tex = FRAMES[MathHelper.clamp(currentFrame, 0, TOTAL_FRAMES - 1)];

        context.setShaderColor(1f, 1f, 1f, alpha);
        context.drawTexture(tex, x, y, 0, 0, drawW, drawH, drawW, drawH);
        context.setShaderColor(1f, 1f, 1f, 1f);
    }
}
