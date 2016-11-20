package Reading;

import Entity.Sensor;

public class FrameLatestReadingActions {

	public static void showLive(FrameLatestReading m) {
		for (Sensor s : m.getSelectedSensors()) {
			FrameLiveReading f=FrameLiveReading.getInstance(s);
			f.setVisible(true);
		}
	}
	
	public static void exportInstance(FrameLatestReading m) {
		DialogReadingExportInstanceSelectRange diag=new DialogReadingExportInstanceSelectRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
	
	public static void exportDaily(FrameLatestReading m) {
		DialogReadingExportDailySelectRange diag=new DialogReadingExportDailySelectRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
	
	public static void exportMonthly(FrameLatestReading m) {
		DialogReadingExportMonthlySelectRange diag=new DialogReadingExportMonthlySelectRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
	
	public static void exportYearly(FrameLatestReading m) {
		DialogReadingExportYearlySelectRange diag=new DialogReadingExportYearlySelectRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
}
