package dominio;

public class FiltroNombresLargos implements Filtro{

	private static Filtro unicaInstancia = new FiltroNombresLargos();
	private FiltroNombresLargos() {
		super();
	}
	
	public static Filtro getUnicaInstancia() {
		return unicaInstancia;
	}
	
	@Override
	public boolean esVideoOK(Video video) {
		if (video.getTitulo().length() > 16)
			return false;
		return true;
	}
}
