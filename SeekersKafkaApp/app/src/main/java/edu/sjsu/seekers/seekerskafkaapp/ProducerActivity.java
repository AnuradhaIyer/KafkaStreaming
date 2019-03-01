package edu.sjsu.seekers.seekerskafkaapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class ProducerActivity extends AppCompatActivity {

    public static final String TAG = "ProducerActivity";
    private Button btnSend, btnClear;
    private TextView txtMessage1, txtMessage2,txtMessage3;
    String url = "http://13.57.252.148:8080/producer";
    RestTemplate restTemplate = new RestTemplate();


    public static  String consumedMessages = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer);
        btnSend = findViewById(R.id.buttonSend);
        btnClear = findViewById(R.id.buttonClear);
        txtMessage1 = findViewById(R.id.etMessage1);
        txtMessage2 = findViewById(R.id.etMessage2);
        txtMessage3 = findViewById(R.id.etMessage3);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "********* NO permission********" );
            return;
        }
        else
            Log.i(TAG, "********* GOT permission********" );

    }

    private class Connection implements Runnable {

        @Override
        public void run() {
            send();
        }
    }

    public void send()
    {
        Log.i(TAG,"text 1 is: " + txtMessage1.getText().toString()) ;
        Log.i(TAG,"text 2 is: " + txtMessage2.getText().toString()) ;
        Log.i(TAG,"text 3 is: " + txtMessage3.getText().toString()) ;
        if(!txtMessage1.getText().toString().equals(""))
            postMessage(txtMessage1.getText().toString());
        if(!txtMessage2.getText().toString().equals(""))
            postMessage(txtMessage2.getText().toString());
        if(!txtMessage3.getText().toString().equals(""))
            postMessage(txtMessage3.getText().toString());
    }

    private void postMessage(String msg) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        String body = "{\"msg\": \"" + msg+ "\"}";
        HttpEntity<String> requestData = new HttpEntity<>(body,headers);
        ResponseEntity<String> response =  restTemplate.exchange(url, HttpMethod.POST, requestData, String.class);
        Log.i(TAG,"sent data") ;
    }

    public void performSendAction(View v) {
        Log.i(TAG, "inside performSendAction" );
        Connection cn = new Connection();
        Thread t1 = new Thread(cn);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getBaseContext(),"Messages Sent!", Toast.LENGTH_SHORT).show();
    }


    public void openConsumer(View v) {
        Intent intent = new Intent(ProducerActivity.this,ConsumerActivity.class);
        startActivity(intent);
    }

    public void cleanText(View v) {

        txtMessage1.setText("");
        txtMessage2.setText("");
        txtMessage3.setText("");
    }

}
