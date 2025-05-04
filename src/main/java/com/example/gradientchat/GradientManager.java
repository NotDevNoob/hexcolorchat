package com.example.gradientchat;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import com.example.gradientchat.gui.ProfileManager;

public class GradientManager {
    private static final int NUM_PRESETS = 5;
    private static final int COLORS_PER_PRESET = 10;
    
    private final List<GradientPreset> hexPresets;
    private final List<GradientPreset> colorCodePresets;
    private int currentHexPresetIndex;
    private int currentColorCodePresetIndex;
    private boolean useColorCodes = false;
    private boolean enabled = true; // Default to enabled
    
    public GradientManager() {
        hexPresets = new ArrayList<>();
        colorCodePresets = new ArrayList<>();
        currentHexPresetIndex = 0;
        currentColorCodePresetIndex = 0;
        
        // Initialize with default presets
        initializeDefaultPresets();
        
        // Load saved presets if available
        loadPresets();
    }
    
    private void initializeDefaultPresets() {
        // Hex Presets
        hexPresets.add(new GradientPreset("🔥 Red Gradient (dark to light)", new int[] {
                0x8B0000, 0xA60000, 0xC80000, 0xE00000, 0xFF0000,
                0xFF4040, 0xFF7373, 0xFF9999, 0xFF0000, 0x8B0000
        }));
        
        hexPresets.add(new GradientPreset("🔵 Blue Gradient (deep to sky)", new int[] {
                0x00008B, 0x0000CD, 0x1E90FF, 0x00BFFF, 0x87CEFA,
                0xB0E0E6, 0xE0FFFF, 0xF0FFFF, 0x00BFFF, 0x00008B
        }));
        
        hexPresets.add(new GradientPreset("💚 Green Gradient (forest to lime)", new int[] {
                0x006400, 0x228B22, 0x2E8B57, 0x3CB371, 0x66CDAA,
                0x7FFFD4, 0x98FB98, 0xADFF2F, 0x3CB371, 0x006400
        }));
        
        hexPresets.add(new GradientPreset("🟣 Purple Gradient (rich to light)", new int[] {
                0x4B0082, 0x6A0DAD, 0x800080, 0xA020F0, 0xDA70D6,
                0xD8BFD8, 0xE6E6FA, 0xF8F8FF, 0xA020F0, 0x4B0082
        }));
        
        hexPresets.add(new GradientPreset("🌈 Rainbow (hard steps, not blended)", new int[] {
                0xFF0000, 0xFF7F00, 0xFFFF00, 0x00FF00, 0x0000FF,
                0x4B0082, 0x8B00FF, 0xFF0000, 0xFF7F00, 0xFFFF00
        }));
        
        // Color Code Presets
        colorCodePresets.add(new GradientPreset("🔥 Red Gradient (dark to light)", new int[] {
                0x8B0000, 0xA60000, 0xC80000, 0xE00000, 0xFF0000,
                0xFF4040, 0xFF7373, 0xFF9999, 0xFF0000, 0x8B0000
        }, "&4H &cE &6L &eL &fO"));
        
        colorCodePresets.add(new GradientPreset("🔵 Blue Gradient (deep to sky)", new int[] {
                0x00008B, 0x0000CD, 0x1E90FF, 0x00BFFF, 0x87CEFA,
                0xB0E0E6, 0xE0FFFF, 0xF0FFFF, 0x00BFFF, 0x00008B
        }, "&1H &9E &bL &fL &fO"));
        
        colorCodePresets.add(new GradientPreset("💚 Green Gradient (forest to lime)", new int[] {
                0x006400, 0x228B22, 0x2E8B57, 0x3CB371, 0x66CDAA,
                0x7FFFD4, 0x98FB98, 0xADFF2F, 0x3CB371, 0x006400
        }, "&2H &aE &eL &fL &fO"));
        
        colorCodePresets.add(new GradientPreset("🟣 Purple Gradient (rich to light)", new int[] {
                0x4B0082, 0x6A0DAD, 0x800080, 0xA020F0, 0xDA70D6,
                0xD8BFD8, 0xE6E6FA, 0xF8F8FF, 0xA020F0, 0x4B0082
        }, "&5H &dE &fL &dL &5O"));
        
        colorCodePresets.add(new GradientPreset("🌈 Rainbow (hard steps, not blended)", new int[] {
                0xFF0000, 0xFF7F00, 0xFFFF00, 0x00FF00, 0x0000FF,
                0x4B0082, 0x8B00FF, 0xFF0000, 0xFF7F00, 0xFFFF00
        }, "&cH &6E &eL &aL &bO &9! &5? &4."));
    }
    
    public void savePresets() {
        try {
            File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "gradient-chat");
            if (!configDir.exists() && !configDir.mkdirs()) {
                GradientChatMod.LOGGER.error("Failed to create config directory");
                return;
            }
            
            File presetsFile = new File(configDir, "presets.dat");
            
            NbtCompound root = new NbtCompound();
            root.putInt("currentHexPreset", currentHexPresetIndex);
            root.putInt("currentColorCodePreset", currentColorCodePresetIndex);
            root.putBoolean("useColorCodes", useColorCodes);
            root.putBoolean("enabled", enabled);
            
            NbtList hexPresetsList = new NbtList();
            for (GradientPreset preset : hexPresets) {
                NbtCompound presetNbt = new NbtCompound();
                presetNbt.putString("name", preset.getName());
                
                NbtList colorsList = new NbtList();
                for (int color : preset.getColors()) {
                    NbtCompound colorNbt = new NbtCompound();
                    colorNbt.putInt("value", color);
                    colorsList.add(colorNbt);
                }
                
                presetNbt.put("colors", colorsList);
                
                hexPresetsList.add(presetNbt);
            }
            
            NbtList colorCodePresetsList = new NbtList();
            for (GradientPreset preset : colorCodePresets) {
                NbtCompound presetNbt = new NbtCompound();
                presetNbt.putString("name", preset.getName());
                
                NbtList colorsList = new NbtList();
                for (int color : preset.getColors()) {
                    NbtCompound colorNbt = new NbtCompound();
                    colorNbt.putInt("value", color);
                    colorsList.add(colorNbt);
                }
                
                presetNbt.put("colors", colorsList);
                
                // Save color codes
                if (preset.getColorCodes() != null) {
                    presetNbt.putString("colorCodes", preset.getColorCodes());
                }
                
                colorCodePresetsList.add(presetNbt);
            }
            
            root.put("hexPresets", hexPresetsList);
            root.put("colorCodePresets", colorCodePresetsList);
            
            NbtIo.write(root, presetsFile.toPath());
        } catch (IOException e) {
            GradientChatMod.LOGGER.error("Failed to save presets", e);
        }
    }
    
    public void loadPresets() {
        File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "gradient-chat");
        File presetsFile = new File(configDir, "presets.dat");
        
        if (!presetsFile.exists()) {
            return;
        }
        
        try {
            NbtCompound root = NbtIo.read(presetsFile.toPath());
            if (root == null) {
                return;
            }
            
            currentHexPresetIndex = root.getInt("currentHexPreset").orElse(0);
            currentColorCodePresetIndex = root.getInt("currentColorCodePreset").orElse(0);
            
            // Load color code preference if available
            if (root.contains("useColorCodes")) {
                useColorCodes = root.getBoolean("useColorCodes").orElse(false);
            }
            
            NbtList hexPresetsList = root.getList("hexPresets").orElse(new NbtList());
            if (hexPresetsList.size() == NUM_PRESETS) {
                hexPresets.clear();
                
                for (int i = 0; i < hexPresetsList.size(); i++) {
                    NbtCompound presetNbt = hexPresetsList.getCompound(i).orElse(new NbtCompound());
                    String name = presetNbt.getString("name").orElse("");
                    
                    NbtList colorsList = presetNbt.getList("colors").orElse(new NbtList());
                    int[] colors = new int[COLORS_PER_PRESET];
                    
                    for (int j = 0; j < COLORS_PER_PRESET; j++) {
                        colors[j] = colorsList.getCompound(j).orElse(new NbtCompound()).getInt("value").orElse(0);
                    }
                    
                    hexPresets.add(new GradientPreset(name, colors));
                }
            }
            
            NbtList colorCodePresetsList = root.getList("colorCodePresets").orElse(new NbtList());
            if (colorCodePresetsList.size() == NUM_PRESETS) {
                colorCodePresets.clear();
                
                for (int i = 0; i < colorCodePresetsList.size(); i++) {
                    NbtCompound presetNbt = colorCodePresetsList.getCompound(i).orElse(new NbtCompound());
                    String name = presetNbt.getString("name").orElse("");
                    
                    NbtList colorsList = presetNbt.getList("colors").orElse(new NbtList());
                    int[] colors = new int[COLORS_PER_PRESET];
                    
                    for (int j = 0; j < COLORS_PER_PRESET; j++) {
                        colors[j] = colorsList.getCompound(j).orElse(new NbtCompound()).getInt("value").orElse(0);
                    }
                    
                    // Load color codes if available
                    String colorCodes = "";
                    if (presetNbt.contains("colorCodes")) {
                        colorCodes = presetNbt.getString("colorCodes").orElse("");
                    }
                    
                    colorCodePresets.add(new GradientPreset(name, colors, colorCodes));
                }
            }
        } catch (IOException e) {
            GradientChatMod.LOGGER.error("Failed to load presets", e);
        }
    }
    
    public GradientPreset getCurrentPreset() {
        return useColorCodes ? colorCodePresets.get(currentColorCodePresetIndex) : hexPresets.get(currentHexPresetIndex);
    }
    
    public void setCurrentPresetIndex(int index) {
        if (useColorCodes) {
            if (index >= 0 && index < colorCodePresets.size()) {
                currentColorCodePresetIndex = index;
                savePresets();
            }
        } else {
            if (index >= 0 && index < hexPresets.size()) {
                currentHexPresetIndex = index;
                savePresets();
            }
        }
    }
    
    public List<GradientPreset> getPresets() {
        return useColorCodes ? colorCodePresets : hexPresets;
    }
    
    public boolean isUsingColorCodes() {
        return useColorCodes;
    }
    
    public void setUseColorCodes(boolean useColorCodes) {
        this.useColorCodes = useColorCodes;
        savePresets();
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        savePresets();
    }
    
    public String applyGradient(String message) {
        if (message.isEmpty() || !enabled) {
            return message;
        }
        
        // Skip if the message contains only numbers
        if (message.matches("^[0-9]+$")) {
            return message;
        }
        
        GradientPreset preset = getCurrentPreset();
        
        if (useColorCodes) {
            // Use Minecraft color codes
            return applyColorCodes(message, preset);
        } else {
            // Use hex gradient
            return applyHexGradient(message, preset);
        }
    }
    
    private String applyColorCodes(String message, GradientPreset preset) {
        String colorCodePattern = preset.getColorCodes();
        if (colorCodePattern == null || colorCodePattern.isEmpty()) {
            // Fallback to hex if no color codes defined
            return applyHexGradient(message, preset);
        }
        
        // Extract just the color codes from the pattern (e.g., "&4", "&c", etc.)
        String[] codes = extractColorCodes(colorCodePattern);
        if (codes.length == 0) {
            return applyHexGradient(message, preset);
        }
        
        StringBuilder result = new StringBuilder();
        int messageLength = message.length();
        int numCodes = codes.length;
        
        // Calculate characters per color code
        int charsPerCode = (int) Math.ceil((double) messageLength / numCodes);
        if (charsPerCode < 1) charsPerCode = 1;
        
        for (int i = 0; i < messageLength; i++) {
            int codeIndex = Math.min(i / charsPerCode, numCodes - 1);
            
            // Add the color code before each character or at the start of each segment
            if (i % charsPerCode == 0 || i == 0) {
                result.append(codes[codeIndex]);
            }
            
            result.append(message.charAt(i));
        }
        
        return result.toString();
    }
    
    private String[] extractColorCodes(String pattern) {
        // Pattern is like "&4H &cE &6L &eL &fO"
        // We want to extract ["&4", "&c", "&6", "&e", "&f"]
        String[] parts = pattern.split(" ");
        String[] codes = new String[parts.length];
        
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.length() >= 2 && part.charAt(0) == '&') {
                codes[i] = part.substring(0, 2); // Extract just the color code
            } else {
                codes[i] = "&f"; // Default to white if invalid
            }
        }
        
        return codes;
    }
    
    private String applyHexGradient(String message, GradientPreset preset) {
        int[] colors = preset.getColors();
        StringBuilder result = new StringBuilder();
        
        // Calculate how many characters per color segment
        int messageLength = message.length();
        double charsPerSegment = (double) messageLength / (colors.length - 1);
        
        for (int i = 0; i < messageLength; i++) {
            char c = message.charAt(i);
            
            // Calculate which segment this character belongs to
            double segmentIndex = i / charsPerSegment;
            int startColorIndex = (int) Math.floor(segmentIndex);
            int endColorIndex = Math.min(startColorIndex + 1, colors.length - 1);
            
            // Calculate interpolation factor
            double factor = segmentIndex - startColorIndex;
            
            // Get interpolated color
            int color = interpolateColor(colors[startColorIndex], colors[endColorIndex], factor);
            
            // Format: &#RRGGBB
            result.append(formatHexColor(color));
            result.append(c);
        }
        
        return result.toString();
    }
    
    private int interpolateColor(int color1, int color2, double factor) {
        Color c1 = new Color(color1);
        Color c2 = new Color(color2);
        
        int red = (int) (c1.getRed() * (1 - factor) + c2.getRed() * factor);
        int green = (int) (c1.getGreen() * (1 - factor) + c2.getGreen() * factor);
        int blue = (int) (c1.getBlue() * (1 - factor) + c2.getBlue() * factor);
        
        return new Color(red, green, blue).getRGB() & 0xFFFFFF; // Remove alpha
    }
    
    private String formatHexColor(int color) {
        return String.format("&#%06X", color);
    }
    
    public List<GradientPreset> getHexPresets() {
        return new ArrayList<>(hexPresets);
    }
    
    public List<GradientPreset> getColorCodePresets() {
        return new ArrayList<>(colorCodePresets);
    }
    
    public int getCurrentHexPresetIndex() {
        return currentHexPresetIndex;
    }
    
    public int getCurrentColorCodePresetIndex() {
        return currentColorCodePresetIndex;
    }
    
    public void setCurrentHexPresetIndex(int index) {
        if (index >= 0 && index < hexPresets.size()) {
            currentHexPresetIndex = index;
            savePresets();
        }
    }
    
    public void setCurrentColorCodePresetIndex(int index) {
        if (index >= 0 && index < colorCodePresets.size()) {
            currentColorCodePresetIndex = index;
            savePresets();
        }
    }
    
    public void updateColorCodePresetColor(int presetIndex, String color) {
        if (presetIndex >= 0 && presetIndex < colorCodePresets.size()) {
            try {
                int colorValue = Integer.parseInt(color.replace("#", ""), 16);
                colorCodePresets.get(presetIndex).setColor(0, colorValue);
                savePresets();
            } catch (NumberFormatException e) {
                System.err.println("Invalid color format: " + color);
            }
        }
    }

    public void updateHexPresetColor(int presetIndex, String color) {
        if (presetIndex >= 0 && presetIndex < hexPresets.size()) {
            try {
                int colorValue = Integer.parseInt(color.replace("#", ""), 16);
                hexPresets.get(presetIndex).setColor(0, colorValue);
                savePresets();
            } catch (NumberFormatException e) {
                System.err.println("Invalid color format: " + color);
            }
        }
    }
    
    public static class GradientPreset {
        private String name;
        private int[] colors;
        private String colorCodes; // Minecraft color codes format
        
        public GradientPreset(String name, int[] colors) {
            this.name = name;
            this.colors = colors;
            this.colorCodes = ""; // Default empty
        }
        
        public GradientPreset(String name, int[] colors, String colorCodes) {
            this.name = name;
            this.colors = colors;
            this.colorCodes = colorCodes;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int[] getColors() {
            return colors;
        }
        
        public String getColorCodes() {
            return colorCodes;
        }
        
        public void setColorCodes(String colorCodes) {
            this.colorCodes = colorCodes;
        }
        
        public void setColor(int index, int color) {
            if (index >= 0 && index < colors.length) {
                colors[index] = color;
            }
        }
    }
}
