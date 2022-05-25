package dominio;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


import controlador.ControladorAppVideo;

public class FiltroMenores implements Filtro{

	private static Filtro unicaInstancia = new FiltroMenores();
	private FiltroMenores() {
		super();
	}
	
	public static Filtro getUnicaInstancia() {
		return unicaInstancia;
	}
	
	private boolean mayorEdad(Date ant, Date hoy) {
		
		SimpleDateFormat dateFormatY,dateFormatM,dateFormatD;
		dateFormatY = new SimpleDateFormat("yyyy");
		dateFormatM = new SimpleDateFormat("MM");
		dateFormatD = new SimpleDateFormat("dd");
		
		LocalDate fecha1 = LocalDate.of(Integer.valueOf(dateFormatY.format(ant)),
				Integer.valueOf(dateFormatM.format(ant)),
				Integer.valueOf(dateFormatD.format(ant)));
		LocalDate fecha2 = LocalDate.of(Integer.valueOf(dateFormatY.format(hoy)),
				Integer.valueOf(dateFormatM.format(hoy)),
				Integer.valueOf(dateFormatD.format(hoy)));
		
		fecha1 = fecha1.plusYears(18);
		
		return fecha1.isBefore(fecha2) || fecha1.isEqual(fecha2);
		
	
	}
	
	@Override
	public boolean esVideoOK(Video video) {
		for(Etiqueta e : video.getEtiquetas()) {
			if (e.getNombre().equals("Adultos")) {
				Date fecha = ControladorAppVideo.getUnicaInstancia().getFechaNacimiento() ;
				Date hoy = new Date();
				return mayorEdad(fecha,hoy);
			}
		}
		return true;
	}
}
