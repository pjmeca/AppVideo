package dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controlador.ControladorAppVideo;
import dao.FactoriaDAO;
import dao.IAdaptadorVideoDAO;
import exceptions.DAOException;

public class CatalogoVideos {

	private Map<String,Video> videos; 
	private static CatalogoVideos unicaInstancia = new CatalogoVideos();
	
	private FactoriaDAO dao;
	private IAdaptadorVideoDAO adaptadorVideo;
	
	private CatalogoVideos() {
		try {
  			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
  			adaptadorVideo = dao.getVideoDAO();
  			videos = new HashMap<String,Video>();
  			this.cargarCatalogo();
  		} catch (DAOException eDAO) {
  			eDAO.printStackTrace();
  		}
	}
	
	public static CatalogoVideos getUnicaInstancia(){
		return unicaInstancia;
	}
	
	/* Devuelve todos los videos*/
	public List<Video> getVideos(){
		ArrayList<Video> lista = new ArrayList<Video>();
		for (Video c:videos.values()) 
			lista.add(c);
		return lista;
	}
	
	/*
	 * Busca todos los v�deos que coinciden con el t�rmino buscado.
	 * @param titulo Cadena que estar� contenida total o parcialmente en el t�tulo.
	 * @return Una lista con los v�deos que coinciden.
	 */
	public List<Video> findVideo(String titulo){		
		
		if(titulo == null) {
			System.err.println("No se han encontrado v�deos.");
			return new ArrayList<Video>();
		} else if(titulo.length() == 0 || titulo.isBlank()) {
			System.out.println("B�squeda en blanco, todos los v�deos coinciden.");
			return new ArrayList<Video>(videos.values());
		}
		
		ArrayList<Video> lista = new ArrayList<Video>();
		
		// T�tulo
		System.out.println("Buscando v�deos cuyo t�tulo contenga: "+titulo);
		for(Video v : videos.values()) {
			if(v.isTitulo(titulo))
				lista.add(v);
		}
		
		return lista;
	}
	
	public Video getVideo(String id) {
		return videos.get(id);
	}
	
	public Video getVideo(int cod) {
		for (Video p : videos.values()) {
			if (p.getCodigo()==cod) 
				return p;
		}
		return null;
	}
	
	public void addVideo(Video pro) {
		if(pro == null)
			return;
		
		videos.put(pro.getId(),pro);
	}
	public void removeVideo(Video pro) {
		if(pro == null)
			return;
		
		videos.remove(pro.getId());
	}
	
	/*Recupera todos los Videos para trabajar con ellos en memoria*/
	private void cargarCatalogo() throws DAOException {
		 List<Video> videosBD = adaptadorVideo.recuperarTodosVideos();
		 for (Video pro: videosBD) 
			     videos.put(pro.getId(),pro);
	}
	
	public List<Etiqueta> getEtiquetasVideo(String id){
		if(id == null)
			return new LinkedList<Etiqueta>();
		
		return getVideo(id).getEtiquetas();
	}
	
	public void addEtiquetasVideo(String id, List<Etiqueta> etiq) {
		if(id == null || etiq == null)
			return;
		
		Video v1 = getVideo(id);
		etiq.stream().map(e -> e.getNombre()).forEach(System.out::println);;
		for (Etiqueta et: etiq) {
			if (!v1.etiquetaContenida(et.getNombre())) {
				if(!ControladorAppVideo.getUnicaInstancia().existeEtiqueta(et.getNombre()))
					ControladorAppVideo.getUnicaInstancia().registrarEtiqueta(et);
				v1.addEtiqueta(et);
			}
		}
		adaptadorVideo.modificarVideo(v1);
		
	}
	
}

