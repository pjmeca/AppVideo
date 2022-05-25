package dao;

import java.util.List;
import dominio.Video;

public interface IAdaptadorVideoDAO {
	
	public void registrarVideo(Video video);
	public void borrarVideo(Video video);
	public void modificarVideo(Video video);
	public Video recuperarVideo(int cod);
	public List<Video> recuperarTodosVideos();
	

}
