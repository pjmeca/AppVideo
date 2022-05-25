package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventObject;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import controlador.ControladorAppVideo;
import pulsador.IEncendidoListener;
import pulsador.Luz;

@SuppressWarnings("serial")
public class CabeceraPanel extends JPanel {

	/*
	 * --------------- | Cabeza | |-------------| | Menu | ---------------
	 */

	private GUI myGUI;
	private ControladorAppVideo controlador;
	private JPanel cabeza = new JPanel();
	private JPanel menu = new JPanel();
	private JLabel imagen, bienvenida;
	private ImageIcon icon;
	private JButton registro, login, logout, premium, explorar, mis_listas, recientes, nueva_lista, currentSelected;
	private Luz luz;
	public static final String DEFAULT_LOGO = "logo.png";

	public CabeceraPanel(GUI myGUI) {
		controlador = ControladorAppVideo.getUnicaInstancia();
		
		this.myGUI = myGUI;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(GUI.DEFAULT_WIDTH, GUI.DEFAULT_WIDTH/10));
		setPreferredSize(new Dimension(GUI.DEFAULT_WIDTH, GUI.DEFAULT_WIDTH/10));
		setMinimumSize(new Dimension(GUI.DEFAULT_WIDTH, GUI.DEFAULT_WIDTH/10));

		cabeza = new JPanel();
		menu = new JPanel();
		menu.setLayout(new GridLayout());
		currentSelected = null;
		luz = new Luz();

		add(cabeza);
		add(menu);

		inicializarCabeza();
		inicializarMenu();
	}

	private void inicializarCabeza() {
		cabeza.setBackground(Color.WHITE);
		cabeza.setBorder(new LineBorder(Color.BLACK));

		// Logo
		imagen = new JLabel();
		icon = ImageController.getImage(DEFAULT_LOGO);
		imagen.setIcon(icon);
		imagen.setMaximumSize(new Dimension(275, 60));
		imagen.setPreferredSize(new Dimension(275, 60));
		imagen.setMinimumSize(new Dimension(275, 60));
		cabeza.add(imagen);

		cabeza.add(Box.createRigidArea(new Dimension(60, 40)));

		// Mensaje Bienvenida
		if (!GUI.isBlank(controlador.getNickUsuario())) // Lo normal es que esto siempre sea false
			bienvenida = new JLabel("Hola " + controlador.getNickUsuario());
		else
			bienvenida = new JLabel();
		cabeza.add(bienvenida);

		cabeza.add(Box.createRigidArea(new Dimension(60, 40)));

		botonesCabeza();
	}

	private void botonesCabeza() {
		registro = new JButton("Registro");
		registro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registro();
			}
		});
		cabeza.add(registro);

		login = new JButton("Login");
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		cabeza.add(login);

		cabeza.add(Box.createRigidArea(new Dimension(30, 40)));

		logout = new JButton("Logout");
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		cabeza.add(logout);

		cabeza.add(Box.createRigidArea(new Dimension(30, 40)));

		premium = new JButton("Premium");
		premium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				premium();
			}
		});
		premium.setForeground(Color.RED);
		cabeza.add(premium);
		luz.setColor(Color.RED);
		luz.addEncendidoListener(new IEncendidoListener() {
			public void enteradoCambioEncendido(EventObject e) {
				//Saca una pestaña para pedirte el archivo xml con los videos.
				JFileChooser jfc = new JFileChooser();
				if (jfc.showOpenDialog(menu) == JFileChooser.APPROVE_OPTION)
				{
				   File fichero = jfc.getSelectedFile();
				   // Aquí debemos abrir y leer el fichero.
				   if (!fichero.getName().endsWith(".xml")) {
					   JOptionPane.showMessageDialog(menu, "El fichero debe tener extensión xml", "Error",
								JOptionPane.ERROR_MESSAGE);
				   }
				   else {
					   controlador.cargarVideos(fichero);
					   
					   System.out.println("Vídeos nuevos cargados.");
				   }
				  
				}
				
			}
		});
		luz.setBounds(49, 115, 30, 30);
		cabeza.add(Box.createRigidArea(new Dimension(30, 40)));
		cabeza.add(luz);
	}

	private void inicializarMenu() {
		explorar = new JButton("Explorar");
		mis_listas = new JButton("Mis Listas");
		recientes = new JButton("Recientes");
		nueva_lista = new JButton("Nueva Lista");

		explorar.setPreferredSize(new Dimension(10, 10));
		explorar.setForeground(Color.WHITE);
		explorar.setBackground(Color.LIGHT_GRAY);
		mis_listas.setForeground(Color.WHITE);
		mis_listas.setBackground(Color.LIGHT_GRAY);
		recientes.setForeground(Color.WHITE);
		recientes.setBackground(Color.LIGHT_GRAY);
		nueva_lista.setForeground(Color.WHITE);
		nueva_lista.setBackground(Color.LIGHT_GRAY);

		explorar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				explorar();
			}
		});
		mis_listas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				misListas();
			}
		});
		recientes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recientes();
			}
		});
		nueva_lista.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nuevaLista();
			}
		});

		menu.add(explorar);
		menu.add(mis_listas);
		menu.add(recientes);
		menu.add(nueva_lista);
	}

	// Métodos de los botones
	public void registro() {
		System.out.println("Cambiar a ventana Registro");
		myGUI.setPestañaActual(new SignUpPanel(myGUI));
	}

	public void login() {
		System.out.println("Cambiar a ventana Login");
		myGUI.setPestañaActual(new LoginPanel(myGUI));
	}

	public void logout() {
		System.out.println("Logout");
		controlador.logout();
		myGUI.logout();
	}

	public void premium() {
		System.out.println("Cambiar a ventana Premium");
		myGUI.setPestañaActual(new PremiumPanel(myGUI));
	}

	public void explorar() {
		System.out.println("Cambiar a ventana Explorar");
		if (myGUI.setPestañaActual(new ExplorarPanel(myGUI)))
			setSelected(explorar);
	}

	public void misListas() {
		System.out.println("Cambiar a ventana Mis Listas");
		if (myGUI.setPestañaActual(new MisListasPanel(myGUI)))
			setSelected(mis_listas);
	}

	public void recientes() {
		System.out.println("Cambiar a ventana Recientes");
		if (myGUI.setPestañaActual(new RecientesPanel(myGUI)))
			setSelected(recientes);
	}

	public void nuevaLista() {
		System.out.println("Cambiar a ventana Nueva Lista");
		if (myGUI.setPestañaActual(new NuevaListaPanel(myGUI)))
			setSelected(nueva_lista);
	}

	/*
	 * Establece el mensaje de bienvenida con el nick del usuario.
	 * 
	 * @param usuario El nick del usuario.
	 */
	public void updateUsuario() {
		bienvenida.setText("Hola " + controlador.getNickUsuario());
	}

	/*
	 * Reestablece el mensaje de bienvenida.
	 */
	public void resetUsuario() {
		bienvenida.setText("");
	}

	/*
	 * Establece el botón seleccionado y resetea.
	 * 
	 * @param button El botón que queremos seleccionar. Si es nulo, resetea
	 */
	private void setSelected(JButton button) {
		// Si son iguales, omite
		if((button == null && currentSelected == null) ||
			(button != null && button.equals(currentSelected)))
			return;
		
		// Resetea
		if (currentSelected != null) {
			currentSelected.setForeground(Color.WHITE);
			currentSelected.setBackground(Color.LIGHT_GRAY);
			currentSelected = null;
		}

		// Si es nulo, termina
		if (button == null)
			return;

		// Comprobamos que sea un botón correcto
		if (button != explorar && button != mis_listas && button != recientes && button != nueva_lista) {
			System.err.println("[ERROR] El botón no se puede seleccionar: No es válido.");
			return;
		}

		// Seleccionamos el botón
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(51,153,255));

		// Actualizamos
		currentSelected = button;
	}

	/*
	 * Método utilizado por las clases externas para actualizar el botón
	 * seleccionado.
	 * 
	 * @param panel El panel seleccionado.
	 */
	public void setSelected(JPanel panel) {

		if (panel.getClass().equals(ExplorarPanel.class)) {
			setSelected(explorar);
		} else if (panel.getClass().equals(MisListasPanel.class)) {
			setSelected(mis_listas);
		} else if (panel.getClass().equals(RecientesPanel.class)) {
			setSelected(recientes);
		} else if (panel.getClass().equals(NuevaListaPanel.class)) {
			setSelected(nueva_lista);
		} else
			setSelected((JButton) null); // no es ninguna de estas, hay que resetear la cabecera
	}
}
