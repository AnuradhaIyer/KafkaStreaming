package edu.sjsu.seekers.seekerskafkaapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

public class ConsumerActivity extends AppCompatActivity {
    public static final String TAG = "ConsumerActivity";

    String url = "http://13.57.252.148:8080/consumer";
    RestTemplate restTemplate = new RestTemplate();
    private TextView tvConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        tvConsumer = findViewById(R.id.tvConsumer);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        Connection cn = new Connection();
        Thread t = new Thread(cn);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tvConsumer.setText(ProducerActivity.consumedMessages);
    }

    private class Connection implements Runnable {

//        @Override
//        protected Object doInBackground(Object... arg0) {
//            receive();
//            this.notify();
//            return null;
//        }

        @Override
        public void run() {
            receive();
        }
    }

    public void receive()
    {
        ResponseEntity<String> response =  restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if(response != null && !StringUtils.isEmpty(response.getBody()))
            ProducerActivity.consumedMessages = response.getBody();
        Log.i(TAG,"result: " + ProducerActivity.consumedMessages) ;
    }
}
