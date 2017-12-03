package com.example.nauman.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nauman.models.CategoryModel;
import com.example.nauman.models.MovieModel;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{


    private TextView tvData;
    private ListView lvMovies;

    private ProgressDialog progressdialog;
    public ListView lvCategory ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressdialog = new ProgressDialog(this);
        progressdialog.setIndeterminate(true);
        progressdialog.setMessage("Loading, Please wait...");


       // Button btnHit = (Button) findViewById(R.id.btnhit);
      //  tvData = (TextView) findViewById(R.id.textView);

        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start
         lvMovies =  (ListView) findViewById(R.id.lvMovies);
         lvCategory =  (ListView) findViewById(R.id.lvCategory);
        //lvCategory =  (ListView) findViewById(R.id.lvCategory);
        /*btnHit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               // new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
               // new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");
                new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
            }
        });*/

        new JSONTask_Cat().execute("http://www.freemcqs.com/WebServiceFreemcqs.asmx/LoadCategory");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
           // new JSONTask().execute("http://www.freemcqs.com/WebServiceFreemcqs.asmx/HelloWorld");
         //   return true;
        }
        else  if (id == R.id.action_loadCat) {
            new JSONTask_Cat().execute("http://www.freemcqs.com/WebServiceFreemcqs.asmx/LoadCategory");
            // new JSONTask().execute("http://www.freemcqs.com/WebServiceFreemcqs.asmx/HelloWorld");
           // return true;
        }
        //action_loadCat

        return super.onOptionsItemSelected(item);
    }

    public  class JSONTask extends AsyncTask<String,String,List<MovieModel>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
               // StringBuffer finalBufferData = new StringBuffer();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");
                List<MovieModel> movieModelList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i=0; i<parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    MovieModel movieModel = gson.fromJson(finalObject.toString(),MovieModel.class);
                    /*MovieModel movieModel = new MovieModel();
                    movieModel.setMovie(finalObject.getString("movie"));
                    movieModel.setYear(Integer.parseInt(finalObject.getString("year")));
                    movieModel.setRating(Float.parseFloat(finalObject.getString("rating")));
                    movieModel.setDuration(finalObject.getString("duration"));
                    movieModel.setDirector(finalObject.getString("director"));
                    movieModel.setTagline(finalObject.getString("tagline"));
                    movieModel.setImage(finalObject.getString("image"));
                    movieModel.setStory(finalObject.getString("story"));
                    String movieName = finalObject.getString("movie");
                    int year = finalObject.getInt("year");
                    List<MovieModel.Cast> castList = new ArrayList<>();
                    for(int j=0; j<finalObject.getJSONArray("cast").length(); j++){
                        //JSONObject castObject = finalObject.getJSONArray("cast").getJSONObject(j);
                        MovieModel.Cast cast = new MovieModel.Cast();
                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
                        castList.add(cast);

                    }
                    movieModel.setCastList(castList);*/
                    movieModelList.add(movieModel);
                   // finalBufferData.append(movieName+"  -  "+year+"\n");
                }

                return  movieModelList;
              //  return buffer.toString();

            } catch (MalformedURLException e) {
                progressdialog.dismiss();
                e.printStackTrace();
            } catch (IOException e) {
                progressdialog.dismiss();
                e.printStackTrace();
            } catch (JSONException e) {
                progressdialog.dismiss();
                e.printStackTrace();
            } finally {
                progressdialog.dismiss();
                if(connection != null){
                    connection.disconnect();
                }

                try {
                    if(reader != null){
                        reader.close();
                    }

                } catch (IOException e) {
                    progressdialog.dismiss();
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieModel> result) {
            super.onPostExecute(result);
progressdialog.dismiss();
            // Need to set data to the list
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(),R.layout.row,result);
            lvMovies.setAdapter(adapter);
          //  tvData.setText(result);
        }


    }
    public class MovieAdapter extends ArrayAdapter{


        private List<MovieModel> movieModelList;
        private int resource;
        private LayoutInflater inflater;
        public MovieAdapter(Context context, int resource, List<MovieModel> objects){
            super(context,resource,objects);
            movieModelList = objects;
            this.resource = resource;
            inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row,null);
                holder.ivMovieIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
                holder.tvMovie = (TextView) convertView.findViewById(R.id.tvMovie);
                holder.tvTagline = (TextView) convertView.findViewById(R.id.tvTagLine);
                holder.tvYear = (TextView) convertView.findViewById(R.id.tvYear);
                holder.tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
                holder.tvDirector = (TextView) convertView.findViewById(R.id.tvDirector);
                holder.tvCast = (TextView) convertView.findViewById(R.id.tvCast);
                holder.tvStory = (TextView) convertView.findViewById(R.id.tvStory);
                holder.rbMovieRating = (RatingBar) convertView.findViewById(R.id.rbMovie);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }



            final ProgressBar progressBar =(ProgressBar) convertView.findViewById(R.id.progressBar);
            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(movieModelList.get(position).getImage(), holder.ivMovieIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            }); // Default options will be used
            holder.tvMovie.setText(movieModelList.get(position).getMovie());
            holder.tvTagline.setText(movieModelList.get(position).getTagline());
            holder.tvYear.setText("Year: "+movieModelList.get(position).getYear());
            holder.tvDuration.setText(movieModelList.get(position).getDuration());
            holder.tvDirector.setText(movieModelList.get(position).getDirector());
            holder.tvStory.setText(movieModelList.get(position).getStory());
            holder.rbMovieRating.setRating(movieModelList.get(position).getRating()/2);
            StringBuffer stringBuffer = new StringBuffer();
            for(MovieModel.Cast cast : movieModelList.get(position).getCastList()){
                stringBuffer.append(cast.getName()+" ");
            }
            holder.tvCast.setText(stringBuffer);
            return convertView;
            // return super.getView(position, convertView, parent);
        }
        class ViewHolder{
            private ImageView ivMovieIcon;
            private TextView tvMovie;
            private TextView tvTagline;
            private TextView tvYear;
            private TextView tvDuration;
            private TextView tvDirector;
            private TextView tvCast;
            private TextView tvStory;
            private RatingBar rbMovieRating;
        }
    }

    public  class JSONTask_Cat extends AsyncTask<String,String,List<CategoryModel>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected List<CategoryModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                // StringBuffer finalBufferData = new StringBuffer();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("category");
                List<CategoryModel> catModelList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i=0; i<parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    CategoryModel categoryModel = gson.fromJson(finalObject.toString(),CategoryModel.class);
                    catModelList.add(categoryModel);
                    // finalBufferData.append(movieName+"  -  "+year+"\n");
                }

                return  catModelList;
                //  return buffer.toString();

            } catch (MalformedURLException e) {
                progressdialog.dismiss();
                e.printStackTrace();
            } catch (IOException e) {
                progressdialog.dismiss();
                e.printStackTrace();
            } catch (JSONException e) {
                progressdialog.dismiss();
                e.printStackTrace();
            } finally {
                progressdialog.dismiss();
                if(connection != null){
                    connection.disconnect();
                }

                try {
                    if(reader != null){
                        reader.close();
                    }

                } catch (IOException e) {
                    progressdialog.dismiss();
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<CategoryModel> result) {
            super.onPostExecute(result);
            progressdialog.dismiss();
            // Need to set data to the list
            CatAdapter adapter = new CatAdapter(getApplicationContext(),R.layout.category_list,result);
            lvCategory.setAdapter(adapter);
            //  tvData.setText(result);
        }


    }
    public class CatAdapter extends ArrayAdapter{


        private List<CategoryModel> categoryModelList;
        private int resource;
        private LayoutInflater inflater;
        public CatAdapter(Context context, int resource, List<CategoryModel> objects){
            super(context,resource,objects);
            categoryModelList = objects;
            this.resource = resource;
            inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.category_list,null);
                holder.ivMovieIcon = (ImageView)convertView.findViewById(R.id.ivCatIcon);
                holder.tvCatName = (TextView) convertView.findViewById(R.id.tvCatName);
                holder.tvTotalTest= (TextView) convertView.findViewById(R.id.tvTotalTest);
                holder.tvTotalSubject= (TextView) convertView.findViewById(R.id.tvTotalSubject);
                holder.tvTagline = (TextView) convertView.findViewById(R.id.tvTagLine);
                holder.tvTotalMcqs = (TextView) convertView.findViewById(R.id.tvTotalMcqs);
                holder.tvCatPurpose = (TextView) convertView.findViewById(R.id.tvCatPurpose);
                holder.tvCatDesc = (TextView) convertView.findViewById(R.id.tvCatDesc);
                holder.btnCatid = (Button) convertView.findViewById(R.id.btnCatid);
               /*

                */
               /*


                holder.rbMovieRating = (RatingBar) convertView.findViewById(R.id.rbMovie);*/
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }



           final ProgressBar progressBar =(ProgressBar) convertView.findViewById(R.id.progressBar);
            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(categoryModelList.get(position).getCatImg(), holder.ivMovieIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            }); // Default options will be used
            holder.tvCatName.setText(categoryModelList.get(position).getCatName());
            holder.tvTotalTest.setText("Total Test(s): "+categoryModelList.get(position).getTotalTest());
            holder.tvTotalSubject.setText("Total Subject(s): "+categoryModelList.get(position).getTotalSub());
            holder.tvTagline.setText(""+categoryModelList.get(position).getTagline());
            holder.tvTotalMcqs.setText("Total Mcq(s): "+categoryModelList.get(position).getTotalMcqs());
            holder.tvCatPurpose.setText(""+categoryModelList.get(position).getCatPurpose());
            holder.tvCatDesc.setText(""+categoryModelList.get(position).getCatDesc());
            holder.btnCatid.setId(categoryModelList.get(position).getCatid());

            holder.btnCatid.setOnClickListener(btnclick);
           // holder.rbMovieRating.setRating(movieModelList.get(position).getRating()/2);
            /*StringBuffer stringBuffer = new StringBuffer();
            for(MovieModel.Cast cast : movieModelList.get(position).getCastList()){
                stringBuffer.append(cast.getName()+" ");
            }
            holder.tvCast.setText(stringBuffer);*/
            return convertView;
            // return super.getView(position, convertView, parent);
        }

        View.OnClickListener btnclick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CatSubject.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("catid", String.valueOf(view.getId()));
                MainActivity.this.startActivity(i);
               /* switch(view.getId()){
                    case 1:
                        //first button click
                        break;
                    //Second button click
                    case 2:
                        break;
                    case 3:
                        //third button click
                        break;
                    case 4:
                        //fourth button click
                        break;

                    default:
                        break;
                }*/

            }
        };

        class ViewHolder{
            private ImageView ivMovieIcon;
            private TextView tvCatName;
            private TextView tvTotalMcqs;
            private TextView tvTotalTest;
            private TextView tvTotalSubject;
            private TextView tvTagline;
            private TextView tvCatPurpose;
            private TextView tvCatDesc;
            private Button btnCatid;
            //private TextView getTvTagline;

            /*

            private TextView tvStory;
            private RatingBar rbCat;*/
        }
    }
}




