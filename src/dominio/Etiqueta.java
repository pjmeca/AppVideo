package dominio;

public class Etiqueta {

	private String nombre;
	private int codigo; 

	public Etiqueta(String nombre) {
		this.nombre = nombre;
		this.codigo=0;
	}

	public String getNombre() {
		return nombre;
	}	
	
	public boolean isNombre(String texto) {
		return nombre.equals(texto);
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
