package SchedulingServer;

import java.time.LocalDateTime;

public class ScheduleUtility {

	//up to minute precision.
	public static int compareScheduleTime(LocalDateTime d1, LocalDateTime d2) {
		int [] d1_data=new int [] {d1.getYear(),d1.getMonthValue(),d1.getDayOfMonth(),d1.getHour(),d1.getMinute()};
		int [] d2_data=new int [] {d2.getYear(),d2.getMonthValue(),d2.getDayOfMonth(),d2.getHour(),d2.getMinute()};
		
		for (int i=0;i<d1_data.length;i++) {
			if (d1_data[i]>d2_data[i]) return 1;
			else if (d1_data[i]<d2_data[i]) return -1;
		}
		return 0;
	}
}
