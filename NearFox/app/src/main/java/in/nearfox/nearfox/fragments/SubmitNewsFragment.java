package in.nearfox.nearfox.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.ApplicationHelper;
import in.nearfox.nearfox.utilities.FileExplore;
import in.nearfox.nearfox.utilities.Preference;
import in.nearfox.nearfox.utilities.RestClient;
import in.nearfox.nearfox.view.PlaceAutoComplete;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SubmitNewsFragment extends Fragment {

    private static String TAG = "MyDebug";
    static Context context;
    private View rootView;
    private EditText submitStoryTitle, submitNewsStory, submitContactNumber;
    private PlaceAutoComplete submitLocality;
    private ScrollView scrollView;


    public SubmitNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frament_submit_news, container, false);
        context = getActivity();

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollNews);

        submitStoryTitle = (EditText) rootView.findViewById(R.id.submitStoryTitle);

        submitNewsStory = (EditText) rootView.findViewById(R.id.submitNewsStory);

        submitLocality = (PlaceAutoComplete) rootView.findViewById(R.id.submitLocality);

        submitContactNumber = (EditText) rootView.findViewById(R.id.submitContactNumber);


        rootView.findViewById(R.id.uploadFile).setVisibility(View.GONE);
        rootView.findViewById(R.id.uploadFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileClicked();
            }
        });

        rootView.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClicked(v);
            }
        });
        submitNewsStory.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() ==R.id.submitNewsStory) {
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
        //getLocations();
        return rootView;
    }

    private void submitClicked(View v) {
        if (submitStoryTitle.getText().toString().trim().length() == 0) {
            submitStoryTitle.setError("Please fill title of your story");
            submitStoryTitle.requestFocus();
        } else if (submitStoryTitle.getText().toString().trim().length() < 10) {
            submitStoryTitle.setError("Story title must be 10 character long");
            submitStoryTitle.requestFocus();
        } else if (submitNewsStory.getText().toString().trim().length() == 0) {
            submitNewsStory.setError("Please describe your story");
            submitNewsStory.requestFocus();
        } else if (submitNewsStory.getText().toString().trim().length() < 10) {
            submitNewsStory.setError("Story title must be 10 character long");
            submitNewsStory.requestFocus();
        } else if (submitLocality.getText().toString().trim().length() == 0) {
            submitLocality.setError("Please fill your location");
            submitLocality.requestFocus();
        } else if (submitContactNumber.getText().toString().trim().length() < 10 && submitContactNumber.getText().toString().trim().length() > 0) {
            submitContactNumber.setError("Please fill valid contact number");
            submitContactNumber.requestFocus();
        } else {
            try {
                v.setEnabled(false);
                v.setClickable(false);
                String location = String.format("{\"locations\":[{\"location_name\":\"%s\"}]}",
                        submitLocality.getText().toString().trim());
                RestClient restClient = new RestClient(RestClient.BASE_URL1);
                Preference preference = new Preference(getActivity());
                restClient.getApiService().postNews(preference.getLoggedInEmail(), submitStoryTitle.getText().toString().trim(),
                        submitNewsStory.getText().toString().trim(), "http://www.eventimage.com/", location, callback);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    retrofit.Callback<DataModels.Location> callback = new retrofit.Callback<DataModels.Location>() {
        @Override
        public void success(DataModels.Location news, Response response) {
            if(news.isSuccess()) {
                new ApplicationHelper(getActivity()).showMessageDialog(news.getMessage());
                submitStoryTitle.setText("");
                submitNewsStory.setText("");
                submitContactNumber.setText("");
                submitLocality.setText("");

                rootView.findViewById(R.id.btnSubmit).setEnabled(true);
                rootView.findViewById(R.id.btnSubmit).setClickable(true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if(error.isNetworkError())
                new ApplicationHelper(context).showMessageDialog("There seems to be a connectivity issue. Please check your internet connection.");
            else
                new ApplicationHelper(getActivity()).showMessageDialog("Something wrong must have happened, please try again.");
            rootView.findViewById(R.id.btnSubmit).setEnabled(true);
            rootView.findViewById(R.id.btnSubmit).setClickable(true);
        }
    };

    private void uploadFileClicked() {
        Intent intent = new Intent(getActivity(), FileExplore.class);

        startActivityForResult(intent, 108);
    }

//    private void getLocations() {
//        RestClient restClient = new RestClient(/*RestClient.NEW_BASE_URL*/);
//        restClient.getApiService().getLocation(locationsCallback);
//
//    }
//
//    private retrofit.Callback<DataModels.Locations> locationsCallback = new retrofit.Callback<DataModels.Locations>() {
//        @Override
//        public void success(DataModels.Locations locations, Response response) {
//            LocalityAdapter localityAdapter = new LocalityAdapter(getActivity(), locations.locations);
//            submitLocality.setAdapter(localityAdapter);
//        }
//
//        @Override
//        public void failure(RetrofitError error) {
//            Log.d("KKK", error.toString());
//        }
//    };

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