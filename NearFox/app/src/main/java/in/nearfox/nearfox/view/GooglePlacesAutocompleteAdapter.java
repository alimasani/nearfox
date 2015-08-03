package in.nearfox.nearfox.view;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by R3 on 6/30/2015.
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {

    private ArrayList resultList;
    private static final String LOG_TAG = "Google Places Autocomplete";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

    private static final String OUT_JSON = "/json";
private Context context;

//private static final  String API_KEY="AIzaSyDNYlRkNAeCnZJGr0Cbe9a7Od4KDnaXeQM";
    private static final String API_KEY = "AIzaSyAqzjJw8jtNF_GF-JgovUYV8EAeszHDWx4";


    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {

        super(context, textViewResourceId);
        this.context=context;

    }

  // @Override
//  public View getView(int position, View convertView, ViewGroup parent) {
////       convertView = super.getView(position, convertView, parent);
////       ((TextView) convertView).setWidth(500);
////      return convertView;
//       View view = super.getView(position, convertView, parent);
//
//       TextView textView = (TextView) view.findViewById(android.R.id.text1);
//       textView.setWidth(50);
//
//       return view;
//    }

    @Override

    public int getCount() {

        return resultList.size();

    }

    @Override

    public String getItem(int index) {

        return resultList.get(index).toString();
    }


    @Override

    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override

            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if (constraint != null) {

                    // Retrieve the autocomplete results.

                    resultList = autocomplete(constraint.toString());



                    // Assign the data to the FilterResults

                    filterResults.values = resultList;

                    filterResults.count = resultList.size();

                }

                return filterResults;

            }



            @Override

            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {

                    notifyDataSetChanged();

                } else {

                    notifyDataSetInvalidated();

                }

            }

        };
        return filter;

    }

    public static ArrayList autocomplete(String input) {

        ArrayList resultList = null;



        HttpURLConnection conn = null;

        StringBuilder jsonResults = new StringBuilder();

        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);

            sb.append("?key=" + API_KEY);

            sb.append("&components=country:in");

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));


            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());



            // Load the results into a StringBuilder

            int read;

            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {

                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Error processing Places API URL", e);

            return resultList;

        } catch (IOException e) {

            Log.e(LOG_TAG, "Error connecting to Places API", e);

            return resultList;

        } finally {

            if (conn != null) {

                conn.disconnect();

            }

        }

        try {

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


            // Extract the Place descriptions from the results

            resultList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

                System.out.println("============================================================");

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Cannot process JSON results", e);

        }



        return resultList;

    }


}