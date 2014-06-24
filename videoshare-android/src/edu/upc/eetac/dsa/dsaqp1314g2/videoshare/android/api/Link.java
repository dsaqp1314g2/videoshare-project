package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api;

import java.util.HashMap;
import java.util.Map;
 
public class Link {
 
	private String target;
	private Map<String, String> parameters;
 
	public Link() {
		parameters = new HashMap<String, String>();
	}
 
	public String getTarget() {
		return target;
	}
 
	public void setTarget(String target) {
		this.target = target;
	}
 
	public Map<String, String> getParameters() {
		return parameters;
	}
}
