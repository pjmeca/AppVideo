package controlador;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import dominio.CatalogoUsuarios;
import dominio.CatalogoVideos;
import dominio.Etiqueta;
import dominio.Filtro;
import dominio.FiltroImpopulares;
import dominio.FiltroMenores;
import dominio.FiltroMisListas;
import dominio.FiltroNombresLargos;
import dominio.NoFiltro;
import dominio.Usuario;
import dominio.Video;
import dominio.VideoList;
import exceptions.DAOException;
import exceptions.UserNotExistsException;
import exceptions.VideoAlreadyExistsException;
import umu.tds.componente.Componente;
import umu.tds.componente.Videos;
import umu.tds.componente.VideosEvent;
import umu.tds.componente.VideosListener;
import dao.AdaptadorVideoTDS;
import dao.FactoriaDAO;
import dao.IAdaptadorEtiquetaDAO;
import dao.IAdaptadorUsuarioDAO;
import dao.IAdaptadorVideoDAO;
import dao.IAdaptadorVideoListDAO;
import java.io.File;

public class ControladorAppVideo implements VideosListener {
	private static final int TOP_TEN = 10;

	private static ControladorAppVideo unicaInstancia = new ControladorAppVideo();

	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorVideoDAO adaptadorVideo;
	private IAdaptadorEtiquetaDAO adaptadorEtiqueta;
	private IAdaptadorVideoListDAO adaptadorVideoList;

	private CatalogoUsuarios catalogoUsuarios;
	private CatalogoVideos catalogoVideos;
	private Componente cargadorVideos;

	private Usuario usuarioActual = null;

	// private Usuario usuarioActual;

	private ControladorAppVideo() {
		inicializarAdaptadores(); // debe ser la primera linea para evitar error
									// de sincronización
		inicializarCatalogos();

		cargadorVideos = new Componente();

		cargadorVideos.addVideoListener(this);
	}

	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorVideo = factoria.getVideoDAO();
		adaptadorVideoList = factoria.getVideoListDAO();
		adaptadorEtiqueta = factoria.getEtiquetaDAO();
	}

	private void inicializarCatalogos() {
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
		catalogoVideos = CatalogoVideos.getUnicaInstancia();

	}

	public boolean existeUsuario(String nick) {
		return catalogoUsuarios.getUsuario(nick) != null;
	}

	public void cargarVideos(File xml) {
		cargadorVideos.setArchivoVideos(xml);
	}

	public boolean existeVideo(String id) {
		return catalogoVideos.getVideo(id) != null;
	}

	public List<Video> getVideos() {
		return catalogoVideos.getVideos();
	}

	public List<Usuario> getUsuarios() {
		return catalogoUsuarios.getUsuarios();
	}

	public static ControladorAppVideo getUnicaInstancia() {
		return unicaInstancia;
	}

	/*
	 * |----------------------------------------------------------| | REGISTRO |
	 * |----------------------------------------------------------|
	 */
	public void registrarUsuario(String nombre, String apellidos, Date fecha, String nick, String contraseña,
			String email) throws exceptions.UserAlreadyExistsException {

		// Si ya existe ese nick, no se puede registrar
		if (existeUsuario(nick))
			throw new IllegalArgumentException("Ya existe un usuario con ese nick.");

		Usuario usuario = new Usuario(nombre, apellidos, fecha, nick, contraseña, email);
		adaptadorUsuario.registrarUsuario(usuario);
		catalogoUsuarios.addUsuario(usuario);
	}

	public void registrarVideo(String titulo, String id, List<Etiqueta> etiquetas)
			throws exceptions.VideoAlreadyExistsException {

		// Si ya existe ese id, no se puede registrar
		if (existeVideo(id))
			throw new IllegalArgumentException("Ya existe un video con esa id.");
		
		Video video = new Video(titulo, id, etiquetas);
		adaptadorVideo.registrarVideo(video);

		catalogoVideos.addVideo(video);
	}

	public void registrarEtiqueta(String nombre) {

		Etiqueta etiq = new Etiqueta(nombre);
		adaptadorEtiqueta.registrarEtiqueta(etiq);

	}
	
	public void registrarEtiqueta(Etiqueta e) {
		adaptadorEtiqueta.registrarEtiqueta(e);

	}

	public void registrarVideoList(String nombre) {
		VideoList v = new VideoList(nombre);
		adaptadorVideoList.registrarVideoList(v);
		usuarioActual.addVideoList(v);
		adaptadorUsuario.modificarUsuario(usuarioActual);

	}

	/*
	 * |----------------------------------------------------------| | LOGIN |
	 * |----------------------------------------------------------|
	 */

	/*
	 * Hace el login del usuario.
	 * 
	 * @params nick El nick del usuario.
	 * 
	 * @params password La contraseña del usuario.
	 * 
	 * @throws exceptions.UserNotExistsException El usuario que quiere hacer el
	 * login no existe.
	 * 
	 * @throws IllegalArgumentException Los campos no son válidos.
	 */
	public boolean login(String nick, String password)
			throws exceptions.UserNotExistsException, IllegalArgumentException {
		if (nick.isBlank() || password.isBlank())
			throw new IllegalArgumentException("Valores del login incorrectos.");

		usuarioActual = catalogoUsuarios.checkContraseña(nick, password);

		return usuarioActual != null;
	}

	/*
	 * Devuelve el usuario actual.
	 */
	public Usuario getUsuario() {
		return usuarioActual;
	}

	/*
	 * Devuelve el nick del usuario o un string vacío si no hay sesión iniciada.
	 */
	public String getNickUsuario() {
		return usuarioActual != null ? usuarioActual.getNick() : "";
	}

	/*
	 * Comprueba si está loggeado.
	 */
	public boolean isLogged() {
		return usuarioActual != null;
	}

	/*
	 * |----------------------------------------------------------| | LOGOUT |
	 * |----------------------------------------------------------|
	 */

	public void logout() {
		usuarioActual = null;
	}

	/*
	 * |----------------------------------------------------------| | PREMIUM |
	 * |----------------------------------------------------------|
	 */

	public void setPremium(boolean value) throws exceptions.UserNotExistsException {
		if (usuarioActual == null)
			throw new exceptions.UserNotExistsException();

		usuarioActual.setPremium(value);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	public boolean isPremium() {
		return usuarioActual != null ? usuarioActual.isPremium() : false;
	}

	/*
	 * |----------------------------------------------------------| | VÍDEOS |
	 * |----------------------------------------------------------|
	 */

	@Override
	public void nuevosVideos(VideosEvent e) {
		Videos videos = e.getVideos();
		List<Etiqueta> etiquetas = new LinkedList<Etiqueta>();
		for (umu.tds.componente.Video v : videos.getVideo()) {
			
			/*for (String s : v.getEtiqueta()) { Etiqueta e1 = new Etiqueta(s);
			 etiquetas.add(e1); }*/
			v.getEtiqueta().stream().map(s -> new Etiqueta(s)).forEach(etiq -> etiquetas.add(etiq));

			if (!existeVideo(v.getURL())) {
				try {
					registrarVideo(v.getTitulo(), v.getURL(), etiquetas);
				} catch (VideoAlreadyExistsException e1) {
					e1.printStackTrace();
				}
			}
			// Si ya está el video, puede que haya cambiado sus etiquetas
			else if (existeVideo(v.getURL())) {
				catalogoVideos.addEtiquetasVideo(v.getURL(), etiquetas);
			}

			etiquetas.clear();
		}

	}

	public List<Video> buscarVideos(String texto, Set<Etiqueta> etiquetas) {
		List<Video> lista = catalogoVideos.findVideo(texto);

		if (lista.size() == 0) {
			System.out.println("No se ha encontrado ningún vídeo.");
			return null;

		} else {

			List<Video> resultado;

			// Comprobamos que tenga alguna de las etiquetas
			if (etiquetas != null && etiquetas.size() > 0) {
				resultado = new LinkedList<>();
				/*
				 * for (Video v : lista) { if (v.contieneAlgunaEtiqueta(etiquetas.toArray(new
				 * Etiqueta[etiquetas.size()]))) { resultado.add(v); } }
				 */

				lista.stream().filter(v -> v.contieneAlgunaEtiqueta(etiquetas.toArray(new Etiqueta[etiquetas.size()])))
						.forEach(resultado::add);

			} else
				resultado = lista;

			// Pasamos los videos por los filtros. Si el usuario es no premium, nos
			// encargamos siempre de que tenga el NoFiltro que no hace nada.
			// Y por tanto, no hace falta que comprobemos si es o no premium al aplicar los
			// filtros.
			List<Video> res_fil = new LinkedList<>();
			/*
			 * for(Video video : resultado) { if (getFiltroUsuarioActual().esVideoOK(video))
			 * res_fil.add(video); }
			 */
			resultado.stream().filter(getFiltroUsuarioActual()::esVideoOK).forEach(res_fil::add);

			System.out.println("Estos son los vídeos que he encontrado:");
			/*
			 * for (Video v : res_fil) { System.out.println(v.getTitulo()); }
			 */
			res_fil.stream().map(v -> v.getTitulo()).forEach(System.out::println);
			return res_fil;
		}
	}

	public VideoList buscarVideoList(String titulo) {

		VideoList lista = adaptadorVideoList.recuperarVideoList(titulo);
		if (lista != null && usuarioActual.tieneVideoList(lista)) {
			List<Video> videos = lista.getVideos();
			System.out.println("Estos son los vídeos que he encontrado:");
			for (Video v : videos) {
				System.out.println(v.getTitulo());
			}

			return lista;
		}
		System.out.println("No se ha encontrado ninguna vídeolist con ese nombre que pertenezca a este usuario.");
		return null;
	}

	public void addReproduccion(Video video) {
		((AdaptadorVideoTDS) adaptadorVideo).incrementarReproducciones(video);
	}

	public List<Etiqueta> recuperarEtiquetas(String id) {
		return catalogoVideos.getEtiquetasVideo(id);
	}

	public List<String> recuperarTodasEtiquetas() {
		List<Etiqueta> etiquetas = adaptadorEtiqueta.recuperarTodosEtiquetas();
		List<String> lista = new LinkedList<String>();
		/*
		 * for (Etiqueta e : etiquetas) if (!lista.contains(e.getNombre()))
		 * lista.add(e.getNombre());
		 */
		etiquetas.stream().filter(e -> !lista.contains(e.getNombre())).forEach(e -> lista.add(e.getNombre()));
		return lista;
	}

	public Set<Etiqueta> stringToEtiquetas(List<String> nombresEtiquetas) {
		List<Etiqueta> etiquetas = adaptadorEtiqueta.recuperarTodosEtiquetas();
		Set<Etiqueta> resultado = new HashSet<>();

		for (String n : nombresEtiquetas) {
			Etiqueta aux = null;
			for (Etiqueta e : etiquetas) {
				if (e.isNombre(n)) {
					aux = e;
					break;
				}
			}
			if (aux != null)
				resultado.add(aux);
		}

		return resultado;
	}

	public List<VideoList> obtenerMisVideoLists() {

		return usuarioActual.getVideoList();
	}

	public String getVideoListsUsuario() {
		String s = "";
		for (VideoList v : obtenerMisVideoLists()) {
			s += v.getNombre();
			s += "\n";
			List<Video> videos = v.getVideos();
			for (Video v1 : videos) {
				s += "     " + v1.getTitulo();
				s += "\n     Número de reproducciones: ";
				s += v1.getNumReproducciones() + "\n";
			}
			// Si no hay videos en la videolist, lo indicamos.
			if (videos.size() == 0)
				s += "     No hay videos en la videolist.\n";
		}
		if (s.equals(""))
			s += "El usuario no tiene ninguna videolist.\n";
		return s;
	}

	public void eliminarVideoList(String nombre) {
		usuarioActual.eliminarVideoList(nombre);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		adaptadorVideoList.borrarVideoList(nombre);

	}

	public List<Filtro> obtenerTodosFiltros() {
		List<Filtro> filtros = new LinkedList<Filtro>();
		filtros.add(NoFiltro.getUnicaInstancia());
		filtros.add(FiltroImpopulares.getUnicaInstancia());
		filtros.add(FiltroMenores.getUnicaInstancia());
		filtros.add(FiltroMisListas.getUnicaInstancia());
		filtros.add(FiltroNombresLargos.getUnicaInstancia());
		return filtros;
	}

	public Filtro getFiltroUsuarioActual() {
		return usuarioActual.getFiltro();
	}

	public void setFiltroUsuarioActual(Filtro f) {
		usuarioActual.setFiltro(f);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	public void bajaPremium() throws UserNotExistsException {
		setFiltroUsuarioActual(NoFiltro.getUnicaInstancia());
		setPremium(false);
	}

	public Date getFechaNacimiento() {
		return usuarioActual.getNacimiento();
	}

	public boolean videolistsContainsVideo(Video video) {
		for (VideoList vl : usuarioActual.getVideoList())
			for (Video v : vl.getVideos())
				if (v.getId().equals(video.getId()))
					return true;

		return false;
	}

	public void addRecientes(Video v) {
		usuarioActual.addRecientes(v);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		System.out.println("Añadido el video a recientes: " + v.getTitulo());
		System.out.println("Los videos recientes del usuario actual son: " + usuarioActual.getRecientes().toString());
	}

	public List<Video> getTop10() {
		List<Video> videos = catalogoVideos.getVideos();
		List<Video> top = new LinkedList<Video>();
		videos.sort(new Comparator<Video>() {

			@Override
			public int compare(Video o1, Video o2) {
				return ((Integer) o2.getNumReproducciones()).compareTo(o1.getNumReproducciones());
			}

		});

		if (videos.size() >= TOP_TEN) {
			for (int i = 0; i < TOP_TEN; i++)
				top.add(videos.get(i));
		}

		else {
			for (int i = 0; i < videos.size(); i++)
				top.add(videos.get(i));
		}
		return top;
	}

	public void addEtiqueta(Video v, String nombre) {
		List<Etiqueta> lista = adaptadorEtiqueta.recuperarTodosEtiquetas();
		if (lista != null) {
			for (Etiqueta e : lista)
				if (e.getNombre().equals(nombre)) {
					v.addEtiqueta(e);
					catalogoVideos.addEtiquetasVideo(v.getId(), Arrays.asList(e));
				}
		}
	}

	public boolean existeEtiqueta(String nombre) {
		List<Etiqueta> lista = adaptadorEtiqueta.recuperarTodosEtiquetas();
		if (lista != null) {
			for (Etiqueta e : lista)
				if (e.getNombre().equals(nombre))
					return true;
		}
		return false;
	}

	public void addVideoVideoList(Video v, VideoList vl) {
		vl.addVideo(v);
		usuarioActual.addVideoVideoList(v, vl);
		adaptadorVideoList.modificarVideoList(vl);
	}

	public void removeVideoVideoList(Video v, VideoList vl) {
		vl.removeVideo(v);
		adaptadorVideoList.modificarVideoList(vl);

	}
}
