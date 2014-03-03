package com.inforscience.web;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.util.*;

import com.inforscience.web.NanoHTTPD.Response.Status;

public class Httpd extends Activity {
	private WebServer server;
	private String msg1 = "/profile_image.png";
	private String msg2 = "SuperBeam/hangouts_message.ogg";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		server = new WebServer();
		try {
			server.start();
		} catch (IOException ioe) {
			Log.w("Httpd", "The server could not start.");
		}
		Log.w("Httpd", "Web server initialized.");
	}

	// DON'T FORGET to stop the server
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (server != null)
			server.stop();
		Log.w("server", "destroyed");
	}

	private class WebServer extends NanoHTTPD {

		private final Status HTTP_OK = Status.OK;
		private final Status HTTP_INTERNALERROR = Status.INTERNAL_ERROR;
		private final Status HTTP_NOTFOUND = Status.NOT_FOUND;

		public WebServer() {
			super(8080);
		}

		@Override
		public Response serve(String uri, Method method,
				Map<String, String> header, Map<String, String> parameters,
				Map<String, String> files) {
			System.out.println(uri);
			if (uri.equals("/")) {
				String str = "<html><head><title>Wifi Direct</title></head>"
						+ "<body>" + "<a href=\"/message1\">message1</a>"
						+ "<br/>" + "<a href =\"/message2\">message2</a>"
						+ "</body></html>";
				return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, str);
			} else if (uri.equals("/message1")) {
				FileInputStream f1 = null;
				try {
					f1 = new FileInputStream(
							Environment.getExternalStorageDirectory() + msg1);
					return new NanoHTTPD.Response(HTTP_OK, "image/*", f1);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (uri.equals("/message2")) {
				FileInputStream f2 = null;
				try {
					f2 = new FileInputStream(
							Environment.getExternalStorageDirectory() + msg1);
					return new NanoHTTPD.Response(HTTP_OK, "audio/*", f2);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	private class MyThread extends Thread {
		PipedOutputStream pos;

		public MyThread(PipedOutputStream s) {
			pos = s;
		}

		public void run() {
			try {
				pos.write("5\r\nhoge \r\n".getBytes());
				pos.flush();
				Thread.sleep(500);
				pos.write("4\r\nhoge\r\n0\r\n\r\n".getBytes());
				pos.close();
			} catch (IOException e) {
				return;
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}