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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.nauman.models.SubjectModel;
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

public class CatSubject extends AppCompatActivity {

    private ProgressDialog progressdialog;
    public ListView lvSubject ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressdialog = new ProgressDialog(this);
        progressdialog.setIndeterminate(true);
        progressdialog.setMessage("Loading, Please wait...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        lvSubject =  (ListView) findViewById(R.id.lvSubject);

        Intent intent = getIntent();
        String catid = intent.getExtras().getString("catid");
        //String catid="";
        new JSONTask_CatSub().execute("http://www.freemcqs.com/WebServiceFreemcqs.asmx/getSubjectList?Catid="+catid);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public  class JSONTask_CatSub extends AsyncTask<String,String,List<SubjectModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected List<SubjectModel> doInBackground(String... params) {
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
                JSONArray parentArray = parentObject.getJSONArray("subject");
                List<SubjectModel> catModelList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    SubjectModel categoryModel = gson.fromJson(finalObject.toString(),SubjectModel.class);
                    catModelList.add(categoryModel);
                }

                return  catModelList;
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
        protected void onPostExecute(List<SubjectModel> result) {
            super.onPostExecute(result);
            progressdialog.dismiss();
            // Need to set data to the list
            CatSubject.CatSubAdapter adapter = new CatSubject.CatSubAdapter(getApplicationContext(),R.layout.cat_subject_list,result);
            lvSubject.setAdapter(adapter);
            //  tvData.setText(result);
        }


    }
    public class CatSubAdapter extends ArrayAdapter {


        private List<SubjectModel> categoryModelList;
        private int resource;
        private LayoutInflater inflater;
        public CatSubAdapter(Context context, int resource, List<SubjectModel> objects){
            super(context,resource,objects);
            categoryModelList = objects;
            this.resource = resource;
            inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CatSubject.CatSubAdapter.ViewHolder holder = null;
            if(convertView == null){
                holder = new CatSubject.CatSubAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.cat_subject_list,null);
                //holder.ivMovieIcon = (ImageView)convertView.findViewById(R.id.ivCatIcon);
                holder.tvCatName = (TextView) convertView.findViewById(R.id.tvSubName);
                holder.tvTotalMcqs = (TextView) convertView.findViewById(R.id.tvTotalMcqs);
                holder.tvTotalTest= (TextView) convertView.findViewById(R.id.tvTotalTest);
                holder.catid= (TextView) convertView.findViewById(R.id.CatId);
                holder.btnSubid = (Button) convertView.findViewById(R.id.btnSubid);
                holder.tvSubPurpose = (TextView) convertView.findViewById(R.id.tvSubPurpose);
                holder.tvSubDesc = (TextView) convertView.findViewById(R.id.tvSubDesc);
                /*

                holder.tvTagline = (TextView) convertView.findViewById(R.id.tvTagLine);



                */

               // holder.rbMovieRating = (RatingBar) convertView.findViewById(R.id.rbMovie);
                convertView.setTag(holder);
            } else{
                holder = (CatSubject.CatSubAdapter.ViewHolder) convertView.getTag();
            }



            final ProgressBar progressBar =(ProgressBar) convertView.findViewById(R.id.progressBar);
            // Then later, when you want to display image
            /*ImageLoader.getInstance().displayImage(categoryModelList.get(position).getCatImg(), holder.ivMovieIcon, new ImageLoadingListener() {
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
            });*/ // Default options will be used
            holder.tvCatName.setText(categoryModelList.get(position).getSubjectName());
            holder.tvTotalTest.setText("Total Test(s): "+categoryModelList.get(position).getTotalTest());
          //  holder.tvTotalSubject.setText("Total Subject(s): "+categoryModelList.get(position).getTotalSub());
            //holder.tvTagline.setText(""+categoryModelList.get(position).getTagline());
            holder.tvTotalMcqs.setText("Total Mcq(s): "+categoryModelList.get(position).getTotalMcqs());
            holder.tvSubPurpose.setText(""+categoryModelList.get(position).getSubjectObjective());
            holder.tvSubDesc.setText(""+categoryModelList.get(position).getSubjectExplanation());
            holder.btnSubid.setId(categoryModelList.get(position).getSubId());
            holder.catid.setId(categoryModelList.get(position).getCatid());

            holder.btnSubid.setOnClickListener(btnclick);


            return convertView;

        }
        View.OnClickListener btnclick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(CatSubject.this,Cat_subject_test.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("subid", String.valueOf(view.getId()));
                CatSubject.this.startActivity(i);
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
            private TextView catid;
            private TextView tvTotalSubject;
            private TextView tvTagline;
            private TextView tvSubPurpose;
            private TextView tvSubDesc;
            private Button btnSubid;

        }
    }
}
