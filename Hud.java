import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;

public class Hud {
    public Image leftHud, heart;
    public int x, width, xHeart;
    public boolean hudInteraction;
    public Hud(int size, Dimension dim) {
        width = dim.width;
        hudInteraction = false;
        x = -width/4;
        xHeart = 10;
        createImages();
        heart = heart.getScaledInstance(size/4,size/4,Image.SCALE_DEFAULT);
		leftHud = leftHud.getScaledInstance(width/4,dim.height,Image.SCALE_DEFAULT);
    }

    public void createImages() {
		try {
            // chemins de fichier
            File pathToHeart = new File("images/heart.png");
            File pathToLeftHud = new File("images/hud.jpg");

			// transformation en objet image des fichier
            heart = ImageIO.read(pathToHeart);
            leftHud = ImageIO.read(pathToLeftHud);
        } catch (Exception e) {
            e.printStackTrace();
		}
    }
    public void openingAnimation() {
        if (x < 0) {
            hudInteraction = true;
            x+=15;
            xHeart = x+10+width/4;
        }
    }
    public void closingAnimation() {
        if (x > -width/4) {
            hudInteraction = false;
            x-=15;
            xHeart = x+10+width/4;

        }
    }
}