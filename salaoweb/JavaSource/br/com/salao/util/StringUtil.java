package br.com.salao.util;

public class StringUtil {
	
//	public static void main(String[] args) {
//		
//		String str = "mária da silva rocha  de miranda";
//		
//		String parts[] = str.split(" ");		
//		StringBuilder completo = new StringBuilder();		
//		for(String s : parts){			
//			int qtd = s.length();
//			if(s.trim().equals("")){
//				continue;
//			}
//			for(int i = 0; i < qtd; i++){
//				
//				if(i == 0){
//					completo.append(new String(""+s.charAt(i)).toUpperCase());
//				}else{
//					completo.append(new String(""+s.charAt(i)).toLowerCase());
//				}				
//			}
//			completo.append(" ");
//		}
//		
//		System.out.println(completo);
//	}
	
	public String formatarNome(String str){
		String parts[] = str.split(" ");		
		StringBuilder completo = new StringBuilder();		
		for(String s : parts){			
			int qtd = s.length();
			if(s.trim().equals("")){
				continue;
			}
			for(int i = 0; i < qtd; i++){				
				if(i == 0){
					completo.append(new String(""+s.charAt(i)).toUpperCase());
				}else{
					completo.append(new String(""+s.charAt(i)).toLowerCase());
				}				
			}
			completo.append(" ");
		}
		return completo.toString();
	}

}
