package dominio;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import gui.TablaResultados;

public class Usuario {

	private String nombre, apellidos, nick, contraseña, email;
	private Date nacimiento;
	int codigo;
	private List<VideoList> videolists;
	private List<Video> recientes;
	public static final int NUM_RECIENTES = 4*TablaResultados.N_COLUM_RESULTADOS_DEFAULT; // Longitud del historial
	public Filtro filtro;
	private boolean premium;
	
	public Usuario(String nombre, String apellidos, Date nacimiento,
			String nick, String contraseña, String email) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.nacimiento = nacimiento;
		this.nick = nick;
		this.contraseña = contraseña;
		this.email = email;
		this.codigo=0;
		videolists = new LinkedList<VideoList>();
		recientes = new LinkedList<Video>();
		filtro = NoFiltro.getUnicaInstancia();
		this.premium = false;
	}
	
	public Usuario(String nombre, String apellidos,	Date nacimiento,
			String nick, String contraseña, String email, Filtro filtro) {
		this(nombre, apellidos, nacimiento, nick, contraseña, email);
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

	public String getContraseña() {
		return contraseña;
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
		// Si ya está, se borra para ponerlo al principio
		int pos = recientesContainsVideo(v);
		if(pos >= 0) {
			recientes.remove(pos);
		}
		
		// Control de errores
		// Esto no debería pasar nunca
		if(recientes.size() > NUM_RECIENTES) {
			System.err.println("ERROR: Recientes supera el tamaño máximo.");
			return;
		}
		
		// Si la lista está llena, borra el más viejo
		if(recientes.size() == NUM_RECIENTES) {
			recientes.remove(0); //Borra el primero
		}

		// Lo añade al principio
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
	
	public boolean checkContraseña(String passwd) {
		return contraseña.equals(passwd);
	}
	
}
