package com.example.gradientchat.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

public class ColorButtonWidget extends ButtonWidget {
    private final String color;

    public ColorButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, String color) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.color = color;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        
        // Draw the color preview
        int colorValue = Integer.parseInt(color.replace("#", ""), 16);
        context.fill(getX() + 2, getY() + 2, getX() + width - 2, getY() + height - 2, 0xFF000000 | colorValue);
    }
} 