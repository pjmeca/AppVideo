package dominio;

public class NoFiltro implements Filtro{

	private static Filtro unicaInstancia = new NoFiltro();
	
	private NoFiltro() {
		super();
	}
	
	public static Filtro getUnicaInstancia() {
		return unicaInstancia;
	}
	
	@Override
	public boolean esVideoOK(Video video) {
		return true;
	}
}
