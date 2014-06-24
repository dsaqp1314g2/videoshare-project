package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class VideosCollection {
	private Map<String, Link> links = new HashMap<String, Link>();
	private List<Videos> videos = new ArrayList<Videos>();
	
	public Map<String, Link> getLinks() {
		return links;
	}
	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}
	public List<Videos> getVideos() {
		return videos;
	}
	public void setVideos(List<Videos> videos) {
		this.videos = videos;
	};
	
	public void addVideo(Videos video) {
		videos.add(video);
	}

	
	
}
