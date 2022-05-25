package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.JPanel;

import dominio.Video;

@SuppressWarnings("serial")
public abstract class Pesta�a extends JPanel {

	private Image bgImage = null;

	public boolean checkRequirements() {
		return true;
	}

	public void inicializar() {
	}; // Algunas pesta�as (MisListas) necesitan inicializarse
		// despu�s de realizar ciertas comprobaciones

	public void setBackground(Image background, int width, int height) {
		bgImage = ImageController.resizeImage(background, width, height);
	}

	public void avisoReproducir(Video v) {}; // Las clases que necesiten reproducir un v�deo a trav�s de
											 // un reproductor incrustado deben implementar este m�todo.
	
	public void detenerVideo() {}; // Las clases que necesiten detener el v�deo en reproducci�n antes de
								   // cambiar de pesta�a deben implementar este m�todo.

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
