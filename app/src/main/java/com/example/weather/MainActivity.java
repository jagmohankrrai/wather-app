package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView info;
    public  class downloadtask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result="" ;
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url= new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data= reader.read();
                while(data !=-1)
                {
                    char res= (char) data;
                    result+=res;
                    data=reader.read();
                }
                return (result);

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String tru="404";
                String MSG="";
                String cod=jsonObject.getString("cod");
                if(cod.equals(tru)) {
                    MSG="couldnt find weather";
                }else {
                    String wather = jsonObject.getString("weather");
                    JSONArray arr = new JSONArray(wather);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonArray = arr.getJSONObject(i);
                        String main = jsonArray.getString("main");
                        String desc = jsonArray.getString("description");
                        MSG = "Rain :" + main + "\n" + "mist :" + desc;
                    }
                }
                info.setText(MSG);

            }catch(Exception e){

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt1=findViewById(R.id.button);
        EditText text =findViewById(R.id.editTextTextPersonName);
        Log.i("city", String.valueOf(text));
        info=findViewById(R.id.textView5);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    downloadtask task = new downloadtask();
                    task.execute("https://api.openweathermap.org/data/2.5/weather?q="+ text.getText().toString()+"&appid=26446b529d984942633d1f0f73deb84f");
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "couldnt find weather", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}