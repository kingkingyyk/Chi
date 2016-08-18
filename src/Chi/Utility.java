package Chi;

import java.awt.Image;
import java.security.MessageDigest;

import javax.swing.ImageIcon;

public class Utility {

    public static ImageIcon resizeImageIcon (ImageIcon ic, int width, int height) {
    	return new ImageIcon(ic.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    
    public static String hashSHA1CharAry (char [] c) {
    	try {
    		MessageDigest md=MessageDigest.getInstance("SHA-1");
    		md.reset();
    		md.update(new String(c).getBytes());
    		return new String(md.digest());
    	} catch (Exception e) {}
    	return "";
    }

}
