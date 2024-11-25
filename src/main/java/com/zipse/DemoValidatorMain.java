package com.zipse;

import com.cthiebaud.passwordvalidator.*;
import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileReader;

public class DemoValidatorMain implements PasswordValidator {
    private static final int MIN_LENGTH = 6;

    @Override
    public ValidationResult validate(String password) {

        if (password == null || password.isEmpty()) {
            playWavFromClasspath("sounds/unsecure.wav");
            printFileContent("password-validator\src\resources\securePasswordInstruction.txt");
            return new ValidationResult(false, "Password cannot be empty.");
        }

        if (password.length() >= MIN_LENGTH) {
            playWavFromClasspath("sounds/secure.wav");
            return new ValidationResult(true, "Password is valid.");
        } else {
            playWavFromClasspath("sounds/unsecure.wav");
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

    /**
     * Plays a WAV file located in the classpath.
     * 
     * @param filePath the path to the WAV file relative to the classpath
     * @throws IOException                   if the file is not found or can't be
     *                                       read
     * @throws UnsupportedAudioFileException if the file format is unsupported
     * @throws LineUnavailableException      if the audio line is unavailable
     * @throws InterruptedException          if the playback is interrupted
     */
    public static void playWavFromClasspath(String filePath) {
        try (
                // Load the .wav file from the classpath
                InputStream inputStream = DemoValidatorMain.class.getClassLoader().getResourceAsStream(filePath);
                InputStream bufferedIn = new BufferedInputStream(inputStream);
                // Create an AudioInputStream from the InputStream
                AudioInputStream audioStream = inputStream == null ? null
                        : AudioSystem.getAudioInputStream(bufferedIn)) {
            if (audioStream == null) {
                throw new IOException("WAV file not found on the classpath: " + filePath);
            }

            // Obtain a Clip directly from the AudioSystem
            Clip clip = AudioSystem.getClip();

            // Open the audio stream
            clip.open(audioStream);

            try {
                // Play the audio
                clip.start();

                // Keep the program running to allow the sound to play completely
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            } finally {
                clip.close(); // Close the clip explicitly
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printFileContent(String filePath) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(getClass().getClassLoader().getResource(filePath).getFile()))) {
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
