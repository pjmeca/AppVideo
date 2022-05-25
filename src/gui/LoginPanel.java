package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import controlador.ControladorAppVideo;
import exceptions.UserNotExistsException;

@SuppressWarnings("serial")
public class LoginPanel extends Pestaña {

	private GUI myGUI;
	private ControladorAppVideo controlador;
	private JPanel centro;
	private JLabel loginLabel, passwordLabel;
	private JTextField loginField;
	private JPasswordField passwordField;
	private JButton aceptarButton, cancelarButton;

	/*
	 * Constructor.
	 */
	public LoginPanel(GUI myGUI) {
		this.myGUI = myGUI;
		controlador = ControladorAppVideo.getUnicaInstancia();

		setBackground(Color.GRAY);
		setLayout(new BorderLayout());

		inicializarCentro();

		add(centro, BorderLayout.CENTER);

		// Alineamos el centro
		add((new JPanel()).add(Box.createRigidArea(new Dimension(0, 150))), BorderLayout.NORTH);
		add((new JPanel()).add(Box.createRigidArea(new Dimension(0, 150))), BorderLayout.SOUTH);
		add((new JPanel()).add(Box.createRigidArea(new Dimension(250, 0))), BorderLayout.WEST);
		add((new JPanel()).add(Box.createRigidArea(new Dimension(250, 0))), BorderLayout.EAST);
	}

	/*
	 * Inicializa el panel central.
	 */
	private void inicializarCentro() {
		// Panel central
		centro = new JPanel();
		centro.setBackground(Color.GRAY);
		centro.setBorder(new EtchedBorder());
		centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

		centro.add((new JPanel()).add(Box.createRigidArea(new Dimension(0, 20))));

		// Labels
		loginLabel = new JLabel("User: ");
		loginLabel.setForeground(Color.WHITE);
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setForeground(Color.WHITE);

		// Login Field
		loginField = new JTextField();
		loginField.setMaximumSize(new Dimension(200, 25));
		loginField.setMinimumSize(new Dimension(200, 25));
		loginField.setPreferredSize(new Dimension(200, 25));

		// Password Field
		passwordField = new JPasswordField();
		passwordField.setMaximumSize(new Dimension(200, 25));
		passwordField.setMinimumSize(new Dimension(200, 25));
		passwordField.setPreferredSize(new Dimension(200, 25));

		// Login Panel
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
		loginPanel.add(loginLabel);
		loginPanel.add(loginField);
		loginPanel.setMaximumSize(new Dimension(250, 30));
		loginPanel.setMinimumSize(new Dimension(250, 30));
		loginPanel.setPreferredSize(new Dimension(250, 30));
		loginPanel.setBackground(Color.GRAY);

		// Password Panel
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		passwordPanel.setMaximumSize(new Dimension(300, 30));
		passwordPanel.setMinimumSize(new Dimension(300, 30));
		passwordPanel.setPreferredSize(new Dimension(300, 30));
		passwordPanel.setBackground(Color.GRAY);

		// Botones
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

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(aceptarButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(105, 40)));
		buttonPanel.add(cancelarButton);
		buttonPanel.setBackground(Color.GRAY);

		// Añadimos los paneles
		centro.add(Box.createRigidArea(new Dimension(105, 40)));
		centro.add(loginPanel);
		centro.add(Box.createRigidArea(new Dimension(105, 30)));
		centro.add(passwordPanel);
		centro.add(Box.createRigidArea(new Dimension(105, 30)));
		centro.add(buttonPanel);
	}

	/*
	 * Método para aceptar el login.
	 */
	public void aceptar() {
		System.out.println("Aceptar Login");

		// Si ya está logeado, no puede hacer login
		if (controlador.isLogged()) {
			JOptionPane.showMessageDialog(this, "Debes cerrar la sesión actual primero.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			// Inicio correcto
			if (ControladorAppVideo.getUnicaInstancia().login(loginField.getText(),
					String.valueOf(passwordField.getPassword()))) {
				
				JOptionPane.showMessageDialog(this, "¡Hola " + loginField.getText() + "!", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				myGUI.login();
				myGUI.setPestañaActual(new RecientesPanel(myGUI));; // cambiamos a la pestaña de explorar
			} else
			// Contraseña incorrecta
				JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
		} 
		// Formato incorrecto
		catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, "El formato del nombre de usuario o contraseña no es válido.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} 
		// No existe el usuario
		catch (UserNotExistsException e) {
			JOptionPane.showMessageDialog(this, "No existe ese usuario.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Método para cancelar el login.
	 */
	public void cancelar() {
		loginField.setText("");
		passwordField.setText("");
	}
	
	/*
	 * No se puede acceder al login si ya está loggeado.
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
