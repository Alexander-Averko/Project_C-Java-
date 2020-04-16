package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends MapObject {

    //player stuff
    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;

    private ArrayList<FireBall> fireBalls;

    //scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    //rolling
    private boolean rolling;
    private double rollingSpeed;
    private double maxRollingSpeed;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            8, 8, 2, 1, 6, 12, 10
    };

    //animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int ROLLING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;


    public Player(TileMap tm) {
        super(tm);

        width = 89;
        height = 76;
        cwidth = 10;
        cheight = 50;

        moveSpeed = 0.3;
        rollingSpeed = 3;
        maxRollingSpeed = 3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        fire = maxFire = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        fireBalls = new ArrayList<FireBall>();

        scratchDamage = 10;
        scratchRange = 50;


        //load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/player_sprite3.gif"
                    )
            );
            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < numFrames.length; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    if (i != SCRATCHING) {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    } else {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                }
                sprites.add(bi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(80);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public void setFiring() {
        firing = true;
    }

    public void setScratching() {
        if (!rolling) scratching = true;
    }

    public void setRolling() {
        if (!scratching && !falling && !jumping) rolling = true;
    }


    public void checkAttack(ArrayList<Enemy> enemies) {

        //loop through enemies
        for (int i = 0; i < enemies.size(); i++) {

            Enemy e = enemies.get(i);

            //scratch attack
            if (scratching) {
                if (facingRight) {
                    if (
                            (e.getX() > x) &&
                                    (e.getX() < x + scratchRange) &&
                                    (e.getY() > y - height / 2) &&
                                    (e.getY() < y + height / 2)
                    ) {
                        e.hit(scratchDamage);
                    }
                } else {
                    if (
                            (e.getX() < x) &&
                                    (e.getX() > x - scratchRange) &&
                                    (e.getY() > y - height / 2) &&
                                    (e.getY() < y + height / 2)
                    ) {
                        e.hit(scratchDamage);
                    }
                }
            }

            // fireballs
            for (int j = 0; j < fireBalls.size(); j++) {
                if (fireBalls.get(j).intersects(e)) {
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            //check enemy collision
            if (intersects(e)) {
                hit(e.getDamage());
            }

        }
    }

    public void hit(int damage) {
        if (flinching) return;
        health -= damage;
        if (health < 0) health = 0;
        if (health == 0) dead = true;


        flinching = true;
        flinchTimer = System.nanoTime();
    }

    private void getNextPosition() {

        //movement
        if (left && !rolling) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right && !rolling) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        //rolling
        else if (rolling && !jumping && !falling) {

            if (facingRight) {
                dx += rollingSpeed;
                if (dx > maxRollingSpeed) {
                    dx = maxRollingSpeed;
                }
            } else {
                dx -= rollingSpeed;
                if (dx < -maxRollingSpeed) {
                    dx = -maxRollingSpeed;
                }
            }
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }


        //cannot move while attacking, except in air
        if ((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
            dx = 0;
        }
        if (currentAction == ROLLING) {
            jumping = false;
        }


        //jumping
        if (jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        //falling
        if (falling) {
            rolling = false;
            //if(dy > 0 && rolling) dy += fallSpeed * 0.1;
            //else
            dy += fallSpeed;

            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += stopJumpSpeed;

            if (dy > maxFallSpeed) dy = maxFallSpeed;

        }
    }

    public void update() {


        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //check attack has stopped
        if (currentAction == SCRATCHING) {
            if (animation.hasPlayedOnce()) scratching = false;
        }
        if (currentAction == FIREBALL) {
            if (animation.hasPlayedOnce()) firing = false;
        }

        //check rolling has stopped
        if (currentAction == ROLLING) {
            if (animation.hasPlayedOnce()) rolling = false;
        }

        //fireball attack
        fire += 1;
        if (fire > maxFire) fire = maxFire;
        if (firing && currentAction != FIREBALL) {
            if (fire > fireCost) {
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }

        //update fireballs
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()) {
                fireBalls.remove(i);
                i--;
            }
        }

        //check done flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1000) {
                flinching = false;
            }
        }

        //set animation
        if (scratching) {
            if (currentAction != SCRATCHING) {
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(80);
                width = 89;
            }
        } else if (firing) {
            if (currentAction != FIREBALL) {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(80);
                width = 89;
            }
        } else if (dy > 0) {

            if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(80);
                width = 89;
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 89;
            }
        }
        //rolling
        else if (rolling) {
            if (currentAction != ROLLING) {
                currentAction = ROLLING;
                animation.setFrames(sprites.get(ROLLING));
                animation.setDelay(80);
                width = 89;
            }

        }
        //movement
        else if (left || right) {

            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(80);
                width = 89;
            }

        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(80);
                width = 89;
            }
        }
        animation.update();

        //set direction
        if (currentAction != SCRATCHING && currentAction != FIREBALL && currentAction != ROLLING) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }


    }

    public void draw(Graphics2D g) {
        setMapPosition();


        //draw fireballs
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).draw(g);
        }

        //draw player
        if (flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }
        }


        super.draw(g);

    }
}
