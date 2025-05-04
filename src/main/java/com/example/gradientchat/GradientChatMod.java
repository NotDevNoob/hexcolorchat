package com.example.gradientchat;

import com.example.gradientchat.gui.GradientScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradientChatMod implements ClientModInitializer {
    public static final String MOD_ID = "gradientchat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static GradientManager gradientManager;
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        gradientManager = new GradientManager();
        
        // Register key binding
        openMenuKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.gradientchat.openmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.gradientchat.general"
            )
        );
        
        // Register tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openMenuKey.wasPressed()) {
                client.setScreen(new GradientScreen(null, gradientManager));
            }
        });
        
        LOGGER.info("GradientChat initialized!");
    }

    public static GradientManager getGradientManager() {
        return gradientManager;
    }
}