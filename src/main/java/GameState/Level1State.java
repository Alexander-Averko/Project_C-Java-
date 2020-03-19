package GameState;

import Entity.Enemies.Slugger;
import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;

    private HUD hud;

    private ArrayList<Enemy> enemies;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();

    }

    @Override
    public void init() {

        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(0.07);

        bg = new Background("/Backgrounds/menubg.gif", 0.1);

        player = new Player(tileMap);

        player.setPosition(100, 100);

        enemies = new ArrayList<Enemy>();
        Slugger s = new Slugger(tileMap);
        s.setPosition(100, 100);
        enemies.add(s);

        hud = new HUD(player);
    }

    @Override
    public void update() {
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getX(),
                GamePanel.HEIGHT / 2 - player.getY()
        );

        //set background
        bg.setPosition(tileMap.getX(), tileMap.getY());

        //update all enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }
    }

    @Override
    public void draw(Graphics2D g) {

        bg.draw(g);

        tileMap.draw(g);

        player.draw(g);

        //draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        hud.draw(g);

    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_A) player.setLeft(true);
        if (k == KeyEvent.VK_D) player.setRight(true);
        if (k == KeyEvent.VK_W) player.setUp(true);
        if (k == KeyEvent.VK_S) player.setDown(true);
        if (k == KeyEvent.VK_SPACE) player.setJumping(true);
        if (k == KeyEvent.VK_E) player.setGliding(true);
        if (k == KeyEvent.VK_R) player.setScratching();
        if (k == KeyEvent.VK_F) player.setFiring();

    }

    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_A) player.setLeft(false);
        if (k == KeyEvent.VK_D) player.setRight(false);
        if (k == KeyEvent.VK_W) player.setUp(false);
        if (k == KeyEvent.VK_S) player.setDown(false);
        if (k == KeyEvent.VK_SPACE) player.setJumping(false);
        if (k == KeyEvent.VK_E) player.setGliding(false);
    }
}
