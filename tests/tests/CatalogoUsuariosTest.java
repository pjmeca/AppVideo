package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.CatalogoUsuarios;
import dominio.Usuario;
import exceptions.UserNotExistsException;

public class CatalogoUsuariosTest {

	private CatalogoUsuarios catalogo;
	
	@Before
	public void unicaInstancia() {
		catalogo = CatalogoUsuarios.getUnicaInstancia();
		
		if(catalogo == null)
			fail("getUnicaInstancia es null");
	}
	
	@Test
	public void getUsuario() {
		if(catalogo.getUsuarios().size() > 0) {
			Usuario u1 = catalogo.getUsuarios().get(0);
			Usuario u2 = catalogo.getUsuario(u1.getNick());
			assertEquals(u1, u2);
			
			u2 = catalogo.getUsuario(u1.getCodigo());
			assertEquals(u1, u2);
			
			try {
				assertNotEquals(null, catalogo.checkContraseña(u1.getNick(), u1.getContraseña()));
				assertEquals(null, catalogo.checkContraseña(u1.getNick(), ""));
				assertEquals(null, catalogo.checkContraseña(u1.getNick(), null));
			} catch (UserNotExistsException e) {
				fail("UserNotExistsException cuando el usuario sí debería existir.");
			}
			
			boolean excepcionLanzada = false;
			
			try {
				assertEquals(null, catalogo.checkContraseña(null, u1.getContraseña()));
			} catch (UserNotExistsException e) {
				excepcionLanzada = true; // La excepción sí debe saltar
			} 
			
			assertEquals(true, excepcionLanzada);
		}
		
		catalogo.addUsuario(null); // No da excepción
		catalogo.removeUsuario(null);
	}

}
