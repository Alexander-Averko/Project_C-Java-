package GameState;

import Entity.Enemies.BigEnemy;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import Save.Save;
import TileMap.Background;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;


    private ArrayList<Enemy> enemies;

    //enemies spawn points
    private ArrayList<Point> points;

    private ArrayList<Explosion> explosions;

    //HUD
    private HUD hud;

    //Saves
    private String path = "src/main/resources/Saves/save.txt";



    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();

    }

    @Override
    public void init() {

        tileMap = new TileMap(32);
        tileMap.loadTiles("/Tilesets/tiles.gif");
        tileMap.loadMap("/Maps/map.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/bg.png", 0.1);


        player = new Player(tileMap);
        try {
            Save save = load();
            player.setHealth(save.getPlayerHealth());
            populateEnemies(save.getPoints());
        } catch (IOException | ClassNotFoundException e) {
            populateEnemies();
            e.printStackTrace();

        }
        player.setPosition(100, 100);

        hud = new HUD(player);




        explosions = new ArrayList<Explosion>();


    }

    private void populateEnemies() {

        enemies = new ArrayList<Enemy>();

        BigEnemy s;
        points = new ArrayList<Point>();
        points.add(new Point(200, 100));
        points.add(new Point(500, 100));

        for (int i = 0; i < points.size(); i++) {
            s = new BigEnemy(tileMap);
            s.setPosition(points.get(i).x, points.get(i).y);
            enemies.add(s);

        }

    }

    private void populateEnemies(ArrayList<Point> points) {
        enemies = new ArrayList<Enemy>();

        BigEnemy s;
        this.points = points;

        for (int i = 0; i < points.size(); i++) {
            s = new BigEnemy(tileMap);
            s.setPosition(points.get(i).x, points.get(i).y);
            enemies.add(s);

        }
    }

    @Override
    public void update() {


        //player update
        player.update();
        if (player.isDead()) reset();

        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getX(),
                GamePanel.HEIGHT / 2 - player.getY()
        );

        //set background
        bg.setPosition(tileMap.getX(), tileMap.getY());

        //attack enemies
        player.checkAttack(enemies);

        //update all enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                points.remove(i);
                i--;
                explosions.add(new Explosion(e.getX(), e.getY()));
            }
        }

        //update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
            }
        }

        //update HUD
        hud.update();

    }

    @Override
    public void draw(Graphics2D g) {


        //draw bg
        bg.draw(g);

        //draw tileMap
        tileMap.draw(g);

        //draw player
        player.draw(g);

        //draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition((int) tileMap.getX(), (int) tileMap.getY());
            explosions.get(i).draw(g);
        }

        //draw HUD
        hud.draw(g);



        //mirrors
        //g.copyArea(200, 200, 50, 50, -100,0);

    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_A) player.setLeft(true);
        if (k == KeyEvent.VK_D) player.setRight(true);
        if (k == KeyEvent.VK_W) player.setUp(true);
        if (k == KeyEvent.VK_S) player.setDown(true);
        if (k == KeyEvent.VK_SPACE) player.setJumping(true);
        if (k == KeyEvent.VK_E) player.setRolling();
        if (k == KeyEvent.VK_R) player.setScratching();
        if (k == KeyEvent.VK_F5) save();
        if (k == KeyEvent.VK_ESCAPE) gsm.setState(GameStateManager.MENUSTATE);

    }

    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_A) player.setLeft(false);
        if (k == KeyEvent.VK_D) player.setRight(false);
        if (k == KeyEvent.VK_W) player.setUp(false);
        if (k == KeyEvent.VK_S) player.setDown(false);
        if (k == KeyEvent.VK_SPACE) player.setJumping(false);

    }

    private void reset() {
        player.reset();
        player.setPosition(100, 100);
        hud.reset();
        populateEnemies();
    }

    private void save() {
        Save save = new Save(player.getHealth(), points);
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(save);
            outputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private Save load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Save save = (Save) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        return save;
    }


}
