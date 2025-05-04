package com.example.gradientchat.gui;

import java.util.ArrayList;
import java.util.List;

import com.example.gradientchat.GradientManager;
import com.example.gradientchat.GradientManager.GradientPreset;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class GradientScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SPACING = 10;
    private static final int BACKGROUND_COLOR = 0x80000000;
    
    private final Screen parent;
    private final GradientManager gradientManager;
    private final List<ButtonWidget> hexPresetButtons = new ArrayList<>();
    private final List<ButtonWidget> colorCodePresetButtons = new ArrayList<>();
    private ButtonWidget toggleButton;
    
    public GradientScreen(Screen parent, GradientManager gradientManager) {
        super(Text.literal("Gradient Chat Settings"));
        this.parent = parent;
        this.gradientManager = gradientManager;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = width / 2;
        int startY = height / 4;
        
        // Add toggle button at the top
        toggleButton = ButtonWidget.builder(
            Text.literal(gradientManager.isEnabled() ? "Disable Gradient Chat" : "Enable Gradient Chat"),
            btn -> {
                gradientManager.setEnabled(!gradientManager.isEnabled());
                toggleButton.setMessage(Text.literal(gradientManager.isEnabled() ? "Disable Gradient Chat" : "Enable Gradient Chat"));
            }
        ).dimensions(centerX - 100, startY - 30, 200, BUTTON_HEIGHT).build();
        addDrawableChild(toggleButton);
        
        // Add hex preset buttons on the left
        List<GradientPreset> hexPresets = gradientManager.getHexPresets();
        for (int i = 0; i < hexPresets.size(); i++) {
            final int index = i;
            ButtonWidget button = ButtonWidget.builder(
                Text.literal(hexPresets.get(i).getName()),
                btn -> {
                    gradientManager.setUseColorCodes(false);
                    gradientManager.setCurrentHexPresetIndex(index);
                }
            ).dimensions(centerX - BUTTON_WIDTH - SPACING, startY + i * (BUTTON_HEIGHT + SPACING), BUTTON_WIDTH, BUTTON_HEIGHT).build();
            addDrawableChild(button);
            hexPresetButtons.add(button);
        }
        
        // Add color code preset buttons on the right
        List<GradientPreset> colorCodePresets = gradientManager.getColorCodePresets();
        for (int i = 0; i < colorCodePresets.size(); i++) {
            final int index = i;
            ButtonWidget button = ButtonWidget.builder(
                Text.literal(colorCodePresets.get(i).getName()),
                btn -> {
                    gradientManager.setUseColorCodes(true);
                    gradientManager.setCurrentColorCodePresetIndex(index);
                }
            ).dimensions(centerX + SPACING, startY + i * (BUTTON_HEIGHT + SPACING), BUTTON_WIDTH, BUTTON_HEIGHT).build();
            addDrawableChild(button);
            colorCodePresetButtons.add(button);
        }
        
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
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 4 - 60, 0xFFFFFF);
        
        // Draw column labels
        context.drawTextWithShadow(textRenderer, Text.literal("Hex Presets"), width / 2 - BUTTON_WIDTH - SPACING, height / 4 - 10, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("Color Code Presets"), width / 2 + SPACING, height / 4 - 10, 0xFFFFFF);
        
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
