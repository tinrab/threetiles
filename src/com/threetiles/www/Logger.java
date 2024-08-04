package com.threetiles.www;

import javax.swing.JOptionPane;

public class Logger {

	public static void log(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}
