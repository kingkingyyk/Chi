package Reading;

import Entity.Sensor;

public class FrameLatestReadingActions {

	public static void showLive(FrameLatestReading m) {
		for (Sensor s : m.getSelectedSensors()) {
			FrameLiveReading f=FrameLiveReading.getInstance(s);
			f.setVisible(true);
		}
	}
	
	public static void exportDaily(FrameLatestReading m) {
		DialogReadingExportSelectDayRange diag=new DialogReadingExportSelectDayRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
	
	public static void exportMonthly(FrameLatestReading m) {
		DialogReadingExportSelectMonthRange diag=new DialogReadingExportSelectMonthRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
	
	public static void exportYearly(FrameLatestReading m) {
		DialogReadingExportSelectYearRange diag=new DialogReadingExportSelectYearRange();
		diag.s=m.getSelectedSensor();
		diag.setLocationRelativeTo(null);
		diag.setVisible(true);
	}
}
