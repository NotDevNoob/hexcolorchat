package com.example.gradientchat.gui;

import com.example.gradientchat.GradientManager;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private final GradientManager gradientManager;
    private final List<Profile> profiles;
    private Profile currentProfile;
    
    public ProfileManager(GradientManager gradientManager) {
        this.gradientManager = gradientManager;
        this.profiles = new ArrayList<>();
        
        // Create default profile
        Profile defaultProfile = new Profile("Default");
        profiles.add(defaultProfile);
        currentProfile = defaultProfile;
    }
    
    public void addProfile(String name) {
        Profile profile = new Profile(name);
        profiles.add(profile);
        if (currentProfile == null) {
            currentProfile = profile;
        }
    }
    
    public void removeProfile(Profile profile) {
        if (profiles.size() > 1 && profiles.contains(profile)) {
            profiles.remove(profile);
            if (currentProfile == profile) {
                currentProfile = profiles.get(0);
            }
        }
    }
    
    public void setCurrentProfile(Profile profile) {
        if (profiles.contains(profile)) {
            currentProfile = profile;
        }
    }
    
    public Profile getCurrentProfile() {
        return currentProfile;
    }
    
    public List<Profile> getProfiles() {
        return new ArrayList<>(profiles);
    }
    
    public GradientManager getGradientManager() {
        return gradientManager;
    }
    
    public static class Profile {
        private final String name;
        private final int[] colors;
        private boolean enabled;
        
        public Profile(String name) {
            this.name = name;
            // Default rainbow gradient colors
            this.colors = new int[] {
                0xFF0000, // Red
                0xFF7F00, // Orange
                0xFFFF00, // Yellow
                0x00FF00, // Green
                0x0000FF, // Blue
                0x4B0082, // Indigo
                0x9400D3  // Violet
            };
            this.enabled = true;
        }
        
        public String getName() {
            return name;
        }
        
        public int[] getColors() {
            return colors;
        }
        
        public void setColor(int index, int color) {
            if (index >= 0 && index < colors.length) {
                colors[index] = color;
            }
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
} 