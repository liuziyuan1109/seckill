package com.example.seckill_backend.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static Captcha generateCaptcha(int width, int height, int length) {
        StringBuilder captchaText = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            captchaText.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        for (int i = 0; i < length; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(String.valueOf(captchaText.charAt(i)), 20 * i + 5, 25);
        }

        g.dispose();

        return new Captcha(captchaText.toString(), image);
    }

    public static class Captcha {
        private final String text;
        private final BufferedImage image;

        public Captcha(String text, BufferedImage image) {
            this.text = text;
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public BufferedImage getImage() {
            return image;
        }
    }
}

