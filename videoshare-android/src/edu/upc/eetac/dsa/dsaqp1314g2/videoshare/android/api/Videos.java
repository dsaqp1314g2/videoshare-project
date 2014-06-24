package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;



public class Videos {

	private Map<String, Link> links = new HashMap<String, Link>();
	private String videoid;
	private String nombre_video;
	private String username;
	private Date fecha;
	private String url;
	private String filename;
	private String categoria;
	private String review;
	
	
	
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Map<String, Link> getLinks() {
		return links;
	}
	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}
	public String getVideoid() {
		return videoid;
	}
	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}
	public String getNombre_video() {
		return nombre_video;
	}
	public void setNombre_video(String nombre_video) {
		this.nombre_video = nombre_video;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	
}
