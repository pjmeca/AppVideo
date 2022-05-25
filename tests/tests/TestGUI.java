/*
 * SI SALTA ERROR ---> Proyecto > Build Path > Configure Build Path > Add Library... > JUnit > Junit 4
 */

package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

import controlador.ControladorAppVideo;
import gui.GUI;

public class TestGUI {
	
	private GUI gui;
	
	private final ByteArrayOutputStream err = new ByteArrayOutputStream();
	private final PrintStream originalErr = System.err;
	
	@Before
	public void inicializar() {
		gui = new GUI();
	    System.setErr(new PrintStream(err));
	    assertNotEquals(null, ControladorAppVideo.getUnicaInstancia());
	}
	
	@Test
	public void testCambioPestaña() {
		gui.setPestañaActual(null);
		assertEquals("Anulado cambio de pestaña: La pestaña es nula.", err.toString().trim());
	}
	
	@After
	public void restoreInitialStreams() {
	    System.setErr(originalErr);
	}

}
