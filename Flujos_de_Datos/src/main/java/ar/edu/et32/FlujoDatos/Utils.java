package ar.edu.et32.FlujoDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Utils {
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
	
	private static BufferedReader lector;
	private static PrintWriter out;
	
	public Utils() {
		//Buffered(alamacenamiento) -> reader(algo que une) ->   consola(canal)
		lector = new BufferedReader(  new InputStreamReader(System.in) );
		out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(System.out) ) ,true );
	}

	public static BufferedReader getLector() {
		return lector;
	}
	public static PrintWriter getOut() {
		return out;
	}
	
	
}



