package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.Videos;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideoshareAPI;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideoshareAndroidException;


 
public class Videosreproductor extends Activity {
	//private final static String TAG = VideosDetailActivity.class.getName();
	VideoView videoView = null;
	String Movie = "";
	//private static final String Movie = "http://10.89.88.49/video/1.webm";
	
	 MediaPlayer mediaPlayer;
	 Button buttonPlayPause, buttonQuit;
	 TextView textState;
	@Override//getextras qe se los pasa el mainactivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String urlVideo = (String) getIntent().getExtras().get("url");
		(new FetchStingTask()).execute(urlVideo);
	    
		
	}
	//metodo privado a partir del sting recu�rado del servicio damos valores a las etiketas qe hemos dado
	private void loadVideos(Videos video) {
		
		Movie = video.getUrl();
		   VideoView vid = (VideoView) findViewById(R.id.videoViewer);
					//Use a media controller so that you can scroll the video contents
					//and also to pause, start the video.
					//vid.setVideoPath("http://10.89.62.167/video/video.webm");
					Uri video1 = Uri.parse(Movie);
					vid.setMediaController(new MediaController(this));
					vid.setVideoURI(video1);
					vid.requestFocus();
					vid.start();
					TextView tvDetailSubject = (TextView) findViewById(R.id.textView1);
					TextView NombreyCategoria = (TextView) findViewById(R.id.textView2);
					NombreyCategoria.setText("Nombre: "+video.getNombre_video()+"\n Categoria:  "+video.getCategoria());
					tvDetailSubject.setText(video.getReview());
					
//		TextView tvDetailSubject = (TextView) findViewById(R.id.tvDetailSubject);
//		TextView tvDetailContent = (TextView) findViewById(R.id.tvDetailContent);
//		TextView tvDetailUsername = (TextView) findViewById(R.id.tvDetailUsername);
//		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
//	 
//		tvDetailSubject.setText(video.getNombre_video());
//		tvDetailContent.setText(video.getUrl());
//		tvDetailUsername.setText(video.getUsername());
//		tvDetailDate.setText(SimpleDateFormat.getInstance().format(
//				video.getFecha()));
		
		
		
		
		
		
	}
	//Clase anidada fetchstingtasktarea en background va a beeterapi, obtiene instancia i llama get sting pasando la url que hemos recperado del recurso
	//decalramos esas strin task , tipo de parametros del background 
	
	
	private class FetchStingTask extends AsyncTask<String, Void, Videos> {
		private ProgressDialog pd;
	 
		@Override
		protected Videos doInBackground(String... params) {
			Videos video = null;
			try {

				video = VideoshareAPI.getInstance(Videosreproductor.this).getVideo(params[0]);
				
			} catch (VideoshareAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return video;
		}
	 
		@Override
		protected void onPostExecute(Videos result) {
			loadVideos(result);
			
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(Videosreproductor.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
}
