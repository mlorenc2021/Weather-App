package edu.niu.cs.z1904531.portfolio18;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder>{

    //add in - constructor, onCreateViewHolder, onBindViewHolder, getItemCount, MyViewHolder, asynctask

    private ArrayList<Weather> weatherData;
    private LayoutInflater inflater;
    private Context context;
    private Map<String, Bitmap> bitmaps;

    //constructor
    public WeatherAdapter(Context newContext, ArrayList<Weather> newWeatherData) {
        weatherData = newWeatherData;
        context = newContext;
        inflater = LayoutInflater.from(context);
        bitmaps = new HashMap<>();
    }//end constructor

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.city_view, parent, false);
        return new MyViewHolder(view);
    }//end onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //get the weather object at the current position
        Weather weather = weatherData.get(position);

        //populate the textviews with weather information
        holder.dayTV.setText(context.getString(R.string.day_description, weather.getDayOfWeek(), weather.getDescription()));
        holder.lowTV.setText(context.getString(R.string.low_temp, weather.getMinTemp()));
        holder.highTV.setText(context.getString(R.string.high_temp, weather.getMaxTemp()));
        holder.humidityTV.setText(context.getString(R.string.humidity, weather.getHumidity()));

        //populate the image view
        if(bitmaps.containsKey(weather.getIconURL())){
            holder.pictureIV.setImageBitmap(bitmaps.get(weather.getIconURL()));
        }
        else{
            LoadImageTask task = new LoadImageTask(holder.pictureIV);
            task.execute(weather.getIconURL());
        }
    }//end onBindViewHolder

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    //custom viewholder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView pictureIV;
        public TextView dayTV, lowTV, highTV, humidityTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureIV = itemView.findViewById(R.id.conditionImageView);
            dayTV = itemView.findViewById(R.id.dayTextView);
            lowTV = itemView.findViewById(R.id.lowTextView);
            highTV = itemView.findViewById(R.id.highTextView);
            humidityTV = itemView.findViewById(R.id.humidityTextView);
        }//end constructor


    }//end MyViewHolder

    //asynctask to download bitmaps
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public LoadImageTask(ImageView newImageView) {
            imageView = newImageView;
        }//end constructor

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try{
                //create a URL object
                URL url = new URL(strings[0]);

                //open the connection with the URL
                connection = (HttpURLConnection) url.openConnection();

                try{
                    //create an input stream to retrieve the bitmap
                    InputStream inputStream = connection.getInputStream();

                    //get hte bitmap
                    bitmap = BitmapFactory.decodeStream(inputStream);

                    //put the bitmap into Map
                    bitmaps.put(strings[0], bitmap);
                }catch (Exception exception){
                    exception.printStackTrace();
                }


            }catch (Exception e){
                e.printStackTrace();

            }finally {
                //disconnect the connection
                connection.disconnect();

            }

            return bitmap;
        }//end doInBackground

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //populate the imageview with the bitmap
            imageView.setImageBitmap(bitmap);
        }//end onPostExecute
    }//end loadimagetask












}//end WeatherAdapter
