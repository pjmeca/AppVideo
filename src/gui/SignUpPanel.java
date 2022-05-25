package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

import controlador.ControladorAppVideo;

@SuppressWarnings("serial")
public class SignUpPanel extends Pestaña {

	private GUI myGUI;
	private ControladorAppVideo controlador;
	private JPanel centro;
	private JLabel nombreLabel, apellidosLabel, fechaLabel, emailLabel, nickLabel, passwdLabel, repetirPasswdLabel,
			obliLabel;
	private JTextField nombreField, apellidosField, emailField, nickField, passwdField, repetirPasswdField;
	private JDateChooser fechaField;
	private JButton aceptarButton, cancelarButton;

	/*
	 * Constructor.
	 */
	public SignUpPanel(GUI myGUI) {
		this.myGUI = myGUI;
		controlador = ControladorAppVideo.getUnicaInstancia();

		setBackground(Color.GRAY);
		setLayout(new BorderLayout());
	}

	private static void setSize(int width, int height, JComponent... c) {
		for (JComponent a : c) {
			a.setMaximumSize(new Dimension(width, height));
			a.setMinimumSize(new Dimension(width, height));
			a.setPreferredSize(new Dimension(width, height));
		}
	}

	/*
	 * Inicializa el panel central.
	 */
	@Override
	public void inicializar() {
		centro = new JPanel();
		centro.setBackground(Color.GRAY);
		centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

		nombreLabel = new JLabel("*Nombre: ");
		nombreLabel.setForeground(Color.WHITE);
		apellidosLabel = new JLabel("Apellidos: ");
		apellidosLabel.setForeground(Color.WHITE);
		fechaLabel = new JLabel("*Fecha nacimiento: ");
		fechaLabel.setForeground(Color.WHITE);
		emailLabel = new JLabel("email: ");
		emailLabel.setForeground(Color.WHITE);
		nickLabel = new JLabel("*Usuario: ");
		nickLabel.setForeground(Color.WHITE);
		passwdLabel = new JLabel("*Contraseña: ");
		passwdLabel.setForeground(Color.WHITE);
		repetirPasswdLabel = new JLabel("*Repetir Contraseña: ");
		repetirPasswdLabel.setForeground(Color.WHITE);

		nombreField = new JTextField();
		apellidosField = new JTextField();
		fechaField = new JDateChooser();
		emailField = new JTextField();
		nickField = new JTextField();
		passwdField = new JPasswordField();
		repetirPasswdField = new JPasswordField();

		JPanel panel_usuario = new JPanel();
		JPanel panel_contraseña = new JPanel();
		JPanel panel_rep = new JPanel();
		JPanel panel_nombre = new JPanel();
		JPanel panel_apellidos = new JPanel();
		JPanel panel_fecha = new JPanel();
		JPanel panel_email = new JPanel();
		JPanel panel_botones_login = new JPanel();

		// Panel Nombre
		panel_nombre.setLayout(new BoxLayout(panel_nombre, BoxLayout.X_AXIS));
		panel_nombre.add(nombreLabel);
		panel_nombre.add(nombreField);
		panel_nombre.setBackground(Color.GRAY);

		// Panel Apellidos
		panel_apellidos.setLayout(new BoxLayout(panel_apellidos, BoxLayout.X_AXIS));
		panel_apellidos.add(apellidosLabel);
		panel_apellidos.add(apellidosField);
		panel_apellidos.setBackground(Color.GRAY);

		// Panel fecha de nacimiento
		panel_fecha.setLayout(new BoxLayout(panel_fecha, BoxLayout.X_AXIS));
		panel_fecha.add(fechaLabel);
		panel_fecha.add(fechaField);
		panel_fecha.setBackground(Color.GRAY);

		// Panel email
		panel_email.setLayout(new BoxLayout(panel_email, BoxLayout.X_AXIS));
		panel_email.add(emailLabel);
		panel_email.add(emailField);
		panel_email.setBackground(Color.GRAY);

		// Panel para el usuario
		panel_usuario.setLayout(new BoxLayout(panel_usuario, BoxLayout.X_AXIS));
		panel_usuario.add(nickLabel);
		panel_usuario.add(nickField);
		panel_usuario.setBackground(Color.GRAY);

		// Panel para la contraseña
		panel_contraseña.setLayout(new BoxLayout(panel_contraseña, BoxLayout.X_AXIS));
		panel_contraseña.add(passwdLabel);
		panel_contraseña.add(passwdField);
		panel_contraseña.setBackground(Color.GRAY);

		// Panel para repetir la contraseña
		panel_rep.setLayout(new BoxLayout(panel_rep, BoxLayout.X_AXIS));
		panel_rep.add(repetirPasswdLabel);
		panel_rep.add(repetirPasswdField);
		panel_rep.setBackground(Color.GRAY);

		// Botones
		panel_botones_login.setLayout(new BoxLayout(panel_botones_login, BoxLayout.X_AXIS));
		aceptarButton = new JButton("Aceptar");
		aceptarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aceptar();
			}
		});
		cancelarButton = new JButton("Cancelar");
		cancelarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cancelar();
			}
		});
		panel_botones_login.add(aceptarButton);
		panel_botones_login.add((new JPanel()).add(Box.createRigidArea(new Dimension(135, 40))));
		panel_botones_login.add(cancelarButton);
		panel_botones_login.setBackground(Color.GRAY);

		obliLabel = new JLabel("* Campos obligatorios");
		obliLabel.setForeground(Color.WHITE);

		setSize(300, 25, nombreField, apellidosField);
		setSize(180, 25, fechaField, emailField, nickField, passwdField, repetirPasswdField);
		setSize(500, 30, panel_nombre, panel_apellidos);
		setSize(605, 30, panel_fecha);
		setSize(455, 30, panel_email);
		setSize(492, 30, panel_usuario);
		setSize(535, 30, panel_contraseña);
		setSize(620, 30, panel_rep);
		setSize(380, 30, panel_botones_login);

		centro.add(panel_nombre);
		centro.add(panel_apellidos);
		centro.add(panel_fecha);
		centro.add(panel_email);
		centro.add(Box.createRigidArea(new Dimension(105, 30)));
		centro.add(panel_usuario);
		centro.add(panel_contraseña);
		centro.add(panel_rep);
		centro.add(Box.createRigidArea(new Dimension(105, 30)));
		centro.add(panel_botones_login);
		centro.add(Box.createRigidArea(new Dimension(105, 30)));
		centro.add(obliLabel);
		
		add(centro, BorderLayout.CENTER);
		add((new JPanel()).add(Box.createRigidArea(new Dimension(0, 100))), BorderLayout.NORTH);
		add((new JPanel()).add(Box.createRigidArea(new Dimension(200, 0))), BorderLayout.WEST);
	}

	/*
	 * Método para aceptar el sign up.
	 */
	public void aceptar() {
		System.out.println("Aceptar Sign Up");
		
		// Si ya está logeado, no puede registrarse
		if(controlador.getUsuario() != null) {
			JOptionPane.showMessageDialog(this, "Debes cerrar la sesión actual primero.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Comprueba que todos los parámetros obligatorios estén rellenados
		if (GUI.isBlank(nombreField.getText()) || fechaField.getDate() == null || GUI.isBlank(nickField.getText())
				|| GUI.isBlank(passwdField.getText()) || GUI.isBlank(repetirPasswdField.getText())) { // importante usar
																										// isBlank() en
																										// lugar de
																										// isEmpty()
			JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos obligatorios.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Comprueba que exista la contraseña
		if (!passwdField.getText().equals(repetirPasswdField.getText())) {
			JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			// Registra
			ControladorAppVideo.getUnicaInstancia().registrarUsuario(
					nombreField.getText(), apellidosField.getText(), fechaField.getDate(),
					nickField.getText(), passwdField.getText(), emailField.getText());

			JOptionPane.showMessageDialog(this, "Registro completado!\nPor favor, inicia sesión.", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			myGUI.setPestañaActual(new LoginPanel(myGUI)); // movemos al login para que inicie sesión
		} 
		// Ya existe el usuario
		catch (exceptions.UserAlreadyExistsException e) {
			JOptionPane.showMessageDialog(this, "Ya existe este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/*
	 * Método para cancelar el sign up. Borra todo lo que haya en las casillas.
	 */
	public void cancelar() {
		nombreField.setText("");
		apellidosField.setText("");
		fechaField.setToolTipText("");
		nickField.setText("");
		passwdField.setText("");
		repetirPasswdField.setText("");
		emailField.setText("");
		fechaField.setDate(null);
	}
	
	/*
	 * No se puede acceder al sign up si ya está loggeado.
	 */
	public boolean checkRequirements() {
		if(controlador.isLogged()) {
			JOptionPane.showMessageDialog(this, "Debes cerrar la sesión actual primero.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}

}
