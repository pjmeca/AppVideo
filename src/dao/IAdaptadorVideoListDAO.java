package dao;

import java.util.List;

import dominio.VideoList;

public interface IAdaptadorVideoListDAO {

	public void registrarVideoList(VideoList lista);
	public void borrarVideoList(VideoList lista);
	public void borrarVideoList(String nombre);
	public void modificarVideoList(VideoList lista);
	public VideoList recuperarVideoList(int codigo);
	public VideoList recuperarVideoList(String titulo);
	public List<VideoList> recuperarTodasVideoLists();
}
