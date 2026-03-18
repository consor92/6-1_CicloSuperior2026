package ar.edu.et32.FlujoDatos;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Lecturas {
	/*	 Canales de comunicacion Standar de una APP/OS
	 *   SALIDA ->    OUT
	 *   Entrada ->   IN
	 *   Errores  ->  ERR
	 *   
	 *   System.OUT.println();
	 *   Scanner sc = new Scanner( System.in );
	 *   try{
	 *    // aca codigo encerrado con posible error
	 *   }catch( tipoError ){
	 *   	//como resuevo
	 *   	er.printStackTrace
	 *   	Logger
	 *   }
	 */
	
	private BufferedReader lector;
	
	public Lecturas() {
//Buffered(alamacenamiento) -> reader(algo que une) ->   consola(canal)
		lector = new BufferedReader(  new InputStreamReader(System.in) );
	}

	public BufferedReader getLector() {
		return lector;
	}
	
	
}



