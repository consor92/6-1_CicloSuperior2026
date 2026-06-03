package GenericFiles;

public class dtoProductos {

	private int codigo;
	private String producto;
	private float precio;
	private int cantidad;
	private String marca;
	
	public dtoProductos(int codigo, String producto, int cantidad, float precio, String marca) {
		this.codigo = codigo;
		this.producto = producto;
		this.precio = precio;
		this.cantidad = cantidad;
		this.marca = marca;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}

	
	@Override
	public String toString() {
		return "dtoProductos [codigo=" + codigo + ", producto=" + producto + ", precio=" + precio + ", cantidad="
				+ cantidad + ", marca=" + marca + "]";
	}

	
}
