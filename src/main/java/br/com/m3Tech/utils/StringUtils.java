package br.com.m3Tech.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.List;

public class StringUtils {
	
	private StringUtils() {}
	
	public static boolean EmptyOrNull(String valor) {
		return valor == null || "".equals(valor);
		
	}

	public static String limite(String nome, int i) {
		if(nome.length() > i) {
			return nome.substring(0, i -1);
		}else {
			return nome;
		}
	}

	public static String tamFinal(String nome, int i) {
		if(nome.length() > i) {
			return nome.substring(0, i);
		}else if(nome.length() < i) {
			for(int t = nome.length(); t < i; t++ ) {
				nome += " ";
			}
			return nome;
		}else {
			return nome;
		}
	}
	
	public static String removerAcentos(String value) {
		return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public static String removerPontoEVirgula(String value) {
		return value.replaceAll("[^0-9]", "");
	}
	
	
	public static String getNumeroComZerosAEsquerda(Integer valor, Integer qtdeNumLeft) {
		
		String patern = "%0" + qtdeNumLeft + "d";
				
		return String.format(patern, valor);
	}
	
	public static int indexOf(String frase, String character, int ordem) {
		
		int orderIni = 1;
		
		for(int i = 0; i < frase.length(); i++) {
			if(character.equals(frase.substring(i, i+1))) {
				if(ordem == orderIni) {
					return i;
				}else {
					orderIni++;
				}
			}
		}
		return 0;
		
	}
	
	public static String deFileParaString(File arquivo) {
		try {
			
			String retorno = "";
			
			List<String> readAllLines = Files.readAllLines(arquivo.toPath(), StandardCharsets.UTF_8);
			
			for(String s : readAllLines){
				retorno += (s + "\r\n") ;
			}
			
			return retorno;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
