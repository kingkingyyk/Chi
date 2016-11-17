package FrameEntityManagement;

import Chi.WaitUI;

public class FrameActuatorManagementFeedbackWait extends WaitUI {
	private static final long serialVersionUID = 4973286001560655413L;
	private static FrameActuatorManagementFeedbackWait f=null;

	public FrameActuatorManagementFeedbackWait() {
		super();
	}
	
	public static FrameActuatorManagementFeedbackWait getCurrent() {
		if (FrameActuatorManagementFeedbackWait.f!=null && FrameActuatorManagementFeedbackWait.f.isVisible()) return FrameActuatorManagementFeedbackWait.f;
		else return null;
	}
}
