package GameState;


import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;


public class MenuState extends GameState {

    private Background bg;


    private int currentChoice = 0;

    private String[] currentOptions;
    private String[] options1 = {
            "RESUME",
            "NEW GAME",
            "SETTINGS",
            "EXIT"
    };

    private String[] options2 = {
            "NEW GAME",
            "SETTINGS",
            "EXIT"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    private File file;



    public MenuState(GameStateManager gameStateManager) {
        this.gsm = gameStateManager;

        try {

            bg = new Background("/Backgrounds/menu_background2.png", 1, GamePanel.WIDTH , GamePanel.HEIGHT);

            bg.setVector(-0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.BOLD, 28);


            font = new Font("Arial", Font.BOLD, 14);
            file = new File("src/main/resources/Saves/save.txt");
            if (file.exists()) currentOptions = options1;
            else currentOptions = options2;



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


        //draw menu options
        g.setFont(font);

        for (int i = 0; i < currentOptions.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(currentOptions[i], 230, 155 + i * 15);
        }

    }


    private void select() {
        if (file.exists()) {
            if (currentChoice == 0) {
                //resume
                gsm.setState(GameStateManager.LEVEL1STATE);
            }
            if (currentChoice == 1) {
                //new game
                deleteSave();
                gsm.setState(GameStateManager.LEVEL1STATE);
            }
            if (currentChoice == 2) {
                //options

            }
            if (currentChoice == 3) {
                //exit

                System.exit(0);
            }
        } else {
            if (currentChoice == 0) {
                //new game
                gsm.setState(GameStateManager.LEVEL1STATE);
            }

            if (currentChoice == 1) {
                //options

            }
            if (currentChoice == 2) {
                //exit

                System.exit(0);
            }
        }

    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }

        if ((k == KeyEvent.VK_UP)) {
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = currentOptions.length - 1;
            }
        }

        if ((k == KeyEvent.VK_DOWN)) {
            currentChoice++;
            if (currentChoice == currentOptions.length) {
                currentChoice = 0;
            }
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    private void deleteSave() {
        file.delete();
    }
}
