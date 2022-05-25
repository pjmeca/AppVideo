package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.JPanel;

import dominio.Video;

@SuppressWarnings("serial")
public abstract class Pestaña extends JPanel {

	private Image bgImage = null;

	public boolean checkRequirements() {
		return true;
	}

	public void inicializar() {
	}; // Algunas pestañas (MisListas) necesitan inicializarse
		// después de realizar ciertas comprobaciones

	public void setBackground(Image background, int width, int height) {
		bgImage = ImageController.resizeImage(background, width, height);
	}

	public void avisoReproducir(Video v) {}; // Las clases que necesiten reproducir un vídeo a través de
											 // un reproductor incrustado deben implementar este método.
	
	public void detenerVideo() {}; // Las clases que necesiten detener el vídeo en reproducción antes de
								   // cambiar de pestaña deben implementar este método.

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		if (bgImage != null) {
			g.drawImage(bgImage, 0, 0, null);
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(bgImage, 0);
			try {
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
