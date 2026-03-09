package ar.edu.et32.modelo;


public class Sede {
    private String direccion;

    public Sede(String direccion) {
        this.direccion = direccion;
    }

    
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
    
}