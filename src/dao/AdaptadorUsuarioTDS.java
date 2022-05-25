package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import dominio.Filtro;
import dominio.Usuario;
import dominio.Video;
import dominio.VideoList;
import exceptions.FilterNotExistsException;

//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;
	
	private SimpleDateFormat dateFormat;

	public static AdaptadorUsuarioTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuarioTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/* cuando se registra un usuario se le asigna un identificador único */
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {}
		if (eUsuario != null) return;

		// registrar primero los atributos que son objetos
		
		//DOY POR HECHO QUE NO HAY YA QUE UN USUARIO SE CREA SIN VIDEOS RECIENTES
		//Y SIN VIDEOLISTS
		

		// crear entidad Usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		try {
			eUsuario.setPropiedades(new ArrayList<Propiedad>(
					Arrays.asList(new Propiedad("nombre", usuario.getNombre()),
							new Propiedad("apellidos", usuario.getApellidos()),
							new Propiedad("nacimiento", dateFormat.format(usuario.getNacimiento())),
							new Propiedad("login", usuario.getNick()),
							new Propiedad("contraseña", usuario.getContraseña()),
							new Propiedad("email", usuario.getEmail()),
							new Propiedad("videolists",null),
							new Propiedad("recientes",null),
							new Propiedad("premium",usuario.isPremium() ? "1" : "0"),
							new Propiedad("filtros",ConversorFiltros.filtersToString(usuario.getFiltro())))));
		} catch (FilterNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// registrar entidad usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		usuario.setCodigo(eUsuario.getId());
	}

	public void borrarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario usuario){
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(usuario.getCodigo()));
			} else if (prop.getNombre().equals("nombre")) {
					prop.setValor(usuario.getNombre());
			} else if (prop.getNombre().equals("apellidos")) {
				prop.setValor(usuario.getApellidos());
			} else if (prop.getNombre().equals("nacimiento")) {
				prop.setValor(dateFormat.format(usuario.getNacimiento()));
			} else if (prop.getNombre().equals("login")) {
				prop.setValor(usuario.getNick());
			} else if (prop.getNombre().equals("contraseña")) {
				prop.setValor(usuario.getContraseña());
			} else if (prop.getNombre().equals("email")) {
				prop.setValor(usuario.getEmail());
			} else if (prop.getNombre().equals("videolists")) {
				String videolists = obtenerCodigosVideoLists(usuario.getVideoList());
				prop.setValor(videolists);
			} else if (prop.getNombre().equals("recientes")) {
				String recientes = obtenerCodigosRecientes(usuario.getRecientes());
				prop.setValor(recientes);
			} else if(prop.getNombre().equals("premium")) {
				prop.setValor(usuario.isPremium() ? "1" : "0");			
			}else if (prop.getNombre().equals("filtros")) {
				try {
					String Filtro = ConversorFiltros.filtersToString(usuario.getFiltro());
					prop.setValor(Filtro);
				} catch (FilterNotExistsException e) {
					e.printStackTrace();
				}
			}
			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Usuario recuperarUsuario(int cod) {

		// Si la entidad estï¿½ en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(cod))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(cod);

		// si no, la recupera de la base de datos
		Entidad eUsuario;
		List<Video> recientes = new LinkedList<Video>();
		String nombre,apellidos, login, contraseña, email;
		List<VideoList> videolists = new LinkedList<VideoList>();
		Filtro filtros = null;

		// recuperar entidad
		eUsuario = servPersistencia.recuperarEntidad(cod);

		// recuperar propiedades que no son objetos
		apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
		nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		login = servPersistencia.recuperarPropiedadEntidad(eUsuario, "login");
		contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		
		// Recupera los filtros
		try {
			filtros = ConversorFiltros.stringToFilters(servPersistencia.recuperarPropiedadEntidad(eUsuario, "filtros"));
		} catch (FilterNotExistsException e1) {
			e1.printStackTrace();
		}
		
		Date fecha = null;
		try {
			fecha = dateFormat.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "nacimiento"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		Usuario usuario = new Usuario(nombre, apellidos, fecha, login, contraseña, email, filtros);
		usuario.setCodigo(cod);
		
		// Premium
		String premium_string = servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium");
		usuario.setPremium(premium_string.equals("0") ? false : true);

		// IMPORTANTE:añadir el usuario al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(cod, usuario);

		// recuperar propiedades que son objetos llamando a adaptadores
		// ventas
		videolists = obtenerVideoListsDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "videolists"));
		recientes = obtenerRecientesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "recientes"));
		
		for (VideoList v : videolists)
			usuario.addVideoList(v);
		for(Video v : recientes)
			usuario.addRecientes(v);

		return usuario;
	}

	public List<Usuario> recuperarTodosUsuarios() {

		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosVideoLists(List<VideoList> listaVideoList) {
		String aux = "";
		for (VideoList v : listaVideoList) {
			aux += v.getCodigo() + " ";
		}
		return aux.trim();
	}
	
	private String obtenerCodigosRecientes(List<Video> listaRecientes) {
		String aux = "";
		for (Video v : listaRecientes) {
			aux += v.getCodigo() + " ";
		}
		return aux.trim();
	}
	/*private String obtenerCodigosFiltros(List<Filtro> listaFiltros){
		String aux = "";
		for (Filtro f : listaFiltros) {
			aux += f.getCodigo() + " ";
		}
		return aux.trim();
	}*/
	
	private List<VideoList> obtenerVideoListsDesdeCodigos(String videolist){
		List<VideoList> listaVideoLists = new LinkedList<VideoList>();
		
		if(videolist != null) {
			StringTokenizer strTok = new StringTokenizer(videolist, " ");
			AdaptadorVideoListTDS adaptadorV = AdaptadorVideoListTDS.getUnicaInstancia();
			while (strTok.hasMoreTokens()) {
				listaVideoLists.add(adaptadorV.recuperarVideoList(Integer.valueOf((String) strTok.nextElement())));
			}
		}
		return listaVideoLists;
	}

	private List<Video> obtenerRecientesDesdeCodigos(String recientes) {

		List<Video> listaRecientes = new LinkedList<Video>();
		
		if(recientes != null) {
			StringTokenizer strTok = new StringTokenizer(recientes, " ");
			AdaptadorVideoTDS adaptadorV = AdaptadorVideoTDS.getUnicaInstancia();
			while (strTok.hasMoreTokens()) {
				listaRecientes.add(adaptadorV.recuperarVideo(Integer.valueOf((String) strTok.nextElement())));
			}
		}
		return listaRecientes;
	}
}
