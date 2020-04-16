package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HUD {

    private Player player;

    private BufferedImage healthImage;


    private int width = 32;
    private int height = 32;

    private ArrayList<BufferedImage[]> healthSprites;
    private final int[] numHealthFrames = {
            1, 7, 10
    };

    private ArrayList<Integer> hearts;

    private static final int IDLE = 0;
    private static final int CREATE = 1;
    private static final int BREAK = 2;

    private Animation animation;

    private int currentHeart;

    public HUD(Player p) {
        player = p;
        try {

            healthImage = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/HUD/health_sprite.png"
                    )
            );


            healthSprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < numHealthFrames.length; i++) {
                BufferedImage[] bi = new BufferedImage[numHealthFrames[i]];
                for (int j = 0; j < numHealthFrames[i]; j++) {

                    bi[j] = healthImage.getSubimage(
                            j * width,
                            i * height,
                            width,
                            height
                    );

                }
                healthSprites.add(bi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        animation = new Animation();

        hearts = new ArrayList<Integer>();
        for (int i = 0; i < player.getHealth(); i++) {
            hearts.add(CREATE);
        }

        currentHeart = 0;
        animation.setFrames(healthSprites.get(CREATE));
        animation.setDelay(20);


    }

    public void setCreating() {
        hearts.add(CREATE);
    }

    public void setBreaking(int h) {

        for (int i = hearts.size() - 1; i >= player.getHealth(); i--) {

            hearts.set(i, BREAK);
        }
    }

    public void update() {
        if (hearts.get(currentHeart) != IDLE) animation.update();
        if (hearts.get(currentHeart) == BREAK) {
            if (animation.hasPlayedOnce()) {
                hearts.remove(currentHeart);


                currentHeart = (currentHeart >= hearts.size() - 1) ? 0 : currentHeart + 1;

                animation.setFrames(healthSprites.get(hearts.get(currentHeart)));
                animation.setDelay(20);
            }
        } else if (hearts.get(currentHeart) == CREATE) {
            if (animation.hasPlayedOnce()) {
                hearts.set(currentHeart, IDLE);


                currentHeart = (currentHeart >= hearts.size() - 1) ? 0 : currentHeart + 1;

                animation.setFrames(healthSprites.get(hearts.get(currentHeart)));
                animation.setDelay(20);
            }
        } else {

            if (hearts.size() > player.getHealth()) {
                for (int i = hearts.size() - 1; i >= player.getHealth(); i--) {

                    hearts.set(i, BREAK);
                }
                currentHeart = hearts.size() - 1;
            } else if (hearts.size() < player.getHealth()) {
                for (int i = 0; i < player.getHealth() - hearts.size(); i++) {

                    hearts.add(CREATE);
                }
                ;

            } else

                currentHeart = (currentHeart >= hearts.size() - 1) ? 0 : currentHeart + 1;
            animation.setFrames(healthSprites.get(hearts.get(currentHeart)));
            animation.setDelay(20);

        }


    }

    public void draw(Graphics2D g) {

        for (int i = 0; i < hearts.size(); i++) {

            if ((hearts.get(i) == IDLE) || ((hearts.get(i) == BREAK) && (i != currentHeart)))
                g.drawImage(healthSprites.get(IDLE)[0], 10 + i * 20, 10, null);
            else g.drawImage(animation.getImage(), 10 + currentHeart * 20, 10, null);
        }


    }
}
