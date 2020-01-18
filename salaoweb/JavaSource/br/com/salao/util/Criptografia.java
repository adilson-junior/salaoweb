package br.com.salao.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Criptografia {
	
	/**
	 * Criptografa uma senha
	 * @param senha
	 * @return senhaCript
	 */
	public String criptografar(String senha){		
		String senhaCript = "";		
		try {			
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));			 
			StringBuilder hexString = new StringBuilder();			
			for (byte b : messageDigest) {				
			  hexString.append(String.format("%02X", 0xFF & b));			  
			}			
			senhaCript = hexString.toString();			
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return senhaCript;
	}
	
	/**
	 * Gera uma senha com 6 caracteres
	 * @return senha
	 */
	public String gerarSenha(){
		String[] keys = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
				"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
				"1", "2", "3", "4", "5", "6", "7", "8", "9","0",
				"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
				"1", "2", "3", "4", "5", "6", "7", "8", "9","0",
				"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a"};
	    Random random = new Random();
	    int tamanho = 10;
	    Set<String> senha = new HashSet<String>();
	    StringBuilder sbSenha = new StringBuilder();
	    while(tamanho > 0){
	    	int index = random.nextInt(keys.length);
	    	if(senha.add(keys[index])){
	    		tamanho--;
	    		sbSenha.append(keys[index]);
	    	}
	    }	    
		return sbSenha.toString();
	}
	
//	public static void main(String[] args) {
//		Criptografia cp = new Criptografia();
//		System.out.println(cp.criptografar("123QWE"));
//		
//		DateTime t = new DateTime("2013-1-11T00:00:00.000-03:00");
//		t = t.plusDays(21);
//		System.out.println(t.toDate());
//		
//	}
	
}
