package com.example.nauman.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.nauman.models.TestModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Cat_subject_test extends AppCompatActivity {
    private ProgressDialog progressdialog;
    public ListView lvSubject ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_subject_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressdialog = new ProgressDialog(this);
        progressdialog.setIndeterminate(true);
        progressdialog.setMessage("Loading, Please wait...");

        lvSubject =  (ListView) findViewById(R.id.lvCatTest);

        Intent intent = getIntent();
        String subid = intent.getExtras().getString("subid");
        //String catid="";
        new Cat_subject_test.JSONTask_CatTest().execute("http://www.freemcqs.com/WebServiceFreemcqs.asmx/LoadHomePages?subid="+subid);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public  class JSONTask_CatTest extends AsyncTask<String,String,List<TestModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected List<TestModel> doInBackground(String... params) {
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
                JSONArray parentArray = parentObject.getJSONArray("test");
                List<TestModel> catModelList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    TestModel categoryModel = gson.fromJson(finalObject.toString(),TestModel.class);
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
        protected void onPostExecute(List<TestModel> result) {
            super.onPostExecute(result);
            progressdialog.dismiss();
            // Need to set data to the list
            Cat_subject_test.CatTestAdapter adapter = new Cat_subject_test.CatTestAdapter(getApplicationContext(),R.layout.cat_subject_testrow,result);
            lvSubject.setAdapter(adapter);
            //  tvData.setText(result);
        }


    }
    public class CatTestAdapter extends ArrayAdapter {


        private List<TestModel> categoryModelList;
        private int resource;
        private LayoutInflater inflater;
        public CatTestAdapter(Context context, int resource, List<TestModel> objects){
            super(context,resource,objects);
            categoryModelList = objects;
            this.resource = resource;
            inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Cat_subject_test.CatTestAdapter.ViewHolder holder = null;
            if(convertView == null){


                holder = new Cat_subject_test.CatTestAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.cat_subject_testrow,null);
                //holder.ivMovieIcon = (ImageView)convertView.findViewById(R.id.ivCatIcon);
                holder.tvTestName = (TextView) convertView.findViewById(R.id.tvTestName);
                holder.tvTotalMcqs = (TextView) convertView.findViewById(R.id.tvTotalMcqs);
                holder.TestDesc= (TextView) convertView.findViewById(R.id.tvTestDesc);
                //holder.Testid= (TextView) convertView.findViewById(R.id.);
                holder.Ismultiple = (TextView) convertView.findViewById(R.id.tvIsmultiple);
                holder.Testid = (Button) convertView.findViewById(R.id.btnTest);
                holder.Pracid = (Button) convertView.findViewById(R.id.btnPractice);


               // holder.tvSubPurpose = (TextView) convertView.findViewById(R.id.tvSubPurpose);
                //holder.tv = (TextView) convertView.findViewById(R.id.tvTestDesc);
                /*

                holder.tvTagline = (TextView) convertView.findViewById(R.id.tvTagLine);



                */

                // holder.rbMovieRating = (RatingBar) convertView.findViewById(R.id.rbMovie);
                convertView.setTag(holder);
            } else{
                holder = (Cat_subject_test.CatTestAdapter.ViewHolder) convertView.getTag();
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
            holder.tvTestName.setText(categoryModelList.get(position).getTestName());
           // holder.tvTotalTest.setText("Total Test(s): "+categoryModelList.get(position).getTotalTest());
            //  holder.tvTotalSubject.setText("Total Subject(s): "+categoryModelList.get(position).getTotalSub());
            //holder.tvTagline.setText(""+categoryModelList.get(position).getTagline());
            int totalq = 0;
            if(categoryModelList.get(position).getIsmultiple()==0)
            {
                totalq = categoryModelList.get(position).getTotalmcqs();
            }
            else
            {
                totalq = categoryModelList.get(position).getTestTotalQuestions();
            }
            holder.tvTotalMcqs.setText("Total Mcq(s): "+totalq);
            holder.Ismultiple.setText(categoryModelList.get(position).getIsmultiple()+"");
           // holder.tvSubPurpose.setText(""+categoryModelList.get(position).getTestDesc());
            holder.TestDesc.setText(""+categoryModelList.get(position).getTestInstructions()+"\n "+categoryModelList.get(position).getTestDesc());
            holder.Testid.setId(categoryModelList.get(position).getTestid());
            holder.Pracid.setId(categoryModelList.get(position).getTestid());

            holder.Pracid.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryModelList.get(position).getTestName();
                    Intent i = new Intent(Cat_subject_test.this,Mcqs.class);
                    String mystr = String.valueOf(categoryModelList.get(position).getTestid());
                    i.putExtra("testid", String.valueOf(categoryModelList.get(position).getTestid()));
                    i.putExtra("Ismultiple", String.valueOf(categoryModelList.get(position).getIsmultiple()));
                    i.putExtra("testname", String.valueOf(categoryModelList.get(position).getTestName()));
                    if(categoryModelList.get(position).getIsmultiple()==0)
                    {
                        i.putExtra("totalmcqs", String.valueOf(categoryModelList.get(position).getTotalmcqs()));
                    }
                    else
                    {
                        i.putExtra("totalmcqs", String.valueOf(categoryModelList.get(position).getTestTotalQuestions()));

                    }

                   // categoryModelList.get(view.getId()).getTestid();
                  /*  i.putExtra("subid", String.valueOf(view.getId()));
                    i.putExtra("subid", String.valueOf(view.getId()));
                    i.putExtra("subid", String.valueOf(view.getId()));*/
                   Cat_subject_test.this.startActivity(i);
                  //  Log.i("TAG", "The index is" + index);
                }
            });
           // holder.Pracid.setOnClickListener(btnclickPract);
          //  holder.Testid.setOnClickListener(btnclickTest);

            return convertView;

        }
        View.OnClickListener btnclickPract = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Cat_subject_test.this,Mcqs.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("testid", String.valueOf(view.getId()));
                i.putExtra("name", String.valueOf(0));
                categoryModelList.get(view.getId()).getTestid();
                i.putExtra("subid", String.valueOf(view.getId()));
                i.putExtra("subid", String.valueOf(view.getId()));
                i.putExtra("subid", String.valueOf(view.getId()));
                Cat_subject_test.this.startActivity(i);


            }
        };
        View.OnClickListener btnclickTest = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Cat_subject_test.this,Mcqs.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("subid", String.valueOf(view.getId()));
                Cat_subject_test.this.startActivity(i);
            }
        };
        class ViewHolder{

            private ImageView ivMovieIcon;
            private TextView tvTestName;
            private TextView tvTotalMcqs;
            private TextView tvTotalTest;
            private TextView catid;
            private TextView TestAttempts;
            private TextView TestType;
            private TextView SpecMarks;
            private TextView IsNegativeMarks;
            private TextView TestTotalQuestions;
            private TextView Ismultiple;
            private TextView TestPassingPercentage;
            private TextView tvSubPurpose;
            private TextView TestDesc;
            private Button Testid;
            private Button Pracid;

        }
    }

}
