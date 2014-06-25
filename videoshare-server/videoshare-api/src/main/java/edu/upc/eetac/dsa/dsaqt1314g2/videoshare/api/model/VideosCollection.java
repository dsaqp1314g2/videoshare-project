package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.Mediatype;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.VideoshareResource;

public class VideosCollection {
	@InjectLinks({ @InjectLink(resource = VideoshareResource.class, style = Style.ABSOLUTE, rel = "create", title = "Create video", type = Mediatype.VIDEOSHARE_API_VIDEOS) })
	
	private List<Link> links;
	private List<Videos> video;

	public VideosCollection() {// Wtf!!
		super();
		video = new ArrayList<Videos>();
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<Videos> getVideo() {
		return video;
	}

	public void setVideo(List<Videos> video) {
		this.video = video;
	}

	public void addVideos(Videos videos) {
		video.add(videos);
	}
	 
	 
	 public void ordenar() {
	        for(int k=0;k < video.size()-1; k++){
	        	Videos aux = null;
	        	
	        	if(video.get(k).getPuntuacion() < video.get(k+1).getPuntuacion()){
	        		aux = video.get(k);
	        		
	        		
	        		video.set(k, video.get(k+1));
	        		video.set(k, aux);
	        		
	        	}
	        }
	    }
}
