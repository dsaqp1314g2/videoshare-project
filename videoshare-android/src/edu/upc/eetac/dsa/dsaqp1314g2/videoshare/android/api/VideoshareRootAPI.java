package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api;

import java.util.HashMap;
import java.util.Map;
 
public class VideoshareRootAPI {
 
	private Map<String, Link> links;
 
	public VideoshareRootAPI() {
		links = new HashMap<String, Link>();
	}
 
	public Map<String, Link> getLinks() {
		return links;
	}
 
}