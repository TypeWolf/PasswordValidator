package com.zipse;
import com.cthiebaud.passwordvalidator.*;
import javax.sound.sampled.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;

public class DemoValidatorMain implements PasswordValidator {
    private static final int MIN_LENGTH = 6;

    @Override
    public ValidationResult validate(String password) {
        if (password == null || password.isEmpty()) {
            playSound("password-validator\\src\\sounds\\unsecure.wav");
            printFileContent("password-validator\src\resources\securePasswordInstruction.txt");
            return new ValidationResult(false, "Password cannot be empty.");
        }

        if (password.length() >= MIN_LENGTH) {
            playSound("password-validator\\src\\sounds\\secure.wav");
            return new ValidationResult(true, "Password is valid.");
        } else {
            playSound("password-validator\\src\\sounds\\unsecure.wav");
            printFileContent("password-validator\src\resources\securePasswordInstruction.txt");
            return new ValidationResult(false, "Password must be at least " + MIN_LENGTH + " characters long.");
        }
    }

    private void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    private void printFileContent(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(filePath).getFile()))) {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
    }
}
}
