package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.Mediatype;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.VideoshareResource;




public class Videos {
	@InjectLinks({
		@InjectLink(resource = VideoshareResource.class, style = Style.ABSOLUTE, rel = "videos", title = "Coleccion de videos", type = Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION),
		@InjectLink(resource = VideoshareResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "videos", type = Mediatype.VIDEOSHARE_API_VIDEOS, method = "getVideoid", bindings = @Binding(name = "videoid", value = "${instance.videoid}")) }) 
	
	private List<Link> links;
	private String videoid;
	private String nombre_video;
	private String username;
	private Date fecha;
	private String url;
	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	private String filename;
	private int puntuacion;
	
    
    
    //Para añadir a Reviews, Tags, Puntuaciones...
    
    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	private List<Review> reviews = new ArrayList<Review>();
    private List<Categoria> categorias = new ArrayList<Categoria>();
    private List<Puntuaciones> puntuaciones = new ArrayList<Puntuaciones>();
	

	// aÃ±adimos mÃ©todos para aÃ±adir y 'coger'
	// Reviews
	public List<Review> getReviews() {

		return reviews;
	}
    
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public void addReview(Review review) {
		reviews.add(review);
	}
    
    //Categorias
	public void addCategoria(Categoria categoria) {
		categorias.add(categoria);
	}
    public List<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
    
    //Puntuaciones
    public void addPuntuacion(Puntuaciones puntuacion) {
		puntuaciones.add(puntuacion);
	}
    public List<Puntuaciones> getPuntuaciones() {
		return puntuaciones;
	}
	public void setPuntuaciones(List<Puntuaciones> puntuaciones) {
		this.puntuaciones = puntuaciones;
	}

    //Métodos para las variables
    
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
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
	
	//faltan que añada Reviews/Tags/Puntuaciones
    
	
}
