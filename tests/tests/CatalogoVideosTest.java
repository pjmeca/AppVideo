package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import dominio.CatalogoVideos;
import dominio.Video;

public class CatalogoVideosTest {

private CatalogoVideos catalogo;
	
	@Before
	public void unicaInstancia() {
		catalogo = CatalogoVideos.getUnicaInstancia();
		
		if(catalogo == null)
			fail("getUnicaInstancia es null");
	}
	
	@Test
	public void getVideo() {
		if(catalogo.getVideos().size() > 0) {
			Video v1 = catalogo.getVideos().get(0);
			Video v2 = catalogo.getVideo(v1.getId());
			assertEquals(v1, v2);
			
			v2 = catalogo.getVideo(v1.getCodigo());
			assertEquals(v1, v2);
			
			assertEquals(true, catalogo.findVideo(v1.getTitulo()).contains(v1));
		}
		
		catalogo.addVideo(null); // No da excepción
		catalogo.removeVideo(null);
		catalogo.getVideo(null);
		catalogo.getEtiquetasVideo(null);
		catalogo.findVideo(null);
		catalogo.addEtiquetasVideo(null, null);
	}
}
