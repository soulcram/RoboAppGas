package br.com.m3Tech.utils;

import javax.swing.JTextField;

public class TextFieldUtils {
	
	private final static int ALTURA_TEXT = 20;
	private final static int LARGURA_TEXT = 200;
	
	public static JTextField getTextField(String textoPadrao, int posX, int posY) {
		
		JTextField componente = new JTextField();
		componente.setBounds(posX, posY, LARGURA_TEXT , ALTURA_TEXT);
		componente.setText(textoPadrao);
		
		return componente;
	}
	
	public static JTextField getTextField(String textoPadrao, int tam, int posX, int posY) {
		
		JTextField componente = new JTextField();
		componente.setBounds(posX, posY, tam , ALTURA_TEXT);
		componente.setText(textoPadrao);
		
		return componente;
	}

}
