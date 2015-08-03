package in.nearfox.nearfox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import in.nearfox.nearfox.R;


public class PlaceAutoComplete extends AutoCompleteTextView {

    private Context context;
    public boolean click=false;

    public PlaceAutoComplete(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.list_item));
    }

    public PlaceAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.list_item));
    }

    public PlaceAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
        setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.list_item));
    }




//    @Override
//    protected CharSequence convertSelectionToString(Object selectedItem) {
//        /**
//         * Each item in the autocompetetextview suggestion list is a hashmap
//         * object
//         */
//        @SuppressWarnings("unchecked")
//        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
//        return hm.get("description");
//    }
//
//    private TextWatcher watcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before,
//                                  int count) {
//            PlacesTask placesTask = new PlacesTask();
//            placesTask.execute(s.toString());
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            // TODO Auto-generated method stub
//
//        }
//    };
//
//    // Fetches all places from GooglePlaces AutoComplete Web Service
//    private class PlacesTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... place) {
//            // For storing data from web service
//            String data = "";
//
//            // Obtain browser key from https://code.google.com/apis/console
//            String key = "key=AIzaSyCuVHb7tK6Bn65oXR0AdXqK0i-2LxEObM0";
//          //  String key = "key=AIzaSyDNYlRkNAeCnZJGr0Cbe9a7Od4KDnaXeQM";
//
//            String input = "";
//
//            try {
//                input = "input=" + URLEncoder.encode(place[0], "utf-8");
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            }
//
//            // place type to be searched
//            String types = "types=geocode";
//
//            // Search within given country
//            String location = "components=country:us";
//
//            // Sensor enabled
//            String sensor = "sensor=false";
//
//            // Building the parameters to the web service
//            String parameters = input + "&" + types + "&" + location + "&" + sensor + "&" + key;
//
//            // Output format
//            String output = "json";
//
//            // Building the url to the web service
//            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
//                    + output + "?" + parameters;
//
//            try {
//                // Fetching the data from we service
//                data = downloadUrl(url);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        /**
//         * A method to download json data from url
//         */
//        private String downloadUrl(String strUrl) throws IOException {
//            String data = "";
//            InputStream iStream = null;
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url = new URL(strUrl);
//
//                // Creating an http connection to communicate with url
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                // Connecting to url
//                urlConnection.connect();
//
//                // Reading data from url
//                iStream = urlConnection.getInputStream();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(
//                        iStream));
//
//                StringBuffer sb = new StringBuffer();
//
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                data = sb.toString();
//
//                br.close();
//
//            } catch (Exception e) {
//                Log.d("Exception while downloading url", e.toString());
//            } finally {
//                iStream.close();
//                urlConnection.disconnect();
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            // Creating ParserTask
//            ParserTask parserTask = new ParserTask();
//
//            // Starting Parsing the JSON string returned by Web Service
//            parserTask.execute(result);
//        }
//    }
//
//    /**
//     * A class to parse the Google Places in JSON format
//     */
//    private class ParserTask extends
//            AsyncTask<String, Integer, List<HashMap<String, String>>> {
//
//        JSONObject jObject;
//
//        @Override
//        protected List<HashMap<String, String>> doInBackground(
//                String... jsonData) {
//
//            List<HashMap<String, String>> places = null;
//
//            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//
//                // Getting the parsed data as a List construct
//                places = placeJsonParser.parse(jObject);
//
//            } catch (Exception e) {
//                Log.d("Exception", e.toString());
//            }
//            return places;
//        }
//
//        @Override
//        protected void onPostExecute(List<HashMap<String, String>> result) {
//
//            String[] from = new String[]{"description"};
//            int[] to = new int[]{android.R.id.text1};
//
//            // Creating a SimpleAdapter for the AutoCompleteTextView
//            SimpleAdapter adapter = new SimpleAdapter(context, result,
//                    android.R.layout.simple_list_item_1, from, to);
//
//            // Setting the adapter
//            setAdapter(adapter);
//
//        }
//    }

}
