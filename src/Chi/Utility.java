package Chi;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Utility {

    public static ImageIcon resizeImageIcon (ImageIcon ic, int width, int height) {
    	return new ImageIcon(ic.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    
}
