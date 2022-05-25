package dominio;

public class FiltroImpopulares implements Filtro{

	private static Filtro unicaInstancia = new FiltroImpopulares();
	
	private FiltroImpopulares() {
		super();
	}
	
	public static Filtro getUnicaInstancia() {
		return unicaInstancia;
	}
	
	@Override
	public boolean esVideoOK(Video video) {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		if (video.getNumReproducciones() < 5)
			return false;
		return true;
	}
}
