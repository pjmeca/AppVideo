package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import dominio.Video;

@SuppressWarnings("serial")
public class TopTenPanel extends Pestaña{

	private GUI myGUI;
	private List<Video> top10;
	private JPanel centro;
	private VideoPlayerPanel reproductor;
	
	public TopTenPanel(GUI myGUI, List<Video> top10) {	
		this.myGUI = myGUI;
		this.top10 = top10;
		setBackground(Color.GRAY);
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());
	}
	
	public void inicializar(){
		
		System.out.print("El top10 actual es: [ ");
		for(Video v : top10) {
			System.out.print(v.getTitulo()+"->"+v.getNumReproducciones()+" ");
		}
		System.out.println("]");
	
		
  		JPanel izqArriba = new JPanel();
		JLabel selLista = new JLabel ("VIDEOS TOP-TEN");
		selLista.setForeground(Color.WHITE);
		selLista.setFont(new Font("Algerian", Font.PLAIN, 25));
		JPanel selListaPanel = new JPanel();
		selListaPanel.setLayout(new BoxLayout(selListaPanel,BoxLayout.X_AXIS));
		selListaPanel.setBackground(Color.GRAY);
		selListaPanel.add(selLista);
		
		izqArriba.setMaximumSize(new Dimension(210,70));
		izqArriba.setMinimumSize(new Dimension(210,70));
		izqArriba.setPreferredSize(new Dimension(210,70));
		izqArriba.setBackground(Color.GRAY);
		izqArriba.setBorder(new EtchedBorder());
		izqArriba.setLayout(new BoxLayout(izqArriba, BoxLayout.Y_AXIS));
		
		izqArriba.add(Box.createRigidArea(new Dimension(210, 10)));
		izqArriba.add(selListaPanel);
		
		JPanel izqAbajo = new JPanel();
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				myGUI.setPestañaActual(new PremiumPanel(myGUI));
			}
		});
		
		izqAbajo.setMaximumSize(new Dimension(210,70));
		izqAbajo.setMinimumSize(new Dimension(210,70));
		izqAbajo.setPreferredSize(new Dimension(210,70));
		izqAbajo.setBackground(Color.GRAY);
		izqAbajo.setBorder(new EtchedBorder());
		izqAbajo.setLayout(new BoxLayout(izqAbajo,BoxLayout.X_AXIS));
		izqAbajo.add(Box.createRigidArea(new Dimension(60, 5)));
		izqAbajo.add(cancelar);
		
		JPanel izquierda = new JPanel();
		izquierda.setLayout(new BoxLayout(izquierda,BoxLayout.Y_AXIS));
		
		JPanel izqCentro = new JPanel();
		izqCentro.setMaximumSize(new Dimension(210,420));
		izqCentro.setMinimumSize(new Dimension(210,420));
		izqCentro.setPreferredSize(new Dimension(210,420));
		izqCentro.setBorder(new EtchedBorder());
		izqCentro.setBackground(Color.GRAY);
		
		// Tabla
		izqCentro.setLayout(new BorderLayout(0, 0));
		izqCentro.add(new TablaResultados(myGUI, 1, top10, TablaResultados.MANDAR_AVISO));
		
		izquierda.add(izqArriba);
		izquierda.add(izqCentro);
		izquierda.add(izqAbajo);
		
		centro = new JPanel();
		centro.setLayout(new BorderLayout(0, 0));
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
		return myGUI.checkLogged();
	}
}

