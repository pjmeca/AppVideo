package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import dominio.Video;

@SuppressWarnings("serial")
public class TablaResultados extends JPanel {

	// APLICAR EL OBJETO SOBRE UN PANEL CON LAYOUT new BorderLayout(0, 0)

	/*
	 * TablaResultados <- JScroll <- JTable de Celda Primero crear el objeto y luego
	 * llamar a setResultados.
	 */

	private GUI myGUI;

	private static final Color BACKGROUND_COLOR = Color.GRAY;
	public static final int N_COLUM_RESULTADOS_DEFAULT = 4;
	public int N_COLUM_RESULTADOS = N_COLUM_RESULTADOS_DEFAULT;
	public static final int ROW_HEIGHT = 120;
	private List<Video> resultados;
	public static final int NUEVA_PESTAÑA = 0;
	public static final int MANDAR_AVISO = 1;
	private int modo = NUEVA_PESTAÑA;
	private Video lastVideoSelected = null;

	public TablaResultados(GUI gui) {
		myGUI = gui;

		setVisible(false);
		setBackground(BACKGROUND_COLOR);
		setLayout(new BorderLayout(0, 0));
	}

	/*
	 * Crea una tabla de resultados con @param ncolumn columnas.
	 * 
	 * @param ncolumn Número de columnas.
	 */
	public TablaResultados(GUI gui, int ncolumn) {
		this(gui);
		N_COLUM_RESULTADOS = ncolumn;
	}

	public TablaResultados(GUI gui, List<Video> resultados) {
		this(gui);

		setResultados(resultados);
	}

	public TablaResultados(GUI gui, int ncolumn, List<Video> resultados) {
		this(gui, ncolumn);

		setResultados(resultados);
	}

	/*
	 * @param modo Modo en el que se reproducirán los vídeos. NUEVA_PESTAÑA --> Se
	 * crea una nueva pestaña con el reproductor. MANDAR_AVISO --> La tabla avisará
	 * a la clase, la cual debe implementar el método avisoReproducir de Pestaña.
	 */
	public TablaResultados(GUI gui, int ncolumn, List<Video> resultados, int modo) {
		this(gui, ncolumn);

		setResultados(resultados);
		this.modo = modo;
	}

	/*
	 * @param modo Modo en el que se reproducirán los vídeos. NUEVA_PESTAÑA --> Se
	 * crea una nueva pestaña con el reproductor. MANDAR_AVISO --> La tabla avisará
	 * a la clase, la cual debe implementar el método avisoReproducir de Pestaña.
	 */
	public TablaResultados(GUI gui, int ncolumn, int modo) {
		this(gui, ncolumn);
		this.modo = modo;
	}

	public void setResultados(List<Video> resultados) {

		// Borramos todo lo que haya en el panel de resultados
		removeAll();

		// Si no ha encontrado resultados, simplemente mostraremos un JLabel
		if (resultados == null) {
			add(new JLabel("No hay resultados."), BorderLayout.NORTH);
			revalidate();
			repaint();
			setVisible(true);
			return;
		}

		this.resultados = resultados;
		System.out.println("Estableciendo los siguientes resultados en la tabla: "+resultados.toString());

		// Creamos una nueva tabla
		JTable tabla = new JTable(0, N_COLUM_RESULTADOS) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabla.setTableHeader(null);
		tabla.setDragEnabled(false);
		tabla.setRowSelectionAllowed(false);
		tabla.setCellSelectionEnabled(true);
		tabla.setRowHeight(ROW_HEIGHT);
		tabla.setBackground(BACKGROUND_COLOR);
		DefaultTableModel tablaModel = (DefaultTableModel) tabla.getModel();

		// La tabla tendrá N_COLUM_RESULTADOS vídeos por fila,
		// así que recorremos los resultados de N_COLUM_RESULTADOS en N_COLUM_RESULTADOS
		int i = 0;
		for (int j = N_COLUM_RESULTADOS - 1; j < resultados.size(); i += N_COLUM_RESULTADOS, j += N_COLUM_RESULTADOS) {

			ArrayList<Celda> data = new ArrayList<>();
			for (int n = i; n <= j; n++) {
				Celda aux = new Celda(resultados.get(n).getTitulo(),
						ImageController.getThumb(resultados.get(n).getId()));
				data.add(aux);
			}

			tablaModel.addRow(data.toArray());
		}

		// Controlamos el caso en el que el número de resultados no sea múltiplo de
		// N_COLUM_RESULTADOS
		if (i < resultados.size()) {
			ArrayList<Celda> data = new ArrayList<>();
			for (int n = 0; n < N_COLUM_RESULTADOS; n++, i++) {
				if (i < resultados.size()) {
					Celda aux = new Celda(resultados.get(i).getTitulo(),
							ImageController.getThumb(resultados.get(i).getId()));
					data.add(aux);
				} else {
					data.add(new Celda("", null));
				}
			}
			tablaModel.addRow(data.toArray());
		}

		// Comportamiento al hacer click
		tabla.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 || e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					
					lastVideoSelected = getVideo(row, column);
					//System.out.println(lastVideoSelected);
				}
				if (e.getClickCount() == 2) {
					mostrarVideo(lastVideoSelected);
				}
			}
		});

		// Cambiamos el renderizado de las celdas para que pueda renderizar imagen+texto
		for (int ncol = 0; ncol < tabla.getColumnModel().getColumnCount(); ncol++)
			tabla.getColumnModel().getColumn(ncol).setCellRenderer(new IconTextRenderer());

		// La añadimos al scroll
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.getViewport().setBackground(BACKGROUND_COLOR);
		add(scroll);

		// Actualizamos el panel y nos aseguramos de que se vea
		revalidate();
		repaint();
		setVisible(true);
	}

	private void mostrarVideo(Video v) {
		// Obtener el vídeo al que se refiere
		// Como tenemos todos los vídeos en una lista, hay que convertir las medidas de
		// la tabla a lista
		// 1 fila --> N_COLUMN columnas --> N_COLUMN posiciones en la lista
		if(v == null)
			return;

		if (modo == NUEVA_PESTAÑA)
			myGUI.setPestañaActual(new VideoPlayerPanel(myGUI, v));
		else
			myGUI.avisoReproducir(v);
	}

	public Video getVideo(int row, int column) {
		int pos = row * N_COLUM_RESULTADOS + column;
		if (pos < resultados.size()) {
			Video v = resultados.get(pos);
			return v;
		}
		return null;
	}

	public Video getSelected() {
		return lastVideoSelected;
	}

}

/*
 * ---------------------- CLASES AUXILIARES ------------------------------
 */

class Celda {

	public static final int THUMB_HEIGHT = 100;

	public String text;
	public Icon icon = null;

	public Celda(String text, ImageIcon icon) {
		if(!text.isBlank())
			System.out.println("Nueva celda para el vídeo: "+text+" con icono: "+icon.toString());
		
		this.text = text;

		if (icon != null) {
			this.icon = ImageController.resizeImage(icon, 2 * THUMB_HEIGHT, THUMB_HEIGHT);
		}
	}
}

@SuppressWarnings("serial")
class IconTextRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		Celda item = (Celda) value;
		setIcon(item.icon);
		setText(item.text);

		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);

		return this;
	}
}
