package com.pearson.thenewbusinessroadtest;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecord {
	
	private static final String LOG_TAG = "AudioRecordTest";
    public String mFileName = null;
    public String filename;

    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;

    public AudioRecord() {
    	File imagesFolder = new File(Environment.getExternalStorageDirectory(), "nbrt/audio/");
		if (!imagesFolder.exists()) {
			imagesFolder.mkdirs();
		}
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        filename = this.generateFileName();
        mFileName += "/nbrt/audio/"+filename+".3gp";
        Log.v(LOG_TAG,mFileName);
    }
    
    private String generateFileName(){
    	SecureRandom random = new SecureRandom();
    	return new BigInteger(130, random).toString(32);
    }
    
    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
