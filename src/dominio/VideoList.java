package dominio;

import java.util.LinkedList;
import java.util.List;

public class VideoList {

	private String nombre;
	private List<Video> videos;
	private int codigo;
	
	
	public VideoList(String nombre) {
		this.nombre = nombre;
		this.codigo=0;
		videos = new LinkedList<Video>();
	}

	public String getNombre() {
		return nombre;
	}
	
	public List<Video> getVideos() {
		return new LinkedList<Video>(videos);
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo=codigo;
	}
	
	public void addVideo(Video v) {
		videos.add(v);
	}
	
	public boolean contieneVideo(Video v) {
		/*
		for(Video video : videos) {
			if (video.getId().equals(v.getId()))
				return true;
		}
		return false;*/
		
		return videos.stream().anyMatch(e -> e.getId().equals(v.getId()));
	}
	
	public void removeVideo(Video v) {
		videos.remove(v);
	}
}
