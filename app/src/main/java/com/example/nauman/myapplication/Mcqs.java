package com.example.nauman.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nauman.models.McqsModel;
import com.example.nauman.models.TestModel;
import com.google.gson.Gson;

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

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Mcqs extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private ProgressDialog progressdialog;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            /*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    public ListView lvSubject;
    private boolean flag_loading;
    private int pageCount =0;
    private String testid;
    private String testname;
    private String Ismultiple;
    private String totalmcqs;
    public  int QuestionNo =0;
    public View parrentView ;
    public List<McqsModel> parrentlist;
    public ArrayList<McqsModel> arrayListMcqs;
    ArrayAdapter<McqsAdapter> mAdapter;
    List<McqsModel> mcqsModels = new ArrayList<McqsModel>();
    //List<String> newList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mcqs);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        progressdialog = new ProgressDialog(this);
        progressdialog.setIndeterminate(true);
        progressdialog.setMessage("Loading, Please wait...");
     //   mAdapter
        lvSubject =  (ListView) findViewById(R.id.mcqs_list);

        Intent intent = getIntent();
         testid ="0";
         testname = "";
         Ismultiple= "";
        if (getIntent().getStringExtra("testid") != null )
        {
            testid = intent.getExtras().getString("testid");
            Ismultiple = intent.getExtras().getString("Ismultiple");
            testname = intent.getExtras().getString("testname");
            totalmcqs = intent.getExtras().getString("totalmcqs");


        }
        getDataFromUrl("http://www.freemcqs.com/WebServiceFreemcqs.asmx/LoadMcqsQuestions?TestId="+testid+"&&name=1&&pageIndex=0&&IsMultiple="+Ismultiple+"&&ClientTestInfo=[]");
        //String catid="";
        lvSubject.setOnScrollListener(onScrollListener());

        // Set up the user interaction to manually show or hide the system UI.
      /*  mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    // calling asynctask to get json data from internet
    private void getDataFromUrl(String url) {
         new JSONTask_Mcqs().execute(url);

       // new Mcqs.JSONTask_Mcqs().execute();
    }
    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
               int count = lvSubject.getCount();
               int maxpage = (Integer.parseInt(totalmcqs)/10);

                if (lvSubject.getLastVisiblePosition() >= count - threshold && pageCount <= maxpage) {
                    //  Log.i(TAG, "loading more data");
                    // Execute LoadMoreDataTask AsyncTask
                   String url_page2 = "http://www.freemcqs.com/WebServiceFreemcqs.asmx/LoadMcqsQuestions?TestId="+testid+"&&name=1&&pageIndex="+pageCount+"&&IsMultiple="+Ismultiple+"&&ClientTestInfo=[]";
                    getDataFromUrl(url_page2);
                }

                }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public  class JSONTask_Mcqs extends AsyncTask<String,String,List<McqsModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected List<McqsModel> doInBackground(String... params) {
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
                JSONArray parentArray = parentObject.getJSONArray("mcqs");
                List<McqsModel> catModelList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i=0; i<parentArray.length(); i++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    McqsModel categoryModel = gson.fromJson(finalObject.toString(),McqsModel.class);
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
        protected void onPostExecute(List<McqsModel> result) {
            super.onPostExecute(result);
            progressdialog.dismiss();
            // Need to set data to the list
           // parrentlist.addAll(result);

                mcqsModels.addAll(result);

               // Mcqs.McqsAdapter adapter = new McqsAdapter(getApplicationContext(), R.layout.mcqs_pract_list,result);
                if(mcqsModels.size()>11)
                {
                    //mAdapter.add(result);

                   // mAdapter.addAll(adapter);
                    //lvSubject.setAdapter(adapter);
                    //int position = mcqsModels.size();
                  //  mcqsModels.remove(mcqsModels.size() - 1);
                    //QuestionNo--;
                    mAdapter.notifyDataSetChanged();
                    //lvSubject.smoothScrollToPosition(position);
                }
                else{
                 //  mcqsModels.remove(mcqsModels.size() - 1);
                   //QuestionNo--;
                    mAdapter = new McqsAdapter(getApplicationContext(), R.layout.mcqs_pract_list,mcqsModels);
                    lvSubject.setAdapter(mAdapter);
                }



                pageCount++;





               // lvSubject.setTextFilterEnabled(true);
               // adapter.notifyDataSetChanged();


            //  tvData.setText(result);
        }
    }




    public class McqsAdapter extends ArrayAdapter {


        private List<McqsModel> categoryModelList;
        private int resource;
        private LayoutInflater inflater;
        public McqsAdapter(Context context, int resource, List<McqsModel> objects){
            super(context,resource,objects);
            categoryModelList = objects;
            this.resource = resource;
            inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QuestionNo++;
           /* if(mcqsModels.size()!=0)
            {
                int myindex = mcqsModels.size()+1;
                position = myindex;
            }*/

            Mcqs.McqsAdapter.ViewHolder holder = null;

            if(convertView == null)
            {


                holder = new Mcqs.McqsAdapter.ViewHolder();

                    convertView = inflater.inflate(R.layout.mcqs_pract_list,null);

                //convertView = inflater.inflate(R.layout.mcqs_pract_list,null);
                //holder.ivMovieIcon = (ImageView)convertView.findViewById(R.id.ivCatIcon);
                holder.QuestionText = (TextView) convertView.findViewById(R.id.tvQuestionText);
                holder.Qno = (TextView) convertView.findViewById(R.id.tvQno);
                holder.ExplanationText = (TextView) convertView.findViewById(R.id.tvExplanationText);
                holder.op1 = (TextView) convertView.findViewById(R.id.tvop1);
                holder.op2 = (TextView) convertView.findViewById(R.id.tvop2);
                holder.op3 = (TextView) convertView.findViewById(R.id.tvop3);
                holder.op4 = (TextView) convertView.findViewById(R.id.tvop4);
                holder.op5 = (TextView) convertView.findViewById(R.id.tvop5);
                holder.op6 = (TextView) convertView.findViewById(R.id.tvop6);
                holder.op7 = (TextView) convertView.findViewById(R.id.tvop7);
                holder.op8 = (TextView) convertView.findViewById(R.id.tvop8);
                holder.Option8Correct = (TextView) convertView.findViewById(R.id.tvOption8Correct);
                holder.Option8Text = (RadioButton) convertView.findViewById(R.id.rdb8);
                holder.Option7Text = (RadioButton) convertView.findViewById(R.id.rdb7);
                holder.Option7Correct = (TextView) convertView.findViewById(R.id.tvOption7Correct);
                holder.Option6Correct = (TextView) convertView.findViewById(R.id.tvOption6Correct);
                holder.Option6Text = (RadioButton) convertView.findViewById(R.id.rdb6);
                holder.Option5Correct = (TextView) convertView.findViewById(R.id.tvOption5Correct);
                holder.Option5Text = (RadioButton) convertView.findViewById(R.id.rdb5);
                holder.Option4Correct = (TextView) convertView.findViewById(R.id.tvOption4Correct);
                holder.Option4Text = (RadioButton) convertView.findViewById(R.id.rdb4);
                holder.Option3Correct = (TextView) convertView.findViewById(R.id.tvOption3Correct);
                holder.Option3Text = (RadioButton) convertView.findViewById(R.id.rdb3);
                holder.Option2Correct = (TextView) convertView.findViewById(R.id.tvOption2Correct);
                holder.Option2Text = (RadioButton) convertView.findViewById(R.id.rdb2);
                holder.Option1Correct = (TextView) convertView.findViewById(R.id.tvOption1Correct);
                holder.Option1Text = (RadioButton) convertView.findViewById(R.id.rdb1);
                holder.McqsQuestionId = (TextView) convertView.findViewById(R.id.tvMcqsQuestionId);
                //holder.Reference = (TextView) convertView.findViewById(R.id.tvReference);
                holder.SpecMarks = (TextView) convertView.findViewById(R.id.tvSpecMarks);
                holder.isNegativeMarks = (TextView) convertView.findViewById(R.id.tvisNegativeMarks);
                holder.SecMergeTestId = (TextView) convertView.findViewById(R.id.tvSecMergeTestId);
                holder.SecId = (TextView) convertView.findViewById(R.id.tvSecId);
                holder.TestId = (TextView) convertView.findViewById(R.id.tvTestId);
                holder.edate = (TextView) convertView.findViewById(R.id.tvedate);
                holder.pagecount = (TextView) convertView.findViewById(R.id.tvpagecount);
                holder.staff_exp_id = (TextView) convertView.findViewById(R.id.tvstaff_exp_id);
                holder.exp_check_staff_acc = (TextView) convertView.findViewById(R.id.tvexp_check_staff_acc);
                holder.exp_check_staff_qual = (TextView) convertView.findViewById(R.id.tvexp_check_staff_qual);
                holder.exp_check_staff_pic = (TextView) convertView.findViewById(R.id.tvexp_check_staff_pic);
                holder.exp_check_staff_name = (TextView) convertView.findViewById(R.id.tvexp_check_staff_name);
                holder.isExplained = (TextView) convertView.findViewById(R.id.tvisExplained);
                holder.IsuserAttempt = (TextView) convertView.findViewById(R.id.tvIsuserAttempt);
                holder.Isusercorrect = (TextView) convertView.findViewById(R.id.tvIsusercorrect);
                holder.userselectedoption = (TextView) convertView.findViewById(R.id.tvuserselectedoption);
                holder.AdminId = (TextView) convertView.findViewById(R.id.tvAdminId);
                holder.AdminName = (TextView) convertView.findViewById(R.id.tvAdminName);
                holder.AdminEmail = (TextView) convertView.findViewById(R.id.tvAdminEmail);
                holder.SubjectName = (TextView) convertView.findViewById(R.id.tvSubjectName);
                holder.TestName = (TextView) convertView.findViewById(R.id.tvTestName);
                holder.Explained = (Button) convertView.findViewById(R.id.btnExp);
                holder.Reference = (Button) convertView.findViewById(R.id.btnRef);
                holder.Report = (Button) convertView.findViewById(R.id.btnReport);
                holder.Points = (TextView) convertView.findViewById(R.id.tvPoints);
                holder.radioGroup = (RadioGroup)convertView.findViewById(R.id.rdbtnOption);
               // holder.tvTotalMcqs = (TextView) convertView.findViewById(R.id.tvTotalMcqs);
                //holder.Testid= (TextView) convertView.findViewById(R.id.);
               // holder.Testid = (Button) convertView.findViewById(R.id.btnTest);
               // holder.Pracid = (Button) convertView.findViewById(R.id.btnPractice);
                // holder.tvSubPurpose = (TextView) convertView.findViewById(R.id.tvSubPurpose);
                //holder.tv = (TextView) convertView.findViewById(R.id.tvTestDesc);
                /*

                holder.tvTagline = (TextView) convertView.findViewById(R.id.tvTagLine);



                */

                // holder.rbMovieRating = (RatingBar) convertView.findViewById(R.id.rbMovie);
                convertView.setTag(holder);
            } else{
                holder = (Mcqs.McqsAdapter.ViewHolder) convertView.getTag();
            }



           // final ProgressBar progressBar =(ProgressBar) convertView.findViewById(R.id.progressBar);
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

           // holder.Qno.setText("Q.No. "+ String.valueOf(position) );
            holder.Qno.setText("QUESTION:");
            holder.QuestionText.setText(categoryModelList.get(position).getQuestionText());

            if(categoryModelList.get(position).isOp1()==true && categoryModelList.get(position).getOption1Text()!=null)
            {
                ((RadioButton) holder.radioGroup.getChildAt(0)).setText(categoryModelList.get(position).getOption1Text());
              //  holder.Option1Text.setText(categoryModelList.get(position).getOption1Text()+"");
             //   holder.Option1Text.setId(categoryModelList.get(position).getOption1Id());
                if(categoryModelList.get(position).isOption1Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(0)).setChecked(true);
                   // holder.Option1Text.setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(0)).setVisibility(View.VISIBLE);

            }
            else 
            {
                ((RadioButton) holder.radioGroup.getChildAt(0)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp2()==true && categoryModelList.get(position).getOption2Text() != null)
            {
                ((RadioButton) holder.radioGroup.getChildAt(1)).setText(categoryModelList.get(position).getOption2Text());
              //  holder.Option2Text.setText(categoryModelList.get(position).getOption2Text()+"");
              //  holder.Option2Text.setId(categoryModelList.get(position).getOption2Id());
                if(categoryModelList.get(position).isOption2Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(1)).setChecked(true);
                  //  holder.Option2Text.setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(1)).setVisibility(View.VISIBLE);
               // holder.Option2Text.setVisibility(View.VISIBLE);
            }
            else {
                ((RadioButton) holder.radioGroup.getChildAt(1)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp3()==true && categoryModelList.get(position).getOption3Text() != null)
            {
                ((RadioButton) holder.radioGroup.getChildAt(2)).setText(String.valueOf(categoryModelList.get(position).getOption3Text()));
              //  holder.Option3Text.setText(categoryModelList.get(position).getOption3Text()+"");
             //   holder.Option3Text.setId(categoryModelList.get(position).getOption3Id());
                if(categoryModelList.get(position).isOption3Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(2)).setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(2)).setVisibility(View.VISIBLE);
            }
            else 
            {
                ((RadioButton) holder.radioGroup.getChildAt(2)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp4()==true && categoryModelList.get(position).getOption4Text() != null)
            {
                ((RadioButton) holder.radioGroup.getChildAt(3)).setText(String.valueOf(categoryModelList.get(position).getOption4Text()));
             //   holder.Option4Text.setText(categoryModelList.get(position).getOption4Text()+"");
            //    holder.Option4Text.setId(categoryModelList.get(position).getOption4Id());
                if(categoryModelList.get(position).isOption4Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(3)).setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(3)).setVisibility(View.VISIBLE);
            }
            else {
                ((RadioButton) holder.radioGroup.getChildAt(3)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp5()==true && categoryModelList.get(position).getOption5Text() != null)
            {
                ((RadioButton) holder.radioGroup.getChildAt(4)).setText(String.valueOf(categoryModelList.get(position).getOption5Text()));
              //  holder.Option5Text.setText(categoryModelList.get(position).getOption5Text()+"");
              //  holder.Option5Text.setId(categoryModelList.get(position).getOption5Id());
                if(categoryModelList.get(position).isOption5Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(4)).setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(4)).setVisibility(View.VISIBLE);
            }
            else 
            {
                ((RadioButton) holder.radioGroup.getChildAt(4)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp6()==true && categoryModelList.get(position).getOption6Text() != null)
            {
                ((RadioButton) holder.radioGroup.getChildAt(5)).setText(String.valueOf(categoryModelList.get(position).getOption6Text()));
              //  holder.Option6Text.setText(categoryModelList.get(position).getOption6Text()+"");
              //  holder.Option6Text.setId(categoryModelList.get(position).getOption6Id());
                if(categoryModelList.get(position).isOption6Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(5)).setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(5)).setVisibility(View.VISIBLE);
            }
            else {
                ((RadioButton) holder.radioGroup.getChildAt(5)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp7()==true && categoryModelList.get(position).getOption7Text() != null){
                ((RadioButton) holder.radioGroup.getChildAt(6)).setText(String.valueOf(categoryModelList.get(position).getOption7Text()));
               // holder.Option7Text.setText(categoryModelList.get(position).getOption7Text()+"");
                //holder.Option7Text.setId(categoryModelList.get(position).getOption7Id());
                if(categoryModelList.get(position).isOption7Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(6)).setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(6)).setVisibility(View.VISIBLE);
            }
            else {
                ((RadioButton) holder.radioGroup.getChildAt(6)).setVisibility(View.GONE);
            }
            if(categoryModelList.get(position).isOp8()==true && categoryModelList.get(position).getOption8Text() != null){
                ((RadioButton) holder.radioGroup.getChildAt(7)).setText(String.valueOf(categoryModelList.get(position).getOption7Text()));
             //   holder.Option8Text.setText(categoryModelList.get(position).getOption8Text()+"");
               // holder.Option8Text.setId(categoryModelList.get(position).getOption8Id());
                if(categoryModelList.get(position).isOption8Correct()==true){
                    ((RadioButton) holder.radioGroup.getChildAt(7)).setChecked(true);
                }
                ((RadioButton) holder.radioGroup.getChildAt(7)).setVisibility(View.VISIBLE);
            }
            else {
                ((RadioButton) holder.radioGroup.getChildAt(7)).setVisibility(View.GONE);
            }
          if(categoryModelList.get(position).getIsExplained()==0){
              holder.Explained.setVisibility(View.GONE);
          }
          if(categoryModelList.get(position).getReference()==""){
              holder.Reference.setVisibility(View.GONE);
          }

            // holder.tvTotalTest.setText("Total Test(s): "+categoryModelList.get(position).getTotalTest());
            //  holder.tvTotalSubject.setText("Total Subject(s): "+categoryModelList.get(position).getTotalSub());
            //holder.tvTagline.setText(""+categoryModelList.get(position).getTagline());
            //int totalq = 0;
           /* if(categoryModelList.get(position).getIsmultiple()==0)
            {
                totalq = categoryModelList.get(position).getTotalmcqs();
            }
            else
            {
                totalq = categoryModelList.get(position).getTestTotalQuestions();
            }*/
          //  holder.tvTotalMcqs.setText("Total Mcq(s): "+totalq);
            // holder.tvSubPurpose.setText(""+categoryModelList.get(position).getTestDesc());
           // holder.TestDesc.setText(""+categoryModelList.get(position).getTestInstructions()+"\n "+categoryModelList.get(position).getTestDesc());
           // holder.Testid.setId(categoryModelList.get(position).getTestid());
           // holder.Pracid.setId(categoryModelList.get(position).getTestid());


            return convertView;

        }
        View.OnClickListener btnclickExp = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Mcqs.this,Cat_subject_test.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("subid", String.valueOf(view.getId()));
              //  CatSubject.this.startActivity(i);
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
        View.OnClickListener btnclickRef = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Mcqs.this,Cat_subject_test.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("subid", String.valueOf(view.getId()));
              //  CatSubject.this.startActivity(i);
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
        View.OnClickListener btnclickReport = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Mcqs.this,Cat_subject_test.class);
                String mystr = String.valueOf(view.getId());
                i.putExtra("subid", String.valueOf(view.getId()));
                //  CatSubject.this.startActivity(i);
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

            private TextView AdminId ;
            private TextView AdminName ;
            private TextView AdminEmail ;
            private TextView SubjectName ;
            private TextView TestName ;
            private TextView QuestionText ;
            private TextView Qno ;
            private TextView Points ;
            private RadioButton Option1Text ;
            private TextView Option1Correct ;
            private RadioButton Option2Text ;
            private TextView Option2Correct ;
            private RadioButton Option3Text ;
            private TextView Option3Correct ;
            private RadioButton Option4Text ;
            private TextView Option4Correct ;
            private RadioButton Option5Text ;
            private TextView Option5Correct ;
            private RadioButton Option6Text ;
            private TextView Option6Correct ;
            private RadioButton Option7Text ;
            private TextView Option7Correct ;
            private RadioButton Option8Text ;
            private TextView Option8Correct ;
            private TextView ExplanationText ;
         //   private TextView Reference ;
            private TextView op1 ;
            private TextView op2 ;
            private TextView op3 ;
            private TextView op4 ;
            private TextView op5 ;
            private TextView op6 ;
            private TextView op7 ;
            private TextView op8 ;
            private TextView McqsQuestionId ;
            private RadioButton Option1Id ;
            private RadioButton Option2Id ;
            private RadioButton Option3Id ;
            private RadioButton Option4Id ;
            private RadioButton Option5Id ;
            private RadioButton Option6Id ;
            private RadioButton Option7Id ;
            private RadioButton Option8Id ;
            private TextView userselectedoption ;
            private TextView Isusercorrect ;
            private TextView IsuserAttempt ;
            private TextView isExplained ;
            private TextView exp_check_staff_name ;
            private TextView exp_check_staff_pic ;
            private TextView exp_check_staff_qual ;
            private TextView exp_check_staff_acc ;
            private TextView staff_exp_id ;
            private TextView pagecount ;
            private TextView edate ;
            private TextView TestId ;
            private TextView SecId ;
            private TextView SecMergeTestId ;
            private TextView isNegativeMarks ;
            private TextView SpecMarks ;
            private Button Explained;
            private Button Reference;
            private Button Report;
            private RadioGroup radioGroup;
            /*
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
            private Button Pracid;*/

        }
    }
}
