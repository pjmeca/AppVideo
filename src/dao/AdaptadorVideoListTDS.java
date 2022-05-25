package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import dominio.Video;
import dominio.VideoList;

public class AdaptadorVideoListTDS implements IAdaptadorVideoListDAO {
	// Usa un pool para evitar problemas doble referencia con cliente

	private static ServicioPersistencia servPersistencia;

	private static AdaptadorVideoListTDS unicaInstancia;

	public static AdaptadorVideoListTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorVideoListTDS();
		else
			return unicaInstancia;
	}

	private AdaptadorVideoListTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra una videolist se le asigna un identificador unico */
	public void registrarVideoList(VideoList videolist) {
		Entidad eVideoList = null;
		try {
			eVideoList = servPersistencia.recuperarEntidad(videolist.getCodigo());
		} catch (NullPointerException e) {}
		if (eVideoList != null)	return;

		// registrar primero los atributos que son objetos
		
		//SE ENTIENDE QUE AL CREARSE LAS LISTAS ESTÁN VACÍAS
		//por eso aqui no se registran objetos y abajo ponemos null
		
		// Crear entidad videolist
		eVideoList = new Entidad();

		eVideoList.setNombre("videolist");
		eVideoList.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("nombre", videolist.getNombre()),
						new Propiedad("videos", null))));
		// registrar entidad videolist
		eVideoList = servPersistencia.registrarEntidad(eVideoList);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		videolist.setCodigo(eVideoList.getId());
	}

	public void borrarVideoList(VideoList videolist) {
		// No se comprueban restricciones de integridad
		Entidad eVideoList = servPersistencia.recuperarEntidad(videolist.getCodigo());
		servPersistencia.borrarEntidad(eVideoList);

	}

	public void modificarVideoList(VideoList videolist) {
		Entidad eVideoList = servPersistencia.recuperarEntidad(videolist.getCodigo());

		for (Propiedad prop : eVideoList.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(videolist.getCodigo()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(videolist.getNombre());
			} else if (prop.getNombre().equals("videos")) {
				String videos = obtenerCodigosVideo(videolist.getVideos());
				prop.setValor(videos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}

	public VideoList recuperarVideoList(int codigo) {
		// Si la entidad estï¿½ en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (VideoList) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eVideoList = servPersistencia.recuperarEntidad(codigo);
		String nombre;
		List<Video> videoLista = new LinkedList<Video>();
		
		// recuperar propiedades que no son objetos

		nombre = servPersistencia.recuperarPropiedadEntidad(eVideoList, "nombre");
		
		VideoList videolist = new VideoList(nombre);
		videolist.setCodigo(codigo);

		// IMPORTANTE:añadir la videolist al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, videolist);

		// recuperar propiedades que son objetos llamando a adaptadores
		
		videoLista = obtenerVideoDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eVideoList, "videos"));
		
		if (videoLista != null) {
			for (Video vl : videoLista)
				videolist.addVideo(vl);
		}	
		// devolver el objeto videolist
		return videolist;
	}
	
	
	public void borrarVideoList(String nombre) {
		VideoList v = recuperarVideoList(nombre);
		borrarVideoList(v);
	}
	
	public List<VideoList> recuperarTodasVideoLists() {
		List<VideoList> videolists = new LinkedList<VideoList>();
		List<Entidad> eVideoLists = servPersistencia.recuperarEntidades("videolist");

		for (Entidad eVideoList : eVideoLists) {
			VideoList videolist = recuperarVideoList(eVideoList.getId());
			if (videolist != null)
				videolists.add(videolist);
		}
		return videolists;
	}
	
	public VideoList recuperarVideoList(String titulo) {
		
		List<VideoList> lista = recuperarTodasVideoLists();
		for(VideoList v : lista) {
			if (v.getNombre().equals(titulo))
				return v;
		}
		return null;
	}



	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosVideo(List<Video> videolist) {
		String lineas = "";
		for (Video v : videolist) {
			lineas += v.getCodigo() + " ";
		}
		return lineas.trim();
	}

	private List<Video> obtenerVideoDesdeCodigos(String lineas) {
		List<Video> lineasVideoList = new LinkedList<Video>();
		if (lineas != null) {
			StringTokenizer strTok = new StringTokenizer(lineas, " ");
			AdaptadorVideoTDS adaptadorLV = AdaptadorVideoTDS.getUnicaInstancia();
			while (strTok.hasMoreTokens()) {
				lineasVideoList.add(adaptadorLV.recuperarVideo(Integer.valueOf((String) strTok.nextElement())));
			}
		}
		return lineasVideoList;
	}

}
