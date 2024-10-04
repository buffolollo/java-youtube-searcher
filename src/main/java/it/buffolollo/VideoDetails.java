package it.buffolollo;

/**
 * Classe VideoDetails
 * Contiene i dettagli del video su YouTube
 * 
 * @author Matteo Basso
 */
public class VideoDetails {
	private String title;
	private String description;
	private String channelName;
	private Thumbnail[] thumbnails;
	private long duration;
	private long views;
	private String videoID;

	/**
	 * 
	 * @param title       il titolo del video
	 * @param description la descrizione del video
	 * @param channelName il canale del video
	 * @param thumbnails  l'array contenente le miniature del video
	 * @param duration    la durata del video in secondi
	 * @param views       il numero di visualizzazioni
	 * @param videoID     l'id del video
	 */
	public VideoDetails(String title, String description, String channelName, Thumbnail[] thumbnails, long duration,
			long views, String videoID) {
		this.title = title;
		this.description = description;
		this.channelName = channelName;
		this.thumbnails = thumbnails;
		this.duration = duration;
		this.views = views;
		this.videoID = videoID;
	}

	/**
	 * 
	 * @return il titolo del video
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 * @return la descrizione del video
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 
	 * @return il nome del canale
	 */
	public String getChannelName() {
		return this.channelName;
	}

	/**
	 * 
	 * @return un'array contenente le miniature del video
	 */
	public Thumbnail[] getThumbnails() {
		return this.thumbnails;
	}

	/**
	 * 
	 * @return la durata del video in secondi
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * 
	 * @return le views del video
	 */
	public long getViews() {
		return this.views;
	}

	/**
	 * 
	 * @return l'id del video
	 */
	public String getVideoID() {
		return this.videoID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VideoDetails:\n");
		sb.append("Title: " + title);
		sb.append("\nDescription: " + description);
		sb.append("\nChannelName: " + channelName);
		sb.append("\nThumbnails: " + thumbnails);
		sb.append("\nDuration: " + duration + " s");
		sb.append("\nViews: " + views);
		sb.append("\nVideoID: " + videoID);
		return sb.toString();
	}
}
