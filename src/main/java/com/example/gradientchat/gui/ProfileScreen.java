package com.example.gradientchat.gui;

import com.example.gradientchat.GradientManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ProfileScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SPACING = 10;
    private static final int PADDING = 20;
    private static final int BACKGROUND_COLOR = 0x80000000;
    
    private final Screen parent;
    private final GradientManager gradientManager;
    
    public ProfileScreen(Screen parent, GradientManager gradientManager) {
        super(Text.literal("Gradient Settings"));
        this.parent = parent;
        this.gradientManager = gradientManager;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = width / 2;
        int startY = height / 4;
        
        // Add toggle button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Toggle Gradient: " + (gradientManager.isEnabled() ? "§aEnabled" : "§cDisabled")),
            button -> {
                gradientManager.setEnabled(!gradientManager.isEnabled());
                button.setMessage(Text.literal("Toggle Gradient: " + (gradientManager.isEnabled() ? "§aEnabled" : "§cDisabled")));
            }
        ).dimensions(centerX - BUTTON_WIDTH/2, startY, BUTTON_WIDTH, BUTTON_HEIGHT).build());
        
        // Add back button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Back"),
            button -> MinecraftClient.getInstance().setScreen(parent)
        ).dimensions(centerX - 50, height - 40, 100, 20).build());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render blurred background
        renderBackground(context, mouseX, mouseY, delta);
        
        // Draw a semi-transparent background
        context.fill(0, 0, width, height, BACKGROUND_COLOR);
        
        // Draw title
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 4 - 30, 0xFFFFFF);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // Escape key
            MinecraftClient.getInstance().setScreen(parent);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
} 