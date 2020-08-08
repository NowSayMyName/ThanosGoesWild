import javax.swing.ImageIcon;
import java.awt.*;


public class Thanos extends Entity {
    public int spawnTimer, checkpointX, checkpointY;
    public boolean leftpressed, rightpressed;
    public boolean spawning, canFireBall;
    public int life;

    public Thanos(int x, int y, int sizeX, int sizeY){
        super(x, y, "images/thanosGifs/thanosStanding.gif", sizeX, sizeY, 95);
        checkpointX = 300;
        checkpointY = 500;
        image = new ImageIcon("images/thanosGifs/StandingFrames/thanosStanding.gif").getImage();
        image = image.getScaledInstance(sizeX, sizeY, Image.SCALE_DEFAULT);
        spawnTimer = 0;
        spawning = downwall = true;
        leftpressed = rightpressed = false;
        canFireBall = false;
        sx = 1;//get out of spawn portal
        life = 10;
    }

    public void moveLR(boolean bool) {
        if (bool){
            rightpressed = true;
            sx = 10;
            //System.out.println("move right");
            if (leftpressed) {
                leftpressed = false;
            }
        } else {
            leftpressed = true;
            sx = -10;
            //System.out.println("move left");
            if (rightpressed) {
                rightpressed = false;
            }
        }
        getNewCoords();
    }

    public void stopMove(boolean move) {
        if (move && !leftpressed) {
            sx = 0;
            getNewCoords();
        } else  if (!move && !rightpressed) {
            sx = 0;
            getNewCoords();
        }
    }

    public void getNewCoords(){     
        if (spawnTimer < 80) {
            //System.out.println("spawn animation");
            downwall = true;
            spawnTimer++;
        } else if (spawnTimer == 80) {
            //System.out.println("spawned");
            downwall = false;
            spawning = false;
            sx = 0;
            spawnTimer++;
        }
        super.getNewCoords();

        if (sx<0 && leftwall) {
            ax = 0;
        } else if (sx>0 && rightwall) {
            ax = 0;
        } else {
            sx += ax;
            x += sx;
        }
    }
    public void damage(int dmg) {
        life -= dmg;
        if (life < 0) {
            x = checkpointX;
            y = checkpointY;
            life = 10;
        }
    }
}