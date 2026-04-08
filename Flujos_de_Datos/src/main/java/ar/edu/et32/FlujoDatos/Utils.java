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


    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_MAGENTA = "\u0033[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    
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



