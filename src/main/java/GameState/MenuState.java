package GameState;



import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "Start",
            "Help",
            "Quit"
    };

    private Color  titleColor;
    private Font titleFont;

    private Font font;




    public MenuState(GameStateManager gameStateManager) {
        this.gsm = gameStateManager;

        try {

            bg = new Background("/Backgrounds/menubg.gif", 1);

            bg.setVector(-0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.BOLD, 28 * GamePanel.SCALE);

            font = new Font("Arial", Font.PLAIN, 20 * GamePanel.SCALE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Project C", GamePanel.WIDTH * GamePanel.SCALE / 2, 70);

        //draw menu options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i],300, 140 + i * 30 * GamePanel.SCALE);
        }
    }

    private void select() {
        System.out.println(currentChoice);
        if (currentChoice == 0) {

            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1) {
            //help

        }
        if (currentChoice == 2) {
            //exit

            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }

        if ((k == KeyEvent.VK_UP)){
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }

        if ((k == KeyEvent.VK_DOWN)){
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
