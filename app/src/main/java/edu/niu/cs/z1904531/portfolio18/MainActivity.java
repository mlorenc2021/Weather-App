package edu.niu.cs.z1904531.portfolio18;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Weather> weatherList;
    private WeatherAdapter weatherAdapter;
    private RecyclerView weatherRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherList = new ArrayList<>();

        weatherAdapter = new WeatherAdapter(this, weatherList);

        weatherRV = findViewById(R.id.weatherRecyclerView);
        weatherRV.setLayoutManager(new LinearLayoutManager(this));
        weatherRV.setAdapter(weatherAdapter);
    }//end onCreate

    //mehtod to create a URL ojbect from a city name
    private URL createURL(String city){
        String apiKey = getString(R.string.api_key),
                baseURL = getString(R.string.web_url);

        try{
            String urlString = baseURL + URLEncoder.encode(city, "UTF-8")
                    + "&units=imperial&cnt=16&APPID=" + apiKey;
            return new URL(urlString);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }//end createURL

    //custom asynctask
    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject>{
        static final String LOG_TAG = "Main";

        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection connection = null;

            try{
                Log.d(LOG_TAG, "Url is " + urls[0]);
                connection = (HttpURLConnection) urls[0].openConnection();

                int response = connection.getResponseCode();
                if(response == HttpURLConnection.HTTP_OK){
                    StringBuilder builder = new StringBuilder();
                    try{
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        line = reader.readLine();
                        while(line != null){
                            builder.append(line);
                            line = reader.readLine();
                        }
                    }catch (IOException ioException){
                        Log.d(LOG_TAG, getString(R.string.read_error));
                        ioException.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                }else{
                    Log.d(LOG_TAG, getString(R.string.connect_error));

                }
            }catch (Exception exception){
                Log.d(LOG_TAG, getString(R.string.connect_error));
                exception.printStackTrace();

            }finally {
                connection.disconnect();
            }
            return null;
        }//end doInBackground

        private void convertJSONtoArrayList(JSONObject forecast){
            weatherList.clear();
            try{
                JSONArray list = forecast.getJSONArray("list");
                for(int sub = 0; sub < list.length(); sub++){
                    JSONObject day = list.getJSONObject(sub);

                    JSONObject temperatures = day.getJSONObject("temp");

                    JSONObject weather = day.getJSONArray("weather").getJSONObject(0);

                    Weather newWeather = new Weather(day.getLong("dt"),
                                                    temperatures.getDouble("min"),
                                                    temperatures.getDouble("max"),
                                                    day.getDouble("humidity"),
                                                    weather.getString("description"),
                                                    weather.getString("icon"));

                    weatherList.add(newWeather);

                }
            }catch (JSONException jsonException){
                jsonException.printStackTrace();

            }//catch
        }//end convertJSONtoArrayList

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            convertJSONtoArrayList(jsonObject);
            weatherAdapter.notifyDataSetChanged();
            weatherRV.smoothScrollToPosition(0);
        }//end onPostExecute
    }//end WeatherTask

    //buton click method
    public void getWeather(View view){
        EditText cityET = findViewById(R.id.cityEditText);

        URL url = createURL(cityET.getText().toString());

        if(url != null){
            dismissKeyboard(cityET);
            GetWeatherTask weatherTask = new GetWeatherTask();
            weatherTask.execute(url);
        }
        else{
            Toast.makeText(MainActivity.this, R.string.invalid_url, Toast.LENGTH_LONG).show();
        }
    }//end getWeather

    private void dismissKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}//end MainActivity