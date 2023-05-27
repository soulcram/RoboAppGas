package br.com.m3Tech.utils;

import javax.swing.JLabel;

public class LabelUtils {
	
	private final static int ALTURA_LABEL = 20;
	
	public static JLabel getLabel(String textLabel, int posX, int posY) {
		
		JLabel newLabel = new JLabel(textLabel);
		newLabel.setBounds(posX, posY, textLabel.length() * 10 , ALTURA_LABEL);
		
		return newLabel;
	}

}
