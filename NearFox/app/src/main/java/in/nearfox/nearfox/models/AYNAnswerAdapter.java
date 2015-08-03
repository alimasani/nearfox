package in.nearfox.nearfox.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.fragments.AYNFragment1;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tangbang on 6/23/2015.
 */
public class AYNAnswerAdapter extends ArrayAdapter<AYNAnswerObject> implements View.OnClickListener {

    static String TAG = "MyDebug";
    Context context;
    ArrayList<AYNAnswerObject> aynAnswers;
    int layoutResourceId;
    ImageLoader mImageLoader;

    public AYNAnswerAdapter(Context context, int layoutResourceId, ArrayList<AYNAnswerObject> aynAnswers) {
        super(context, layoutResourceId, aynAnswers);
        this.context = context;
        this.aynAnswers = aynAnswers;
        this.layoutResourceId = layoutResourceId;
        mImageLoader = ImageLoader.getInstance();
    }

    public static void upVoteClicked(View view, final Context context, final AYNAnswerAdapter aynAnswerAdapter) {

        String emailId = AYNFragment1.checkUser(context);
        if ( emailId == null) {
            emailId = "" ;
        } else {
            final RelativeLayout rootView = (RelativeLayout) view.getParent().getParent().getParent().getParent().getParent().getParent().getParent();

            final FontAwesomeText upVoteImage = (FontAwesomeText) view;
            upVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
            TextView postIdTextView = (TextView) rootView.findViewById(R.id.ayn_comment_id);
            String answerId = postIdTextView.getText().toString();

            Callback<DataModels.AYNAnswerVoteOrAnswer> cb = new Callback<DataModels.AYNAnswerVoteOrAnswer>() {
                @Override
                public void success(DataModels.AYNAnswerVoteOrAnswer aynPostVoteOrComment, Response response) {
                    boolean success = aynPostVoteOrComment.isSuccess();
                    boolean opposite = aynPostVoteOrComment.isOpposite();
                    String message = aynPostVoteOrComment.getMessage();
                    int upCount, downCount;
                    boolean upVoted, downVoted;

                    if (success) {

//                        setting upVote blue
                        upVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));

//                        increment upvote count
                        TextView upVoteTextView = (TextView) rootView.findViewById(R.id.ayn_comment_upvote_count);

                        if (upVoteTextView.getText().toString().equals(""))
                            upCount = 0;
                        else
                            upCount = Integer.parseInt(upVoteTextView.getText().toString());
                        upCount++;
                        upVoteTextView.setText(String.valueOf(upCount));
                        upVoted = true;
                        downVoted = false;

                        TextView downVoteCountTextView = (TextView) rootView.findViewById(R.id.ayn_comment_downvote_count);
                        downCount = Integer.parseInt(downVoteCountTextView.getText().toString());

                        if (opposite) {
                            // nothing to do
                        } else {

//                            setting downvote black
                            FontAwesomeText downVoteImage = (FontAwesomeText) rootView.findViewById(R.id.ayn_comment_downvote_image);
                            downVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
                            downVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));

//                            decrementing downVote count
                            downCount--;
                            if (downCount == 0)
                                downVoteCountTextView.setText("");
                            else
                                downVoteCountTextView.setText(String.valueOf(downCount));
                        }

                        persistchanges(upCount, downCount, upVoted, downVoted, rootView, aynAnswerAdapter);
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(context, "Unable to connect to server, try later", Toast.LENGTH_LONG).show();
                }
            };

            RestClient restClient = new RestClient();


            /**
             * hardcoding email
             */
//            emailId = "nikunj.sharma9299@gmail.com";

            restClient.getApiService1().answerVoteUp(emailId, answerId, cb);
        }
    }

    public static void downVoteClicked(View view, final Context context, final AYNAnswerAdapter aynAnswerAdapter) {

        String emailId = AYNFragment1.checkUser(context) ;
        if ( emailId == null) {
            emailId = "";
        } else {
            final RelativeLayout rootView = (RelativeLayout) view.getParent().getParent().getParent().getParent().getParent().getParent().getParent();

            final FontAwesomeText downVoteImage = (FontAwesomeText) view;
            downVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
            TextView postIdTextView = (TextView) rootView.findViewById(R.id.ayn_comment_id);
            String answerId = postIdTextView.getText().toString();

            Callback<DataModels.AYNAnswerVoteOrAnswer> cb = new Callback<DataModels.AYNAnswerVoteOrAnswer>() {
                @Override
                public void success(DataModels.AYNAnswerVoteOrAnswer aynPostVoteOrComment, Response response) {
                    boolean success = aynPostVoteOrComment.isSuccess();
                    boolean opposite = aynPostVoteOrComment.isOpposite();
                    String message = aynPostVoteOrComment.getMessage();

                    int upCount, downCount;
                    boolean upVoted, downVoted;

                    if (success) {

//                        setting upVote blue
                        downVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));

//                        increment upvote count
                        TextView downVoteTextView = (TextView) rootView.findViewById(R.id.ayn_comment_downvote_count);

                        if (downVoteTextView.getText().toString().equals(""))
                            downCount = 0;
                        else
                            downCount = Integer.parseInt(downVoteTextView.getText().toString());
                        downCount++;
                        downVoteTextView.setText(String.valueOf(downCount));

                        downVoted = true;
                        upVoted = false;

                        TextView upVoteCountTextView = (TextView) rootView.findViewById(R.id.ayn_comment_upvote_count);
                        upCount = Integer.parseInt(upVoteCountTextView.getText().toString());

                        if (opposite) {
                            // nothing to do
                        } else {

//                            setting upvote black
                            FontAwesomeText upVoteImage = (FontAwesomeText) rootView.findViewById(R.id.ayn_comment_upvote_image);
                            upVoteImage.startFlashing(context, false, FontAwesomeText.AnimationSpeed.FAST);
                            upVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));

//                            decrementing upvote count
                            upCount--;

                            if (upCount == 0)
                                upVoteCountTextView.setText("");
                            else
                                upVoteCountTextView.setText(String.valueOf(upCount));
                        }

                        persistchanges(upCount, downCount, upVoted, downVoted, rootView, aynAnswerAdapter);
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(context, "Unable to connect to server, try later.", Toast.LENGTH_LONG).show();
                }
            };

            RestClient restClient = new RestClient();

            /**
             * hardcoding email
             */
//            emailId = "nikunj.sharma9299@gmail.com";
            restClient.getApiService1().answerVoteDown(emailId, answerId, cb);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AYNAnswerViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
        convertView = mInflater.inflate(layoutResourceId, parent, false);
        Log.d(TAG, "in adapter");
        holder = new AYNAnswerViewHolder();

        holder.title = (TextView) convertView.findViewById(R.id.ayn_comment_title);
        holder.userImage = (ImageView) convertView.findViewById(R.id.ayn_comment_user_image);
        holder.commentId = (TextView) convertView.findViewById(R.id.ayn_comment_id);
        holder.period = (TextView) convertView.findViewById(R.id.ayn_comment_period);
        holder.downVoteCount = (TextView) convertView.findViewById(R.id.ayn_comment_downvote_count);
        holder.whetherDownVoted = (TextView) convertView.findViewById(R.id.ayn_comment_whether_downvoted);
        holder.upVoteCount = (TextView) convertView.findViewById(R.id.ayn_comment_upvote_count);
        holder.whetherUpvoted = (TextView) convertView.findViewById(R.id.ayn_comment_whether_upvoted);
        holder.userImageUrl = (TextView) convertView.findViewById(R.id.ayn_comment_user_image_url);
        holder.userName = (TextView) convertView.findViewById(R.id.ayn_comment_user_name);
        holder.upVoteImage = (FontAwesomeText) convertView.findViewById(R.id.ayn_comment_upvote_image);
        holder.downVoteImage = (FontAwesomeText) convertView.findViewById(R.id.ayn_comment_downvote_image);

        convertView.setTag(holder);
//        } else {
//            holder = (AYNAnswerViewHolder) convertView.getTag();
//        }

        AYNAnswerObject row_pos = aynAnswers.get(position);

        /*if (row_pos.getUser_image_url() != null && !row_pos.getUser_image_url().isEmpty()) {
            *//*Picasso.with(context)
                    .load(row_pos.getUser_image_url())
                    .placeholder(R.drawable.default_person)
                    .into(holder.userImage);*//*
            mImageLoader.displayImage(row_pos.getUser_image_url(), holder.userImage);
        }
        else {
            mImageLoader.displayImage("drawable://" + R.drawable.default_person , holder.userImage);
        }*/

        mImageLoader.displayImage(row_pos.getUser_image_url(),
                holder.userImage,
                new DisplayImageOptions
                        .Builder()
                        .displayer(new RoundedBitmapDisplayer(context.getResources().getInteger(R.integer.ayn_rounded_image_corner)))
                        .showImageForEmptyUri(R.drawable.default_person)
                        .showStubImage(R.drawable.default_person)
                        .showImageOnFail(R.drawable.default_person)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .build());

        holder.title.setText(row_pos.getComment_content());
        holder.commentId.setText(row_pos.getId());
        holder.period.setText(row_pos.getPeriod());

        if (row_pos.getDownvote_count().trim().equals("0"))
            holder.downVoteCount.setText("");
        else
            holder.downVoteCount.setText(row_pos.getDownvote_count());

        if (row_pos.getUpvote_count().trim().equals("0"))
            holder.upVoteCount.setText("");
        else
            holder.upVoteCount.setText(row_pos.getUpvote_count());

        holder.userImageUrl.setText(row_pos.getUser_image_url());
        holder.userName.setText(row_pos.getUsername());
        Log.d(TAG, "down ayn answer: " + row_pos.isWhether_downvoted());

        if (row_pos.isWhether_downvoted()) {
            holder.whetherDownVoted.setText("TRUE");
            holder.downVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
            Log.d(TAG, "in if");
        } else {
            holder.whetherDownVoted.setText("FALSE");
            holder.downVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));
            Log.d(TAG, "in else");
        }

        if (row_pos.isWhether_upvoted()) {
            holder.whetherUpvoted.setText("TRUE");
            holder.upVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
            holder.whetherUpvoted.setText("FALSE");
            holder.upVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));
        }

        holder.downVoteImage.setOnClickListener(this);
        holder.upVoteImage.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ayn_comment_upvote_image) {
            Log.d(TAG, "upVote clicked");
            upVoteClicked(v, context, this);
        } else if (id == R.id.ayn_comment_downvote_image) {
            Log.d(TAG, "downVote clicked");
            downVoteClicked(v, context, this);
        }
    }

    class AYNAnswerViewHolder {
        public ImageView userImage;
        public TextView userName;
        public TextView period;
        public TextView title;
        public TextView upVoteCount;
        public TextView downVoteCount;
        public TextView commentId;
        public TextView whetherUpvoted;
        public TextView whetherDownVoted;
        public TextView userImageUrl;
        public FontAwesomeText upVoteImage;
        public FontAwesomeText downVoteImage;
    }

    private static void persistchanges(int upCount, int downCount, boolean upVoted, boolean downVoted, RelativeLayout rootView, AYNAnswerAdapter aynAnswerAdapter){
        ListView listView = (ListView)rootView.getParent();
        int position = listView.getPositionForView(rootView);
        aynAnswerAdapter.aynAnswers.get(position-1).setUpvote_count(String.valueOf(upCount));
        aynAnswerAdapter.aynAnswers.get(position-1).setDownvote_count(String.valueOf(downCount));
        aynAnswerAdapter.aynAnswers.get(position-1).setWhether_upvoted(upVoted);
        aynAnswerAdapter.aynAnswers.get(position-1).setWhether_downvoted(downVoted);

        aynAnswerAdapter.notifyDataSetChanged();
    }
}