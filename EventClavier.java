import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class EventClavier extends JFrame implements KeyListener, ActionListener{
	private Dimension dim;
	private JPanel mainPanel;
	private Image background, impact, portal1, portal2;
	private BufferedImage monBuf; // buffer d’affichage
	private Thanos thanos;
	private MobGroot groot;
	private Level level;
	private int size, xOffset, xOffsetTerrain, xOffsetBackground, firstValue, lastValue, xImpact, yImpact;
	private Projectile fireball;
	private Hud hud;

	//timer
	private Timer t;
	private int dt = 20;

	public EventClavier(String nom){
        super(nom);
        dim = Toolkit.getDefaultToolkit().getScreenSize();

        //this.setSize(400, 850);//set la taille de la fenetre
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
		this.setUndecorated(true);//enleve la bande du haut

        //Jpanel principal (content pane)
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
		mainPanel.setBackground(Color.green);
		this.setContentPane(mainPanel);
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		createImages();

		size = 150;
		level = new Level(20, 4, "images/levels/level1/", 3, size, size);

		thanos = new Thanos(100, dim.height-200, size/2, size/2);
		groot = new MobGroot(800, 240, size/2, size/2);
		xImpact = yImpact = -1;
		fireball = new Projectile(-1, -1, size, size, false, 0);
		hud = new Hud(size, dim);

		t = new Timer(dt, this);
		t.start();
		
		monBuf = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
		background = background.getScaledInstance((int) (2*dim.getWidth()), (int) dim.getHeight(), Image.SCALE_DEFAULT);
		impact = impact.getScaledInstance(size,size,Image.SCALE_DEFAULT);
		portal1 = portal1.getScaledInstance(size/2,size,Image.SCALE_DEFAULT);
		portal2 = portal2.getScaledInstance(size/2,size,Image.SCALE_DEFAULT);

		repaint();
	}
   
	public void keyPressed(KeyEvent e) {
		if (!thanos.spawning) {
			int code = e.getKeyChar();
			//System.out.print("Code clavier "+code+"\n ");
			if (code == 100) { //touche d
				thanos.moveLR(true);
			} else if (code == 113) {//touche q
				thanos.moveLR(false);
			} else if (code == KeyEvent.VK_SPACE) {//touche z
				//System.out.println("jump");
				thanos.jump();
			} else if (code == 97 && fireball.timeAlive == 0 && thanos.canFireBall) {//touche a
				fireball = new Projectile(thanos.x+10, thanos.y-70, size, size, true, 100);
			} else if (code == 101) {//touche e
				hud.hudInteraction = !hud.hudInteraction;
			}
			repaint();
		}
	}

	public void keyReleased(KeyEvent e) {
		if (!thanos.spawning) {
			int code = e.getKeyChar();
			//System.out.print("Code clavier "+code+"\n ");
			if (code == 100) { //touche d
				thanos.stopMove(true);
			} else if (code == 113) {//touche q
				thanos.stopMove(false);
			}
		}
	}
	public void keyTyped(KeyEvent e) {
	}

	public void createImages() {
		try {
            // chemins de fichier
			File pathToBackground = new File("images/background.jpg");
			File pathToPortal1 = new File("images/portal1half.png");
			File pathToPortal2 = new File("images/portal2half.png");
			impact = new ImageIcon("images/impact.gif").getImage();

			// transformation en objet image des fichier
			background = ImageIO.read(pathToBackground);
			portal1 = ImageIO.read(pathToPortal1);
			portal2 = ImageIO.read(pathToPortal2);
        } catch (Exception e) {
            e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
        Prepaint(monBuf.getGraphics());

        //g.drawImage(monBuf, getInsets().left, getInsets().top, null);
        g.drawImage(monBuf, 0, 0, null);
    }

	public void Prepaint(Graphics g) {
		if (thanos.x < dim.width/2) {//start of level
			xOffset = thanos.x;
			xOffsetTerrain = 0;
			firstValue = 0;
		} else if (thanos.x > (level.length)*size-dim.width/2) {//end of level
			xOffset = thanos.x-level.length*size+dim.width;
			xOffsetTerrain = -level.length*size+dim.width;
			firstValue = level.length - dim.width/size - 1;
		} else { // all the rest
			xOffset = dim.width/2;
			xOffsetTerrain = - thanos.x + dim.width/2;
			firstValue =  (thanos.x - dim.width/2)/size;
		}
		lastValue = Math.min(level.length, firstValue+dim.width/size+2);
		xOffsetBackground = xOffsetTerrain/10;

		// draw background image
		g.drawImage(background, xOffsetBackground, 0, null);
		
		//for (int i=0; i<level.length; i++) {
		for (int i=firstValue; i<lastValue; i++) {
			for (int j=0; j<level.height+2; j++) {
				if ((level.lvl[i][j] == 3 && !thanos.canFireBall) || (level.lvl[i][j] > 0 && level.lvl[i][j] < 3 )) {
					g.drawImage(level.blocks[level.lvl[i][j]-1], size*i + xOffsetTerrain, dim.height-size*(j+1), null);
				}
			}
		}
		if (thanos.spawning) {
			g.drawImage(portal2, 100, 200, null);
		}

		//draw Thanos
		g.drawImage(thanos.image, xOffset, dim.height-thanos.y, null);

		//draw Groot
		g.drawImage(groot.image, xOffset+groot.x-thanos.x, dim.height-groot.y, null);

		if (thanos.spawning) {
			g.drawImage(portal1, 100, 200, null);
		}

		if (thanos.fallGif) {
			if (xImpact == -1 && yImpact == -1) {
				xImpact = thanos.x;
				yImpact = thanos.y;
				thanos.fallCounter = 0;
			}
			//System.out.println("fall");
			g.drawImage(impact, xOffset+xImpact-thanos.x+size/4-55, dim.height-yImpact-25, null);
			thanos.fallCounter++;
			if (thanos.fallCounter == 13) {
				thanos.fallGif = false;
				thanos.fallCounter = 0;
				xImpact = yImpact = -1;
			}
		}

		if (fireball.timeAlive>0) {
			//System.out.println("shooting");
			g.drawImage(fireball.image, xOffset+fireball.x-thanos.x+20, dim.height-fireball.y-thanos.sizeY-80, null);
			fireball.move();
		}

		if (hud.hudInteraction) {
			hud.openingAnimation();
		} else {
			hud.closingAnimation();
		}
		if (hud.xHeart-10 > 0) {
			g.drawImage(hud.leftHud, hud.x, 0, null);
		}
		if (thanos.life > 0) {
			g.drawImage(hud.heart, hud.xHeart, 10, null);
		}

		

		g.setColor(Color.black);
		//g.drawString("life:" + thanos.life, 50, 50);
		//g.drawString("timeAlive:" + fireball.timeAlive, 50, 50);
		g.drawString("can jump:" + thanos.canJump, 50, 140);
		g.drawString("falling:" + thanos.falling, 50, 170);
		//g.drawString("height:" + thanos.height, xOffset, thanos.y-50);
		//g.drawString("Groot: x: " + groot.x + " y: " + groot.y, 50, 50);
		//g.drawString("Fireball: x: " + (xOffset+fireball.x-thanos.x) + " y: " + fireball.y, 50, 80);
		//g.drawString("leftwall: " + groot.leftwall + " rightwall: " + groot.rightwall, 50, 80);
		//g.drawString("height:" + groot.height, 80, 110);
		//g.drawString("%x:" + thanos.x%size, xOffset, thanos.y-50);
		//g.drawString("x:" + thanos.x, xOffset, thanos.y-50);
		//g.drawString("xOffset:" + xOffset, xOffset, thanos.y-50);
		//g.drawString("xOffset:" + xOffset, 50, 100);
		//g.drawString("xOffsetTerrain:" + xOffsetTerrain, 50, 50);
		//g.drawString("xOffsetBackground:" + xOffsetBackground, 50, 80);
		//g.drawString("charged terrain: " + firstValue + "->" + lastValue, 50, 50);
		g.drawString("x:" + thanos.x/size + " y:" + thanos.y/size, 50, 110);
		g.drawString("x:" + thanos.x + " y:" + (thanos.y-thanos.sizeY), xOffset, dim.height-thanos.y-50);
		g.drawString("ax:" + thanos.ax + " ay:" + thanos.ay, xOffset, dim.height-thanos.y-80);
		g.drawString("sx:" + thanos.sx + " sy:" + thanos.sy, xOffset, dim.height-thanos.y-110);

		g.drawString("leftwall: " + thanos.leftwall + " rightwall: " + thanos.rightwall, 50, 50);
		g.drawString("downwall: " + thanos.downwall + " upwall: " + thanos.upwall, 50, 80);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==t) {
			update(thanos);
			update(groot);
			if (Math.sqrt(Math.pow(thanos.xCenter-groot.xCenter, 2) + Math.pow(thanos.yCenter-groot.yCenter, 2)) < (thanos.sizeX/2 + thanos.sizeY/2)) {
				thanos.damage(3);
				if (thanos.xCenter < groot.xCenter) {
					thanos.rightwall = true;
					groot.leftwall = true;
				} else {
					groot.rightwall = true;
					thanos.leftwall = true;
				}
			}
			if (!thanos.canFireBall && Math.sqrt(Math.pow(thanos.xCenter-(level.length-1)*size, 2) + Math.pow(thanos.yCenter-(5*size), 2)) < (thanos.sizeX/2 + thanos.sizeY/2)) {
				thanos.canFireBall = true;
			}
			repaint();
		}
	}

	public void update(Entity ent) {
		//for (Entity ent : listEnt) {
			ent.minY = size * Math.max(level.getHeight(ent.x/size), level.getHeight((ent.x + ent.sizeX)/size));
			//ent.minX = 
			ent.getNewCoords();
			/*
			if (!ent.downwall) {
				ent.y = ent.newY;
			} else {
				ent.y = ent.y-ent.y%size + size/2;
			}*/
			if (ent.x%size <= 2) {
				ent.leftwall = level.wallBlocked(true, ent.x/size, (ent.y-ent.sizeY)/size);
			} else {
				ent.leftwall = false;
			} 
			if (ent.x%size >= size/2 - 5){
				ent.rightwall = level.wallBlocked(false, ent.x/size, (ent.y-ent.sizeY)/size);
			} else {
				ent.rightwall = false;
			}
		//}
	}

	public static void main(String[] args) {
		EventClavier evf = new EventClavier("coucou");
	}
}
