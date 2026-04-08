package ar.edu.et32.FlujoDatos;

import java.io.IOException;


public class EjemploEjercicios {


	public EjemploEjercicios() {
		boolean continuar = true;
		while( continuar ) {
			mostrarOpciones();
			int option = pedirNumero();
			
			switch (option) {
			case 0: {
				Utils.getOut().println( Utils.ANSI_PURPLE.concat("\tSALIENDO").concat(Utils.ANSI_RESET) );
				continuar = false;
				break;
			}
			case 1: {
				Utils.getOut().println( Utils.ANSI_BLUE.concat("-------Guia 1 - Ejericio 7--------").concat(Utils.ANSI_RESET) );
				Guia1_Ej7();
				break;
			}
			default:
				Utils.getOut().println( Utils.ANSI_RED.concat("Opcion Invalida.").concat(Utils.ANSI_RESET) );
			}
		}
	}
	
	public void mostrarOpciones() {
		Utils.getOut().println(Utils.ANSI_GREEN.concat("===============================") );
		Utils.getOut().println("========Menu Principal=========");
		Utils.getOut().println("===============================".concat(Utils.ANSI_RESET) );
		
		Utils.getOut().println("\t 1. Guia 1 - Ejercicio 7");
		Utils.getOut().println("\t 0. Salir \n");
	}
	
	public int pedirNumero() {
		Utils.getOut().print("Ingrese una opcion:");
		Utils.getOut().flush();
		
		try {
			String linea = Utils.getLector().readLine();
			
			return Integer.valueOf( linea );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/*
	 * Ejercicio 7:  Lee una contraseña ingresada por consola. El sistema debe validarla aplicando tres 
	 * reglas: debe tener un mínimo de 8 caracteres de longitud, debe contener al menos un número, 
	 * y no debe contener la palabra "clave" en ninguna parte. 
	 * Imprime un mensaje indicando si la contraseña es segura o vulnerable.
	*/
	public void Guia1_Ej7() {
		Utils.getOut().print("\tIngrese el password:");
		Utils.getOut().flush();
		
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
				Utils.getOut().println("\tPassword SEGURO");
			}else {
				Utils.getOut().println("\tVULNERABLE");
			}
			Utils.getOut().println();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}


