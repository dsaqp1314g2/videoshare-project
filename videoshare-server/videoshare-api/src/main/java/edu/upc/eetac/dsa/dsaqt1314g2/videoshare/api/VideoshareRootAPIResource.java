package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.VideoshareRootAPI;

@InjectLinks({
	@InjectLink(resource = VideoshareRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "VIdeoshare Root API", method = "getRootAPI"),
	@InjectLink(resource = VideoshareResource.class, style = Style.ABSOLUTE, rel = "create", title = "videos", type = edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)})


@Path("/")
public class VideoshareRootAPIResource {
	
	@GET
	public VideoshareRootAPI getRootAPI() {
		VideoshareRootAPI api = new VideoshareRootAPI();
		return api;
	}

	private boolean admin;

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	
	
	
	
}
