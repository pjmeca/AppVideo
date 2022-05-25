package dominio;

import controlador.ControladorAppVideo;

public class FiltroMisListas implements Filtro{

	private static Filtro unicaInstancia = new FiltroMisListas();
	private FiltroMisListas() {
		super();
	}
	
	public static Filtro getUnicaInstancia() {
		return unicaInstancia;
	}
	
	@Override
	public boolean esVideoOK(Video video) {
		return !ControladorAppVideo.getUnicaInstancia().videolistsContainsVideo(video);
	}
}
