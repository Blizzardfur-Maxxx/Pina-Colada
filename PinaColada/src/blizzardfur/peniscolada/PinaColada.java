package blizzardfur.peniscolada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class PinaColada extends JPanel implements ActionListener {
    private BufferedImage image;
    private double angle = 0;
    private Timer timer;

    public PinaColada() {
        try {
            image = ImageIO.read(getClass().getResource("/spin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = (getWidth() - image.getWidth()) / 2;
        int y = (getHeight() - image.getHeight()) / 2;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);
        g2d.drawImage(image, x, y, null);
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 1;
        repaint();
    }
    
    public static void playAudioLoop(String audioFilePath) {
        try {
            while (true) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(PinaColada.class.getResource(audioFilePath));
                
                AudioFormat audioFormat = audioInputStream.getFormat();
                
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
                
                line.start();
                
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }
                
                line.drain();
                line.close();
                audioInputStream.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Piña Colada");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.add(new PinaColada());
            frame.setVisible(true);
        });

        Thread audioThread = new Thread(() -> {
            playAudioLoop("/escape.wav");
        });
        audioThread.start();
    }

    private class MainPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int x = (getWidth() - image.getWidth()) / 2;
            int y = (getHeight() - image.getHeight()) / 2;

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);
            g2d.drawImage(image, x, y, null);
            g2d.dispose();
        }
    }
}
