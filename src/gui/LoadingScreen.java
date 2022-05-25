package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LoadingScreen extends JFrame {

	private JPanel panel;
	public static final String LOADING_IMAGE = "loading.jpg";
	private static final int WIDTH=462, HEIGHT=240;
	
	/*
	 * Crea una pantalla de carga y la muestra.
	 */
	public LoadingScreen() {
		panel = new JPanel();
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);

		// Icono
		ImageIcon icon = ImageController.getImage(GUI.DEFAULT_ICON);
		setIconImage(icon.getImage());

		// Coordenadas para que aparezca centrado
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);

		// Imagen	
		JLabel imagen = new JLabel(ImageController.resizeImage(ImageController.getImage(LOADING_IMAGE), WIDTH, HEIGHT));
		
		panel.setLayout(new FlowLayout());
		panel.add(imagen);
		add(panel);

		setResizable(false);
		setVisible(true);
	}
}
