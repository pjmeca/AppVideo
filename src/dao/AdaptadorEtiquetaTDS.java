package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import beans.Entidad;
import beans.Propiedad;
import dominio.Etiqueta;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorEtiquetaTDS implements IAdaptadorEtiquetaDAO{
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorEtiquetaTDS unicaInstancia = null;

	public static AdaptadorEtiquetaTDS getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorEtiquetaTDS();
		else
			return unicaInstancia;
	}
	
	private AdaptadorEtiquetaTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un etiqueta se le asigna un identificador ï¿½nico */
	public void registrarEtiqueta(Etiqueta etiqueta) {
		Entidad eEtiqueta = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eEtiqueta = servPersistencia.recuperarEntidad(etiqueta.getCodigo());
		} catch (NullPointerException e) {}
		
		if (eEtiqueta != null) return;

		// crear entidad Etiqueta
		eEtiqueta = new Entidad();
		eEtiqueta.setNombre("etiqueta");
		eEtiqueta.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("nombre", etiqueta.getNombre()))));

		// registrar entidad etiqueta
		eEtiqueta = servPersistencia.registrarEntidad(eEtiqueta);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		etiqueta.setCodigo(eEtiqueta.getId());
	}

	public void borrarEtiqueta(Etiqueta etiqueta) {
		// No se comprueban restricciones de integridad con Venta
		Entidad eEtiqueta = servPersistencia.recuperarEntidad(etiqueta.getCodigo());

		servPersistencia.borrarEntidad(eEtiqueta);
	}

	public void modificarEtiqueta(Etiqueta etiqueta) {

		Entidad eEtiqueta = servPersistencia.recuperarEntidad(etiqueta.getCodigo());

		for (Propiedad prop : eEtiqueta.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(etiqueta.getCodigo()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(etiqueta.getNombre());
			}
			servPersistencia.modificarPropiedad(prop);
		}

	}

	public Etiqueta recuperarEtiqueta(int cod) {

		// Si la entidad está en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(cod))
			return (Etiqueta) PoolDAO.getUnicaInstancia().getObjeto(cod);

		// si no, la recupera de la base de datos
		Entidad eEtiqueta;
		String nombre;

		// recuperar entidad
		eEtiqueta = servPersistencia.recuperarEntidad(cod);
		
		// recuperar propiedades que no son objetos
		nombre = servPersistencia.recuperarPropiedadEntidad(eEtiqueta, "nombre");

		Etiqueta etiqueta = new Etiqueta(nombre);
		etiqueta.setCodigo(cod);

		// IMPORTANTE:añadir el etiqueta al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(cod, etiqueta);
		
		//NO HAY PROPIEDADES QUE SEAN OBJETOS EN ETIQUETA

		return etiqueta;
	}

	public List<Etiqueta> recuperarTodosEtiquetas() {

		List<Entidad> eEtiquetas = servPersistencia.recuperarEntidades("etiqueta");
		List<Etiqueta> etiquetas = new LinkedList<Etiqueta>();

		for (Entidad eEtiqueta : eEtiquetas) {
			etiquetas.add(recuperarEtiqueta(eEtiqueta.getId()));
		}
		return etiquetas;
	}


}
