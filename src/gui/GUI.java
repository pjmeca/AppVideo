package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controlador.ControladorAppVideo;
import dominio.Video;
import tds.video.VideoWeb;

public class GUI {

	public static final int DEFAULT_HEIGHT = 700;
	public static final int DEFAULT_WIDTH = 1000;
	public static final String DEFAULT_ICON = "icono.png";

	private ControladorAppVideo controlador = ControladorAppVideo.getUnicaInstancia();
	private static VideoWeb videoWeb = new VideoWeb(true);

	/*
	 * La GUI estará formada por un panel que se dividirá en dos secciones (JPanel):
	 * 1. La cabecera, que no cambiará 2. La pestañaActual, que cambiará dependiendo
	 * de dónde nos encontremos
	 */
	private JFrame frame;
	private JPanel panel;
	private Pestaña pestañaActual;
	private CabeceraPanel cabecera;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					JFrame f = new LoadingScreen(); // si el ordenador va muy rápido, no dará tiempo a verse (hacer
													// wait(true) para ver como sí funciona)
					// (new Server()).run(); clase eliminada, tengo una copia por si luego decidimos
					// usarla
					ControladorAppVideo.getUnicaInstancia(); // inicializa el controlador y lanza la BBDD
					GUI window = new GUI();
					ControladorAppVideo.getUnicaInstancia();
					window.frame.setVisible(true);
					f.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Creación de la ventana
		frame = new JFrame();
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		frame.setBackground(Color.GRAY);

		// Icono
		ImageIcon icon = ImageController.getImage(DEFAULT_ICON);
		frame.setIconImage(icon.getImage());

		// Coordenadas para que aparezca centrado
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.GRAY);
		cabecera = new CabeceraPanel(this);
		pestañaActual = new LoginPanel(this);
		panel.add(cabecera);
		panel.add(pestañaActual);
		frame.add(panel);
	}

	/*
	 * @return El video web.
	 */
	public static VideoWeb getVideoWeb() {
		return videoWeb;
	}

	/*
	 * Cambia de pestaña.
	 * 
	 * @return Falso si no se ha podido efectuar, verdadero si sí.
	 */
	public boolean setPestañaActual(Pestaña nuevaPestaña) {
		if (nuevaPestaña == null) {
			System.err.println("Anulado cambio de pestaña: La pestaña es nula.");
			return false;
		}

		if (!pestañaActual.getClass().equals(nuevaPestaña.getClass())) {
			// Comprueba los requerimientos de la clase
			if (!nuevaPestaña.checkRequirements()) {
				System.err.println("Anulado cambio de pestaña: No se cumplen los requisitos.");
				return false;
			}
			
			// Si la pestaña actual era el reproductor, hay que parar el vídeo primero
			pestañaActual.detenerVideo();

			panel.remove(pestañaActual);
			pestañaActual = nuevaPestaña;
			pestañaActual.inicializar();
			panel.add(pestañaActual);
			cabecera.setSelected(pestañaActual);
			frame.revalidate();
			frame.repaint();

			System.out.println("Cambio de pestaña completado.");
			
			return true;
		}

		System.err.println("Anulado cambio de pestaña: Las pestañas son iguales.");
		return false;
	}
	
	/*
	 * La tabla de resultados puede avisar que se debe reproducir un vídeo en la pestaña actual.
	 */
	public void avisoReproducir(Video v) {
		pestañaActual.avisoReproducir(v);
	}

	/*
	 * En Java 8 no existe el método isBlank(), por lo que hemos creado nuestra
	 * propia versión del método.
	 */
	public static boolean isBlank(String s) {
		if (s == null)
			return true;

		s.replaceAll(" ", "");
		return s.length() == 0;
	}

	/*
	 * Inicia la sesión.
	 */
	public void login() {
		cabecera.updateUsuario();
	}

	/*
	 * Cierra la sesión.
	 */
	public void logout() {
		cabecera.resetUsuario();
		setPestañaActual(new LoginPanel(this));
	}

	/*
	 * Comprueba que el usuario esté loggeado para permitirle el acceso al panel.
	 */
	public boolean checkLogged() {
		if (!controlador.isLogged()) {
			setPestañaActual(new LoginPanel(this));
			JOptionPane.showMessageDialog(null, "Debes iniciar sesión para acceder.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
