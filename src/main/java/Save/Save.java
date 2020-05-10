package Save;

import java.io.Serializable;

public class Save implements Serializable {
    private static final long serialVersionUID = 1L;


    private int playerHealth;

    public Save(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }


}
