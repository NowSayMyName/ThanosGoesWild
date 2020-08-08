
public class MobGroot extends Entity{
    public MobGroot(int x, int y, int sizeX, int sizeY) {
        super(x, y, "images/groot.png", sizeX, sizeY, 75);
        //sx = (5*(int)(2*Math.random()))-10;
        sx = 3;
    }
    public void getNewCoords(){     
        super.getNewCoords();
        if ((sx<0 && leftwall) || (sx>0 && rightwall)) {
            if (2*Math.random() > 1) {
                sx = -sx;
            } else {
                jump();
            }
        } else {
            sx += ax;
            x += sx;
        }
    }
}