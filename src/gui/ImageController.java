package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageController {

	/*
	 * Versión modificada del método de VideoWeb.
	 */
	public static ImageIcon getThumb(String urlyoutube, int width, int height) {
		URL urlthumb = null;
		ImageIcon imgThumb = ImageController.getImage("noThumb.jpg");
		if (urlyoutube.startsWith("https://www.youtube.com/watch?v=") && urlyoutube.length() == 43) {
			String idvideo = urlyoutube.substring(urlyoutube.indexOf('=') + 1);
			try {
				urlthumb = new URL("http://img.youtube.com/vi/" + idvideo + "/mqdefault.jpg"); //mqdefault devuelve la versión 16:9
				imgThumb = new ImageIcon(urlthumb);
			} catch (Exception exception) {}
		}
		
		if(width > 0 && height > 0) {
			Image image= imgThumb.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return new ImageIcon(image);
		}
		return imgThumb;
	}

	/*
	 * @param videoID La id del vídeo.
	 * @return Un ImageIcon con la miniatura del vídeo en el tamaño por defecto.
	 */
	public static ImageIcon getThumb(String videoID) {

		return getThumb(videoID, 0, 0);
	}

	/*
	 * Guarda una imagen como .jpg dentro del proyecto.
	 */
	public static void storeImage(Image image, String filename) {
		if (image != null) {
			BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = bi.createGraphics();
			g2.drawImage(image, 0, 0, null);
			g2.dispose();
			try {
				ImageIO.write(bi, "jpg", new File(filename + ".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * Obtiene una imagen de los assets.
	 * 
	 * @param name La ruta al fichero a partir de assets.
	 * @return Un ImageIcon o null si no lo ha encontrado.
	 */
	public static ImageIcon getImage(String name) {
		String dir = "assets/" + name;
		ImageIcon icon = new ImageIcon(dir);
		return icon;
	}

	/*
	 * Reescala una ImageIcon.
	 * 
	 * @param img La imagen original.
	 * @param width El nuevo ancho.
	 * @param height El nuevo alto.
	 * @return Un ImageIcon reescalado.
	 */
	public static ImageIcon resizeImage(ImageIcon img, int width, int height) {
		Image resizeImage = img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		return new ImageIcon(resizeImage);
	}
	
	public static Image resizeImage(Image img, int width, int height) {
		Image resizeImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		return resizeImage;
	}

	/*
	 * Extrae la imagen que se encuentra en las coordenadas (x, y) con ancho w y
	 * alto h.
	 * 
	 * @param x the x location of the top of the rectangle to be extracted
	 * @param y the y location of the top of the rectangle to be extracted
	 * @param w the width of the rectangle to be extracted
	 * @param h the height of the rectangle to be extracted
	 */
	public static /* ImageIcon */ Image cropImage(Image icon, int x, int y, int w, int h) {
		// Image aux = icon.getImage();
		Image aux = icon;
		aux = Toolkit.getDefaultToolkit()
				.createImage(new FilteredImageSource(aux.getSource(), new CropImageFilter(x, y, w, h)));
		// icon.setImage(aux);
		return icon;
	}

}
