package dao;

import dominio.Filtro;
import dominio.FiltroImpopulares;
import dominio.FiltroMenores;
import dominio.FiltroMisListas;
import dominio.FiltroNombresLargos;
import dominio.NoFiltro;

public class ConversorFiltros {
	
	// Filtros existentes
	// NO CAMBIAR EL ORDEN, NUEVOS FILTROS SE A�ADEN AL FINAL!!!
	private static Filtro filters[] = {
			NoFiltro.getUnicaInstancia(),
			FiltroImpopulares.getUnicaInstancia(),
			FiltroMenores.getUnicaInstancia(),
			FiltroMisListas.getUnicaInstancia(),
			FiltroNombresLargos.getUnicaInstancia()
	};
	
	/*
	 * Busca en el cat�logo el filtro con el c�digo especificado.
	 * @param code El c�digo que buscamos.
	 * @return El filtro o null si no se ha encontrado.
	 */
	public static Filtro getFiltro(int code) throws IndexOutOfBoundsException{
		// Se sale de la lista
		if(filters.length <= code)
			throw new IndexOutOfBoundsException();
		
		return filters[code];
	}
	
	/*
	 * Busca en el cat�logo el c�digo del filtro especificado.
	 * @param filtro El filtro que buscamos.
	 * @return El c�digo o -1 si no se ha encontrado.
	 */
	public static int getCode(Filtro filtro) {
		int pos = -1;
		
		for(int i=0; i<filters.length && pos == -1; i++) {
			if(filters[i].getClass().equals(filtro.getClass()))
				pos = i;
		}
		
		return pos;
	}
	
	/*
	 * Indica si un filtro existe.
	 * @param filter El filtro que queremos comprobar.
	 * @return Un valor booleano.
	 */
	public static boolean exists(Filtro filter) {
		return getCode(filter) != -1;
	}
	
	/*
	 * Convierte un filtro a un string con su c�digo.
	 * @param filtro Filtro a convertit.
	 * @return Una cadena indicando el c�digo del filtro incluido.
	 */
	public static String filtersToString(Filtro filtro) throws exceptions.FilterNotExistsException{
		
		if (!exists(filtro))
			throw new exceptions.FilterNotExistsException();
		return ((Integer)getCode(filtro)).toString();
	}
	
	/*
	 * Convierte un string de c�digo de filtros a filtro.
	 * @param codes String con el c�digo.
	 * @return Filtro.
	 */
	public static Filtro stringToFilters(String codes) throws exceptions.FilterNotExistsException{

		if(codes == null)
			return null;
		
		/*CON EL CODIGO DE ABAJO Y UN PAR DE MODIFICACIONES, PODRIAMOS APLICAR M�S DE UN FILTRO AL MISMO TIEMPO*/
		/*
		// Quita los []
		Pattern p = Pattern.compile("\\[(.*)\\]");
		Matcher m = p.matcher(codes);	
		
		char codigos[];
		
		if(m.find()) {
			codigos = m.group(1).toCharArray();
		}else
			throw new IllegalArgumentException();
		
		// Extrae los c�digos
		List<Filtro> lista = new LinkedList<>(); 
		for(char c : codigos) { // M�ximo 9 filtros, cambiar si se quieren a�adir m�s
			if(c != ',' && c != ' ') {
				// Es un n�mero
				lista.add(getFiltro(c-'0'));
			}
		}*/
		
		return getFiltro(Integer.valueOf(codes));
	}
}
