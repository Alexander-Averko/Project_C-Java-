package TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {

    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale;

    private int width;
    private  int height;


    public Background(String s, double ms) {


        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            moveScale = ms;
            width = image.getWidth();
            height = image.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Background(String s, double ms, int width, int height) {


        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            moveScale = ms;
            this.width = width;
            this.height = height;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
//        x += dx;
//        y += dy;

        x = dx;
        y = dy;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, width, height, null);

        //System.out.println(GamePanel.WIDTH * GamePanel.SCALE);

//        if (x < 0) {
//            g. drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
//        }
//
//        if (x > 0) {
//            g. drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
//        }
    }

}
