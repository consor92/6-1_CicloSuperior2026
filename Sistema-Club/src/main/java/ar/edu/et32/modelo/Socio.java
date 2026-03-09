package ar.edu.et32.modelo;
import ar.edu.et32.interfaces.Identificable;

/**
 * 
 */

/**
 * @author Profes
 *
 */
public class Socio extends Persona implements Identificable {
    private int numeroSocio;

    public Socio(String nombre, int numeroSocio) {
        super(nombre);
        this.numeroSocio = numeroSocio;
    }

    @Override
    public void mostrarCredencial() {
        System.out.println("Credencial de Socio: " + getNombre() + " | Nro: " + numeroSocio);
    }
}
