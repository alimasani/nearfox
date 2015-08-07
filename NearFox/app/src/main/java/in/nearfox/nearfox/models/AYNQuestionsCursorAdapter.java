package in.nearfox.nearfox.models;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.fragments.AYNFragment1;
import in.nearfox.nearfox.utilities.DBHelper;

/**
 * Created by dell on 12/7/15.
 */
public class AYNQuestionsCursorAdapter extends CursorAdapter {

    LayoutInflater mLayoutInflator;
    Context context;
    ImageLoader mImageLoader;

    int ayn_question_id;
    int content;
    int question_author;
    int period;
    int thumbnail_url;
    int imageURL;
    int user_up_voted;
    int up_voted;
    int user_down_voted;
    int down_voted;
    int answered;
    int answers;
    int shared;
    int shares;
    int deviceHeight;
    int deviceWidth;
    int desiredHeight;


    public AYNQuestionsCursorAdapter(Context context, Cursor c) {
        this(context, c, false);
        this.mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.mImageLoader = ImageLoader.getInstance();

        ayn_question_id = c.getColumnIndex(DBHelper.AYN_QUESTION_ID);
        content = c.getColumnIndex(DBHelper.CONTENT);
        question_author = c.getColumnIndex(DBHelper.QUESTION_AUTHOR);
        period = c.getColumnIndex(DBHelper.PERIOD);
        thumbnail_url = c.getColumnIndex(DBHelper.THUMBNAIL_URL);
        imageURL = c.getColumnIndex(DBHelper.IMAGEURL);
        user_up_voted = c.getColumnIndex(DBHelper.USER_UP_VOTED);
        up_voted = c.getColumnIndex(DBHelper.UP_VOTED);
        user_down_voted = c.getColumnIndex(DBHelper.USER_DOWN_VOTED);
        down_voted = c.getColumnIndex(DBHelper.DOWN_VOTED);
        answered = c.getColumnIndex(DBHelper.ANSWERED);
        answers = c.getColumnIndex(DBHelper.ANSWERS);
        shares = c.getColumnIndex(DBHelper.SHARES);
        shared = c.getColumnIndex(DBHelper.SHARED);

/*        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        deviceWidth = point.x;
        deviceHeight = point.y;
        desiredHeight = (int) (0.45 * deviceHeight);*/
    }

    public AYNQuestionsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public AYNQuestionsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        AYNQuestionViewHolder aynQuestionViewHolder = new AYNQuestionViewHolder();
        View view = mLayoutInflator.inflate(R.layout.ayn_list_row_1, null);

        aynQuestionViewHolder.period = (TextView) view.findViewById(R.id.ayn_period);
        aynQuestionViewHolder.title = (TextView) view.findViewById(R.id.ayn_title);
        aynQuestionViewHolder.userName = (TextView) view.findViewById(R.id.ayn_user_name);
        aynQuestionViewHolder.upVote = (TextView) view.findViewById(R.id.ayn_upvote_count);
        aynQuestionViewHolder.downVote = (TextView) view.findViewById(R.id.ayn_downvote_count);
        aynQuestionViewHolder.comment = (TextView) view.findViewById(R.id.ayn_comment_count);
        aynQuestionViewHolder.id = (TextView) view.findViewById(R.id.ayn_id);
        aynQuestionViewHolder.whetherDownvoted = (TextView) view.findViewById(R.id.ayn_whether_downvoted);
        aynQuestionViewHolder.whetherUpvoted = (TextView) view.findViewById(R.id.ayn_whether_upvoted);
        aynQuestionViewHolder.upVoteImage = (FontAwesomeText) view.findViewById(R.id.ayn_upvote_image);
        aynQuestionViewHolder.downVoteImage = (FontAwesomeText) view.findViewById(R.id.ayn_downvote_image);
        aynQuestionViewHolder.commentImage = (FontAwesomeText) view.findViewById(R.id.ayn_comment_image);
        aynQuestionViewHolder.whetherCommented = (TextView) view.findViewById(R.id.ayn_whether_commented);
        aynQuestionViewHolder.userImage = (ImageView) view.findViewById(R.id.ayn_user_image);
        aynQuestionViewHolder.userImageURL = (TextView) view.findViewById(R.id.ayn_user_image_url);
        aynQuestionViewHolder.shareCount = (TextView)view.findViewById(R.id.ayn_share_count);
        aynQuestionViewHolder.shareImage = (FontAwesomeText)view.findViewById(R.id.ayn_share_image);
        aynQuestionViewHolder.whetherShared = (TextView)view.findViewById(R.id.ayn_whether_shared);

        view.setTag(aynQuestionViewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AYNQuestionViewHolder viewHolder = (AYNQuestionViewHolder) view.getTag();

        mImageLoader.displayImage(cursor.getString(thumbnail_url),
                viewHolder.userImage,
                new DisplayImageOptions
                        .Builder()
                        .displayer(new RoundedBitmapDisplayer(context.getResources().getInteger(R.integer.ayn_rounded_image_corner)))
                        .showImageForEmptyUri(R.drawable.default_person)
                        .showImageOnFail(R.drawable.default_person)
                        .showStubImage(R.drawable.default_person)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .build());

        viewHolder.userImageURL.setText(cursor.getString(thumbnail_url));
//        Log.d(TAG, "ayn adap: " + rowPos.getImageURL()) ;
        viewHolder.period.setText(cursor.getString(period));
        viewHolder.title.setText(cursor.getString(content));
        viewHolder.userName.setText(cursor.getString(question_author));

        if (cursor.getString(up_voted).trim().equals("0"))
            viewHolder.upVote.setText("");
        else
            viewHolder.upVote.setText(cursor.getString(up_voted));

        if (cursor.getString(down_voted).trim().equals("0"))
            viewHolder.downVote.setText("");
        else
            viewHolder.downVote.setText(cursor.getString(down_voted));

        if (cursor.getString(answers).trim().equals("0"))
            viewHolder.comment.setText("");
        else
            viewHolder.comment.setText(cursor.getString(answers));

        viewHolder.id.setText(cursor.getString(ayn_question_id));
        Log.d("Show Ayn", cursor.getString(ayn_question_id));
        if (cursor.getInt(user_up_voted) > 0) {
            viewHolder.whetherUpvoted.setText("TRUE");
//            viewHolderAYN.upVoteImage.setBackgroundColor(context.getResources().getColor(R.color.fbutton_default_color));
            viewHolder.upVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
            viewHolder.whetherUpvoted.setText("FALSE");
            viewHolder.upVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));
        }

        if (cursor.getInt(user_down_voted) > 0) {
            viewHolder.whetherDownvoted.setText("TRUE");
//            viewHolder.downVoteImage.setBackgroundColor(context.getResources().getColor(R.color.fbutton_default_color));
            viewHolder.downVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
            viewHolder.whetherDownvoted.setText("FALSE");
            viewHolder.downVoteImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));
        }

        if (cursor.getInt(answered) > 0) {
//            Log.d(TAG, "is whether commented: " + true) ;
            viewHolder.whetherCommented.setText("TRUE");
            viewHolder.commentImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        } else {
//            Log.d(TAG, "is whether commented: " + false) ;
            viewHolder.whetherCommented.setText("FALSE");
            viewHolder.commentImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));
        }

        if (cursor.getInt(shared) > 0){
            viewHolder.whetherShared.setText("TRUE");
            viewHolder.shareImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        }else{
            viewHolder.whetherShared.setText("FALSE");
            viewHolder.shareImage.setTextColor(context.getResources().getColor(R.color.counter_text_color));
        }

        final Context context1 = context;
        viewHolder.upVoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AYNFragment1.onClickUpVote((LinearLayout) view.getParent(), context1);
            }
        });

        viewHolder.downVoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AYNFragment1.onClickDownVote((LinearLayout) view.getParent(), context1);
            }
        });

        viewHolder.shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ayn shared click
            }
        });
    }

    public class AYNQuestionViewHolder {
        public ImageView userImage;
        public TextView userImageURL;
        public TextView userName;
        public TextView period;
        public TextView title;
        public TextView upVote;
        public TextView downVote;
        public TextView comment;
        public TextView id;
        public TextView whetherUpvoted;
        public TextView whetherDownvoted;
        public TextView whetherCommented;
        public FontAwesomeText upVoteImage;
        public FontAwesomeText downVoteImage;
        public FontAwesomeText commentImage;
        public FontAwesomeText shareImage;
        public TextView whetherShared;
        public TextView shareCount;
    }
}