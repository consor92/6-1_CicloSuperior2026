package ar.edu.et32.FlujoDatos;

import java.util.ArrayList;
import ar.edu.et32.FlujoDatos.Utils;

public class coleciones {
	
	String[] VectorBebidas;
	public coleciones()
	{
		String[] VectorNombres = new String[5]; //vector creado
		VectorBebidas = new String[5];
		String[] VectorApellidos = { "Perez", "Gonzalez", "Ramirez" };
		
		ArrayList<String> ListaFrutas = new ArrayList<>();
		
		ListaFrutas.add("Manzana");		//0
		ListaFrutas.add("banana");  	//1
		ListaFrutas.add("Naranja");		//2
		ListaFrutas.add("Anana");		//3
		ListaFrutas.add("Limon");		//4
		ListaFrutas.add("Pera");		//5
		ListaFrutas.add("Frutilla");	//6
		ListaFrutas.add("Moras");		//7
		
		ListaFrutas.remove(2);          // "Naranja" se va
		ListaFrutas.remove("Manzana");  // "Manzana" se va
		ListaFrutas.get(5);				//  Devuelve	"Moras"
		ListaFrutas.indexOf("Limon"); 	//   devuelve -> 2  (si no existe -> null/-1) 
		ListaFrutas.size();				//   cantidad total -> 8
		
		Utils.getOut().println( ListaFrutas );
		ListaFrutas.set(3, "Naranja");  //    ?
		ListaFrutas.subList(3, 5);     //    New  ArrayList -> {"peras","Frutillas","Moras"}
		ListaFrutas.contains("Kiwi");
		ListaFrutas.containsAll(ListaFrutas);
		ListaFrutas.isEmpty();
		ListaFrutas.toArray();         // {"","",""}
		
		Utils.getOut().println( ListaFrutas ); //lista completa
		Utils.getOut().println( ListaFrutas.get(5) );
		Utils.getOut().println( ListaFrutas.indexOf("Limon") );
		Utils.getOut().println( ListaFrutas.size() );
		Utils.getOut().println( ListaFrutas.subList(3, 5) );
		
		ListaFrutas.clear();			//  vacia la lista
		
	}
}
