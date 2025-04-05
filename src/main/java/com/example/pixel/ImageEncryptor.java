package com.example.pixel;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageEncryptor{

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String inputPath = "src/image/wallpaper.png";
        String outputPath = "src/image/out.png/output";
        System.out.println("enter the operation");
        String operation=scanner.nextLine();
        int key = 12345;

        try {
            BufferedImage image = ImageIO.read(new File(inputPath));
            BufferedImage encryptedImage = applyEncryption(image, operation, key);
            ImageIO.write(encryptedImage, "jpg", new File(outputPath));
            displayImage(encryptedImage, "Encrypted Image");
        } catch (IOException e) {
            System.err.println("Error processing the image: " + e.getMessage());
        }
        scanner.close();
    }

    public static BufferedImage applyEncryption(BufferedImage image, String operation, int key) {
        int w= image.getWidth();
        int h = image.getHeight();
        Random random = new Random(key);

        if ("swap".equalsIgnoreCase(operation)) {
            return swapPixels(image, w, h, random);
        } else if ("add".equalsIgnoreCase(operation)) {
            return adjustPixels(image, w, h, random, true);
        } else if ("subtract".equalsIgnoreCase(operation)) {
            return adjustPixels(image, w, h, random, false);
        } else {
            throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    private static BufferedImage swapPixels(BufferedImage image, int width, int height, Random random) {
        for (int i = 0; i < width * height / 2; i++) {
            int a = random.nextInt(width);
            int b = random.nextInt(height);
            int c = random.nextInt(width);
            int d = random.nextInt(height);

            int temp = image.getRGB(a, b);
            image.setRGB(a, b, image.getRGB(c, d));
            image.setRGB(c, d, temp);
        }
        return image;
    }

    private static BufferedImage adjustPixels(BufferedImage image, int width, int height, Random random, boolean add) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int offset = random.nextInt(50);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                if (add) {
                    r = Math.min(r + offset, 255);
                    g = Math.min(g + offset, 255);
                    b = Math.min(b + offset, 255);
                } else {
                    r = Math.max(r - offset, 0);
                    g = Math.max(g - offset, 0);
                    b = Math.max(b - offset, 0);
                }

                image.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return image;
    }

    private static void displayImage(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}