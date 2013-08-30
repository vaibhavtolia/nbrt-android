package com.pearson.thenewbusinessroadtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pearson.thenewbusinessroadtest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class CameraActivity extends Activity {

	private QuestionsDataStore datasource;
	Context context;
	private String qid;
	private String idea;
	private String type;
	private static final int CAMERA_REQUEST_CODE = 100;
	private static final int VIDEO_REQUEST_CODE = 101;
	private static final int GALLERY_REQUEST_CODE = 102;
	private Bitmap mImageBitmap;
	private String JPEG_FILE_PREFIX = "yello_";
	private String JPEG_FILE_SUFFIX = "jpg";
	private String response_type;
	private String video_file_path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_activity);
		context = this;
		
		Intent intent = getIntent();
		qid = intent.getStringExtra("qid");
		type =  intent.getStringExtra("type");
		idea = intent.getStringExtra("idea");
		
		Thread th1 = new Thread(){
			public void run(){
				final String question = getQuestion(qid);
				final TextView text = (TextView) ((Activity) context).findViewById(R.id.question);
				runOnUiThread(new Runnable(){
					public void run(){
						text.setText(question);
					}
				});
			}
		};
		th1.start();
	}
	
	private String getQuestion(String qid){
		datasource = new QuestionsDataStore(this);
	    datasource.open();
	    String question_data = datasource.getQuestion(qid);
	    datasource.close();
	    return question_data;
	}
	
	public void openCamera(View view) throws IOException{
		response_type = "image";
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}
	
	public void openGallery(View view){
		response_type = "image";
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, GALLERY_REQUEST_CODE);
	}

	public void openVideoRecorder(View view){
		response_type = "video";
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    startActivityForResult(takeVideoIntent, VIDEO_REQUEST_CODE);
	}
	
	public void saveMedia(View view){
		if( response_type == "image" ){
			try {
				
				Thread save_image = new Thread(){
					public void run(){
						File imagesFolder = new File(Environment.getExternalStorageDirectory(), "nbrt/images/");
						if (!imagesFolder.exists()) {
							imagesFolder.mkdirs();
						}
				        
						String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
						String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
						String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/nbrt/images/"+imageFileName+".jpg";
						Log.v("image file path", imageFilePath);
						
			            FileOutputStream out = null;
						try {
							out = new FileOutputStream(imageFilePath);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
						
			            datasource.open();
			            if( new String("answer").equals(type)){
			            	datasource.insertAnswer(idea, qid, response_type, imageFileName+".jpg");
						}
						else{
							datasource.insertRisk(idea, qid, response_type, imageFileName+".jpg");
						}
			            datasource.close();
			            
			            runOnUiThread(new Runnable(){
			            	public void run(){
			            		Toast.makeText(context, "Image saved successfully", 2000).show();
			            		onBackPressed();
			            	}
			            });
			            
			            
					}
				};
				save_image.start();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            Toast.makeText(this, "There occured some error in storing file", 2000).show();
	        }
		}
		else if( response_type == "video" ){
			try {
				Thread save_video = new Thread(){
					public void run(){
				
						File imagesFolder = new File(Environment.getExternalStorageDirectory(), "nbrt/videos/");
						if (!imagesFolder.exists()) {
							imagesFolder.mkdirs();
						}
						
						String[] a = video_file_path.split("/");
						int len = a.length;
						String last = a[len-1];
						Log.v("ext",last);
						String[] exts = last.split("\\.");
						String ext = exts[exts.length-1];
				        
						String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
						String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
						String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/nbrt/videos/"+imageFileName+"."+ext;
						
		
						Log.v("video file path", video_file_path);
						Log.v("move to",imageFilePath);
						
						File afile = new File(video_file_path);
						File bfile = new File(imageFilePath);
						
						InputStream inStream = null;
						try {
							inStream = new FileInputStream(afile);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						OutputStream outStream = null;
						try {
							outStream = new FileOutputStream(bfile);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						byte[] buffer = new byte[1024];
						
						int length;
			    	    //copy the file content in bytes 
			    	    try {
							while ((length = inStream.read(buffer)) > 0){
 
								outStream.write(buffer, 0, length);
 
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 
			    	    try {
							inStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	    try {
							outStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 
			    	    //delete the original file
			    	    afile.delete();
			 
			    	    //System.out.println("File is copied successful!");
			    	    
			    	    datasource.open();
			    	    
			    	    if( new String("answer").equals(type)){
			            	datasource.insertAnswer(idea, qid, response_type, imageFileName+"."+ext );
						}
						else{
							datasource.insertRisk(idea, qid, response_type, imageFileName+"."+ext );
						}
			            datasource.close();
			            
			            runOnUiThread(new Runnable(){
			            	public void run(){
			            		Toast.makeText(context, "Video saved successfully", 2000).show();
			            		onBackPressed();
			            	}
			            });
						
					}
				};
				save_video.start();
	            
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            Toast.makeText(this, "There occured some error in storing file", 2000).show();
	        }
		}
		
	}
	
	public void cancelMedia(View view){
		final EditText ed = (EditText) this.findViewById(R.id.editText1);
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Confirm Not Save")
        .setMessage("Do not save this entry? ")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            	runOnUiThread(new Runnable(){
            		public void run(){
            			onBackPressed();
            		}
            	});

            }

        })
        .setNegativeButton("No", null)
        .show();
	}
	
	private String generateFileName(){
    	SecureRandom random = new SecureRandom();
    	return new BigInteger(130, random).toString(32);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		View sl = this.findViewById(R.id.sl);
		sl.setVisibility(View.VISIBLE);
		
		if( requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
			//Log.v("Camera Intent","");
			Bundle extras = data.getExtras();
		    mImageBitmap = (Bitmap) extras.get("data");
		    setImage(mImageBitmap);
		}
		
		if( requestCode == VIDEO_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
			Log.v("Video Intent","");
			Uri mVideoUri = data.getData();
			video_file_path = getPath(mVideoUri);
			Log.v("video file path",video_file_path);
			Bitmap bMap = ThumbnailUtils.createVideoThumbnail(video_file_path, MediaStore.Video.Thumbnails.MICRO_KIND);
			ImageView mImageView = (ImageView) this.findViewById(R.id.imageView3);
			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mImageView.setImageBitmap(bMap);
		}
		
		if( requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
			Uri selectedImage = data.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            
            //mImageBitmap = BitmapFactory.decodeFile(filePath);
            
            scaleBitmap(filePath);

		}
		
	}
	
	private void setImage(Bitmap bitmap){
		ImageView mImageView = (ImageView) this.findViewById(R.id.imageView3);
	    mImageView.setImageBitmap(bitmap);
	    mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	}
	
	@SuppressWarnings("deprecation")
	public String getPath(Uri contentUri) {
	     String[] proj = { MediaStore.Audio.Media.DATA };
	     Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	     int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
	     cursor.moveToFirst();
	     return cursor.getString(column_index);
	}
	
	private void scaleBitmap(String path){
		final ImageView mImageView = (ImageView) this.findViewById(R.id.imageView3);
		final String filepath = path;
		Thread scale_bitmap = new Thread(){
			public void run(){
		        int targetW = mImageView.getWidth();
		        int targetH = mImageView.getHeight();
		        
		        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		        bmOptions.inJustDecodeBounds = true;
		        BitmapFactory.decodeFile(filepath, bmOptions);
		        int photoW = bmOptions.outWidth;
		        int photoH = bmOptions.outHeight;
		        
		        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		        bmOptions.inJustDecodeBounds = false;
		        bmOptions.inSampleSize = scaleFactor;
		        bmOptions.inPurgeable = true;
		        
		        final Bitmap bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
		        
		        mImageBitmap = bitmap;
		        
		        runOnUiThread(new Runnable(){
		        	public void run(){
		        		setImage(bitmap);
		        	}
		        });
			}
		};
		scale_bitmap.start();
	}
	
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
}
