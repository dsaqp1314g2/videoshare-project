package edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android;

import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideoshareAPI;
import edu.upc.eetac.dsa.dsaqp1314g2.videoshare.android.api.VideoshareAndroidException;
import android.app.Activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.view.View;
import android.widget.EditText;
 
public class WriteCommentActivity extends Activity {
/*	private final static String TAG = WriteStingActivity.class.getName();
 
	private class PostStingTask extends AsyncTask<String, Void, Video> {
		private ProgressDialog pd;
 
		@Override
		protected Video doInBackground(String... params) {
			Video sting = null;
			try {
				sting = VideoshareAPI.getInstance(WriteStingActivity.this).createcomment(params[0]);
			} catch (VideoshareAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sting;
		} */
 /*
		@Override
		protected void onPostExecute(Video result) {
			showStings();
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteStingActivity.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_comment_layout);
 
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postSting(View v) {
		EditText etSubject = (EditText) findViewById(R.id.etSubject);
	//	EditText etContent = (EditText) findViewById(R.id.etContent);
 
		String subject = etSubject.getText().toString();
	//	String content = etContent.getText().toString();
 
		(new PostStingTask()).execute(subject);
	}
 
	private void showStings() {
		Intent intent = new Intent(this, VideoshareMainActivity.class);
		startActivity(intent);
	}
  */
}