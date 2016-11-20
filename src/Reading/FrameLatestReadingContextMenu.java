package Reading;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import FrameEntityManagement.FrameContextMenu;

public class FrameLatestReadingContextMenu extends FrameContextMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem liveUpdateMenu;
	private JMenuItem exportInstanceMenu;
	private JMenuItem exportDailyMenu;
	private JMenuItem exportMonthlyMenu;
	private JMenuItem exportYearlyMenu;
	
	public FrameLatestReadingContextMenu (FrameLatestReading m) {
		this.liveUpdateMenu=addMenuItem("Show Live Update","GraphIcon","ENTER");
		liveUpdateMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameLatestReadingActions.showLive(m);}
		});
		
		this.exportInstanceMenu=addMenuItem("Export Instance Report...","EditIcon","I");
		exportInstanceMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { FrameLatestReadingActions.exportInstance(m);}
		});
		
		this.exportDailyMenu=addMenuItem("Export Daily Report...","EditIcon","D");
		exportDailyMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { FrameLatestReadingActions.exportDaily(m);}
		});
		
		this.exportMonthlyMenu=addMenuItem("Export Monthly Report...","EditIcon","M");
		exportMonthlyMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { FrameLatestReadingActions.exportMonthly(m);}
		});
		
		this.exportYearlyMenu=addMenuItem("Export Yearly Report...","EditIcon","Y");
		exportYearlyMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { FrameLatestReadingActions.exportYearly(m);}
		});
		
		this.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				liveUpdateMenu.setEnabled(m.getSelectedCount()>0);
				exportDailyMenu.setEnabled(m.getSelectedCount()==1);
				exportMonthlyMenu.setEnabled(m.getSelectedCount()==1);
				exportYearlyMenu.setEnabled(m.getSelectedCount()==1);
			};
		
		});

	}
	
}
