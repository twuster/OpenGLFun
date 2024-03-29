package com.example.openglfun;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.*;

public class MyGL20Renderer implements GLSurfaceView.Renderer {

	private final String vertexShaderCode =
			"attribute vec4 vPosition;" +
					"void main() {" +
					"  gl_Position = vPosition;" +
					"}";

	private final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";

	Triangle mTriangle;
	Square mSquare;
	int mProgram;
	int mPositionHandle;
	int vertexStride;
	int mColorHandle;
	int vertexCount;

	public static int loadShader(int type, String shaderCode){

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}


	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl,
			javax.microedition.khronos.egl.EGLConfig config) {
		GLES20.glClearColor(0.f, 0f, 0f, 1.0f);
		mTriangle = new Triangle();
		//mTriangle.draw();

	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		mTriangle.draw();
	}


	class Triangle {

		private FloatBuffer vertexBuffer;

		// number of coordinates per vertex in this array
		static final int COORDS_PER_VERTEX = 3;
		float triangleCoords[] = { // in counterclockwise order:
				0.0f,  0.622008459f, 0.0f,   // top
				-0.5f, -0.311004243f, 0.0f,   // bottom left
				0.5f, -0.311004243f, 0.0f    // bottom right
		};

		// Set color with red, green, blue and alpha (opacity) values
		float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

		public Triangle() {
			// initialize vertex byte buffer for shape coordinates
			ByteBuffer bb = ByteBuffer.allocateDirect(
					// (number of coordinate values * 4 bytes per float)
					triangleCoords.length * 4);
			// use the device hardware's native byte order
			bb.order(ByteOrder.nativeOrder());

			// create a floating point buffer from the ByteBuffer
			vertexBuffer = bb.asFloatBuffer();
			// add the coordinates to the FloatBuffer
			vertexBuffer.put(triangleCoords);
			// set the buffer to read the first coordinate
			vertexBuffer.position(0);

			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);  
		}

		public void draw() {
			// Add program to OpenGL ES environment
			GLES20.glUseProgram(mProgram);

			// get handle to vertex shader's vPosition member
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

			// Enable a handle to the triangle vertices
			GLES20.glEnableVertexAttribArray(mPositionHandle);

			// Prepare the triangle coordinate data
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
					GLES20.GL_FLOAT, false,
					vertexStride, vertexBuffer);

			// get handle to fragment shader's vColor member
			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

			// Set color for drawing the triangle
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);

			// Draw the triangle
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

			// Disable vertex array
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}

	}
	class Square {

		private FloatBuffer vertexBuffer;
		private ShortBuffer drawListBuffer;

		// number of coordinates per vertex in this array
		static final int COORDS_PER_VERTEX = 3;
		float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
				-0.5f, -0.5f, 0.0f,   // bottom left
				0.5f, -0.5f, 0.0f,   // bottom right
				0.5f,  0.5f, 0.0f }; // top right

		private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

		public Square() {
			// initialize vertex byte buffer for shape coordinates
			ByteBuffer bb = ByteBuffer.allocateDirect(
					// (# of coordinate values * 4 bytes per float)
					squareCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(squareCoords);
			vertexBuffer.position(0);

			// initialize byte buffer for the draw list
			ByteBuffer dlb = ByteBuffer.allocateDirect(
					// (# of coordinate values * 2 bytes per short)
					drawOrder.length * 2);
			dlb.order(ByteOrder.nativeOrder());
			drawListBuffer = dlb.asShortBuffer();
			drawListBuffer.put(drawOrder);
			drawListBuffer.position(0);
		}
	}
}
