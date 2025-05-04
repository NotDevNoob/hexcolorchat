package com.example.gradientchat.gui;

import com.example.gradientchat.gui.ProfileManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NewProfileDialog extends Screen {
    private final Screen parent;
    private final ProfileManager profileManager;
    private TextFieldWidget nameField;
    
    public NewProfileDialog(Screen parent, ProfileManager profileManager) {
        super(Text.literal("New Profile"));
        this.parent = parent;
        this.profileManager = profileManager;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Create name text field
        nameField = new TextFieldWidget(
            textRenderer,
            width / 2 - 100,
            height / 2 - 10,
            200,
            20,
            Text.literal("Profile Name")
        );
        nameField.setMaxLength(32);
        addDrawableChild(nameField);
        
        // Create buttons
        addDrawableChild(ButtonWidget.builder(Text.literal("Create"), button -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                profileManager.addProfile(name);
                MinecraftClient.getInstance().setScreen(parent);
            }
        }).dimensions(width / 2 - 50, height / 2 + 20, 100, 20).build());
        
        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(width / 2 - 50, height / 2 + 50, 100, 20).build());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }
} 