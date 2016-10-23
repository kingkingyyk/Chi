package FrameEntityManagement;

import java.awt.Dimension;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import Chi.Theme;
import Chi.Utility;

public abstract class FrameContextMenu extends JPopupMenu  {
	private static final long serialVersionUID = 1L;

	protected JMenuItem addMenuItem(String text, String icon, String keyStroke) {
		JMenuItem i=new JMenuItem(text,Utility.resizeImageIcon(Theme.getIcon(icon),14,14));
		this.add(i);
		i.setPreferredSize(new Dimension(200,22));
		i.setAccelerator(KeyStroke.getKeyStroke(keyStroke));
		return i;
	}
	
}
