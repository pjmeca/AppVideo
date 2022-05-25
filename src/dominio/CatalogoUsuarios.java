package dominio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.FactoriaDAO;
import dao.IAdaptadorUsuarioDAO;
import exceptions.DAOException;


public class CatalogoUsuarios {
	
	private Map<String,Usuario> usuarios; 
	private static CatalogoUsuarios unicaInstancia = new CatalogoUsuarios();
	
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
	private CatalogoUsuarios() {
		try {
  			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
  			adaptadorUsuario = dao.getUsuarioDAO();
  			usuarios = new HashMap<String,Usuario>();
  			cargarCatalogo();
  		} catch (DAOException eDAO) {
  			eDAO.printStackTrace();
  		}
	}
	
	/*
	 * Obtiene el cat�logo de usuarios.
	 * @return La �nica instancia del cat�logo de usuarios.
	 */
	public static CatalogoUsuarios getUnicaInstancia(){
		return unicaInstancia;
	}
	
	/*
	 * Obtiene todos los clientes.
	 * @return Una lista con todos los usuarios.
	 */
	
	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u:usuarios.values()) 
			lista.add(u);
		return lista;
		
	}
	
	/*
	 * Busca en el cat�logo el usuario con el nick especificado.
	 * @param nick El nick del usuario que buscamos.
	 * @return El usuario o null si no se ha encontrado.
	 */
	
	public Usuario getUsuario(String login) {
		return usuarios.get(login);
	}
	
	/*
	 * Busca en el cat�logo el usuario con el nick especificado.
	 * @param nick El c�digo del usuario que buscamos.
	 * @return El usuario o null si no se ha encontrado.
	 */
	
	public Usuario getUsuario(int cod) {
		for (Usuario u:usuarios.values()) {
			if (u.getCodigo() == cod) 
				return u;
		}
		return null;
	}
	
	/*
	 * A�ade un usuario al cat�logo.
	 * @param usuario El usuario que se desea a�adir.
	 */
	public void addUsuario(Usuario u) {
		if(u != null)
			usuarios.put(u.getNick(),u);
	}
	
	/*
	 * Elimina un usuario del cat�logo de usuarios.
	 * @param usuario El usuario a eliminar.
	 */
	
	public void removeUsuario (Usuario u) {
		if(u != null)
			usuarios.remove(u.getNick());
	}
	
	/*
	 * Recupera todos los clientes para trabajar con ellos en memoria
	 */
	private void cargarCatalogo() throws DAOException {
		 List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		 for (Usuario u: usuariosBD) 
			 usuarios.put(u.getNick(),u);
	}
	
	/*
	 * Comprueba que la contrase�a sea correcta.
	 * @return El usuario si es correcta, null si no.
	 */
	public Usuario checkContrase�a(String nick, String passwd) throws exceptions.UserNotExistsException{
		Usuario user = getUsuario(nick);
		
		if(user == null)
			throw new exceptions.UserNotExistsException();
		
		System.out.println(user.getFiltro().toString());
		
		if(user.checkContrase�a(passwd))
			return user;
		else
			return null;
	}
}
