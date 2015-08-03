package in.nearfox.nearfox.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.nearfox.nearfox.R;

/**
 * Created by Uss on 7/27/2015.
 */
public class PreviousLocationAdapter extends BaseAdapter {
    private ArrayList<String> arrayList;
    private Context context;

    public PreviousLocationAdapter(Context context, ArrayList<String> list){
        arrayList = new ArrayList<>(list);
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public String getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.previou_location_list_item, null);
        TextView preLocation = (TextView) view.findViewById(R.id.txtPreviousLocations);
        preLocation.setText(getItem(i));
        return view;
    }
}
