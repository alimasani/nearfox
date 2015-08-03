package in.nearfox.nearfox.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
    private PlaceAutoComplete submitLocality;

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
        submitLocality = (PlaceAutoComplete) rootView.findViewById(R.id.submitLocality);

        rootView.findViewById(R.id.uploadFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileClicked();
            }
        });

        rootView.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClicked();
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
            postQuestion();
        }
    }

    private void postQuestion(){
        RestClient restClient = new RestClient(RestClient.BASE_URL1);
        Preference preference = new Preference(getActivity());
        restClient.getApiService().postQuestions(preference.getLoggedEmail(), submitQuestion.getText().toString()
                ,  submitCallback);

    }

    private void uploadFileClicked() {
        Intent intent = new Intent(getActivity(), FileExplore.class);

        startActivityForResult(intent, 108);
    }

    private Callback<DataModels.HomeLocation> submitCallback = new Callback<DataModels.HomeLocation>() {
        @Override
        public void success(DataModels.HomeLocation homeLocation, Response response2) {
            if(homeLocation.isSuccess()) {
                new ApplicationHelper(getActivity()).showMessageDialog("Your Question submitted successfully");
                submitQuestion.setText("");
                submitLocality.setText("");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            new ApplicationHelper(getActivity()).showMessageDialog(error.getLocalizedMessage());
            Log.d("KKK", error.toString());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 108 && resultCode == -1) {
            TextView uploadFile = (TextView) rootView.findViewById(R.id.uploadFile);
            uploadFile.setText(data.getStringExtra("fileName"));
            uploadFile.setTag(data.getStringExtra("fileName"));
        }
    }
}