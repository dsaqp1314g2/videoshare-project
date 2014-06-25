package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
 


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
 
public class VideoshareAPI {
	private final static String TAG = VideoshareAPI.class.getName();
	private static VideoshareAPI instance = null;
	private URL url;
 
	private VideoshareRootAPI rootAPI = null;
 
	private VideoshareAPI(Context context) throws IOException,
			VideoshareAndroidException {
		super();
 
		AssetManager assetManager = context.getAssets();
		Properties config = new Properties();
		config.load(assetManager.open("config.properties"));//carga fichero configuracion 
		String serverAddress = config.getProperty("server.address");//obtiene los valores de es fichero
		String serverPort = config.getProperty("server.port");
		url = new URL("http://" + serverAddress + ":" + serverPort
				+ "/videoshare-api"); //se qeda cn la base url esta si utilizamos hateoas nunca cambia
 
		Log.d("LINKS", url.toString());
		getRootAPI();
	}
 
	public final static VideoshareAPI getInstance(Context context)
			throws VideoshareAndroidException {
		if (instance == null)
			try {
				instance = new VideoshareAPI(context);//context es la actividad, para recuperar valores del fichero conf.
			} catch (IOException e) {
				throw new VideoshareAndroidException(
						"Can't load configuration file");
			}
		return instance;
	}
 
	private void getRootAPI() throws VideoshareAndroidException { //rea un modelo y ataka al servicio
		Log.d(TAG, "getRootAPI()");
		rootAPI = new VideoshareRootAPI();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);// true por defecto, significa que qiero leer
			urlConnection.connect();
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't connect to Videoshare API Web Service");
		}
 
		BufferedReader reader;
		try {//lee json que le devuelve htps://localhost:8080/beeterapi
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());// aparti de un string y objeto json lo convierte
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//asi poder manipular y obtener get, arrays.. 
			parseLinks(jsonLinks, rootAPI.getLinks());//lo proceso con el metodo priado de esta clase y lo guardas en el modelo rootAPI
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't get response from Videoshare API Web Service");
		} catch (JSONException e) {
			throw new VideoshareAndroidException("Error parsing Beeter Root API");
		}
 
	}
 
	public VideosCollection getCollectionVideos() throws VideoshareAndroidException {
		Log.d(TAG, "getCollectionVideos");
		VideosCollection videos = new VideosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("create").getTarget()).openConnection();
			//System.out.println(rootAPI.getLinks()
			//		.get("create").getTarget());
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't connect to Videoshare API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, videos.getLinks());
 
			
			JSONArray jsonStings = jsonObject.getJSONArray("video");
			for (int i = 0; i < jsonStings.length(); i++) {
				Videos video = new Videos();//creo un sting
				JSONObject jsonSting = jsonStings.getJSONObject(i);// le doy valor a traves del array y lo a�ado a la coleccion qe es lo qe lo devuelves
				video.setNombre_video(jsonSting.getString("nombre_video"));
				video.setVideoid(jsonSting.getString("videoid"));//se van a�adiendo
				video.setPuntuacion(jsonSting.getString("puntuacion"));
			//	video.setPuntuacion(jsonSting.getString("puntuacion"));;
				video.setUrl(jsonSting.getString("url"));
				video.setUsername(jsonSting.getString("username"));
				jsonLinks = jsonSting.getJSONArray("links");
				parseLinks(jsonLinks, video.getLinks());
				videos.getVideos().add(video);
			}
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new VideoshareAndroidException("Error parsing Beeter Root API");
		}
 
		return videos;
	}
	
	public VideosCollection getCollectionVideosByCategoria() throws VideoshareAndroidException {
		Log.d(TAG, "getCollectionVideosByCategoria");
		VideosCollection videos = new VideosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("create").getTarget()+"/bycategoria").openConnection();
			//System.out.println(rootAPI.getLinks()
			//		.get("create").getTarget());
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't connect to Videoshare API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, videos.getLinks());
 
			
			JSONArray jsonStings = jsonObject.getJSONArray("video");
			for (int i = 0; i < jsonStings.length(); i++) {
				Videos video = new Videos();//creo un sting
				JSONObject jsonSting = jsonStings.getJSONObject(i);// le doy valor a traves del array y lo a�ado a la coleccion qe es lo qe lo devuelves
				video.setNombre_video(jsonSting.getString("nombre_video"));
				video.setVideoid(jsonSting.getString("videoid"));//se van a�adiendo
				video.setPuntuacion(jsonSting.getString("puntuacion"));
			//	video.setPuntuacion(jsonSting.getString("puntuacion"));;
				video.setUrl(jsonSting.getString("url"));
				video.setUsername(jsonSting.getString("username"));
				jsonLinks = jsonSting.getJSONArray("links");
				parseLinks(jsonLinks, video.getLinks());
				videos.getVideos().add(video);
			}
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new VideoshareAndroidException("Error parsing Beeter Root API");
		}
 
		return videos;
	}
	
	public VideosCollection getCollectionVideosByPuntuacion() throws VideoshareAndroidException {
		Log.d(TAG, "getCollectionVideosByPuntuacion");
		VideosCollection videos = new VideosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("create").getTarget()+"/bypuntuacion").openConnection();
			//System.out.println(rootAPI.getLinks()
			//		.get("create").getTarget());
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't connect to Videoshare API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, videos.getLinks());
 
			
			JSONArray jsonStings = jsonObject.getJSONArray("video");
			for (int i = 0; i < jsonStings.length(); i++) {
				Videos video = new Videos();//creo un sting
				JSONObject jsonSting = jsonStings.getJSONObject(i);// le doy valor a traves del array y lo a�ado a la coleccion qe es lo qe lo devuelves
				video.setNombre_video(jsonSting.getString("nombre_video"));
				video.setVideoid(jsonSting.getString("videoid"));//se van a�adiendo
				video.setPuntuacion(jsonSting.getString("puntuacion"));
			//	video.setPuntuacion(jsonSting.getString("puntuacion"));;
				video.setUrl(jsonSting.getString("url"));
				video.setUsername(jsonSting.getString("username"));
				jsonLinks = jsonSting.getJSONArray("links");
				parseLinks(jsonLinks, video.getLinks());
				videos.getVideos().add(video);
			}
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new VideoshareAndroidException("Error parsing Beeter Root API");
		}
 
		return videos;
	}
	
	public VideosCollection getCollectionVideosByUsers() throws VideoshareAndroidException {
		Log.d(TAG, "getCollectionVideosByUsers");
		VideosCollection videos = new VideosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("create").getTarget()+"/byusername").openConnection();
			//System.out.println(rootAPI.getLinks()
			//		.get("create").getTarget());
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't connect to Videoshare API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, videos.getLinks());
 
			
			JSONArray jsonStings = jsonObject.getJSONArray("video");
			for (int i = 0; i < jsonStings.length(); i++) {
				Videos video = new Videos();//creo un sting
				JSONObject jsonSting = jsonStings.getJSONObject(i);// le doy valor a traves del array y lo a�ado a la coleccion qe es lo qe lo devuelves
				video.setNombre_video(jsonSting.getString("nombre_video"));
				video.setVideoid(jsonSting.getString("videoid"));//se van a�adiendo
				video.setPuntuacion(jsonSting.getString("puntuacion"));
			//	video.setPuntuacion(jsonSting.getString("puntuacion"));;
				video.setUrl(jsonSting.getString("url"));
				video.setUsername(jsonSting.getString("username"));
				jsonLinks = jsonSting.getJSONArray("links");
				parseLinks(jsonLinks, video.getLinks());
				videos.getVideos().add(video);
			}
		} catch (IOException e) {
			throw new VideoshareAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new VideoshareAndroidException("Error parsing Beeter Root API");
		}
 
		return videos;
	}
 
	private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
			throws VideoshareAndroidException, JSONException {
		for (int i = 0; i < jsonLinks.length(); i++) {
			Link link = SimpleLinkHeaderParser
					.parseLink(jsonLinks.getString(i));
			//REL PODIA ser multiple rel=" home boomark self" -> 3 enlaces qe obtienes a traves del mapa
			String rel = link.getParameters().get("rel");//tb podria obteet el title i el target(?) pRA QITARME LOS ESPACIOS BLANCOS DE ENCIAM
			String rels[] = rel.split("\\s");
			for (String s : rels)
				map.put(s, link);
		}
	}

	public Videos getVideo(String urlVideo) throws VideoshareAndroidException {
		Videos video = new Videos();
	 
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlVideo);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonVideo = new JSONObject(sb.toString()); //revuperado json, i procesado json
			video.setNombre_video(jsonVideo.getString("nombre_video"));
			video.setVideoid(jsonVideo.getString("videoid"));//se van a�adiendo
			
			video.setUrl("http://10.89.89.150/video/"+jsonVideo.getString("videoid")+".webm");
			video.setUsername(jsonVideo.getString("username"));
			JSONArray categorias = jsonVideo.getJSONArray("categorias");
		//	JSONArray puntuacion = jsonVideo.getJSONArray("puntuaciones");
			
		/*	for (int i = 0; i < puntuacion.length(); i++) {
				JSONObject jsonObject = puntuacion.getJSONObject(i);
			video.setPuntuacion(jsonObject.getString("puntuacion"));
			//video.setCategoria(jsonVideo.getString("categorias"));
			} */
			for (int i = 0; i < categorias.length(); i++) {
		        JSONObject jsonObject = categorias.getJSONObject(i);
		     video.setCategoria(jsonObject.getString("categoria"));
		     
		     
//		    String var1 = jsonObject.getJSONArray("videoid");
//		    int var2 = jsonObject.getJSONArray("videoid");
//		    System.out.println(var1);
//		    System.out.println(var2);
//		    
		}
				JSONArray reviews = jsonVideo.getJSONArray("reviews");
				 String Coment ;
				 Coment = "";
				for (int i = 0; i < reviews.length(); i++) {
			        JSONObject jsonObject2 = reviews.getJSONObject(i);
			        int j=i+1;
			         Coment = Coment +"\n"+"-Comentario n# "+j+":  \n"+ jsonObject2.getString("reviewtext")+"\n";
			        
			        
			        
				}video.setReview(Coment);
			
			
			JSONArray jsonLinks = jsonVideo.getJSONArray("links");
			parseLinks(jsonLinks, video.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new VideoshareAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new VideoshareAndroidException("Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new VideoshareAndroidException("Exception parsing response");
		}
	 
		return video;
	}
/*	
	public Videos createcomment(String comentario) throws VideoshareAndroidException {
		Videos sting = new Videos();
		sting.setReview(comentario);;
		//sting.setContent(content);
		HttpURLConnection urlConnection = null;
		try {
			JSONObject jsonSting = createJsonSting(sting);
			URL urlPostStings = new URL(rootAPI.getLinks().get("create-stings")
					.getTarget());
			urlConnection = (HttpURLConnection) urlPostStings.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.VIDEOSHARE_API_REVIEWS);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.VIDEOSHARE_API_REVIEWS);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			writer.println(jsonSting.toString());
			writer.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			jsonSting = new JSONObject(sb.toString());
	 
			sting.setAuthor(jsonSting.getString("author"));
			sting.setId(jsonSting.getString("id"));
			sting.setLastModified(jsonSting.getLong("lastModified"));
			sting.setSubject(jsonSting.getString("subject"));
			sting.setContent(jsonSting.getString("content"));
			sting.setUsername(jsonSting.getString("username"));
			JSONArray jsonLinks = jsonSting.getJSONArray("links");
			parseLinks(jsonLinks, sting.getLinks());
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new VideoshareAndroidException("Error parsing response");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new BeeterAndroidException("Error getting response");
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return sting;
	}
	//writesting activity progrso void i sting tipo de retorno stings..params (aqi esta tanto el subject como el content) 
	//onpostexecute recarga lista con todos los stings inclusive el nuevo k hemos creado
	//oncreate carga layout
	//dosmetodos poststing y cancel
	//finish acaba actividad y vuelve a la anterior en este caso a la lista de stings , mostrat actividad tal i como estaba
	//en el showstings parecido al finish pero con la lista actualizada
	//post obtenemos  do input leemos, doutpu envamios, createstingjson, atraves de el metodo pivado, se crea json object i se van colocando valores 
	private JSONObject createJsonSting(Videos sting) throws JSONException {
		JSONObject jsonSting = new JSONObject();
		jsonSting.put("subject", sting.getReview());
		//jsonSting.put("content", sting.getContent());
	 
		return jsonSting;
	}
	
	
	*/
}