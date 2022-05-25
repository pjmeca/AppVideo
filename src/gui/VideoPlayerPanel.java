package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controlador.ControladorAppVideo;
import dominio.Etiqueta;
import dominio.Video;
import tds.video.VideoWeb;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class VideoPlayerPanel extends Pestaña {

	private GUI myGUI;
	
	private VideoWeb videoWeb;
	private JPanel etiquetas;

	public VideoPlayerPanel(GUI gui, Video v) {
		myGUI = gui;

		// Sumamos 1 reproducción
		v.addReproduccion();
		videoWeb = GUI.getVideoWeb();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.GRAY);
		add(Box.createRigidArea(new Dimension(0, 40)));

		// Título
		JLabel tituloLabel = new JLabel(v.getTitulo());
		tituloLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		tituloLabel.setForeground(Color.WHITE);
		add(tituloLabel);

		// Reproducciones
		JLabel visitasLabel = new JLabel("Visto por: " + v.getNumReproducciones() + " usuarios");
		visitasLabel.setForeground(Color.WHITE);
		visitasLabel.setFont(new Font("Arial", Font.BOLD, 15));
		add(visitasLabel);

		add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Vídeo
		videoWeb.playVideo(v.getId());
		add(videoWeb);
		
		//Añadimos el video a los vistos recientemente
		ControladorAppVideo.getUnicaInstancia().addRecientes(v);

		add(Box.createRigidArea(new Dimension(0, 20)));

		// Etiquetas
		etiquetas = new JPanel();
		etiquetas.setBackground(Color.GRAY);
		etiquetas.setLayout(new FlowLayout());
		crearEtiquetas(v);
		add(etiquetas);
		
		JPanel añadirEtiquetas = new JPanel();
		añadirEtiquetas.setBackground(Color.GRAY);
		etiquetas.setLayout(new FlowLayout());
		JLabel nueva = new JLabel("Nueva etiqueta: ");
		nueva.setForeground(Color.WHITE);
		JTextField texto = new JTextField();
		texto.setMaximumSize(new Dimension(450, 25));
		texto.setMinimumSize(new Dimension(450, 25));
		texto.setPreferredSize(new Dimension(450, 25));
		JButton añadir = new JButton("Añadir");
		añadir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(texto.getText());
				añadirEtiquetas(v,texto.getText());
				crearEtiquetas(v);
				revalidate();
				repaint();
			}
		
		});
		añadirEtiquetas.add(nueva);
		añadirEtiquetas.add(texto);
		añadirEtiquetas.add(añadir);
		
		add(añadirEtiquetas);

		centerAll();
	}

	// Centra todos los componentes del panel
	private void centerAll() {
		for (Component c : this.getComponents()) {
			if (c instanceof JComponent)
				((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
		}
	}

	public void detenerVideo() {
		videoWeb.cancel();
	}
	
	/*
	 * Crea los botones con las etiquetas.
	 */
	public void crearEtiquetas(Video v) {
		etiquetas.removeAll();
		if (v != null) {
			for (Etiqueta e : v.getEtiquetas()) {
				JButton b = new JButton(e.getNombre());
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						Set<Etiqueta> aux = new HashSet<Etiqueta>();
						aux.add(e);
						myGUI.setPestañaActual(new ExplorarPanel(myGUI, aux));
					}
				});
				etiquetas.add(b);
			}
		}
	}
	
	public void añadirEtiquetas(Video v, String texto) {
		if (texto != null && !texto.isBlank()) {
			if (!ControladorAppVideo.getUnicaInstancia().existeEtiqueta(texto)) 
				ControladorAppVideo.getUnicaInstancia().registrarEtiqueta(texto);
			if (!v.etiquetaContenida(texto)) {
				ControladorAppVideo.getUnicaInstancia().addEtiqueta(v, texto);
			}
		}
	}

	/*
	 * ----------------------------- CLASE AUXILIAR ------------------------------
	 */
	class EtiquetaRectangulo extends JComponent {
		private String text;
		private int espacio;
		private static final int DEFAULT_ESPACIO = 5;

		public EtiquetaRectangulo(String text) {
			this.text = text;
			espacio = DEFAULT_ESPACIO;
		}

		public void paint(java.awt.Graphics g) {
			g.drawRect(0, 0, 2 * espacio + g.getFontMetrics().stringWidth(text), 2 * espacio + 10);
			g.setColor(Color.BLACK);
			g.drawString(text, espacio, 2 * espacio + 3);
		}
	}

}
