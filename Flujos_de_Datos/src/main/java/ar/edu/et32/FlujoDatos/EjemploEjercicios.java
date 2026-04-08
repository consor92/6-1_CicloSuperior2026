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
			case 2: {
				Utils.getOut().println( Utils.ANSI_BLUE.concat("-------Guia 1 - Ejericio 9--------").concat(Utils.ANSI_RESET) );
				Guia1_Ej9();
				break;
			}
			case 3: {
				Utils.getOut().println( Utils.ANSI_BLUE.concat("-------Guia 1 - Ejericio 11--------").concat(Utils.ANSI_RESET) );
				Guia1_Ej11();
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
		Utils.getOut().println("\t 2. Guia 1 - Ejercicio 9");
		Utils.getOut().println("\t 3. Guia 1 - Ejercicio 11");
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
	
	/* Ejercicio 9:  Solicita una palabra o frase corta. El código debe transformar 
	//el texto para que las letras alternen estrictamente entre mayúsculas y minúsculas 
	una por una. Muestra el resultado final impreso en la consola.
	*/
	public void Guia1_Ej9() {
		try {
			Utils.getOut().println("Ingrese una frase de 3 palabras");
			String linea = Utils.getLector().readLine();
			String textoFinal="";
			
			for(int i=0 ; i<=linea.length() ; i++) {//hOlA cOmO vA
				if( i%2==0 && linea.charAt(i) == ' ') 
				{	
					textoFinal = textoFinal.concat( linea.substring( i , 1 ).toLowerCase() );
				}else if(i%2==1 && linea.charAt(i) == ' '){
					textoFinal = textoFinal.concat( linea.substring( i , 1 ).toUpperCase() );
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Ejercicio 11:  Solicita tres datos al usuario en diferentes lecturas: 
	 * el nombre de un producto (texto), la cantidad comprada (número entero) y 
	 * el precio unitario (número decimal). Utiliza el constructor de cadenas para ir 
	 * uniendo todos estos tipos de datos en un solo renglón con un formato legible que 
	 * simule un recibo, conviértelo a texto definitivo y muéstralo.
	 */
	public void Guia1_Ej11() {
		try {
			Utils.getOut().print("Nombre Producto:"); Utils.getOut().flush();
			String v1 = Utils.getLector().readLine();
			
			Utils.getOut().print("Cant:"); Utils.getOut().flush();
			int v2 =  Integer.parseInt( Utils.getLector().readLine() );
			
			Utils.getOut().print("Precio:"); Utils.getOut().flush();
			float v3 = Float.parseFloat( Utils.getLector().readLine() );
			
			StringBuilder st = new StringBuilder();
			st.append("Producto: ").append(v1);
			st.append(" | Cant:").append(v2);
			st.append(" | Precio Unit:").append( String.format("%.2f",v3)  );
			st.append(" | Precio Final:").append( (float)(v2 * v3) );
			Utils.getOut().println( st.toString() );
			
			Utils.getOut().printf("Producto:%s | Cant:%d | precio Unit:%.2f | Precio Final:%.1f %n", 
									        v1      , v2             , v3             , v2*v3);
			Utils.getOut().flush();
		}catch(IOException ex){
			
		}
	}
	
}





