package com.example.gradientchat.mixin;

import com.example.gradientchat.GradientChatMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    private static final int MAX_MESSAGE_LENGTH = 256; // Standard Minecraft chat limit
    
    @Shadow private TextFieldWidget chatField;
    
    @ModifyArg(
            method = "sendMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V"
            ),
            index = 0
    )
    private String modifyChatMessage(String message) {
        // Don't modify commands
        if (message.startsWith("/")) {
            return message;
        }
        
        // Check if gradient chat is enabled
        if (!GradientChatMod.getGradientManager().isEnabled()) {
            return message;
        }
        
        // Check message length
        if (message.length() > MAX_MESSAGE_LENGTH) {
            // Show warning message
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("§cWarning: Message exceeds character limit (" + message.length() + "/" + MAX_MESSAGE_LENGTH + ")"));
            return message.substring(0, MAX_MESSAGE_LENGTH);
        }
        
        // Apply gradient to the message
        return GradientChatMod.getGradientManager().applyGradient(message);
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        String message = chatField.getText();
        
        // Show character count in chat field
        if (message.length() > MAX_MESSAGE_LENGTH * 0.9) { // Show warning at 90% of limit
            int color = message.length() >= MAX_MESSAGE_LENGTH ? 0xFF0000 : 0xFFFF00; // Red if over limit, yellow if close
            chatField.setSuggestion("§" + Integer.toHexString(color >> 16) + message.length() + "/" + MAX_MESSAGE_LENGTH);
        } else {
            chatField.setSuggestion(null);
        }
    }
}