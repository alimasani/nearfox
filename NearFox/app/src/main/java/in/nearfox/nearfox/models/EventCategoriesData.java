package in.nearfox.nearfox.models;

import java.util.ArrayList;

import in.nearfox.nearfox.R;

/**
 * Created by tangbang on 5/28/2015.
 */
public class EventCategoriesData {

    public class Categories {
        private ArrayList<Integer> images;
        private ArrayList<String> titles;

        public void setImages(ArrayList<Integer> images) {
            this.images = images;
        }

        public void setTitles(ArrayList<String> titles) {
            this.titles = titles;
        }

        public ArrayList<Integer> getImages() {
            return images;
        }

        public ArrayList<String> getTitles() {
            return titles;
        }
    }

    private ArrayList<Integer> images;
    private ArrayList<String> titles;

    public EventCategoriesData() {
        images = new ArrayList<Integer>();
        titles = new ArrayList<String>();
    }

    public Categories getEventCategories() {
        // category 1
        images.add(R.drawable.all);
        titles.add("All");

        // category 2
        images.add(R.drawable.arts);
        titles.add("Arts");

        // category 3
        images.add(R.drawable.drama);
        titles.add("Drama");

        // category 4
        images.add(R.drawable.entertainment);
        titles.add("Entertainment");

        // categoty 5
        images.add(R.drawable.food);
        titles.add("Food");

        //category 6
        images.add(R.drawable.music);
        titles.add("Music");

        // category 7
        images.add(R.drawable.nightllife);
        titles.add("NightLife");

        // category 8
        images.add(R.drawable.spiritual);
        titles.add("Spiritual");

        // category 9
        images.add(R.drawable.travel);
        titles.add("Travel");

        // category 10
        images.add(R.drawable.workshop);
        titles.add("Workshop");

        // Creating result
        Categories categories = new Categories();
        categories.setImages(images);
        categories.setTitles(titles);

        // returning result
        return categories;
    }
}