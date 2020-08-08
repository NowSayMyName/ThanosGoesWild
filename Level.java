import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;

public class Level {

    public int[][] lvl;
    public int length, height, num, sizeX, sizeY;
    public Image[] blocks;
    public Level(int length, int height, String path, int num, int sizeX, int sizeY) {
        this.length = length;
        this.height = height;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.num = num;
        lvl = new int[length][height+2];
        blocks = new Image[num];
        createImages(path);
        generateLevel();
    }
    public void generateLevel() {
        for (int k=0; k<length; k++) {
            lvl[k][0] = 2;
        }
        for (int j=1; j<4; j++) {
            for (int i=0; i<length; i++) {
                if (j==0) {
                    lvl[i][j] = (int)(3*Math.random());
                } else {
                    if (lvl[i][j-1] == 0 || lvl[i][j-1] == 1) {
                        lvl[i][j] = 0;
                    } else {
                        lvl[i][j] = 1+(int)(2*Math.random());
                    }
                }
                //System.out.print(lvl[i][j] + " ");
            }
            //System.out.print("\n");
        }
        lvl[length-1][height+1] = 3;
    }
    public int getHeight(int here) {
        if (here >= 0 && here<length) {
            for (int j=0; j<height; j++) {
                if (lvl[here][j] == 0) {
                    return j;
                }
            }
            return height;
        }
        return height+5;
    }

    public boolean wallBlocked(boolean bool, int x, int y) {
        //System.out.println("x " + x);
        //System.out.println("y " + y);

        if ((bool && x == 0) || (!bool && x == length-1)) {
            //System.out.println("true");
            return true;
        } else if (y > height-1) {
            return false;
        } else if (bool) {
            return lvl[x-1][y] != 0;
        } else {
            return lvl[x+1][y] != 0;
        }
    }

    public void createImages(String path) {
        for (int i=0; i < num; i++) {
            try {
                //System.out.println(path + (i+1) + ".png");
                File pathToImage = new File(path + (i+1) + ".png");
                blocks[i] = ImageIO.read(pathToImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            blocks[i] = blocks[i].getScaledInstance(sizeX, sizeY, Image.SCALE_DEFAULT);
        }
    }
    
    public void collisionWall(int x, int y, Entity ent) {
        //System.out.println("x: " + ((ent.xCenter-ent.sizeX/2)/sizeX) + " y: " + ((ent.yCenter - ent.sizeY/2)/sizeY));
        if (lvl[(x-ent.sizeX/2)/sizeX][Math.min(5,(y - ent.sizeY/2)/sizeY-1)] > 0 || lvl[(x+ent.sizeX/2)/sizeX][Math.min(5,(y - ent.sizeY/2)/sizeY-1)] > 0) {
            ent.downwall = true;
        } else {
            ent.downwall = false;
        }

/*
        if (ent.x%size <= 2) {
            ent.leftwall = wallBlocked(true, ent.x/sizeX, ((dim.height-ent.y)-size/2)/size);
        } else {
            ent.leftwall = false;
        } 
        if (ent.x%size >= size/2 - 5){
            ent.rightwall = wallBlocked(false, ent.x/size, ((dim.height-ent.y)-size/2)/size);
        } else {
            ent.rightwall = false;
        }*/
    }
}