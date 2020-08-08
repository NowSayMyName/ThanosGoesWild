import javax.swing.ImageIcon;

public class Projectile extends Entity {
    public int timeAlive;
    public Projectile(int x, int y, int sizeX, int sizeY, boolean dir, int timeAlive) {
        super(x, y, "images/fireball.gif", sizeX, sizeY, 10);
        image = new ImageIcon("images/fireball.gif").getImage();
        fallEnabled = false;
        this.timeAlive = timeAlive;
        moveLR(dir);
    }
    public void move() {
        if (timeAlive > 0) {
            timeAlive--;
            getNewCoords();
        } else {
            sx = 0;
        }
    }

    public void getNewCoords(){
        super.getNewCoords();
        x += sx;
    }

}