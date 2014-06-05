package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoshareMainActivity extends Activity {
	VideoView videoView = null;
	private static final String Movie = "http://10.89.68.163/video/1.webm";
	
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
	 
}}
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

	
