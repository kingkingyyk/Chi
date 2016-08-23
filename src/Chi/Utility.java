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

	public static String [] Hours={"12","01","02","03","04","05","06","07","08","09","10","11"};
	public static String [] Minutes; //generated dynamically, too long :P
	public static String [] AMPM={"AM","PM"};
	
    public static String [] formatTime (int h, int m) {
    	String [] s=new String [3];
    	s[0]=Hours[h%Hours.length];
    	s[1]=Minutes[m%Minutes.length];
    	s[2]=AMPM[h/Hours.length];
    	return s;
    }
    
    public static void initialize() {
		Minutes=new String [60];
		for (int i=0;i<60;i++) {
			if (i<10) {
				StringBuilder sb=new StringBuilder();
				sb.append("0");
				sb.append(i);
				Minutes[i]=sb.toString();
			} else {
				Minutes[i]=String.valueOf(i);
			}
		}
    }
}
