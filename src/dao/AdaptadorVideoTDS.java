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
import dominio.Etiqueta;
import dominio.Video;


public class AdaptadorVideoTDS implements IAdaptadorVideoDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorVideoTDS unicaInstancia = null;

	public static AdaptadorVideoTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null) {
			return new AdaptadorVideoTDS();
		} else
			return unicaInstancia;
	}

	private AdaptadorVideoTDS() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un video se le asigna un identificador unico */
	public void registrarVideo(Video video) {
		Entidad eVideo = null;
		try {
			eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
		} catch (NullPointerException e) {}
		if (eVideo != null) return;
		
		//LOS VIDEOS TIENEN ETIQUETAS AL CREARSE
		AdaptadorEtiquetaTDS adaptadorEtiqueta = AdaptadorEtiquetaTDS.getUnicaInstancia();
		for(Etiqueta e : video.getEtiquetas()) {
			adaptadorEtiqueta.registrarEtiqueta(e);
		}
		// crear entidad video
		eVideo = new Entidad();
		eVideo.setNombre("video");
		eVideo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("titulo", video.getTitulo()),
				new Propiedad("identificador", video.getId()),
				new Propiedad("num_reproducciones", "0"),
				new Propiedad("etiquetas", obtenerCodigosEtiquetas(video.getEtiquetas())))));
		// registrar entidad video
		eVideo = servPersistencia.registrarEntidad(eVideo);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		video.setCodigo(eVideo.getId());  
	}

	public void borrarVideo(Video video) {
		// No se comprueba integridad
		Entidad eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
		servPersistencia.borrarEntidad(eVideo);
	}

	public void modificarVideo(Video video) {
		Entidad eVideo = servPersistencia.recuperarEntidad(video.getCodigo());

		for (Propiedad prop : eVideo.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(video.getCodigo()));
			} else if (prop.getNombre().equals("titulo")) {
				prop.setValor(video.getTitulo());
			} else if (prop.getNombre().equals("num_reproducciones")) {
				prop.setValor(String.valueOf(video.getNumReproducciones()));
			} else if (prop.getNombre().equals("etiquetas")) {
				String etiquetas = obtenerCodigosEtiquetas(video.getEtiquetas());
				prop.setValor(etiquetas);
			} else if (prop.getNombre().equals("identificador")) {
				prop.setValor(video.getId());
			} 
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public void incrementarReproducciones(Video video) {
		Entidad eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
		
		Propiedad p = (Propiedad) eVideo.getPropiedades().stream().filter(a -> a.getNombre().equals("num_reproducciones")).toArray()[0];
		p.setValor(Integer.toString(Integer.parseInt(p.getValor())+1));
		servPersistencia.modificarPropiedad(p);
	}

	public Video recuperarVideo(int id) {
		Entidad eVideo;
		String titulo,identificador;
		int  num_reproducciones;
		List<Etiqueta> etiquetas = new LinkedList<Etiqueta>();
		
		eVideo = servPersistencia.recuperarEntidad(id);
		titulo = servPersistencia.recuperarPropiedadEntidad(eVideo, "titulo");
		identificador = servPersistencia.recuperarPropiedadEntidad(eVideo, "identificador");
		num_reproducciones = Integer.valueOf(servPersistencia.recuperarPropiedadEntidad(eVideo, "num_reproducciones"));
		
		Video video = new Video(titulo, identificador);
		video.setCodigo(id);
		video.setNum_Rep(num_reproducciones);
		
		/*RECUPERAR LA LISTA DE ETIQUETAS*/
		etiquetas = obtenerEtiquetasDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eVideo, "etiquetas"));
		for(Etiqueta e : etiquetas)
			video.addEtiqueta(e);
		
		return video;
	}

	public List<Video> recuperarTodosVideos() {
		List<Video> videos = new LinkedList<Video>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades("video");

		for (Entidad eVideo : entidades) {
			videos.add(recuperarVideo(eVideo.getId()));
		}
		return videos;
	}
	
	//----------------------------------MÉTODOS AUXILIARES-------------------
	private String obtenerCodigosEtiquetas(List<Etiqueta> listaEtiquetas) {
		String aux = "";
		for (Etiqueta v : listaEtiquetas) {
			aux += v.getCodigo() + " ";
		}
		return aux.trim();
	
	}
	
	private List<Etiqueta> obtenerEtiquetasDesdeCodigos(String etiquetas) {

		List<Etiqueta> listaEtiquetas = new LinkedList<Etiqueta>();
		if (etiquetas != null) {
			StringTokenizer strTok = new StringTokenizer(etiquetas, " ");
			AdaptadorEtiquetaTDS adaptadorV = AdaptadorEtiquetaTDS.getUnicaInstancia();
			while (strTok.hasMoreTokens()) {
				listaEtiquetas.add(adaptadorV.recuperarEtiqueta(Integer.valueOf((String) strTok.nextElement())));	
			}
		}
		return listaEtiquetas;
	}

}
