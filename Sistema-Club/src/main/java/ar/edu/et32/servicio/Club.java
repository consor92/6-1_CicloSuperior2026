package ar.edu.et32.servicio;
import java.util.ArrayList;

import ar.edu.et32.modelo.Sede;
import ar.edu.et32.modelo.Socio;

public class Club {
    private String nombre;
    private Sede sede;                      
    private ArrayList<Socio> listaSocios;   

    // Constructor completo (Composición)
    public Club(String nombre, String direccionSede) {
        this.nombre = nombre;
        this.sede = new Sede(direccionSede); 
        this.listaSocios = new ArrayList<>();
    }
    
    // Tu segundo constructor
    public Club(String nombre) {
        this.nombre = nombre;
        this.listaSocios = new ArrayList<>(); // IMPORTANTE: Inicializar la lista aquí también
    }

    // Cambiamos 'registrarSocio' por 'afiliarSocio' para que coincida con tu Main
    public void afiliarSocio(Socio s) {
        this.listaSocios.add(s);
    }

    // Cambiamos 'mostrarInfoClub' por 'mostrarSocios' para que coincida con tu Main
    public void mostrarSocios() {
        System.out.println("--- Listado del Club: " + nombre + " ---");
        // Verificamos si hay sede para evitar errores si usaste el constructor simple
        if (sede != null) {
            System.out.println("Sede en: " + sede.getDireccion());
        }
        
        for (Socio s : listaSocios) {
            s.mostrarCredencial(); // Este método está en la interfaz/clase Socio
        }
    }
}