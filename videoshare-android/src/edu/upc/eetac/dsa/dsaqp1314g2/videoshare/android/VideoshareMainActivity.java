package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android;


import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.Videos;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideosCollection;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideoshareAPI;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideoshareAndroidException;






public class VideoshareMainActivity extends ListActivity {
	
	
	private class FetchStingsTask extends AsyncTask<Void, Void, VideosCollection> {

		private ProgressDialog pd;

		@Override
		protected VideosCollection doInBackground(Void... params) {
			VideosCollection videos = null;
			try {
				videos = VideoshareAPI.getInstance(VideoshareMainActivity.this)
						.getCollectionVideos();
			} catch (VideoshareAndroidException e) {
				e.printStackTrace();
			}
			return videos;
		}
		
		

		@Override
		protected void onPostExecute(VideosCollection result) {
			addVideo(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(VideoshareMainActivity.this);
			pd.setTitle("Buscando el servidor...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
		private void addVideo(VideosCollection videos){
			videoList.addAll(videos.getVideos());
			adapter.notifyDataSetChanged();
		}
		private ArrayList<Videos> videoList;
		private VideosAdapter adapter;
	
		private final static String TAG = VideoshareMainActivity.class.toString();
		
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.videoshare_layout);
		/*	SharedPreferences prefs = getSharedPreferences("videoshare-profile",
					Context.MODE_PRIVATE);
		/*	final String username = prefs.getString("username", null);
			final String password = prefs.getString("password", null);
		 
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password
							.toCharArray());
				}
			});
			Log.d(TAG, "authenticated with " + username + ":" + password);
		 */
			videoList = new ArrayList<Videos>();
			adapter = new VideosAdapter(this, videoList);
			setListAdapter(adapter);
			
			(new FetchStingsTask()).execute();
		}
		
		protected void onListItemClick(ListView l, View v, int position, long videoid) {
			Videos video = videoList.get(position);
			Log.d(TAG, video.getLinks().get("self").getTarget());

			Intent intent = new Intent(this, Videosreproductor.class);
			intent.putExtra("url", video.getLinks().get("self").getTarget());
			
			startActivity(intent);
		}
		
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.videoshare_actions, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.miWrite:
			//	Intent intent = new Intent(this, WriteStingActivity.class);
			//	startActivity(intent);
				videoList = new ArrayList<Videos>();
				adapter = new VideosAdapter(this, videoList);
				setListAdapter(adapter);
				
				(new FetchStingsTask2()).execute();
				
				return true;
			case R.id.miWrite2:
				videoList = new ArrayList<Videos>();
				adapter = new VideosAdapter(this, videoList);
				setListAdapter(adapter);
				
				(new FetchStingsTask3()).execute();
				
				return true;
			case R.id.miWrite3:
				
				videoList = new ArrayList<Videos>();
				adapter = new VideosAdapter(this, videoList);
				setListAdapter(adapter);
				
				(new FetchStingsTask4()).execute();
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}

		} 

		private class FetchStingsTask2 extends AsyncTask<Void, Void, VideosCollection> {

			private ProgressDialog pd;

			@Override
			protected VideosCollection doInBackground(Void... params) {
				VideosCollection videos = null;
				try {
					videos = VideoshareAPI.getInstance(VideoshareMainActivity.this)
							.getCollectionVideosByCategoria();
				} catch (VideoshareAndroidException e) {
					e.printStackTrace();
				}
				return videos;
			}
			
			

			@Override
			protected void onPostExecute(VideosCollection result) {
				addVideo(result);
				if (pd != null) {
					pd.dismiss();
				}
			}

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(VideoshareMainActivity.this);
				pd.setTitle("Buscando el servidor...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

		}		
		
		private class FetchStingsTask3 extends AsyncTask<Void, Void, VideosCollection> {

			private ProgressDialog pd;

			@Override
			protected VideosCollection doInBackground(Void... params) {
				VideosCollection videos = null;
				try {
					videos = VideoshareAPI.getInstance(VideoshareMainActivity.this)
							.getCollectionVideosByPuntuacion();
				} catch (VideoshareAndroidException e) {
					e.printStackTrace();
				}
				return videos;
			}
			
			

			@Override
			protected void onPostExecute(VideosCollection result) {
				addVideo(result);
				if (pd != null) {
					pd.dismiss();
				}
			}

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(VideoshareMainActivity.this);
				pd.setTitle("Buscando el servidor...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

		}		
		private class FetchStingsTask4 extends AsyncTask<Void, Void, VideosCollection> {

			private ProgressDialog pd;

			@Override
			protected VideosCollection doInBackground(Void... params) {
				VideosCollection videos = null;
				try {
					videos = VideoshareAPI.getInstance(VideoshareMainActivity.this)
							.getCollectionVideosByUsers();
				} catch (VideoshareAndroidException e) {
					e.printStackTrace();
				}
				return videos;
			}
			
			

			@Override
			protected void onPostExecute(VideosCollection result) {
				addVideo(result);
				if (pd != null) {
					pd.dismiss();
				}
			}

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(VideoshareMainActivity.this);
				pd.setTitle("Buscando el servidor...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}

		}		
		
/*	
_____________________________________________________________________________	
	VideoView videoView = null;
	private static final String Movie = "http://193.145.53.249/video/1.webm";
	
	 MediaPlayer mediaPlayer;
	 Button buttonPlayPause, buttonQuit;
	 TextView textState;
	 @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.main);
	       VideoView vid = (VideoView) findViewById(R.id.videoViewer);
			//Use a media controller so that you can scroll the video contents
			//and also to pause, start the video.
			//vid.setVideoPath("http://10.89.62.167/video/video.webm");
			Uri video = Uri.parse(Movie);
			vid.setMediaController(new MediaController(this));
			vid.setVideoURI(video);
			vid.requestFocus();
			vid.start();
			}
			__________________________________________________ */
	
	
	
	
	 

	/* 
	 private int stateMediaPlayer;
	 private final int stateMP_Error = 0;
	 private final int stateMP_NotStarter = 1;
	 private final int stateMP_Playing = 2;
	 private final int stateMP_Pausing = 3;
	 */
	   /** Called when the activity is first created. */
/*	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.main);
	      
	       buttonPlayPause = (Button)findViewById(R.id.playpause);
	       buttonQuit = (Button)findViewById(R.id.quit);
	       textState = (TextView)findViewById(R.id.state);
	      
	       buttonPlayPause.setOnClickListener(buttonPlayPauseOnClickListener);
	       buttonQuit.setOnClickListener(buttonQuitOnClickListener);
	      
	       initMediaPlayer();
	      
	   }
	  
	   private void initMediaPlayer()
	   {
	    String PATH_TO_FILE = "http://10.89.62.167/video/video.webm";    
	    mediaPlayer = new  MediaPlayer();
	    
	    try {
	   mediaPlayer.setDataSource(PATH_TO_FILE);
	   mediaPlayer.prepare();
	   Toast.makeText(this, PATH_TO_FILE, Toast.LENGTH_LONG).show();
	   stateMediaPlayer = stateMP_NotStarter;
	         textState.setText("- IDLE -");
	  } catch (IllegalArgumentException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	   Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	   stateMediaPlayer = stateMP_Error;
	         textState.setText("- ERROR!!! -");
	  } catch (IllegalStateException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	   Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	   stateMediaPlayer = stateMP_Error;
	         textState.setText("- ERROR!!! -");
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	   Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	   stateMediaPlayer = stateMP_Error;
	         textState.setText("- ERROR!!! -");
	  }
	   }
	  
	   Button.OnClickListener buttonPlayPauseOnClickListener
	    = new Button.OnClickListener(){

	   @Override
	   public void onClick(View v) {
	    // TODO Auto-generated method stub
	    switch(stateMediaPlayer){
	    case stateMP_Error:
	     break;
	    case stateMP_NotStarter:
	     mediaPlayer.start();
	     buttonPlayPause.setText("Pause");
	     textState.setText("- PLAYING -");
	     stateMediaPlayer = stateMP_Playing;
	     break;
	    case stateMP_Playing:
	     mediaPlayer.pause();
	     buttonPlayPause.setText("Play");
	     textState.setText("- PAUSING -");
	     stateMediaPlayer = stateMP_Pausing;
	     break;
	    case stateMP_Pausing:
	     mediaPlayer.start();
	     buttonPlayPause.setText("Pause");
	     textState.setText("- PLAYING -");
	     stateMediaPlayer = stateMP_Playing;
	     break;
	    }
	    
	   }
	   };
	  
	   Button.OnClickListener buttonQuitOnClickListener
	 = new Button.OnClickListener(){

	  @Override
	  public void onClick(View v) {
	   // TODO Auto-generated method stub
	   mediaPlayer.stop();
	   mediaPlayer.release();
	   finish();
	  } 
	   };
	}
	*/
	
/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		 super.onCreate(savedInstanceState);
         
         MediaPlayer  myMediaPlayer=new MediaPlayer();
         
         try
         {
             myMediaPlayer.setDataSource("http://10.89.62.167/video/video.webm");
             myMediaPlayer.prepare();
             myMediaPlayer.start();
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
		/*
		Log.d("AGASDG", "agfsddsg");
		VideoView vid = (VideoView) findViewById(R.id.videoViewer);
		//Use a media controller so that you can scroll the video contents
		//and also to pause, start the video.
		//vid.setVideoPath("http://10.89.62.167/video/video.webm");
		Uri video = Uri.parse(Movie);
		vid.setMediaController(new MediaController(this));
		vid.setVideoURI(video);
		vid.requestFocus();
		vid.start();
		*/
		
		
		
		/*
		MediaController mediaController = new MediaController(this); 
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(Uri.parse("http://10.89.62.167/video/video.webm"));
		videoView.start();
	*/
	// Clean up and release resources
	}
	
