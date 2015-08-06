package in.nearfox.nearfox.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.google.android.gms.maps.model.LatLng;

import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.authentication.LoginActivity;
import in.nearfox.nearfox.models.AYNQuestionsCursorAdapter;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.DBManager;
import in.nearfox.nearfox.utilities.MyCallback;
import in.nearfox.nearfox.utilities.Preference;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyAYNFragment1 extends android.support.v4.app.Fragment implements MyCallback.CallAdapter {

    static AYNQuestionsCursorAdapter mAynQuestionsCursorAdapter;
    final String TAG = this.getClass().getName();
    Context context;
    DBManager dbManager;
    ListView listView;
    Cursor mCursor;
    ProgressDialog progressDialog;
    MyCallback.LocationListener mLocationListener;
    static String TAG1 ;
    boolean firstTime = true;

    public MyAYNFragment1() {
        // Required empty public constructor
        TAG1 = TAG;
    }

    public static void onClickUpVote(View view, final Context context) {

        String emailId = checkUser(context);

        if (emailId == null) {
            emailId = "";
        } else {

            final LinearLayout rootView = (LinearLayout) view.getParent().getParent();
            final FontAwesomeText upVoteImage = (FontAwesomeText) view.findViewById(R.id.ayn_upvote_image);

            upVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
            TextView QuestionIdTextView = (TextView) rootView.findViewById(R.id.ayn_id);

            String questionId = QuestionIdTextView.getText().toString();

            Callback<DataModels.AYNAnswerVoteOrAnswer> cb = new Callback<DataModels.AYNAnswerVoteOrAnswer>() {
                @Override
                public void success(DataModels.AYNAnswerVoteOrAnswer aynPostVoteOrComment, Response response) {
                    boolean success = aynPostVoteOrComment.isSuccess();
                    boolean opposite = aynPostVoteOrComment.isOpposite();

                    Log.d(TAG1, "opposite: " + opposite);

                    String message = aynPostVoteOrComment.getMessage();
                    int upCount, downCount = 0;
                    boolean whetherUpvoted, whetherDownvoted;
                    boolean whetherUpCountChanged, whetherDownCountChanged;

                    if (success) {

                        //                    setting upVote blue
                        upVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));

                        //                    increment upvote count
                        TextView upVoteTextView = (TextView) rootView.findViewById(R.id.ayn_upvote_count);
                        if (upVoteTextView.getText().toString().equals(""))
                            upCount = 0;
                        else
                            upCount = Integer.parseInt(upVoteTextView.getText().toString());

                        upCount++;
                        whetherUpCountChanged = true;
                        upVoteTextView.setText(String.valueOf(upCount));

                        whetherUpvoted = true;
                        whetherDownvoted = false;


                        TextView downVoteCountTextView = (TextView) rootView.findViewById(R.id.ayn_downvote_count);
                        downCount = Integer.parseInt(downVoteCountTextView.getText().toString());

                        if (opposite) {
                            // nothing to do
                            whetherDownCountChanged = false;
                        } else {

                            //                        setting downvote black
                            FontAwesomeText downVoteImage = (FontAwesomeText) rootView.findViewById(R.id.ayn_downvote_image);
                            downVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
                            downVoteImage.setTextColor(context.getResources().getColor(R.color.extreme_light_grey));

                            //                        decrementing downVote count
                            downCount--;
                            if (downCount == 0)
                                downVoteCountTextView.setText("");
                            else
                                downVoteCountTextView.setText(String.valueOf(downCount));

                            whetherDownCountChanged = true;
                        }
                        persistChanges(rootView, upCount, downCount, whetherUpCountChanged, whetherDownCountChanged, whetherUpvoted, whetherDownvoted, context);

                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(context, "Unable to connect to server, try later", Toast.LENGTH_LONG).show();
                }
            };


            /**
             * hardcoding email
             */
//            emailId = "nikunj.sharma9299@gmail.com";

            RestClient restClient = new RestClient();
            restClient.getApiService1().questionVoteUp(emailId, questionId, cb);
        }
    }

    public static void onClickDownVote(View view, final Context context) {

        String emailId = checkUser(context);
        if (emailId == null) {

        } else {

            final LinearLayout rootView = (LinearLayout) view.getParent().getParent();

            final FontAwesomeText downVoteImage = (FontAwesomeText) view.findViewById(R.id.ayn_downvote_image);
            downVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
            TextView postIdTextView = (TextView) rootView.findViewById(R.id.ayn_id);
            String questionId = postIdTextView.getText().toString();

            Callback<DataModels.AYNAnswerVoteOrAnswer> cb = new Callback<DataModels.AYNAnswerVoteOrAnswer>() {
                @Override
                public void success(DataModels.AYNAnswerVoteOrAnswer aynPostVoteOrComment, Response response) {
                    boolean success = aynPostVoteOrComment.isSuccess();
                    boolean opposite = aynPostVoteOrComment.isOpposite();
                    String message = aynPostVoteOrComment.getMessage();
                    int upCount = 0, downCount;
                    boolean whetherUpvoted, whetherDownvoted;
                    boolean whetherUpCountChanged, whetherDownCountChanged;


                    if (success) {

//                        setting upVote blue
                        downVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));

//                        increment upvote count
                        TextView downVoteTextView = (TextView) rootView.findViewById(R.id.ayn_downvote_count);

                        if (downVoteTextView.getText().toString().equals(""))
                            downCount = 0;
                        else
                            downCount = Integer.parseInt(downVoteTextView.getText().toString());

                        downCount++;
                        whetherDownCountChanged = true;
                        downVoteTextView.setText(String.valueOf(downCount));

                        whetherDownvoted = true;
                        whetherUpvoted = false;

                        TextView upVoteCountTextView = (TextView) rootView.findViewById(R.id.ayn_upvote_count);
                        upCount = Integer.parseInt(upVoteCountTextView.getText().toString());

                        if (opposite) {
                            // nothing to do
                            whetherUpCountChanged = false;
                        } else {

//                            setting upvote black
                            FontAwesomeText upVoteImage = (FontAwesomeText) rootView.findViewById(R.id.ayn_upvote_image);
                            upVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
                            upVoteImage.setTextColor(context.getResources().getColor(R.color.extreme_light_grey));

//                            decrementing downVote count
                            upCount--;

                            if (upCount == 0)
                                upVoteCountTextView.setText("");
                            else
                                upVoteCountTextView.setText(String.valueOf(upCount));
                            whetherUpCountChanged = true;
                        }
                        persistChanges(rootView, upCount, downCount, whetherUpCountChanged, whetherDownCountChanged, whetherUpvoted, whetherDownvoted, context);
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(context, "Unable to connect to server, try later.", Toast.LENGTH_LONG).show();
                }
            };

            /**
             * hardcoding email
             */
//            emailId = "nikunj.sharma9299@gmail.com";

            RestClient restClient = new RestClient();
            restClient.getApiService1().questionVoteDown(emailId, questionId, cb);
        }
    }

    public static String checkUser(Context context) {
        String emailId = MainActivity.checkLoggedIn(context);
        if (emailId == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }

        return emailId;
    }

    public static String onlyCheckUser(Context context) {
        return MainActivity.checkLoggedIn(context);
    }

    public static void persistChanges(View view, int upCount, int downCount, boolean whetherUpCountChanged, boolean whetherDownCountCanged, boolean whetherUpvoted, boolean whetherDownVoted, Context context) {

        String questionId = ((TextView) (view
                .findViewById(R.id.ayn_id)))
                .getText()
                .toString();

        DBManager dbManager = new DBManager(context);
        dbManager.updateAYNQuestionUpvoteDownvote(questionId, upCount, downCount, whetherUpvoted, whetherDownVoted);
        mAynQuestionsCursorAdapter.swapCursor(dbManager.getAYNQuestionsList());
        mAynQuestionsCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        Log.d(TAG, "oncreate ayn");

        try {
            mLocationListener = (MyCallback.LocationListener) getActivity();
        } catch (ClassCastException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "oncreateview ayn");
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        context = getActivity();

        listView = (ListView) rootView.findViewById(R.id.list_view);


        /**
         * start showing events already in the database
         */
        startShowingAYNQuestionsFromDB();

        Callback<DataModels.AYNQuestions> cb = new Callback<DataModels.AYNQuestions>() {
            @Override
            public void success(DataModels.AYNQuestions aynQuestions, Response response) {
                Log.d(TAG, "success network call ayn");
                String currentTimeStamp = aynQuestions.getCurrent_time_stamp();
                setCurrentTimeStamp(currentTimeStamp);

                boolean dataChanged = aynQuestions.isChange_status();
                Log.d(TAG, "data found ayn" + dataChanged);

//                dataChanged = true;

                if (dataChanged) {
                    Log.d(TAG, "calling mThread ayn");
                    new MThread(dataChanged, MyAYNFragment1.this, aynQuestions).start();
                }


                /**
                 * stopping progress dialog
                 */
                EventFragment.stopProgressDialog(progressDialog);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());

                /**
                 * stopping progress dialog
                 */
                EventFragment.stopProgressDialog(progressDialog);
            }
        };


        /**
         * StartShowing progressbar
         */
        EventFragment.startProgressDialog(progressDialog);

        /**
         * Meawhile send the request to backend for loading new Questions
         * and checking if question order has changed
         * if it has, we will
         * empty the Db and start afresh
         */
        RestClient restClient = new RestClient();

        String emailId = onlyCheckUser(context);
        if (emailId == null) {
            Log.d(TAG, "AYN for not signed in calling server");
            emailId = "";
            LatLng currentLatLng = mLocationListener.getCurrentLocation();
            Double latitude = 0.0;
            Double longitude = 0.0;
            if (currentLatLng != null) {
                latitude = currentLatLng.latitude;
                longitude = currentLatLng.longitude;
            }

            /**
             * hard coding latitude and longitude
             */
//            latitude = 19.0544718;
//            longitude = 72.8475496;
            restClient.getApiService1().getAynQuestionsNotSignedIn(emailId, getLastTimeStamp(), latitude, longitude, cb);
        } else {
            Log.d(TAG, "AYN for signed in calling server");

            if(new Preference(getActivity()).getLoggedEmail().trim().length() == 0)
                new Preference(context).setLoggedInEmail(emailId);
            /**
             * hardcoding the email id
             */
//            emailId = "nikunj.sharma9299@gmail.com";
            restClient.getApiService1().getAynQuestions(emailId, getLastTimeStamp(), cb);
        }

        rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).submitClick(2);
            }
        });

        return rootView;
    }

    private void startShowingAYNQuestionsFromDB() {
        Log.d(TAG, "started from sb ayn");
        mCursor = dbManager.getAYNQuestionsList();
        mAynQuestionsCursorAdapter = new AYNQuestionsCursorAdapter(getActivity(), mCursor);
        listView.setAdapter(mAynQuestionsCursorAdapter);
        Log.d(TAG, "end from db ayn");
    }

    @Override
    public void callAdapter() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "adapter called ayn");
                    Cursor cursor = dbManager.getAYNQuestionsList();
//                    listView.setAdapter(new AYNQuestionsCursorAdapter(getActivity(), dbManager.getAYNQuestionsList()));
                    Log.d(TAG, "swapping cursor ayn");
                    mAynQuestionsCursorAdapter.swapCursor(cursor);
                    Log.d(TAG, "notifying ayn");
                    mAynQuestionsCursorAdapter.notifyDataSetChanged();
                    Log.d(TAG, "notified ayn");
                }
            });
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public String getLastTimeStamp() {
        /*try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            return sharedPreferences.getString(getActivity().getString(R.string.CURRENT_TIME_STAMP_AYNQUESTIONS), "0.0");
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }*/
        return "0.0";
    }

    public void setCurrentTimeStamp(String currentTimeStamp) {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getActivity().getString(R.string.CURRENT_TIME_STAMP_AYNQUESTIONS), currentTimeStamp);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }

    class MThread extends Thread {

        boolean dataChanged;
        MyCallback.CallAdapter mCallAdapter;
        DataModels.AYNQuestions aynQuestions;

        public MThread(boolean dataChanged, MyCallback.CallAdapter callAdapter, DataModels.AYNQuestions aynQuestions) {
            Log.d(TAG, "mThread called ayn");
            this.dataChanged = dataChanged;
            this.mCallAdapter = callAdapter;
            this.aynQuestions = aynQuestions;
        }

        public void run() {
            Log.d(TAG, "called start ayn");
            dbManager.emptyAYNQustionstable();
            dbManager.insertAYNQuestions(aynQuestions.getQuestions_all());
            Log.d(TAG, "called adapter ayn");
            mCallAdapter.callAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!firstTime) {

            Log.d(TAG, "adapter called ayn after resume");
            Cursor cursor = dbManager.getAYNQuestionsList();
//                    listView.setAdapter(new AYNQuestionsCursorAdapter(getActivity(), dbManager.getAYNQuestionsList()));
            Log.d(TAG, "swapping cursor ayn after resume");
            mAynQuestionsCursorAdapter.swapCursor(cursor);
            Log.d(TAG, "notifying ayn after resume");
            mAynQuestionsCursorAdapter.notifyDataSetChanged();
            Log.d(TAG, "notified ayn after resume");
        }

        firstTime = false;
    }
}