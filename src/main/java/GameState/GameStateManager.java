package GameState;


import java.awt.*;

public class GameStateManager {

    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    public static final int NUMGAMESTATE = 2;

    private GameState[] gameStates;
    private int currentState;

    public GameStateManager() {
        gameStates = new GameState[NUMGAMESTATE];

        currentState = MENUSTATE;
        loadState(currentState);
    }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void update() {
        try {
            gameStates[currentState].update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {
        try {
            gameStates[currentState].draw(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }

    private void loadState(int state) {
        if (state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if (state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
        }
    }
    private void unloadState(int state) {
        gameStates[state] = null;
    }
}
