package com.example.gradientchat.gui;

import com.example.gradientchat.gui.ProfileManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.function.Consumer;

public class ColorWheelScreen extends Screen {
    private static final int WHEEL_SIZE = 200;
    private static final int SELECTOR_SIZE = 4;
    
    private final Screen parent;
    private final ProfileManager profileManager;
    private final Consumer<String> onColorSelected;
    private int selectedX = -1;
    private int selectedY = -1;
    private int wheelX;
    private int wheelY;
    
    public ColorWheelScreen(Screen parent, ProfileManager profileManager, String currentColor, Consumer<String> onColorSelected) {
        super(Text.literal("Color Wheel"));
        this.parent = parent;
        this.profileManager = profileManager;
        this.onColorSelected = onColorSelected;
        
        // Parse current color
        if (currentColor != null && !currentColor.isEmpty()) {
            try {
                int color = Integer.parseInt(currentColor.replace("#", ""), 16);
                float[] hsb = java.awt.Color.RGBtoHSB(
                    (color >> 16) & 0xFF,
                    (color >> 8) & 0xFF,
                    color & 0xFF,
                    null
                );
                
                // Convert HSB to wheel coordinates
                double angle = hsb[0] * 2 * Math.PI;
                double radius = hsb[1] * WHEEL_SIZE/2;
                selectedX = (int)(WHEEL_SIZE/2 + radius * Math.cos(angle));
                selectedY = (int)(WHEEL_SIZE/2 + radius * Math.sin(angle));
            } catch (NumberFormatException e) {
                System.err.println("Invalid hex color: " + currentColor);
            }
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        wheelX = width/2 - WHEEL_SIZE/2;
        wheelY = height/2 - WHEEL_SIZE/2;
        
        // Add back button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(parent)
        )
                .dimensions(width/2 - 50, height - 40, 100, 20)
                .build());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render blurred background
        renderBackground(context, mouseX, mouseY, delta);
        
        // Draw color wheel
        for (int y = 0; y < WHEEL_SIZE; y++) {
            for (int x = 0; x < WHEEL_SIZE; x++) {
                double dx = x - WHEEL_SIZE/2.0;
                double dy = y - WHEEL_SIZE/2.0;
                double distance = Math.sqrt(dx*dx + dy*dy);
                
                if (distance <= WHEEL_SIZE/2.0) {
                    double angle = Math.atan2(dy, dx);
                    float hue = (float)((angle + Math.PI) / (2 * Math.PI));
                    float saturation = (float)(distance / (WHEEL_SIZE/2.0));
                    float brightness = 1.0f;
                    
                    int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
                    context.fill(wheelX + x, wheelY + y, wheelX + x + 1, wheelY + y + 1, rgb | 0xFF000000);
                }
            }
        }
        
        // Draw selection indicator
        if (selectedX >= 0 && selectedY >= 0) {
            context.fill(wheelX + selectedX - SELECTOR_SIZE/2, wheelY + selectedY - SELECTOR_SIZE/2,
                        wheelX + selectedX + SELECTOR_SIZE/2, wheelY + selectedY + SELECTOR_SIZE/2,
                        0xFFFFFFFF);
            context.fill(wheelX + selectedX - SELECTOR_SIZE/2 + 1, wheelY + selectedY - SELECTOR_SIZE/2 + 1,
                        wheelX + selectedX + SELECTOR_SIZE/2 - 1, wheelY + selectedY + SELECTOR_SIZE/2 - 1,
                        0xFF000000);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int wheelX = width/2 - WHEEL_SIZE/2;
            int wheelY = height/2 - WHEEL_SIZE/2;
            
            if (mouseX >= wheelX && mouseX < wheelX + WHEEL_SIZE &&
                mouseY >= wheelY && mouseY < wheelY + WHEEL_SIZE) {
                
                double dx = mouseX - (wheelX + WHEEL_SIZE/2.0);
                double dy = mouseY - (wheelY + WHEEL_SIZE/2.0);
                double distance = Math.sqrt(dx*dx + dy*dy);
                
                if (distance <= WHEEL_SIZE/2.0) {
                    selectedX = (int)(mouseX - wheelX);
                    selectedY = (int)(mouseY - wheelY);
                    
                    double angle = Math.atan2(dy, dx);
                    float hue = (float)((angle + Math.PI) / (2 * Math.PI));
                    float saturation = (float)(distance / (WHEEL_SIZE/2.0));
                    float brightness = 1.0f;
                    
                    int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
                    String hexColor = String.format("#%06X", rgb & 0xFFFFFF);
                    onColorSelected.accept(hexColor);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
} 