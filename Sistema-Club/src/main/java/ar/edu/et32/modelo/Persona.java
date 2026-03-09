package ar.edu.et32.modelo;

public abstract class Persona {
    private String nombre; // Encapsulamiento: privado

    public Persona(String nombre) {
        this.nombre = nombre;
    }

    // Getter para que otros puedan leer el nombre de forma segura
    public String getNombre() {
        return nombre;
    }
}