package com.example.gradientchat.gui;

import com.example.gradientchat.GradientManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import java.util.function.Consumer;

public class HexColorScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SPACING = 10;
    private static final int PADDING = 20;
    private static final int BACKGROUND_COLOR = 0x80000000;
    
    private final Screen parent;
    private final GradientManager gradientManager;
    private final String currentColor;
    private final Consumer<String> onColorSelected;
    private TextFieldWidget colorField;
    
    public HexColorScreen(Screen parent, GradientManager gradientManager, String currentColor, Consumer<String> onColorSelected) {
        super(Text.literal("Select Hex Color"));
        this.parent = parent;
        this.gradientManager = gradientManager;
        this.currentColor = currentColor;
        this.onColorSelected = onColorSelected;
    }
    
    @Override
    protected void init() {
        super.init();
        
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Add color input field
        colorField = new TextFieldWidget(
            textRenderer,
            centerX - 100,
            centerY - 10,
            200,
            20,
            Text.literal("Hex Color")
        );
        colorField.setText(currentColor);
        addDrawableChild(colorField);
        
        // Add apply button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Apply"),
            button -> {
                String color = colorField.getText();
                if (color.startsWith("#")) {
                    color = color.substring(1);
                }
                if (color.length() == 6) {
                    try {
                        Integer.parseInt(color, 16);
                        onColorSelected.accept("#" + color);
                        MinecraftClient.getInstance().setScreen(parent);
                    } catch (NumberFormatException e) {
                        // Invalid hex color
                    }
                }
            }
        ).dimensions(centerX - 50, centerY + 20, 100, 20).build());
        
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
        
        // Draw preview
        if (colorField != null) {
            String color = colorField.getText();
            if (color.startsWith("#")) {
                color = color.substring(1);
            }
            if (color.length() == 6) {
                try {
                    int colorValue = Integer.parseInt(color, 16);
                    context.fill(width / 2 - 50, height / 2 + 50, width / 2 + 50, height / 2 + 100, 0xFF000000 | colorValue);
                } catch (NumberFormatException e) {
                    // Invalid hex color
                }
            }
        }
        
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