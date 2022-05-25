package dominio;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import gui.TablaResultados;

public class Usuario {

	private String nombre, apellidos, nick, contrase�a, email;
	private Date nacimiento;
	int codigo;
	private List<VideoList> videolists;
	private List<Video> recientes;
	public static final int NUM_RECIENTES = 4*TablaResultados.N_COLUM_RESULTADOS_DEFAULT; // Longitud del historial
	public Filtro filtro;
	private boolean premium;
	
	public Usuario(String nombre, String apellidos, Date nacimiento,
			String nick, String contrase�a, String email) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.nacimiento = nacimiento;
		this.nick = nick;
		this.contrase�a = contrase�a;
		this.email = email;
		this.codigo=0;
		videolists = new LinkedList<VideoList>();
		recientes = new LinkedList<Video>();
		filtro = NoFiltro.getUnicaInstancia();
		this.premium = false;
	}
	
	public Usuario(String nombre, String apellidos,	Date nacimiento,
			String nick, String contrase�a, String email, Filtro filtro) {
		this(nombre, apellidos, nacimiento, nick, contrase�a, email);
		this.filtro = filtro;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public Date getNacimiento() {
		return nacimiento;
	}

	public String getNick() {
		return nick;
	}

	public String getContrase�a() {
		return contrase�a;
	}

	public String getEmail() {
		return email;
	}
	
	public void setFiltro(Filtro f) {
		filtro = f;
	}

	public boolean isPremium() {
		return premium;
	}
	
	public void setPremium(boolean value) {
		premium = value;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public List<VideoList> getVideoList(){
		return new LinkedList<VideoList> (videolists);
	}
	public List<Video> getRecientes(){
		return new LinkedList<Video> (recientes);
	}
	
	public Filtro getFiltro(){
		return filtro;
	}
	
	public void addVideoList(VideoList v) {
		videolists.add(v);
	}
	
	public void addRecientes(Video v) {
		// Si ya est�, se borra para ponerlo al principio
		int pos = recientesContainsVideo(v);
		if(pos >= 0) {
			recientes.remove(pos);
		}
		
		// Control de errores
		// Esto no deber�a pasar nunca
		if(recientes.size() > NUM_RECIENTES) {
			System.err.println("ERROR: Recientes supera el tama�o m�ximo.");
			return;
		}
		
		// Si la lista est� llena, borra el m�s viejo
		if(recientes.size() == NUM_RECIENTES) {
			recientes.remove(0); //Borra el primero
		}

		// Lo a�ade al principio
		recientes.add(0, v);
	}
	
	public int recientesContainsVideo(Video v) {
		for(Video video : recientes) {
			if (video.getId().equals(v.getId()))
				return recientes.indexOf(video);
		}
		return -1;
	}
	
	public boolean tieneVideoList(VideoList v) {
		/*for(VideoList v1 : videolists) {
			if (v1.getNombre().equals(v.getNombre()))
				return true;
		}
		return false;*/
		
		return videolists.stream().anyMatch(e -> e.getNombre().equals(v.getNombre()));
		
	}
	
	public void eliminarVideoList(String nombre) {

		videolists.removeIf( x -> x.getNombre().equals(nombre));
		
	}
	
	public void addVideoVideoList(Video v, VideoList vl) {
		for(VideoList vlist : videolists) {
			if(vlist.getNombre().equals(vl.getNombre()))
				vlist.addVideo(v);
		}
		
		
	}
	
	public boolean checkContrase�a(String passwd) {
		return contrase�a.equals(passwd);
	}
	
}
