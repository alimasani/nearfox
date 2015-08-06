package in.nearfox.nearfox.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.ApplicationHelper;
import in.nearfox.nearfox.utilities.FileExplore;
import in.nearfox.nearfox.utilities.Preference;
import in.nearfox.nearfox.utilities.RestClient;
import in.nearfox.nearfox.view.PlaceAutoComplete;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SubmitAYNFragment extends Fragment {

    private static String TAG = "MyDebug";
    static Context context;
    private View rootView;

    private EditText submitQuestion;

    public SubmitAYNFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frament_submit_ask, container, false);
        context = getActivity();

        submitQuestion = (EditText) rootView.findViewById(R.id.submitQuestion);

        rootView.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClicked();
            }
        });

        submitQuestion.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() ==R.id.submitQuestion) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

       // getLocations();
        return rootView;
    }

    private void submitClicked() {
        if(submitQuestion.getText().toString().trim().length() == 0) {
            submitQuestion.setError("Please fill your question");
            submitQuestion.requestFocus();
        } else if(submitQuestion.getText().toString().trim().length() <  10) {
            submitQuestion.setError("Question must be 10 character long.");
            submitQuestion.requestFocus();
        } else {
            rootView.findViewById(R.id.btnSubmit).setEnabled(false);
            rootView.findViewById(R.id.btnSubmit).setClickable(false);
            postQuestion();
        }
    }

    private void postQuestion(){
        RestClient restClient = new RestClient(RestClient.BASE_URL1);
        Preference preference = new Preference(getActivity());
        restClient.getApiService().postQuestions(preference.getLoggedEmail(), submitQuestion.getText().toString()
                ,  submitCallback);
    }

    private Callback<DataModels.Location> submitCallback = new Callback<DataModels.Location>() {
        @Override
        public void success(DataModels.Location homeLocation, Response response2) {
            if(homeLocation.isSuccess()) {
                new ApplicationHelper(getActivity()).showMessageDialog(homeLocation.getMessage());
                submitQuestion.setText("");
            }
            rootView.findViewById(R.id.btnSubmit).setEnabled(true);
            rootView.findViewById(R.id.btnSubmit).setClickable(true);
        }

        @Override
        public void failure(RetrofitError error) {
            if(error.isNetworkError())
                new ApplicationHelper(context).showMessageDialog("There seems to be a connectivity issue. Please check your internet connection.");
            else
                new ApplicationHelper(getActivity()).showMessageDialog("Something wrong must have happened, please try again.");
            Log.d("KKK", error.toString());
            rootView.findViewById(R.id.btnSubmit).setEnabled(true);
            rootView.findViewById(R.id.btnSubmit).setClickable(true);
        }
    };

}