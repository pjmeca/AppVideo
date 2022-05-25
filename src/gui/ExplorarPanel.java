package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.*;
import controlador.ControladorAppVideo;
import dominio.Etiqueta;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ExplorarPanel extends Pestaña {
	
	public static final Color BACKGROUND_COLOR = Color.GRAY;

	private GUI myGUI;
	private TablaResultados tablaResultados;
	private ControladorAppVideo controlador = ControladorAppVideo.getUnicaInstancia();

	/*
	 * Constructor.
	 */
	public ExplorarPanel(GUI myGUI) {
		this.myGUI = myGUI;

		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());

		init();
	}
	
	public ExplorarPanel(GUI myGUI, Set<Etiqueta> etiquetas) {
		this(myGUI);
		
		buscar("", etiquetas);
	}
	

	@Override
	public void inicializar() {} // No debe inicializar para no colisionar con las búsquedas
	
	private void init() {
		/* PANEL DE LA DERECHA */
		JPanel derecha = new JPanel();
		JLabel etiq_disp = new JLabel("Etiquetas disponibles: ");
		etiq_disp.setForeground(Color.WHITE);
		JLabel etiq_busc = new JLabel("Buscar etiquetas: ");
		etiq_busc.setForeground(Color.WHITE);
		derecha.setBackground(BACKGROUND_COLOR);
		derecha.setBorder(new EtchedBorder());
		derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));

		JList<String> lista1 = new JList<String>();
		// ahora hay que pasarle los datos a la lista pero los datos los tendrá un
		// modelo:
		lista1.setModel(new AbstractListModel<String>() {
			
			List<String> valores = new LinkedList<String>(ControladorAppVideo.getUnicaInstancia().recuperarTodasEtiquetas());

			@Override
			public String getElementAt(int arg0) {
				return valores.get(arg0);
			}

			@Override
			public int getSize() {
				return valores.size();
			}
		});

		JList<String> lista2 = new JList<String>();
		// ahora hay que pasarle los datos a la lista pero los datos los tendrá un
		// modelo:
		lista2.setModel(new DefaultListModel<String>());
		
		lista1.addListSelectionListener(new ListSelectionListener(){

			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					JList<String> source = (JList<String>) e.getSource();
					String selected = source.getSelectedValue().toString();
					if (!((DefaultListModel<String>) lista2.getModel()).contains(selected))
						((DefaultListModel<String>) lista2.getModel()).addElement(selected);
					else ((DefaultListModel<String>) lista2.getModel()).removeElement(selected);
				}
			}
		});
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setMaximumSize(new Dimension(110, 190));
		scrollPane1.setMinimumSize(new Dimension(110, 190));
		scrollPane1.setPreferredSize(new Dimension(110, 190));
		scrollPane1.setViewportView(lista1);
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setMaximumSize(new Dimension(110, 190));
		scrollPane2.setMinimumSize(new Dimension(110, 190));
		scrollPane2.setPreferredSize(new Dimension(110, 190));
		scrollPane2.setViewportView(lista2);
		derecha.add(Box.createRigidArea(new Dimension(10, 20)));
		etiq_disp.setAlignmentX(CENTER_ALIGNMENT);
		derecha.add(etiq_disp);
		derecha.add(Box.createRigidArea(new Dimension(10, 20)));
		scrollPane1.setAlignmentX(CENTER_ALIGNMENT);
		derecha.add(scrollPane1);
		derecha.add(Box.createRigidArea(new Dimension(10, 20)));
		etiq_busc.setAlignmentX(CENTER_ALIGNMENT);
		derecha.add(etiq_busc);
		derecha.add(Box.createRigidArea(new Dimension(10, 20)));
		scrollPane2.setAlignmentX(CENTER_ALIGNMENT);
		derecha.add(scrollPane2);

		/* BÚSQUEDA */
		JPanel buscarPanel = new JPanel();
		JPanel buscarContenido = new JPanel();

		buscarPanel.setBackground(BACKGROUND_COLOR);
		buscarPanel.setBorder(new EtchedBorder());
		buscarPanel.setLayout(new BoxLayout(buscarPanel, BoxLayout.Y_AXIS));

		buscarContenido.setBackground(BACKGROUND_COLOR);
		buscarContenido.setLayout(new BoxLayout(buscarContenido, BoxLayout.X_AXIS));

		JLabel busq = new JLabel("Buscar título: ");
		busq.setForeground(Color.WHITE);
		JTextField campo_busq = new JTextField();

		campo_busq.setMaximumSize(new Dimension(450, 25));
		campo_busq.setMinimumSize(new Dimension(450, 25));
		campo_busq.setPreferredSize(new Dimension(450, 25));

		JButton boton_buscar = new JButton("Buscar");
		boton_buscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> list = new ArrayList<>(lista2.getModel().getSize());
				for(int i = 0; i < lista2.getModel().getSize(); i++) {
				    list.add(lista2.getModel().getElementAt(i));
				}
				buscar(campo_busq.getText(), controlador.stringToEtiquetas(list));
			}
		});
		JButton nueva_busq = new JButton("Nueva Búsqueda");
		nueva_busq.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				campo_busq.setText("");
				((DefaultListModel<String>) lista2.getModel()).removeAllElements();
				tablaResultados.removeAll();
				tablaResultados.revalidate();
				tablaResultados.repaint();
			}
		});

		buscarContenido.add(busq);
		buscarContenido.add(campo_busq);
		buscarContenido.add(Box.createRigidArea(new Dimension(10, 20)));
		buscarContenido.add(boton_buscar);
		buscarContenido.add(Box.createRigidArea(new Dimension(10, 20)));
		buscarContenido.add(nueva_busq);

		buscarPanel.add(Box.createRigidArea(new Dimension(1, 20)));
		buscarPanel.add(buscarContenido);
		buscarPanel.add(Box.createRigidArea(new Dimension(1, 5)));

		/* PANEL CENTRAL */
		JPanel centro = new JPanel();
		centro.setBackground(BACKGROUND_COLOR);
		centro.setBorder(new EtchedBorder());

		/* AÑADIMOS TODOS LOS COMPONENTES */
		add(buscarPanel, BorderLayout.NORTH);
		add(centro, BorderLayout.CENTER);
		add(derecha, BorderLayout.EAST);

		/* RESULTADOS DE LA BÚSQUEDA */
		centro.setLayout(new BorderLayout(0, 0));
		centro.add(tablaResultados = new TablaResultados(myGUI));

	}
	
	private void buscar(String texto, Set<Etiqueta> etiquetas) {
		System.out.println("Buscando vídeos con texto: "+texto+" y etiquetas: "+etiquetas.toString());
		tablaResultados.setResultados(controlador.buscarVideos(texto, etiquetas));
	}

	@Override
	public boolean checkRequirements() {
		return myGUI.checkLogged();
	}
}
