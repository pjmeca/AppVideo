package dominio;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import controlador.ControladorAppVideo;

public class Video {

	private String titulo;
	private String id;
	private int numReproducciones;
	private int codigo;
	private List<Etiqueta> etiquetas;
	
	public Video(String titulo, String id, List<Etiqueta> etiquetas) {
		this.titulo = titulo;
		this.id = id;
		this.numReproducciones=0;
		this.codigo=0;
		this.etiquetas = new LinkedList<>(etiquetas);
	}
	
	public Video(String titulo, String id, Etiqueta... etiqueta) {
		this.titulo = titulo;
		this.id = id;
		numReproducciones=0;
		List<Etiqueta> l = new LinkedList<Etiqueta>();
		for(Etiqueta e:etiqueta) {
			l.add(e);
		}
		etiquetas = l;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public boolean isTitulo(String titulo) {
		return this.titulo.contains(titulo);
	}

	public String getId() {
		return id;
	}

	public int getNumReproducciones() {
		return numReproducciones;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public List<Etiqueta> getEtiquetas(){
		return new LinkedList<Etiqueta>(etiquetas);
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public void setNum_Rep(int n) {
		numReproducciones=n;
	}
	
	public void addReproduccion() {
		numReproducciones++;
		ControladorAppVideo.getUnicaInstancia().addReproduccion(this);
	}
	
	public void addEtiqueta(Etiqueta e) {
		etiquetas.add(e);
	}
	
	public boolean etiquetaContenida(Etiqueta e) {
		return etiquetaContenida(e.getNombre());
	}
	
	public boolean etiquetaContenida(String nombre) {
		if(etiquetas == null)
			return false;
		return etiquetas.stream().anyMatch(e -> e.getNombre().equals(nombre));
	}
	
	public boolean contieneAlgunaEtiqueta(Etiqueta... etiquetas) {
		if(etiquetas.length == 0 && this.etiquetas.size() == 0)
			return true;
		else if(etiquetas.length == 0)
			return false;
		
		/*for(Etiqueta e : etiquetas) {
			if(etiquetaContenida(e.getNombre()))
				return true;
		}*/
		
		return Arrays.asList(etiquetas).stream().anyMatch(e->etiquetaContenida(e.getNombre()));
		
	}
}
