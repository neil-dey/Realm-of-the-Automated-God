package AutoRealm;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PlayerController {

	// Update these two if firefox changes its dimensions
	private static final int UPPER_LEFT_CORNER_X = 360;
	private static final int UPPER_LEFT_CORNER_Y = 100;

	private static final int PLAYER_MINIMAP_POSITION_X = 1210;
	private static final int PLAYER_MINIMAP_POSITION_Y = 250;

	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 900;

	private static final int AUTO_ATTACK_KEY = KeyEvent.VK_I;
	private static final int NEXUS_KEY = KeyEvent.VK_F;
	private static final int UP_KEY = KeyEvent.VK_W;
	private static final int LEFT_KEY = KeyEvent.VK_A;
	private static final int DOWN_KEY = KeyEvent.VK_S;
	private static final int RIGHT_KEY = KeyEvent.VK_D;
	private static final int LEFT_MOUSE_BUTTON = InputEvent.BUTTON1_MASK;

	private Robot robot;
	private static final int minimapSize = 280;
	private static final Rectangle minimap = new Rectangle(UPPER_LEFT_CORNER_X + 910, UPPER_LEFT_CORNER_Y + 10,
			minimapSize, minimapSize);
	private int currentTargetOctant;

	public ArrayList<int[]> enemyPositions;

	PlayerController(Robot robot) {
		this.robot = robot;
		this.enemyPositions = new ArrayList<int[]>();
		this.currentTargetOctant = -1;
	}

	public void shootEnemies() {
		try {
			findEnemies();
			if(enemyPositions.size()==0) {
				return;
			}
			int[] closestEnemyPosition = this.enemyPositions.get(0);
	
			for (int i = 1; i < this.enemyPositions.size(); i++) {
				if (closestEnemyPosition[0] + closestEnemyPosition[1] > this.enemyPositions.get(i)[0]
						+ this.enemyPositions.get(i)[1]) {
					closestEnemyPosition = this.enemyPositions.get(i);
				}
			}
			
			closestEnemyPosition[1] -= 140;
			closestEnemyPosition[1] -= 140;
			
			robot.mouseMove(813+closestEnemyPosition[0]*2, 561+closestEnemyPosition[1]*2);
			System.out.println((813+closestEnemyPosition[0]*2) + " " + (561+closestEnemyPosition[1]*2));
			robot.mousePress(LEFT_MOUSE_BUTTON);
			robot.delay(100);
			robot.mouseRelease(LEFT_MOUSE_BUTTON);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			nexus();
			nexus();
			nexus();
			System.exit(0);
		}
	}

	/*
	 * Octants are arranged as follows:
	 * 
	 * 7  0  1
	 * 6  x  2
	 * 5  4  3
	 * 
	 * Where x is the current player position
	 */
	public void moveTo(int targetOctant) {
		if (targetOctant != currentTargetOctant) {
			robot.keyRelease(UP_KEY);
			robot.keyRelease(RIGHT_KEY);
			robot.keyRelease(DOWN_KEY);
			robot.keyRelease(LEFT_KEY);
			currentTargetOctant = targetOctant;
			if (targetOctant == 7 || targetOctant == 0 || targetOctant == 1) {
				//System.out.println("U");
				robot.keyPress(UP_KEY);
			}
			if (targetOctant == 1 || targetOctant == 2 || targetOctant == 3) {
				//System.out.println("R");
				robot.keyPress(RIGHT_KEY);
			}
			if (targetOctant == 3 || targetOctant == 4 || targetOctant == 5) {
				//System.out.println("D");
				robot.keyPress(DOWN_KEY);
			}
			if (targetOctant == 5 || targetOctant == 6 || targetOctant == 7) {
				//System.out.println("L");
				robot.keyPress(LEFT_KEY);
			}
			if(targetOctant < 0 || targetOctant > 7) {
				robot.keyPress(UP_KEY);
			}
		}

	}

	public void monitorHealth() {
		// If the halfway point of the HP Bar isn't red
		if (robot.getPixelColor(1050 + UPPER_LEFT_CORNER_X, 395 + UPPER_LEFT_CORNER_Y).getRed() < 200) {
			nexus();
		}
	}

	public void findEnemies() {

		this.enemyPositions.clear();
		BufferedImage miniMap = robot.createScreenCapture(minimap);
		int[] minimapPixel;

		for (int y = 0; y < miniMap.getHeight(); y += 6) {
			for (int x = 0; x < miniMap.getWidth(); x += 6) {
				minimapPixel = miniMap.getRaster().getPixel(x, y, new int[3]);
				if (minimapPixel[0] == 255 && minimapPixel[1] == 0 && minimapPixel[2] == 0) {
					this.enemyPositions.add(new int[] { x, y });
				}
			}
		}
	}

	public int findBestOctant() {
		int[] octantCount = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int[] pos : this.enemyPositions) {
			if (pos[0] < 93) {
				if (pos[1] < 93) {
					octantCount[7]++;
					continue;
				}
				if (pos[1] < 187) {
					octantCount[6]++;
					continue;
				}
				octantCount[5]++;
				continue;
			}
			if (pos[0] > 187) {
				if (pos[1] < 93) {
					octantCount[1]++;
					continue;
				}
				if (pos[1] < 187) {
					octantCount[2]++;
					continue;
				}
				octantCount[3]++;
				continue;
			}
			if (pos[1] < 140) {
				octantCount[0]++;
				continue;
			}
			octantCount[4]++;
		}

		// If nothing can be found, go forwards.
		int bestOctant = 0;
		for (int i = 0; i < 8; i++) {
			if (octantCount[bestOctant] < octantCount[i]) {
				bestOctant = i;
			}
		}

		// System.out.print(bestOctant + " " + octantCount[bestOctant]);
		return bestOctant;
	}
	
	public void nexus() {
		robot.keyPress(NEXUS_KEY);
		robot.delay(50);
		robot.keyRelease(NEXUS_KEY);
		robot.delay(100);
	}
}