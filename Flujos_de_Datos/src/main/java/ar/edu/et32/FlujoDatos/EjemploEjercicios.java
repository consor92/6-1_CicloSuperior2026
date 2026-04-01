package ar.edu.et32.FlujoDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


public class EjemploEjercicios {


	public EjemploEjercicios() {

		
	}
	
	
	/*
	 * Ejercicio 7:  Lee una contraseña ingresada por consola. El sistema debe validarla aplicando tres 
	 * reglas: debe tener un mínimo de 8 caracteres de longitud, debe contener al menos un número, 
	 * y no debe contener la palabra "clave" en ninguna parte. 
	 * Imprime un mensaje indicando si la contraseña es segura o vulnerable.
	*/
	public void Guia1_Ej7() {
		Utils.getOut().println("Ingrese el password:");
		try {
			String psw = Utils.getLector().readLine();
			
			boolean longitudValida   = psw.length() >= 8;
			boolean contientePalabra = psw.contains("clave");
			boolean contieneNumeros = false;
			
			//boolean contieneNumeros = psw.matches( ".*\\d.*" ); //expresion regulares   "[a-zA-Z0-9_+&*-]*@"
			for(int i =0 ; i<=9 ; i++)
			{
				if( psw.contains( String.valueOf(i) ) == true )
				{
					contieneNumeros= true;
				}
			}
				
			if(longitudValida && contieneNumeros && !contientePalabra)
			{
				Utils.getOut().println("Password SEGURO");
			}else {
				Utils.getOut().println("VULNERABLE");
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}


