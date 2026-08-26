package UDP_peer_to_peer;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//  dto - dao
public class MisDatos implements Serializable {
	private static final long serialVersionUID = 111111L;

	private String nombre = "";
	private int dni;

	public MisDatos(String n, int d) {
		this.nombre = n;
		this.dni = d;
	}

	public byte[] toByteArray() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream os;

			os = new ObjectOutputStream(baos);
			os.writeObject(this);
			os.close();

			return baos.toByteArray();
		} catch (IOException e) {
			Logger.getLogger(MisDatos.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}

	public static MisDatos fromByArray(byte[] bytes) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream is = new ObjectInputStream(bais);
			return (MisDatos) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Logger.getLogger(MisDatos.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	public String toString() {
		return "MisDatos [Nombre=" + nombre + ", Dni=" + dni + "]";
	}

	public String getNombre() {
		return nombre;
	}

	public int getDni() {
		return dni;
	}

}
