package in.nearfox.nearfox.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

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

public class SubmitEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static String TAG = "MyDebug";
    static Context context;
    private RadioGroup rgPaymentType;
    private View rootView;
    private EditText eventTitle, eventDescription, orgName, orgEmail, contactNumber, amount, linkToBuy, venuename;
    private PlaceAutoComplete venue;
    private LinearLayout layoutTiming;


    public SubmitEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_submit_event, container, false);
        context = getActivity();

        init(rootView);


        return rootView;
    }

    private void init(final View rootView) {
        rgPaymentType = (RadioGroup) rootView.findViewById(R.id.radioPaymentType);
        rgPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.radioPaid) {
                    rootView.findViewById(R.id.laypaid).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.laypaid).setVisibility(View.GONE);
                }
            }
        });

        eventDescription = (EditText) rootView.findViewById(R.id.eventdescription);
        eventTitle = (EditText) rootView.findViewById(R.id.titleofevent);
        venue = (PlaceAutoComplete) rootView.findViewById(R.id.venue);
        venuename = (EditText) rootView.findViewById(R.id.edtVenueName);
        orgEmail = (EditText) rootView.findViewById(R.id.edtOrganisationEmail);
        orgName = (EditText) rootView.findViewById(R.id.edtOrganisationName);
        contactNumber = (EditText) rootView.findViewById(R.id.edtContactNumber);
        amount = (EditText) rootView.findViewById(R.id.amount);
        linkToBuy = (EditText) rootView.findViewById(R.id.linktobuy);

        layoutTiming = (LinearLayout) rootView.findViewById(R.id.layEventDates);

        rootView.findViewById(R.id.btnAddAnotherTiming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View timeLayuout = inflater.inflate(R.layout.layout_timing, null);
                timeLayuout.findViewById(R.id.imgRemove).setTag(timeLayuout);
                timeLayuout.findViewById(R.id.imgRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentView = (View) view.getTag();
                        int counter = (int) layoutTiming.getTag();
                        if(counter > 1) {
                            layoutTiming.setTag(--counter);
                            layoutTiming.removeView(parentView);
                        }
                    }
                });
                eventDescription.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View view, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if (view.getId() ==R.id.eventdescription) {
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
                timeLayuout.findViewById(R.id.txtDateVal).setTag(timeLayuout);
                timeLayuout.findViewById(R.id.txtDateVal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentView = (View) view.getTag();
                        final TextView textView = (TextView) parentView.findViewById(R.id.txtDateVal);
                        String date[] = textView.getText().toString().split("-");
                        DatePickerDialog datePickerDialog;

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                                textView.setText(date);
                            }
                        };

                        if (date.length == 3) {
                            datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, Integer.parseInt(date[2]), Integer.parseInt(date[1]) -1, Integer.parseInt(date[0]));
                        } else {
                            Calendar calendar = Calendar.getInstance();

                            datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        }

                        datePickerDialog.show();
                    }
                });

                timeLayuout.findViewById(R.id.txtTimeVal).setTag(timeLayuout);
                timeLayuout.findViewById(R.id.txtTimeVal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentView = (View) view.getTag();
                        final TextView textView = (TextView) parentView.findViewById(R.id.txtTimeVal);
                        String time[] = textView.getText().toString().split(":");

                        TimePickerDialog timePickerDialog;
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String ampm = " AM";
                                if (hourOfDay >= 12) {
                                    if(hourOfDay > 12)
                                        hourOfDay = hourOfDay - 12;
                                    ampm = " PM";
                                }
                                String time = String.format("%02d:%02d %s", hourOfDay, minute, ampm);
                                textView.setText(time);

                            }
                        };

                        if (time.length == 2) {
                            int hour = Integer.parseInt(time[0]) + (time[1].contains("PM") ? 12 : 0);
                            int min = Integer.parseInt(time[1].substring(0, 2).trim());
                            timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, min, false);
                        } else {
                            Calendar calendar = Calendar.getInstance();

                            timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                        }

                        timePickerDialog.show();
                    }
                });

                if(layoutTiming.getTag() == null) {
                    layoutTiming.setTag(1);
                    timeLayuout.findViewById(R.id.imgRemove).setVisibility(View.INVISIBLE);
                } else {
                    int counter = (Integer) layoutTiming.getTag();
                    timeLayuout.findViewById(R.id.imgRemove).setVisibility(View.VISIBLE);
                    layoutTiming.setTag(++counter);
                }
                layoutTiming.addView(timeLayuout);
            }
        });

        rootView.findViewById(R.id.btnAddAnotherTiming).performClick();
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
                submitClicked();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }


    private boolean checkforDateTime() {
        int count = (int) layoutTiming.getTag();
        for (int i = 0; i < count; ++i) {
            View parentView = layoutTiming.getChildAt(i);
            if(parentView != null) {
                TextView dateVal = (TextView) parentView.findViewById(R.id.txtDateVal);
                TextView timeVal = (TextView) parentView.findViewById(R.id.txtTimeVal);

                if (dateVal.getText().toString().trim().length() == 0) {
                    dateVal.setError("Please fill event date");
                    dateVal.requestFocus();
                    return true;
                } else if (timeVal.getText().toString().trim().length() == 0) {
                    timeVal.setError("Please fill event time");
                    timeVal.requestFocus();
                    return true;
                }
            }
        }
        return false;
    }

    private String getDateTime() {
        int count = (int) layoutTiming.getTag();
        String dates = "{\"dates\":[";
        for (int i = 0; i < count; ++i) {
            View parentView = layoutTiming.getChildAt(i);
            TextView dateVal = (TextView) parentView.findViewById(R.id.txtDateVal);
            TextView timeVal = (TextView) parentView.findViewById(R.id.txtTimeVal);
            String date_split[] = dateVal.getText().toString().trim().split("-");
            String date = String.format("%s-%s-%s", date_split[2], date_split[1], date_split[0]);
            dates += String.format("{\"sd\":\"%s\",\"st\":\"%s\"}", date , timeVal.getText().toString().trim());
        }
        return dates + "]}";
    }

    private void submitClicked() {
        if (eventTitle.getText().toString().trim().length() == 0) {
            eventTitle.setError("Please fill title of your event");
            eventTitle.requestFocus();
        } else if (eventTitle.getText().toString().trim().length() < 10) {
            eventTitle.setError("Event title must be 10 character long");
            eventTitle.requestFocus();
        } else if (eventDescription.getText().toString().trim().length() == 0) {
            eventDescription.setError("Please fill description of your event");
            eventDescription.requestFocus();
        } else if (eventDescription.getText().toString().trim().length() < 10) {
            eventDescription.setError("Event description must be 10 character long");
            eventDescription.requestFocus();
        } else if (checkforDateTime()) {
            ;
        } else if (rgPaymentType.getCheckedRadioButtonId() == R.id.radioPaid) {
            if (amount.getText().toString().trim().length() == 0) {
                amount.setError("Please fill amount ");
                amount.requestFocus();
            } else if (linkToBuy.getText().toString().trim().length() == 0) {
                linkToBuy.setError("Please fill link to buy");
                linkToBuy.requestFocus();
            }
        } else if (venuename.getText().toString().trim().length() == 0) {
            venuename.setError("Please fill  your venue name");
            venuename.requestFocus();
        } else if (venue.getText().toString().trim().length() == 0) {
            venue.setError("Please fill  your venue's address");
            venue.requestFocus();
        } else if (orgName.getText().toString().trim().length() == 0) {
            orgName.setError("Please fill name of your organisation");
            orgName.requestFocus();
        } else if (orgEmail.getText().toString().trim().length() == 0) {
            orgEmail.setError("Please fill your contact email");
            orgEmail.requestFocus();
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(orgEmail.getText().toString().trim()).matches()) {
            orgEmail.setError("Please fill valid email address");
            orgEmail.requestFocus();
        } else if (contactNumber.getText().toString().trim().length() < 10 && contactNumber.getText().toString().trim().length() > 0) {
            contactNumber.setError("Please fill valid contact number");
            contactNumber.requestFocus();
        } else {
            rootView.findViewById(R.id.btnSubmit).setEnabled(false);
            rootView.findViewById(R.id.btnSubmit).setClickable(false);
            RestClient restClient = new RestClient(RestClient.BASE_URL1);
            Preference preference = new Preference(context);
            String dates = getDateTime();
            restClient.getApiService().postEvents(preference.getLoggedInEmail(),
                    eventTitle.getText().toString().trim(),
                    "Sub1",
                    eventDescription.getText().toString().trim(),
                    amount.getText().toString().trim().length() == 0? "0" : amount.getText().toString().trim(),
                    //"http://google.com",
                    linkToBuy.getText().toString().trim(),
                    "http://yahoo.com/image",
                    orgName.getText().toString().trim(),
                    orgEmail.getText().toString().trim(),
                    "111111111",
                    dates,
                    "http://hotmail.com",
                    venuename.getText().toString().trim(),
                    venue.getText().toString().trim(),
                    submitCallback);
        }
    }

    private Callback<DataModels.Location> submitCallback = new Callback<DataModels.Location>() {
        @Override
        public void success(DataModels.Location homeLocation, Response response2) {

            if (homeLocation.isSuccess()) {
                new ApplicationHelper(context).showMessageDialog(homeLocation.getMessage());
                eventTitle.setText("");
                eventDescription.setText("");
                venue.setText("");
                orgName.setText("");
                orgEmail.setText("");
                contactNumber.setText("");
            } else {
                new ApplicationHelper(context).showMessageDialog(response2.getReason());
            }

            rootView.findViewById(R.id.btnSubmit).setEnabled(true);
            rootView.findViewById(R.id.btnSubmit).setClickable(true);
        }

        @Override
        public void failure(RetrofitError error) {
            if(error.isNetworkError())
                new ApplicationHelper(context).showMessageDialog("There seems to be a connectivity issue. Please check your internet connection.");
            else
                new ApplicationHelper(context).showMessageDialog("Something wrong must have happened, please try again.");
            rootView.findViewById(R.id.btnSubmit).setEnabled(true);
            rootView.findViewById(R.id.btnSubmit).setClickable(true);
        }
    };


    private void uploadFileClicked() {
        Intent intent = new Intent(getActivity(), FileExplore.class);

        startActivityForResult(intent, 108);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 108 && resultCode == -1) {
            TextView uploadFile = (TextView) rootView.findViewById(R.id.uploadFile);
            uploadFile.setText(data.getStringExtra("fileName"));
            uploadFile.setTag(data.getStringExtra("fileName"));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}