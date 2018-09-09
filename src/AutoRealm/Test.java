package AutoRealm;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
public class Test {
	public static void main(String[] args) throws AWTException {
		Point pi; 
		Robot robot = new Robot();
		PlayerController player = new PlayerController(robot);
		robot.delay(1000);
		while(true) {
//			pi = MouseInfo.getPointerInfo().getLocation();
//			System.out.println(pi.getX() + " " + pi.getY());
//			System.out.println(robot.getPixelColor((int)pi.getX(), (int)pi.getY()));
			
//		
			player.monitorHealth();
			
			player.findEnemies();
			
			player.moveTo(player.findBestOctant());
			
			player.shootEnemies();
			
			
		}
	}
	
//	showImageOnScreen(BufferedImage i){
//		JFrame frame = new JFrame();
//		frame.getContentPane().setLayout(new FlowLayout());
//		frame.getContentPane().add(new JLabel(new ImageIcon(i)));
//		frame.pack();
//		frame.setVisible(true);
//	}
}

//Full Game Window
//(360, 101) Upper Left
//(360, 1000) Lower left
//(1160, 1000) Lower Right
//(1160, 101) Upper Right

//HP Bar:
//(1278, 495) to (1542, 495)

//MP Bar:
//(1278, 530) to (1542, 530)

//MiniMap:
//(1267, 110) Upper Left
//(1267, 390) Lower Left
//(1550, 390) Lower Right
//l(1150, 110) Upper Right