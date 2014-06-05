package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class VideoshareApplication extends ResourceConfig {

	public VideoshareApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
		ResourceBundle bundle = ResourceBundle.getBundle("application");

		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			property(key, bundle.getObject(key));
		}
	}
}
