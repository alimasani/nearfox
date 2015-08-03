package in.nearfox.nearfox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import in.nearfox.nearfox.fragments.AYNFragment1;
import in.nearfox.nearfox.fragments.EventFragment;
import in.nearfox.nearfox.models.AYNAnswerAdapter;
import in.nearfox.nearfox.models.AYNAnswerObject;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.DBManager;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SingleAynQuestion extends AppCompatActivity implements ObservableScrollViewCallbacks, View.OnClickListener {

    //    Meta variables for getting the post details from intent
    public final static String ID = "id";
    public final static String TITLE = "title";
    public final static String USERNAME = "userName";
    public final static String PERIOD = "period";
    public final static String USERIMAGEURL = "userImageUrl";
    public final static String IMAGEURL = "imageURL";
    public final static String UPVOTED = "upVoted";
    public final static String UPVOTECOUNT = "upVoteCount";
    public final static String DOWNVOTED = "downVoted";
    public final static String DOWNVOTEDCOUNT = "downVoteCount";
    public final static String COMMENTED = "commented";
    public final static String COMMENTCOUNT = "commentCount";

    public final static String SHARECOUNT = "shareCount";
    public final static String SHARED = "shared";

    ArrayList<AYNAnswerObject> ayNcomments;
    AYNAnswerAdapter adapter;
    ObservableListView listView;
    Context context;
    ImageLoader mImageLoader;
    private View postContainer;

    //    private View mOverlayView;
    private View mListBackgroundView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;
    private ImageView commentSend;
    private FontAwesomeText upVoteImage;
    private FontAwesomeText downVoteImage;
    private FontAwesomeText commentImage;
    private String TAG = this.getClass().getName();

    //    varibles for storing post details as fetched from the intent
    private String id;
    private String title;
    private String userName;
    private String period;
    private String userImageUrl;
    private String imageURL;
    private String upVoted;
    private String upVoteCount;
    private String downVoted;
    private String downVoteCount;
    private String commented;
    private String commentCount;
    private String shareCount;
    private String shared;

    private ProgressDialog progressDialog;
    private DBManager dbManager;

    public SingleAynQuestion() {
        super();
        mImageLoader = ImageLoader.getInstance();
    }

    private static void changeColor(FontAwesomeText view, Context context, int color) {
        view.setTextColor(context.getResources().getColor(color));
    }

    private static void changeCount(TextView view, int step) {
        String count = view.getText().toString();
        view.setText(String.valueOf(Integer.parseInt(count) + step));
    }

    private static void incrementCountInDB(DBManager dbManager, String id) {
        dbManager.incrementAnswerCount(id);
    }

    public static void persistChanges(View view, int upCount, int downCount, boolean whetherUpCountChanged, boolean whetherDownCountCanged, boolean whetherUpvoted, boolean whetherDownVoted, Context context) {

        String questionId = ((TextView) (view
                .findViewById(R.id.ayn_post_id)))
                .getText()
                .toString();

        DBManager dbManager = new DBManager(context);
        dbManager.updateAYNQuestionUpvoteDownvote(questionId, upCount, downCount, whetherUpvoted, whetherDownVoted);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ayn_question);
        progressDialog = new ProgressDialog(this);
        dbManager = new DBManager(this);

        getSupportActionBar().hide();

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.comment_height);
        mActionBarSize = getActionBarSize();
        postContainer = findViewById(R.id.ayn_single_post_body);
//        mOverlayView = findViewById(R.id.overlay);
//        commentSend = (FontAwesomeText) findViewById(R.id.ayn_comment_send_image);
        commentSend = (ImageView) findViewById(R.id.ayn_comment_send_image);
        upVoteImage = (FontAwesomeText) findViewById(R.id.ayn_upvote_image);
        downVoteImage = (FontAwesomeText) findViewById(R.id.ayn_downvote_image);
        commentImage = (FontAwesomeText) findViewById(R.id.ayn_comment_image);
        getPostDetails();
        displayMainContent();

        context = this;
        listView = (ObservableListView) findViewById(R.id.list);
        listView.setScrollViewCallbacks(this);

        populateAnswersInList(listView);

        final RelativeLayout mainQuestionBody = (RelativeLayout) findViewById(R.id.ayn_single_post_body);
        mainQuestionBody.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mainQuestionBody.getHeight();
                View paddingView = new View(context);
                Log.d(TAG, "heght: " + height);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
                paddingView.setLayoutParams(lp);
                paddingView.setClickable(true);

                listView.addHeaderView(paddingView);
//                mainQuestionBody.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mainQuestionBody.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        setTitle(null);
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
//        mListBackgroundView = findViewById(R.id.list_background);

        setTitle();

        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "clicked comment send");
                pushAnswer(v, listView, dbManager);
            }
        });

/*        upVoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postVoteUp(v);
            }
        });
        downVoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDownVote(v);
            }
        });*/

        setClickListeners();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight;
        int minOverlayTransitionY = 0 - (int) (getResources().getDimension(R.dimen.comment_height));
//        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0)) ;
        ViewHelper.setTranslationY(postContainer, ScrollUtils.getFloat(-(float) (scrollY / 2), minOverlayTransitionY, 0));
//        ViewHelper.setTranslationY(listView, ScrollUtils.getFloat(-scrollY , minOverlayTransitionY, 0)) ;

        // Translate list background
//        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight)) ;

        // Change alpha of overlay
//        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1)) ;
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public void populateAnswersInList(final ListView listView) {
        ayNcomments = new ArrayList<>();

        Callback<DataModels.AYNAnswers> cb = new Callback<DataModels.AYNAnswers>() {
            @Override
            public void success(DataModels.AYNAnswers aynAnswers, Response response) {

                ArrayList<DataModels.AYNAnswer> aynAnswersList = aynAnswers.getAnswers();

                boolean success = aynAnswers.isSuccess();
                boolean changeStatus = aynAnswers.isChange_status();
                String message = aynAnswers.getMessage();
                String currentTimeStamp = aynAnswers.getCurrent_time_stamp();

                if (success) {
                    for (int i = 0; i < aynAnswersList.size(); i++) {

                        ayNcomments.add(new AYNAnswerObject(aynAnswersList.get(i).getAyn_answer_id(),
                                aynAnswersList.get(i).getFirstname(),
                                aynAnswersList.get(i).getThumbnail_url(),
                                aynAnswersList.get(i).getContent(),
                                aynAnswersList.get(i).isUser_up_voted(),
                                aynAnswersList.get(i).getUp_voted(),
                                aynAnswersList.get(i).isUser_down_voted(),
                                aynAnswersList.get(i).getDown_voted(),
                                aynAnswersList.get(i).getPeriod()));
                    }

                    adapter = new AYNAnswerAdapter(context, R.layout.single_answer, ayNcomments);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(SingleAynQuestion.this, message, Toast.LENGTH_LONG).show();
                }

                EventFragment.stopProgressDialog(progressDialog);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "error: " + retrofitError);
                EventFragment.stopProgressDialog(progressDialog);
            }
        };


        Log.d(TAG, "starting progress bar " + progressDialog.toString());
        EventFragment.startProgressDialog(progressDialog);

        String emailId = AYNFragment1.onlyCheckUser(context);


        RestClient restClient = new RestClient();
        if (emailId == null) {
            restClient.getApiService1().getAYNAnswersNotSignedIn("", this.id, getLastTimeStamp(), cb);
        } else {

            /**
             * hardcoding email
             */
//            emailId = "nikunj.sharma9299@gmail.com";
            restClient.getApiService1().getAYNAnswers(emailId, this.id, getLastTimeStamp(), cb);
        }
    }

    private void getPostDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(ID))
                this.id = intent.getStringExtra(ID);
            if (intent.hasExtra(TITLE))
                this.title = intent.getStringExtra(TITLE);
            if (intent.hasExtra(USERNAME))
                this.userName = intent.getStringExtra(USERNAME);
            if (intent.hasExtra(PERIOD))
                this.period = intent.getStringExtra(PERIOD);
            if (intent.hasExtra(USERIMAGEURL))
                this.userImageUrl = intent.getStringExtra(USERIMAGEURL);
            if (intent.hasExtra(IMAGEURL))
                this.imageURL = intent.getStringExtra(IMAGEURL);
            if (intent.hasExtra(UPVOTED))
                this.upVoted = intent.getStringExtra(UPVOTED);
            if (intent.hasExtra(UPVOTECOUNT))
                this.upVoteCount = intent.getStringExtra(UPVOTECOUNT);
            if (intent.hasExtra(DOWNVOTED))
                this.downVoted = intent.getStringExtra(DOWNVOTED);
            if (intent.hasExtra(DOWNVOTEDCOUNT))
                this.downVoteCount = intent.getStringExtra(DOWNVOTEDCOUNT);
            if (intent.hasExtra(COMMENTED))
                this.commented = intent.getStringExtra(COMMENTED);
            if (intent.hasExtra(COMMENTCOUNT))
                this.commentCount = intent.getStringExtra(COMMENTCOUNT);
            if (intent.hasExtra(SHARECOUNT))
                this.shareCount = intent.getStringExtra(SHARECOUNT);
            if (intent.hasExtra(SHARED))
                this.shared = intent.getStringExtra(SHARED);

//            Log.d(TAG, "ayn post1: " + intent.getStringExtra(USERIMAGEURL)) ;
        }
    }

    private void setTitle() {
//        setTitle("Post");
    }

    public void displayMainContent() {

        TextView id = (TextView) findViewById(R.id.ayn_post_id);
        TextView title = (TextView) findViewById(R.id.ayn_title);
        TextView userName = (TextView) findViewById(R.id.ayn_user_name);
        TextView period = (TextView) findViewById(R.id.ayn_period);
        TextView userImageUrl = (TextView) findViewById(R.id.ayn_user_image_url);
        TextView imageURL = (TextView) findViewById(R.id.ayn_image_url);
        TextView upVoted = (TextView) findViewById(R.id.ayn_whether_upvoted);
        TextView upVoteCount = (TextView) findViewById(R.id.ayn_upvote_count);
        TextView downVoted = (TextView) findViewById(R.id.ayn_whether_downvoted);
        TextView downVoteCount = (TextView) findViewById(R.id.ayn_downvote_count);
        TextView commented = (TextView) findViewById(R.id.ayn_whether_commented);
        TextView commentCount = (TextView) findViewById(R.id.ayn_comment_count);
        ImageView userImage = (ImageView) findViewById(R.id.ayn_user_image);

        mImageLoader.displayImage(this.userImageUrl,
                userImage,
                new DisplayImageOptions
                        .Builder()
                        .displayer(new RoundedBitmapDisplayer(40))
                        .showImageForEmptyUri(R.drawable.default_person)
                        .showImageOnFail(R.drawable.default_person)
                        .showStubImage(R.drawable.default_person)
                        .build());

//        populating text fields
        id.setText(this.id);
        title.setText(this.title);
        userName.setText(this.userName);
        period.setText(this.period);
        userImageUrl.setText(this.userImageUrl);
        imageURL.setText(this.imageURL);
        upVoted.setText(this.upVoted);

        if (this.upVoteCount.trim().equals("0"))
            upVoteCount.setText("");
        else
            upVoteCount.setText(this.upVoteCount);

        downVoted.setText(this.downVoted);

        if (this.downVoteCount.trim().equals("0"))
            downVoteCount.setText("");
        else
            downVoteCount.setText(this.downVoteCount);

        commented.setText(this.commented);

        if (this.commentCount.trim().equals("0"))
            commentCount.setText("");
        else
            commentCount.setText(this.commentCount);

        if (this.upVoted.toUpperCase().equals("TRUE")) {
            upVoteImage.setTextColor(getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
            upVoteImage.setTextColor(getResources().getColor(R.color.counter_text_color));
        }

        if (this.downVoted.toUpperCase().equals("TRUE")) {
            downVoteImage.setTextColor(getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
            downVoteImage.setTextColor(getResources().getColor(R.color.counter_text_color));
        }

        if (this.commented.toUpperCase().equals("TRUE")) {
            commentImage.setTextColor(getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
            commentImage.setTextColor(getResources().getColor(R.color.counter_text_color));
        }
    }

    private void pushAnswer(View view, final ListView listView, final DBManager dbManager) {
        String emailId = AYNFragment1.checkUser(context);
        if (emailId != null) {
            //        ImageView commentSendButton = (ImageView) view;
            final EditText commentBody = (EditText) findViewById(R.id.ayn_comment_body);
            final String editBody = commentBody.getText().toString();
            //        Log.d(TAG, "wrriten: "+ editBody ) ;

            if (editBody.isEmpty() || editBody.equals(getResources().getString(R.string.default_comment_text)) || editBody.equals("")) {
                Toast.makeText(this, "Please write something else!!", Toast.LENGTH_LONG).show();
            } else {

                Callback<DataModels.AYNPushAnswer> cb = new Callback<DataModels.AYNPushAnswer>() {
                    @Override
                    public void success(DataModels.AYNPushAnswer aynPushComment, Response response) {
                        boolean success = aynPushComment.isSuccess();

                        if (success) {
                            String answerId = aynPushComment.getAnswer_id();
                            String name = MainActivity.getUserName(SingleAynQuestion.this);
                            ayNcomments.add(0, new AYNAnswerObject(answerId, name, userImageUrl, editBody, false, "0", false, "0", "just now"));
                            adapter.notifyDataSetChanged();
                            Answered(dbManager);
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    listView.smoothScrollToPosition(0);
                                }
                            });
                        } else {
                            String message = aynPushComment.getMessage();
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(context, "Unable to connet to server,try later", Toast.LENGTH_LONG).show();
                    }
                };

                RestClient restClient = new RestClient();

                /**
                 * hardcoding email
                 */
//                emailId = "nikunj.sharma9299@gmail.com";
                restClient.getApiService1().pushAnswer(emailId, this.id, editBody, cb);

                commentBody.clearFocus();
                hideKeyboard();
                commentBody.setText("");
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void setClickListeners() {
        FontAwesomeText upVoteTextView = (FontAwesomeText) findViewById(R.id.ayn_upvote_image);
        FontAwesomeText downVoteTextView = (FontAwesomeText) findViewById(R.id.ayn_downvote_image);
        EditText answerEditText = (EditText) findViewById(R.id.ayn_comment_body);

        upVoteTextView.setOnClickListener(this);
        downVoteTextView.setOnClickListener(this);
        answerEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ayn_upvote_image: {
                upVoteQuestionOnClick(view);
                break;
            }

            case R.id.ayn_downvote_image: {
                downVoteQuestionOnClick(view);
                break;
            }

            case R.id.ayn_comment_body: {
                AYNFragment1.checkUser(this);
            }

            default: {
                break;
            }
        }
    }

    public void upVoteQuestionOnClick(View view) {

        String emailId = AYNFragment1.checkUser(this);
        if (emailId == null) {
            emailId = "";
        } else {
            final RelativeLayout rootView = (RelativeLayout) view.getParent().getParent().getParent().getParent();

            final FontAwesomeText upVoteImage = (FontAwesomeText) view;
            upVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
            TextView postIdTextView = (TextView) rootView.findViewById(R.id.ayn_post_id);
            String questionId = postIdTextView.getText().toString();

            Callback<DataModels.AYNAnswerVoteOrAnswer> cb = new Callback<DataModels.AYNAnswerVoteOrAnswer>() {
                @Override
                public void success(DataModels.AYNAnswerVoteOrAnswer aynPostVoteOrComment, Response response) {
                    boolean success = aynPostVoteOrComment.isSuccess();
                    boolean opposite = aynPostVoteOrComment.isOpposite();
                    String message = aynPostVoteOrComment.getMessage();

                    int upCount, downCount = 0;
                    boolean whetherUpvoted, whetherDownvoted;
                    boolean whetherUpCountChanged, whetherDownCountChanged;

                    if (success) {

//                        setting upVote blue
                        upVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));

//                        increment upvote count
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
                            whetherDownCountChanged = false;
                        } else {
//                            setting dR.color.extreme_light_grey
                            FontAwesomeText downVoteImage = (FontAwesomeText) rootView.findViewById(R.id.ayn_downvote_image);
                            downVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
                            downVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));

//                             decrementing downVote count
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

    public void downVoteQuestionOnClick(View view) {

        String emailId = AYNFragment1.checkUser(this);
        if (emailId == null) {
            emailId = "";
        } else {
            final RelativeLayout rootView = (RelativeLayout) view.getParent().getParent().getParent().getParent();

            final FontAwesomeText downVoteImage = (FontAwesomeText) view;
            downVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
            TextView postIdTextView = (TextView) rootView.findViewById(R.id.ayn_post_id);
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
                            whetherUpCountChanged = false;
                        } else {
//                            settingR.color.extreme_light_grey
                            FontAwesomeText upVoteImage = (FontAwesomeText) rootView.findViewById(R.id.ayn_upvote_image);
                            upVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
                            upVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));

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

    public void upVoteContainerOnClick(View view) {
        FontAwesomeText upVoteText = (FontAwesomeText) view.findViewById(R.id.ayn_upvote_image);
        upVoteQuestionOnClick(upVoteText);
    }

    public void downVoteContainerOnClick(View view) {
        FontAwesomeText downVoteText = (FontAwesomeText) view.findViewById(R.id.ayn_downvote_image);
        downVoteQuestionOnClick(downVoteText);
    }

    public void up_vote_container_clicked(View view) {
        FontAwesomeText upVoteText = (FontAwesomeText) view.findViewById(R.id.ayn_comment_upvote_image);
        AYNAnswerAdapter.upVoteClicked(upVoteText, context, adapter);
    }

    public void down_vote_container_clicked(View view) {
        FontAwesomeText downVoteText = (FontAwesomeText) view.findViewById(R.id.ayn_comment_downvote_image);
        AYNAnswerAdapter.downVoteClicked(downVoteText, context, adapter);
    }

    private void Answered(DBManager dbManager) {
        FontAwesomeText answerImage = (FontAwesomeText) findViewById(R.id.ayn_comment_image);
        TextView answerCountTextView = (TextView) findViewById(R.id.ayn_comment_count);

        answerImage.startFlashing(this, false, FontAwesomeText.AnimationSpeed.FAST);
        changeColor(answerImage, this, R.color.fbutton_default_shadow_color);
        changeCount(answerCountTextView, 1);

        incrementCountInDB(dbManager, this.id);
    }

    public String getLastTimeStamp() {
        /*try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            return sharedPreferences.getString(getString(R.string.CURRENT_TIME_STAMP_AYNQUESTIONS), "0.0");
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }*/
        return "0.0";
    }

    public void setCurrentTimeStamp(String currentTimeStamp) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.CURRENT_TIME_STAMP_AYNQUESTIONS), currentTimeStamp);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }
}