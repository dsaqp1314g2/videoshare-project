package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model;



import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.VideoshareResource;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.VideoshareRootAPIResource;

public class VideoshareRootAPI {
	@InjectLinks({
		@InjectLink(resource = VideoshareRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "VIdeoshare Root API", method = "getRootAPI"),
		@InjectLink(resource = VideoshareResource.class, style = Style.ABSOLUTE, rel = "create", title = "videos", type = edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)})

	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	
	
	
}
