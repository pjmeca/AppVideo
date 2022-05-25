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
	 * La GUI estar� formada por un panel que se dividir� en dos secciones (JPanel):
	 * 1. La cabecera, que no cambiar� 2. La pesta�aActual, que cambiar� dependiendo
	 * de d�nde nos encontremos
	 */
	private JFrame frame;
	private JPanel panel;
	private Pesta�a pesta�aActual;
	private CabeceraPanel cabecera;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					JFrame f = new LoadingScreen(); // si el ordenador va muy r�pido, no dar� tiempo a verse (hacer
													// wait(true) para ver como s� funciona)
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
		// Creaci�n de la ventana
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
		pesta�aActual = new LoginPanel(this);
		panel.add(cabecera);
		panel.add(pesta�aActual);
		frame.add(panel);
	}

	/*
	 * @return El video web.
	 */
	public static VideoWeb getVideoWeb() {
		return videoWeb;
	}

	/*
	 * Cambia de pesta�a.
	 * 
	 * @return Falso si no se ha podido efectuar, verdadero si s�.
	 */
	public boolean setPesta�aActual(Pesta�a nuevaPesta�a) {
		if (nuevaPesta�a == null) {
			System.err.println("Anulado cambio de pesta�a: La pesta�a es nula.");
			return false;
		}

		if (!pesta�aActual.getClass().equals(nuevaPesta�a.getClass())) {
			// Comprueba los requerimientos de la clase
			if (!nuevaPesta�a.checkRequirements()) {
				System.err.println("Anulado cambio de pesta�a: No se cumplen los requisitos.");
				return false;
			}
			
			// Si la pesta�a actual era el reproductor, hay que parar el v�deo primero
			pesta�aActual.detenerVideo();

			panel.remove(pesta�aActual);
			pesta�aActual = nuevaPesta�a;
			pesta�aActual.inicializar();
			panel.add(pesta�aActual);
			cabecera.setSelected(pesta�aActual);
			frame.revalidate();
			frame.repaint();

			System.out.println("Cambio de pesta�a completado.");
			
			return true;
		}

		System.err.println("Anulado cambio de pesta�a: Las pesta�as son iguales.");
		return false;
	}
	
	/*
	 * La tabla de resultados puede avisar que se debe reproducir un v�deo en la pesta�a actual.
	 */
	public void avisoReproducir(Video v) {
		pesta�aActual.avisoReproducir(v);
	}

	/*
	 * En Java 8 no existe el m�todo isBlank(), por lo que hemos creado nuestra
	 * propia versi�n del m�todo.
	 */
	public static boolean isBlank(String s) {
		if (s == null)
			return true;

		s.replaceAll(" ", "");
		return s.length() == 0;
	}

	/*
	 * Inicia la sesi�n.
	 */
	public void login() {
		cabecera.updateUsuario();
	}

	/*
	 * Cierra la sesi�n.
	 */
	public void logout() {
		cabecera.resetUsuario();
		setPesta�aActual(new LoginPanel(this));
	}

	/*
	 * Comprueba que el usuario est� loggeado para permitirle el acceso al panel.
	 */
	public boolean checkLogged() {
		if (!controlador.isLogged()) {
			setPesta�aActual(new LoginPanel(this));
			JOptionPane.showMessageDialog(null, "Debes iniciar sesi�n para acceder.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
