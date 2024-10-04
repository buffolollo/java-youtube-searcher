package it.buffolollo;

/**
 * Classe Thumbnail
 * 
 * @author Matteo Basso
 */
public class Thumbnail {
	private int height;
	private int width;
	private String url;
	
	/**
	 * 
	 * @param height l'altezza dell'immagine
	 * @param width la larghezza dell'immagine
	 * @param url l'url dell'immagine
	 */
	public Thumbnail(int height, int width, String url) {
		this.height = height;
		this.width = width;
		this.url = url;
	}
	
	/**
	 * 
	 * @return l'altezza dell'immagine
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * 
	 * @return la larghezza dell'immagine
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * 
	 * @return l'url dell'immagine
	 */
	public String getUrl() {
		return this.url;
	}
	
	@Override
	public String toString() {
		return "Thumbnail\nUrl: " + url + "\nHeight: " + height + " px\nWidth: " + width + " px";
	}
}
