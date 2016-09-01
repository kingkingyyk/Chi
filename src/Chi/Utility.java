package Chi;

import java.awt.Image;
import java.security.MessageDigest;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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

    public static Date localDateTimeToDate (LocalDateTime dt) {
        return new Date(dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
    
    public static LocalDateTime dateToLocalDateTime (Date d) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneOffset.UTC);
    }
    
	public static String [] Days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	public static String [] DaysShort={"Mo","Tu","We","Th","Fr","Sa","Su"};
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
    
    public static String dayMaskToStr (int mask) {
		StringBuilder sb=new StringBuilder();
		for (int i=1;i<=7;i++) { //1 = Monday, 7 = Sunday
			if ((mask & (1 << i))!=0) {
				sb.append(DaysShort[i-1]);
				sb.append(',');
			}
		}
		if (sb.length()==0) {
			sb.append("X,");
		}
		String s=sb.toString();
		return s.substring(0,s.length()-1);
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
