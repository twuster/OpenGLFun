package com.example.openglfun;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.*;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private GLSurfaceView mGLView;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView  = new MyGLSurfaceView(this);
		
		setContentView(mGLView);
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	class MyGLSurfaceView extends GLSurfaceView {

	    public MyGLSurfaceView(Context context){
	        super(context);
	        setEGLContextClientVersion(2);

	        // Set the Renderer for drawing on the GLSurfaceView
	        setRenderer(new MyGL20Renderer());

	        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	        

	    }
	}
	
	// Call back when the activity is going into the background
	   @Override
	   protected void onPause() {
	      super.onPause();
	      mGLView.onPause();
	   }
	   
	   // Call back after onPause()
	   @Override
	   protected void onResume() {
	      super.onResume();
	      mGLView.onResume();
	   }
	

}

