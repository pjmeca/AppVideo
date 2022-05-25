package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import controlador.ControladorAppVideo;
import dominio.Etiqueta;
import dominio.Video;
import dominio.VideoList;

@SuppressWarnings("serial")
public class NuevaListaPanel extends Pestaña {

	private static final int W_IZQUIERDO = 240;
	private static final int W_CENTRAL = 850;

	private GUI myGUI;

	private JPanel panel_central, panel_izquierdo;

	private TablaResultados tablaResultados;
	private TablaResultados tablaLista;
	private VideoList listaActual;

	public NuevaListaPanel(GUI myGUI) {
		this.myGUI = myGUI;

		setBackground(Color.GRAY);
		setLayout(new BorderLayout());
	}

	@Override
	public void inicializar() {

		/* PANEL IZQUIERDO */
		JPanel izqArriba = new JPanel();
		JLabel sel_lista = new JLabel("     Introducir nombre lista: ");
		sel_lista.setForeground(Color.WHITE);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.setBackground(Color.GRAY);
		panel1.add(sel_lista);

		izqArriba.setMaximumSize(new Dimension(W_IZQUIERDO, 120));
		izqArriba.setMinimumSize(new Dimension(W_IZQUIERDO, 120));
		izqArriba.setPreferredSize(new Dimension(W_IZQUIERDO, 120));
		izqArriba.setBackground(Color.GRAY);
		izqArriba.setBorder(new EtchedBorder());
		izqArriba.setLayout(new BoxLayout(izqArriba, BoxLayout.Y_AXIS));

		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.GRAY);
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));

		JTextField texto = new JTextField();
		texto.setMaximumSize(new Dimension(130, 25));
		texto.setMinimumSize(new Dimension(130, 25));
		texto.setPreferredSize(new Dimension(130, 25));
		JButton buscar_bot = new JButton("Buscar");

		tablaLista = new TablaResultados(myGUI, 1, TablaResultados.MANDAR_AVISO); // Vamos a ignorar el aviso
		buscar_bot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!texto.getText().isBlank()) {
					VideoList lista = ControladorAppVideo.getUnicaInstancia().buscarVideoList(texto.getText());
					if (lista == null) {
						// La lista no existe
						int result = JOptionPane.showConfirmDialog(texto,
								"No existe ninguna lista con ese nombre" + ". ¿Desea crearla?");
						// Presionó que si
						if (result == 0) {
							ControladorAppVideo.getUnicaInstancia().registrarVideoList(texto.getText());
							tablaLista.setResultados(new LinkedList<Video>());
							listaActual = ControladorAppVideo.getUnicaInstancia().buscarVideoList(texto.getText());
						}
						// result == 1 presionó no y ==2 presionó cancelar
					} else {
						// Mostrar la lista
						tablaLista.setResultados(lista.getVideos());
						listaActual = lista;
					}
				}
			}
		});

		panel3.add(texto);
		panel3.add(buscar_bot);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		JButton eliminar = new JButton("Eliminar");
		eliminar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!texto.getText().isBlank()) {
					if (ControladorAppVideo.getUnicaInstancia().buscarVideoList(texto.getText()) != null) {
						int result = JOptionPane.showConfirmDialog(texto, "La lista existe" + ". ¿Desea eliminarla?");
						// Presionó que si
						if (result == 0) {
							ControladorAppVideo.getUnicaInstancia().eliminarVideoList(texto.getText());
							tablaLista.setResultados(new LinkedList<Video>());
							texto.setText("");
						}
						// result == 1 presionó no y ==2 presionó cancelar
					}
				}

			}
		});
		panel2.add(eliminar);

		izqArriba.add(panel1);
		izqArriba.add(panel3);
		izqArriba.add(Box.createRigidArea(new Dimension(W_IZQUIERDO, 10)));
		izqArriba.add(panel2);

		// Botones abajo a la izquierda
		JPanel izqAbajo = new JPanel();
		izqAbajo.setLayout(new BoxLayout(izqAbajo, BoxLayout.Y_AXIS));
		JPanel abajo = new JPanel();
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myGUI.setPestañaActual(new NuevaListaPanel(myGUI));
			}
		});
		JButton añadir = new JButton("Añadir");
		añadir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Video v = tablaResultados.getSelected();
				if (listaActual != null && v != null && !listaActual.contieneVideo(v)) {
					ControladorAppVideo.getUnicaInstancia().addVideoVideoList(v, listaActual);
					tablaLista.setResultados(listaActual.getVideos());
					revalidate();
					repaint();

				}
			}
		});
		JButton quitar = new JButton("Quitar");
		quitar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Video v = tablaLista.getSelected();
				System.out.println("Quitar el vídeo: " + v.getTitulo());
				if (listaActual != null && v != null && listaActual.contieneVideo(v)) {
					ControladorAppVideo.getUnicaInstancia().removeVideoVideoList(v, listaActual);
					tablaLista.setResultados(listaActual.getVideos());
					revalidate();
					repaint();
				}
			}
		});

		izqAbajo.setBackground(Color.GRAY);
		izqAbajo.setBorder(new EtchedBorder());
		izqAbajo.add(Box.createRigidArea(new Dimension(W_IZQUIERDO - 5, 13)));
		abajo.setLayout(new BoxLayout(abajo, BoxLayout.X_AXIS));
		abajo.add(añadir);
		abajo.add(quitar);
		abajo.add(cancelar);
		izqAbajo.add(abajo);
		izqAbajo.add(Box.createRigidArea(new Dimension(W_IZQUIERDO - 7, 13)));

		JPanel general = new JPanel();
		general.setLayout(new BoxLayout(general, BoxLayout.Y_AXIS));

		JPanel izqCentro = new JPanel();
		izqCentro.setMinimumSize(new Dimension(240, 380));
		izqCentro.setMaximumSize(new Dimension(240, 380));
		izqCentro.setPreferredSize(new Dimension(240, 380));
		izqCentro.setBorder(new EtchedBorder());
		izqCentro.setBackground(Color.GRAY);
		izqCentro.setLayout(new BorderLayout(0, 0));
		izqCentro.add(tablaLista);

		panel_izquierdo = new JPanel();
		panel_izquierdo.setBorder(new EtchedBorder());
		panel_izquierdo.setBackground(Color.GRAY);
		panel_izquierdo.setLayout(new BoxLayout(panel_izquierdo, BoxLayout.Y_AXIS));
		panel_izquierdo.add(izqArriba);
		panel_izquierdo.add(izqCentro);
		panel_izquierdo.add(izqAbajo);
		add(panel_izquierdo, BorderLayout.WEST);
		/*--------------------*/

		/* PANEL CENTRAL */
		JPanel grande = new JPanel();
		grande.setBackground(Color.GRAY);
		grande.setBorder(new EtchedBorder());

		JPanel general2 = new JPanel();
		general2.setBackground(Color.GRAY);
		general2.setLayout(new BoxLayout(general2, BoxLayout.Y_AXIS));
		general2.add(grande);

		panel_central = new JPanel();
		panel_central.setBackground(Color.GRAY);
		panel_central.setBorder(new EtchedBorder());
		panel_central.setLayout(new BoxLayout(panel_central, BoxLayout.Y_AXIS));

		JPanel buscar = new JPanel();
		panel_central.add(buscar);
		JPanel buscar1 = new JPanel();
		JPanel buscar2 = new JPanel();

		buscar.setMaximumSize(new Dimension(W_CENTRAL, 120));
		buscar.setMinimumSize(new Dimension(W_CENTRAL, 120));
		buscar.setPreferredSize(new Dimension(W_CENTRAL, 120));
		buscar.setBackground(Color.GRAY);
		buscar.setBorder(new EtchedBorder());
		buscar.setLayout(new BoxLayout(buscar, BoxLayout.Y_AXIS));

		buscar1.setMaximumSize(new Dimension(W_CENTRAL, 50));
		buscar1.setMinimumSize(new Dimension(W_CENTRAL, 50));
		buscar1.setPreferredSize(new Dimension(W_CENTRAL, 50));
		buscar1.setBackground(Color.GRAY);
		buscar1.setLayout(new BoxLayout(buscar1, BoxLayout.X_AXIS));

		buscar2.setMaximumSize(new Dimension(200, 30));
		buscar2.setMinimumSize(new Dimension(200, 30));
		buscar2.setPreferredSize(new Dimension(200, 30));
		buscar2.setBackground(Color.GRAY);
		buscar2.setLayout(new FlowLayout());

		JLabel busq = new JLabel("     Buscar título: ");
		busq.setForeground(Color.WHITE);
		JTextField campo_busq = new JTextField();

		campo_busq.setMaximumSize(new Dimension(450, 25));
		campo_busq.setMinimumSize(new Dimension(450, 25));
		campo_busq.setPreferredSize(new Dimension(450, 25));

		JButton boton_buscar = new JButton("Buscar");
		boton_buscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				buscar(campo_busq.getText(), null);
			}
		});

		JButton nueva_busq = new JButton("Nueva Búsqueda");
		nueva_busq.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				campo_busq.setText("");
				tablaResultados.removeAll();
				tablaResultados.revalidate();
				tablaResultados.repaint();
			}
		});

		buscar1.add(busq);
		buscar1.add(campo_busq);
		buscar1.add(Box.createRigidArea(new Dimension(10, 20)));
		buscar1.add(boton_buscar);
		buscar2.add(nueva_busq);

		buscar.add(Box.createRigidArea(new Dimension(1, 10)));
		buscar.add(buscar1);
		buscar.add(buscar2);
		panel_central.add(general2);

		add(panel_central, BorderLayout.CENTER);

		/***** TABLA DE RESULTADOS *****/
		panel_central.add(tablaResultados = new TablaResultados(myGUI, TablaResultados.N_COLUM_RESULTADOS_DEFAULT,
				TablaResultados.MANDAR_AVISO));
	}

	private void buscar(String texto, Set<Etiqueta> etiquetas) {
		tablaResultados.setResultados(ControladorAppVideo.getUnicaInstancia().buscarVideos(texto, etiquetas));
	}

	@Override
	public boolean checkRequirements() {
		// TODO Auto-generated method stub
		return myGUI.checkLogged();
	}

}
