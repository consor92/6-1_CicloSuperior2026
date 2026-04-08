package ar.edu.et32.FlujoDatos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

import ar.edu.et32.FlujoDatos.Utils;

public class coleciones {
	
	String[] VectorBebidas;
	public coleciones()
	{
		String[] VectorNombres = new String[5]; //vector creado
		VectorBebidas = new String[5];
		String[] VectorApellidos = { "Perez", "Gonzalez", "Ramirez" };
		
		ArrayList<String> ListaFrutas = new ArrayList<>();
		 LinkedList<Integer> ListaNumeros = new LinkedList<>();		
		HashMap<String, String> DiccPalabras = new HashMap<>();
		 LinkedHashMap<String, String> a;
		HashSet<Integer> PrimaryKeys = new HashSet<>(); 
		 LinkedHashSet<Integer> b;
		
		DiccPalabras.containsKey("");
		DiccPalabras.containsValue("");
		DiccPalabras.get("Clave a Buscar");
		DiccPalabras.put("apple", "manzana");
		DiccPalabras.entrySet();
		
		
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
		
		
		
		for(int i=0 ; i<=ListaFrutas.size() ; i++) {
			ListaFrutas.get(i);
		}
		
		for( int numero : ListaNumeros  ) {
			Utils.getOut().println(  numero );
		}
		
		//HashMap<String, String> DiccPalabras
		for( Map.Entry<String,String> e : DiccPalabras.entrySet()  ) {
			Utils.getOut().println(  e.getKey() + e.getValue() );
		}
		
		for( String key : DiccPalabras.keySet() ) {
			
		}
		
		for( String value : DiccPalabras.values() ) {
			
		}
		
		Iterator i = ListaFrutas.iterator();
		while( i.hasNext() ) {
			 Utils.getOut().println( i.next() );
		}
	}
}
