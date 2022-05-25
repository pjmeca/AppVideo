package gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import controlador.ControladorAppVideo;
import dao.ConversorFiltros;
import dominio.Filtro;
import exceptions.UserNotExistsException;

@SuppressWarnings("serial")
public class PremiumPanel extends Pestaña {

	private GUI myGUI;
	private ControladorAppVideo controlador;
	public static final String QUALITY_SEAL_IMAGE = "quality-control-seal.png";
	public static final int QUALITY_SEAL_WIDTH = 150, QUALITY_SEAL_HEIGHT = 140;
	public static final String ARROW_IMAGE = "arrow.png";
	private static final int ARROW_WIDTH = 52, ARROW_HEIGHT = 52;
	public static final String OFERTA_CARTEL_IMAGE = "cartel.png";
	private static final int OFERTA_CARTEL_WIDTH = 160, OFERTA_CARTEL_HEIGHT = 140;
	public static final String PREMIUM_IMAGE = "premium_wallpaper.jpg";
	public static final int PREMIUM_WIDTH = GUI.DEFAULT_WIDTH, PREMIUM_HEIGHT = GUI.DEFAULT_HEIGHT;
	public static final String ALREADY_PREMIUM_IMAGE = "es_premium_wallpaper.jpg";
	public static final int ALREADY_PREMIUM_WIDTH = GUI.DEFAULT_WIDTH, ALREADY_PREMIUM_HEIGHT = GUI.DEFAULT_HEIGHT;

	public PremiumPanel(GUI myGUI) {
		this.myGUI = myGUI;
		controlador = ControladorAppVideo.getUnicaInstancia();

		setMinimumSize(new Dimension(1000, 600));
		setMaximumSize(new Dimension(1000, 600));
		setPreferredSize(new Dimension(1000, 600));
		setBackground(Color.GRAY);
		setLayout(null);

		if (!controlador.isPremium())
			hacersePremium();
		else
			premiumScreen();
	}

	// Si no es premium, se le muestra esta pantalla
	private void hacersePremium() {
		JLabel lblMensaje = new JLabel("\u00A1HAZTE PREMIUM!");
		lblMensaje.setFont(new Font("Arial Black", Font.BOLD, 18));
		lblMensaje.setForeground(Color.WHITE);
		lblMensaje.setBounds(325, 191, 188, 41);
		add(lblMensaje);

		JLabel lblImagen = new JLabel(
				ImageController.resizeImage(ImageController.getImage(QUALITY_SEAL_IMAGE), QUALITY_SEAL_WIDTH, QUALITY_SEAL_HEIGHT));
		lblImagen.setBounds(523, 168, QUALITY_SEAL_WIDTH, QUALITY_SEAL_HEIGHT);
		add(lblImagen);

		JLabel lblArrow = new JLabel(ImageController.resizeImage(ImageController.getImage(ARROW_IMAGE), ARROW_WIDTH, ARROW_HEIGHT));
		lblArrow.setBounds(273, 221, ARROW_WIDTH, ARROW_HEIGHT);
		add(lblArrow);

		JButton btnHazClick = new JButton("CLICK AQUÍ");
		btnHazClick.setBackground(new Color(0, 191, 255));
		btnHazClick.setForeground(Color.WHITE);
		btnHazClick.setBounds(335, 229, 163, 41);
		btnHazClick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickHacersePremium();

			}
		});
		add(btnHazClick);

		JLabel lblNewLabel = new JLabel("1 solo pago. 100% Legal. Sin compromisos. Cancela cuando quieras.");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(297, 307, 439, 32);
		add(lblNewLabel);

		JLabel lblVengaHazClick = new JLabel("Venga, haz click :)");
		lblVengaHazClick.setForeground(Color.WHITE);
		lblVengaHazClick.setBounds(411, 333, 243, 32);
		add(lblVengaHazClick);

		JTextPane txtpngratis = new JTextPane();
		txtpngratis.setEditable(false);
		txtpngratis.setToolTipText("");
		txtpngratis.setFont(new Font("Arial Black", Font.BOLD, 15));
		txtpngratis.setForeground(Color.WHITE);
		txtpngratis.setText("\u00A1GRATIS por tiempo LIMITADO!");
		txtpngratis.setBounds(10, 21, 120, 99);
		txtpngratis.setBackground(new Color(0, 0, 0, 0));
		add(txtpngratis);

		JLabel lblOfertaCartel = new JLabel(
				ImageController.resizeImage(ImageController.getImage(OFERTA_CARTEL_IMAGE), OFERTA_CARTEL_WIDTH, OFERTA_CARTEL_HEIGHT));
		lblOfertaCartel.setBounds(0, 0, OFERTA_CARTEL_WIDTH, OFERTA_CARTEL_HEIGHT);
		add(lblOfertaCartel);

		JPanel rectangle = new JPanel() {
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(0, 0, 0, 75));
				g.fillRect(0, 0, 500, 500);
				super.paintComponent(g);
			}
		};
		rectangle.setOpaque(false);
		rectangle.setBounds(230, 156, 495, 224);
		add(rectangle);

		JLabel lblPremiumWallpaper = new JLabel(
				ImageController.resizeImage(ImageController.getImage(PREMIUM_IMAGE), PREMIUM_WIDTH, PREMIUM_HEIGHT));
		lblPremiumWallpaper.setBounds(0, 0, PREMIUM_WIDTH, PREMIUM_HEIGHT);
		add(lblPremiumWallpaper);
	}

	// Si es premium, se le muestra esta pantalla
	private void premiumScreen() {		
		JLabel lblMensaje = new JLabel("¡YA ERES PREMIUM!");
		lblMensaje.setFont(new Font("Arial Black", Font.BOLD, 18));
		lblMensaje.setForeground(Color.WHITE);
		lblMensaje.setBounds(325, 191, 300, 41);
		add(lblMensaje);
		
		JButton btnHazClick = new JButton("DARSE DE BAJA");
		btnHazClick.setBackground(Color.RED);
		btnHazClick.setForeground(Color.WHITE);
		btnHazClick.setBounds(330, 229, 163, 41);
		btnHazClick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickDarseDeBaja();

			}
		});
		add(btnHazClick);
		
		JButton btnGenerarPDF = new JButton("GENERAR PDF");
		btnGenerarPDF.setBackground(Color.RED);
		btnGenerarPDF.setForeground(Color.WHITE);
		btnGenerarPDF.setBounds(360, 20, 163, 41);
		btnGenerarPDF.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				generarPDF();
			}
		});
		add(btnGenerarPDF);
		
		JButton btnVerTop= new JButton("VER TOP-TEN");
		btnVerTop.setBackground(Color.RED);
		btnVerTop.setForeground(Color.WHITE);
		btnVerTop.setBounds(770, 250, 163, 41);
		btnVerTop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cambiarTop10();
			}
		});
		add(btnVerTop);
		
		JComboBox<String> filtro = new JComboBox<>();
		
		filtro.setBackground(Color.RED);
		filtro.setForeground(Color.WHITE);
		filtro.setBounds(360, 410, 163, 41);

		List<Filtro> filtros = controlador.obtenerTodosFiltros();
		for(Filtro f : filtros) {
			filtro.addItem(f.getClass().getSimpleName());
		}
		filtro.setSelectedIndex(ConversorFiltros.getCode(controlador.getFiltroUsuarioActual()));
		filtro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.setFiltroUsuarioActual(ConversorFiltros.getFiltro(filtro.getSelectedIndex()));		
			}	
		});
		
		//System.out.println(ConversorFiltros.getFiltro(filtro.getSelectedIndex()));
		add(filtro);
		
		JLabel lblNewLabel = new JLabel("1 solo pago. 100% Legal. Sin compromisos. Cancela cuando quieras.");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(297, 307, 439, 32);
		add(lblNewLabel);

		JLabel lblVengaHazClick = new JLabel("Venga, haz click :)");
		lblVengaHazClick.setForeground(Color.WHITE);
		lblVengaHazClick.setBounds(411, 333, 243, 32);
		add(lblVengaHazClick);

		JTextPane txtpngratis = new JTextPane();
		txtpngratis.setEditable(false);
		txtpngratis.setToolTipText("");
		txtpngratis.setFont(new Font("Arial Black", Font.BOLD, 15));
		txtpngratis.setForeground(Color.WHITE);
		txtpngratis.setText("\u00A1GRATIS por tiempo LIMITADO!");
		txtpngratis.setBounds(10, 21, 120, 99);
		txtpngratis.setBackground(new Color(0, 0, 0, 0));
		add(txtpngratis);

		JLabel lblOfertaCartel = new JLabel(
				ImageController.resizeImage(ImageController.getImage(OFERTA_CARTEL_IMAGE), OFERTA_CARTEL_WIDTH, OFERTA_CARTEL_HEIGHT));
		lblOfertaCartel.setBounds(0, 0, OFERTA_CARTEL_WIDTH, OFERTA_CARTEL_HEIGHT);
		add(lblOfertaCartel);
		
		//El rectangulo donde se mete dentro todo
		JPanel rectangle = new JPanel() {
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(0, 0, 0, 75));
				g.fillRect(0, 0, 500, 500);
				super.paintComponent(g);
			}
		};
		rectangle.setOpaque(false);
		rectangle.setBounds(230, 156, 495, 224);
		add(rectangle);

		JLabel lblPremiumWallpaper = new JLabel(
				ImageController.resizeImage(ImageController.getImage(ALREADY_PREMIUM_IMAGE), ALREADY_PREMIUM_WIDTH, ALREADY_PREMIUM_HEIGHT));
		lblPremiumWallpaper.setBounds(0, 0, ALREADY_PREMIUM_WIDTH, ALREADY_PREMIUM_HEIGHT);
		add(lblPremiumWallpaper);
	}

	public void clickHacersePremium() {
		System.out.println("Click en hacerse premium.");
		JOptionPane.showMessageDialog(this, "Efectuando el pago...", "En proceso", JOptionPane.WARNING_MESSAGE);
		try {
			controlador.setPremium(true);
		} catch (UserNotExistsException e) {
			e.printStackTrace(); // no debería ocurrir nunca si controlamos bien que haya que estar loggeado
		}
		JOptionPane.showMessageDialog(this,
				"El pago se efectuá con éxito.\n¡Bienvenido al programa de usuarios premium!\nDesde AppVideo estamos comprometidos por hacer\ntu estancia lo mejor posible.",
				"éxito", JOptionPane.INFORMATION_MESSAGE);
		removeAll();
		premiumScreen();
		revalidate();
		repaint();
	}
	
	public void clickDarseDeBaja() {
		System.out.println("Click en darse de baja de premium.");
		try {
			controlador.bajaPremium();
		} catch (UserNotExistsException e) {
			e.printStackTrace(); // no debería ocurrir nunca si controlamos bien que haya que estar loggeado
		}
		JOptionPane.showMessageDialog(this,
				"Te has dado de baja de nuestro servicio premium,\npor lo que ya no podrás seguir disfrutando de tus ventajas exclusivas.\n¡Seguiremos trabajando muy fuerte para traerte de vuelta!\n",
				"Sentimos que te vayas :(", JOptionPane.INFORMATION_MESSAGE);
		
		removeAll();
		hacersePremium();
		revalidate();
		repaint();
	}
	
	public void generarPDF() {
		
		FileOutputStream archivo = null;
		try {
			archivo = new FileOutputStream("mis_listas.pdf");
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		Document documento = new Document();
		try {
			PdfWriter.getInstance(documento, archivo);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		documento.open(); 
		try {
			documento.add(new Paragraph(controlador.getVideoListsUsuario()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		documento.close();
	}
	
	public void cambiarTop10() {
		System.out.println("Cambiar a ventana Top10");
		myGUI.setPestañaActual(new TopTenPanel(myGUI,controlador.getTop10()));
	}

	@Override
	public boolean checkRequirements() {
		if(!controlador.isLogged()) {
			JOptionPane.showMessageDialog(this, "Para gestionar tu suscripción premium\ndebes iniciar sesión en tu cuenta primero.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}