package com.inforscience.web;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.util.*;

import com.inforscience.web.NanoHTTPD.Response.Status;


public class Httpd extends Activity
{
    private WebServer server;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        server = new WebServer();
        try {
            server.start();
        } catch(IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");
    }


    // DON'T FORGET to stop the server
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (server != null)
            server.stop();
        Log.w("server", "destroyed");
    }

    private class WebServer extends NanoHTTPD {

        public WebServer()
        {
            super(8080);
        }

        @Override
        public Response serve(String uri, Method method,
            Map<String, String> header, Map<String, String> parameters,
            Map<String, String> files) {
        String answer = "";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(Environment.getExternalStorageDirectory()
                    + "/profile_image.png");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new NanoHTTPD.Response(Status.OK, "image/*", fis);
      }
    }

}