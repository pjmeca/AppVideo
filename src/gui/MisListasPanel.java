package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import controlador.ControladorAppVideo;
import dominio.Video;
import dominio.VideoList;

@SuppressWarnings("serial")
public class MisListasPanel extends Pestaña {

	private GUI myGUI;
	private JPanel centro;
	private TablaResultados tabla;
	private Map<String, VideoList> nombreListasCombo;
	private VideoPlayerPanel reproductor;

	public MisListasPanel(GUI myGUI) {
		this.myGUI = myGUI;

		setBackground(Color.GRAY);
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());
	}

	@Override
	public void inicializar() {

		JPanel izqSuperior = new JPanel();
		JLabel sel_lista = new JLabel("Seleccione la lista: ");
		sel_lista.setForeground(Color.WHITE);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.setBackground(Color.GRAY);
		panel1.add(sel_lista);

		izqSuperior.setMaximumSize(new Dimension(210, 80));
		izqSuperior.setMinimumSize(new Dimension(210, 80));
		izqSuperior.setPreferredSize(new Dimension(210, 80));
		izqSuperior.setBackground(Color.GRAY);
		izqSuperior.setBorder(new EtchedBorder());
		izqSuperior.setLayout(new BoxLayout(izqSuperior, BoxLayout.Y_AXIS));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		JComboBox<String> combo = new JComboBox<>();
		List<VideoList> lista = new LinkedList<VideoList>(
				ControladorAppVideo.getUnicaInstancia().obtenerMisVideoLists());
		nombreListasCombo = new HashMap<>();
		for (VideoList v : lista) {
			combo.addItem(v.getNombre());
			nombreListasCombo.put(v.getNombre(), v);
		}
		
		combo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabla.setResultados(nombreListasCombo.get(combo.getSelectedItem()).getVideos());
				revalidate();
				repaint();
			}
		});
		
		//JButton reproducir = new JButton("Reproducir");
		//panel2.add(reproducir);

		izqSuperior.add(panel1);
		izqSuperior.add(combo);
		izqSuperior.add(Box.createRigidArea(new Dimension(210, 10)));
		izqSuperior.add(panel2);

		JPanel izqAbajo = new JPanel();
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reproductor.detenerVideo();
				centro.removeAll();
				revalidate();
				repaint();
			}
		});

		izqAbajo.setMaximumSize(new Dimension(210, 60));
		izqAbajo.setMinimumSize(new Dimension(210, 60));
		izqAbajo.setPreferredSize(new Dimension(210, 60));
		izqAbajo.setBackground(Color.GRAY);
		izqAbajo.setBorder(new EtchedBorder());
		izqAbajo.setLayout(new BoxLayout(izqAbajo, BoxLayout.X_AXIS));
		izqAbajo.add(Box.createRigidArea(new Dimension(60, 5)));
		izqAbajo.add(cancelar);

		JPanel izquierda = new JPanel();
		izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));

		JPanel izqMedio = new JPanel();
		izqMedio.setMaximumSize(new Dimension(210, 418));
		izqMedio.setMinimumSize(new Dimension(210, 418));
		izqMedio.setPreferredSize(new Dimension(210, 418));
		izqMedio.setBorder(new EtchedBorder());
		izqMedio.setBackground(Color.GRAY);

		
		VideoList l = nombreListasCombo.get(combo.getSelectedItem());
		tabla = new TablaResultados(myGUI, 1, l == null ? null : l.getVideos(), TablaResultados.MANDAR_AVISO);
		izqMedio.setLayout(new BorderLayout(0, 0));
		izqMedio.add(tabla);

		izquierda.add(izqSuperior);
		izquierda.add(izqMedio);
		izquierda.add(izqAbajo);

		centro = new JPanel();
		centro.setBackground(Color.GRAY);
		centro.setBorder(new EtchedBorder());

		add(izquierda, BorderLayout.WEST);
		add(centro, BorderLayout.CENTER);

	}

	@Override
	public void avisoReproducir(Video v) {
		centro.removeAll();
		
		reproductor = new VideoPlayerPanel(myGUI, v);
		centro.add(reproductor);
		
		revalidate(); 
		repaint();
	}
	
	@Override
	public void detenerVideo() {
		if(reproductor != null)
			reproductor.detenerVideo();
	}

	@Override
	public boolean checkRequirements() {
		// TODO Auto-generated method stub
		return myGUI.checkLogged();
	}

}
