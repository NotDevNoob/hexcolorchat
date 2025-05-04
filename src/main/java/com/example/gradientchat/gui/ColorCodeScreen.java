package com.example.gradientchat.gui;

import com.example.gradientchat.GradientManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ColorCodeScreen extends Screen {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SPACING = 5;
    private static final int PADDING = 20;
    private static final int BACKGROUND_COLOR = 0x80000000;
    
    private final Screen parent;
    private final GradientManager gradientManager;
    private final String currentColor;
    private final Consumer<String> onColorSelected;
    
    public ColorCodeScreen(Screen parent, GradientManager gradientManager, String currentColor, Consumer<String> onColorSelected) {
        super(Text.literal("Select Color Code"));
        this.parent = parent;
        this.gradientManager = gradientManager;
        this.currentColor = currentColor;
        this.onColorSelected = onColorSelected;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = width / 2;
        int startY = height / 4;
        int currentY = startY;
        
        // Add color code buttons
        String[] colorCodes = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        for (int i = 0; i < colorCodes.length; i++) {
            final String colorCode = colorCodes[i];
            ButtonWidget button = ButtonWidget.builder(
                    Text.literal("§" + colorCode + "Color " + colorCode),
                    btn -> {
                        onColorSelected.accept(colorCode);
                        MinecraftClient.getInstance().setScreen(parent);
                    }
            )
                    .dimensions(centerX - BUTTON_WIDTH/2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                    .build();
            addDrawableChild(button);
            currentY += BUTTON_HEIGHT + SPACING;
        }
        
        // Add back button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(parent)
        )
                .dimensions(centerX - 50, height - 40, 100, 20)
                .build());
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