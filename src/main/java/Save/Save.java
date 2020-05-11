package Save;

import Entity.Enemy;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Save implements Serializable {
    private static final long serialVersionUID = 1L;


    private int playerHealth;

    private ArrayList<Point> points;


    public Save(int playerHealth, ArrayList<Point> enemies) {
        this.playerHealth = playerHealth;
        this.points = enemies;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }


    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

}
