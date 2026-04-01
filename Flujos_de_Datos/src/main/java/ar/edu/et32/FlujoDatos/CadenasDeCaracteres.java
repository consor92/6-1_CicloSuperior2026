package ar.edu.et32.FlujoDatos;

import java.io.IOException;

public class CadenasDeCaracteres {

	
	public CadenasDeCaracteres() {
		Utils lec = new Utils();
		
		//Lecturas.getLector();  // instancia con STATIC
				// int  <=>  char 
				try {
					//int caracter = lec.getLector().read();
					String aux = "Raichu";
					String linea = lec.getLector().readLine(); //Pikachu
					linea.charAt(0);     			 	// P->80
					linea.codePointAt(5); 				// h (unicode)
					linea.compareTo(aux); 				// true o false
					linea.compareToIgnoreCase(aux);
					linea.concat(aux);                  // pikachu + raichu
					linea.contains("ka");				// true
					linea.contentEquals("Ka");			// false
					linea.endsWith("u");				// true
					linea.startsWith("P");				// true
					linea.indexOf(0);					// P
					linea.isBlank();					// si es un ESPACIO
					linea.isEmpty();					// si es NULL
					linea.length();						// largo de caracteres
					linea.replace("P", "p");			// pikachu
					linea.replaceAll("P", "7");			// 7ikachu
					linea.split("k");					// vec[0]=Pi vec[1]=achu
					linea.toLowerCase();				// pikachu
					linea.toUpperCase();				// PIKACHU
					linea.trim();						//  quita espacios
					linea.substring(3);					//	achu   
					linea.substring(3,5);				//  ac
					String.valueOf( 5 );					// conviente el 5 en String
					String.join("+", "Hola", "Chau", "Todo bien");  // Hola+Chau+Todo Bien
					
					StringBuilder sb = new StringBuilder();
					sb.append(false);   //1°
					sb.append("hola");  //2°
					sb.append(55);		//3°
					sb.insert(2, "lalal");
					sb.delete(2, 6);
					sb.toString();		//devuelve el texto completo construido
					
					Utils.getOut().println( sb.toString() );
					Utils.getOut().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//Lecturas.getOut().println("Holaaa");
				//Lecturas.getOut().flush();		
		
	}
	
}
