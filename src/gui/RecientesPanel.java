package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import controlador.ControladorAppVideo;

@SuppressWarnings("serial")
public class RecientesPanel extends Pestaña{

	private GUI myGUI;
	
	public RecientesPanel(GUI myGUI) {	
		this.myGUI = myGUI;
		
		setBackground(Color.GRAY);
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());		
	}
	
	@Override
	public void inicializar() {
		
		JLabel txtRecientes = new JLabel("Vídeos Recientes");
		txtRecientes.setForeground(Color.WHITE);
		txtRecientes.setFont(new Font("Arial Black", Font.BOLD, 25));
		
  		JPanel izqArriba = new JPanel();
		JLabel selLista = new JLabel ("VIDEOS RECIENTES: ");
		selLista.setForeground(Color.WHITE);
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
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		JButton reproducir = new JButton("Reproducir");
		panel2.add(reproducir);
		
		izqArriba.add(Box.createRigidArea(new Dimension(210, 10)));
		izqArriba.add(selListaPanel);
		izqArriba.add(Box.createRigidArea(new Dimension(0, 10)));
		izqArriba.add(panel2);
		izqArriba.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel izqAbajo = new JPanel();
		JButton cancelar = new JButton("Cancelar");
		
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
		izqCentro.setMaximumSize(new Dimension(210,418));
		izqCentro.setMinimumSize(new Dimension(210,418));
		izqCentro.setPreferredSize(new Dimension(210,418));
		izqCentro.setBorder(new EtchedBorder());
		izqCentro.setBackground(Color.GRAY);
		
		izquierda.add(izqArriba);
		izquierda.add(izqCentro);
		izquierda.add(izqAbajo);
		
		JPanel centro = new JPanel();
		centro.setLayout(new BorderLayout(0, 0));
		centro.setBackground(Color.GRAY);
		centro.setBorder(new EtchedBorder());
		centro.add(new TablaResultados(myGUI, ControladorAppVideo.getUnicaInstancia().getUsuario().getRecientes()));
		
		//add(izquierda, BorderLayout.WEST);
		JPanel aux = new JPanel();
		aux.setBackground(Color.GRAY);
		aux.setLayout(new BoxLayout(aux, BoxLayout.Y_AXIS));
		txtRecientes.setAlignmentX(CENTER_ALIGNMENT);
		aux.add(txtRecientes);
		centro.setAlignmentX(CENTER_ALIGNMENT);
		aux.add(centro);
		add(aux, BorderLayout.CENTER);
		
	}
	
	@Override
	public boolean checkRequirements() {
		// TODO Auto-generated method stub
		return myGUI.checkLogged();
	}
}
