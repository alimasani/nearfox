package in.nearfox.nearfox.models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.fragments.AYNFragment;

/**
 * Created by tangbang on 6/19/2015.
 */
public class RecentAYNQuestionAdapter extends ArrayAdapter<AYNObject> {

    public static final String TAG= "MyDebug" ;
    Context context ;
    int layoutResource;
    ArrayList<AYNObject> aynObjects ;
    ImageLoader mImageLoader ;

    public RecentAYNQuestionAdapter(Context context, int layoutResource, ArrayList<AYNObject> aynObjects){
        super(context, layoutResource, aynObjects) ;

        this.context = context ;
        this.layoutResource = layoutResource ;
        this.aynObjects = aynObjects ;
        mImageLoader = ImageLoader.getInstance() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        AYNAnswerViewHolder viewHolderAYN = null ;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) ;

//        if (convertView == null){

            convertView = inflater.inflate(layoutResource, null) ;
            viewHolderAYN = new AYNAnswerViewHolder() ;

            viewHolderAYN.period = (TextView)convertView.findViewById(R.id.ayn_period) ;
            viewHolderAYN.title = (TextView)convertView.findViewById(R.id.ayn_title) ;
            viewHolderAYN.userName = (TextView)convertView.findViewById(R.id.ayn_user_name) ;
            viewHolderAYN.upVote = (TextView)convertView.findViewById(R.id.ayn_upvote_count) ;
            viewHolderAYN.downVote = (TextView)convertView.findViewById(R.id.ayn_downvote_count) ;
            viewHolderAYN.comment = (TextView)convertView.findViewById(R.id.ayn_comment_count) ;
            viewHolderAYN.id = (TextView)convertView.findViewById(R.id.ayn_id) ;
            viewHolderAYN.whetherDownvoted = (TextView)convertView.findViewById(R.id.ayn_whether_downvoted) ;
            viewHolderAYN.whetherUpvoted = (TextView)convertView.findViewById(R.id.ayn_whether_upvoted) ;
            viewHolderAYN.upVoteImage = (FontAwesomeText)convertView.findViewById(R.id.ayn_upvote_image) ;
            viewHolderAYN.downVoteImage = (FontAwesomeText)convertView.findViewById(R.id.ayn_downvote_image) ;
            viewHolderAYN.commentImage = (FontAwesomeText)convertView.findViewById(R.id.ayn_comment_image) ;
            viewHolderAYN.whetherCommented = (TextView)convertView.findViewById(R.id.ayn_whether_commented) ;
            viewHolderAYN.userImage = (ImageView)convertView.findViewById(R.id.ayn_user_image) ;
            viewHolderAYN.userImageURL = (TextView)convertView.findViewById(R.id.ayn_user_image_url) ;

            ((LinearLayout)viewHolderAYN.upVote.getParent()).findViewById(R.id.ayn_upvote_image).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
//                    AYNFragment.onClickUpVote(view, context);
                }
            });

            ((LinearLayout)viewHolderAYN.downVote.getParent()).findViewById(R.id.ayn_downvote_image).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
//                    AYNFragment.onClickDownVote(view, context);
                }
            });

//            convertView.setTag(viewHolderAYN);
/*        }else{
            viewHolderAYN = (AYNAnswerViewHolder)convertView.getTag() ;
        }*/

        AYNObject rowPos = aynObjects.get(position) ;

        if (rowPos.getUserImageURL() != null && !rowPos.getUserImageURL().equals("")) {
            mImageLoader.displayImage(rowPos.getUserImageURL(),
                    viewHolderAYN.userImage,
                    new DisplayImageOptions
                            .Builder()
                            .showImageForEmptyUri(R.drawable.default_person)
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showImageOnFail(R.drawable.default_person)
                            .showStubImage(R.drawable.default_person)
                            .build());
        }
        else {
            mImageLoader.displayImage("drawable://" + R.drawable.default_person, viewHolderAYN.userImage);
        }

        viewHolderAYN.userImageURL.setText(rowPos.getUserImageURL()) ;
//        Log.d(TAG, "ayn adap: " + rowPos.getImageURL()) ;
        viewHolderAYN.period.setText(rowPos.getPeriod()) ;
        viewHolderAYN.title.setText(rowPos.getTitle());
        viewHolderAYN.userName.setText(rowPos.getUserName());
        viewHolderAYN.upVote.setText(rowPos.getUpVoteCount());
        viewHolderAYN.downVote.setText(rowPos.getDownVoteCount());
        viewHolderAYN.comment.setText(rowPos.getCommentCount());
        viewHolderAYN.id.setText(rowPos.getId());

        if (rowPos.isWhetherUpvoted()){
            viewHolderAYN.whetherUpvoted.setText("TRUE");
//            viewHolderAYN.upVoteImage.setBackgroundColor(context.getResources().getColor(R.color.fbutton_default_color));
            viewHolderAYN.upVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        }else{
            viewHolderAYN.whetherUpvoted.setText("FALSE");
            viewHolderAYN.whetherUpvoted.setTextColor(context.getResources().getColor(R.color.extreme_light_grey));
        }

        if (rowPos.isWhetherDownvoted()){
            viewHolderAYN.whetherDownvoted.setText("TRUE");
//            viewHolderAYN.downVoteImage.setBackgroundColor(context.getResources().getColor(R.color.fbutton_default_color));
            viewHolderAYN.downVoteImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        }else{
            viewHolderAYN.whetherDownvoted.setText("FALSE");
            viewHolderAYN.whetherDownvoted.setTextColor(context.getResources().getColor(R.color.extreme_light_grey)) ;
        }

        if (rowPos.isWhetherCommented()){
//            Log.d(TAG, "is whether commented: " + true) ;
            viewHolderAYN.whetherCommented.setText("TRUE");
            viewHolderAYN.commentImage.setTextColor(context.getResources().getColor(R.color.fbutton_default_shadow_color));
        }else{
//            Log.d(TAG, "is whether commented: " + false) ;
            viewHolderAYN.whetherCommented.setText("FALSE");
            viewHolderAYN.commentImage.setTextColor(context.getResources().getColor(R.color.extreme_light_grey));
        }

        viewHolderAYN.upVoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AYNFragment.onClickUpVote((LinearLayout) view.getParent(), context);
            }
        });

        viewHolderAYN.downVoteImage.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view){
//                AYNFragment.onClickDownVote((LinearLayout)view.getParent(), context);
            }
        });

        return convertView ;
    }

    public class AYNAnswerViewHolder {
        public ImageView userImage ;
        public TextView userImageURL ;
        public TextView userName ;
        public TextView period ;
        public TextView title ;
        public TextView upVote ;
        public TextView downVote ;
        public TextView comment ;
        public TextView id ;
        public TextView whetherUpvoted ;
        public TextView whetherDownvoted ;
        public TextView whetherCommented ;
        public FontAwesomeText upVoteImage ;
        public FontAwesomeText downVoteImage ;
        public FontAwesomeText commentImage ;
    }
}