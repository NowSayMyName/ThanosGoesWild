import java.io.File;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Entity {
    protected String path;
    protected Image image;
    protected int x, y, sx, sy, ax, ay, fallCounter, sizeX, sizeY, xCenter, yCenter, jumpA, minY, maxY, minX, maxX;
    protected boolean leftwall, rightwall, upwall, downwall, falling, fallGif, fallEnabled, canJump;
    public Entity(int x, int y, String path, int sizeX, int sizeY, int jumpA) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.path = path;
        ax = ay = sx = sy = 0;
        this.x=x;
        this.y=y;
        falling = leftwall = rightwall = upwall = downwall = false;
        createImages();
        fallEnabled = canJump = true;
        this.jumpA = jumpA;
        getNewCoords();
    }

    public void moveLR(boolean bool) {
        if (bool){
            sx = 10;
            //System.out.println("move left");
        } else {
            sx = -10;
            //System.out.println("move right");
        }
        getNewCoords();
    }

    public void getNewCoords(){   
        if (sy <= 0 && downwall) {
            canJump = true;
        }  
        /*
        System.out.println("ax : " + ax);
        System.out.println("ay : " + ay);
        System.out.println("sx : " + sx);
        System.out.println("sy : " + sy);
        System.out.println("x : " + x);
        System.out.println("y : " + y);*/
        if (fallEnabled) {
            fall();
        }
        xCenter = x + sizeX/2;
        yCenter = y + sizeY/2;
    }

    public void fall() {
        if (sy < -50) {
            falling = true;
            //System.out.println("falling");
        } else if (y == minY+sizeY && falling) {//cette condition entraine un bug: si on saute en étant déjà en l'air et on atteint une vitesse nulle y'a l'animation
            //System.out.println("not falling");
            falling = false;
            fallGif = true;
            fallCounter = 0;
            sy = 0;
        }

        if (ay <= 0 && y == minY+sizeY) {
            ay = 0;
            sy = 0;
            canJump = true;
            //System.out.println("can't go down");
        } else {
            //System.out.println("can move");
            sy += ay - 8;//gravity
            y = Math.max(minY+sizeY, y + sy);
        }
    }

    public void createImages() {
        if (path.contains(".png")) {
            //System.out.println("png");
            try {
                File pathToImage = new File(path);
                image = ImageIO.read(pathToImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //System.out.println("not png (gif)");
            image = new ImageIcon("path").getImage();
        }
		
        image = image.getScaledInstance(sizeX, sizeY, Image.SCALE_DEFAULT);
    }

    public void jump(){
        if (canJump) {
            System.out.println("jump");
            canJump = false;
            ay += jumpA;
            this.getNewCoords();
            ay = 0;
        }
    }
}