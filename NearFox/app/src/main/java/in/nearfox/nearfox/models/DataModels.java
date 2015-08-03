package in.nearfox.nearfox.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class DataModels {

    public class Events {
        @Expose
        private ArrayList<Event> events_all;
        @Expose
        private boolean eod;
        @Expose
        private String current_time_stamp;
        @Expose
        private boolean change_status;

        public void setData_changed(boolean data_changed) {
            this.change_status = data_changed;
        }

        public boolean isData_changed() {

            return change_status;
        }

        public ArrayList<Event> getEvents() {
            return events_all;
        }

        public void setEod(boolean eod) {
            this.eod = eod;
        }

        public void setCurrent_time_stamp(String current_time_stamp) {
            this.current_time_stamp = current_time_stamp;
        }

        public boolean isEod() {

            return eod;
        }

        public String getCurrent_time_stamp() {
            return current_time_stamp;
        }

        public void setEvents(ArrayList<Event> events) {
            this.events_all = events;
        }
    }

    public class Event extends SingleEvent {

        @Expose
        private String location;



        @Expose
        private String contact_phone;
        @Expose
        private String website;
        @Expose
        private String day;
        @Expose
        private String venue;

        private String fullStartDate;

        public void setFullStartDate(String fullStartDate) {
            this.fullStartDate = fullStartDate;
        }

        public String getFullStartDate() {

            return fullStartDate;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }

        public void setLocality(String locality) {
            this.location = locality;
        }

        public String getVenue() {

            return venue;
        }

        public String getLocality() {
            return location;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        public void setContact(String contact) {
            this.contact_phone = contact;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getContact() {
            return contact_phone;
        }

        public String getWebsite() {
            return website;
        }
    }

    public class SingleEvent {
        @Expose
        private String subtitle;
        @Expose
        private String description;
        @Expose
        private String start_time;
        @Expose
        private String lat;
        @Expose
        private String category;
        @Expose
        private String event_id;
        @Expose
        private String title;
        @Expose
        private String lon;
        @Expose
        private String image_url;
        @Expose
        private String start_date;
        @Expose
        private String address_line1;


        @Expose
        private String end_time;
        @Expose
        private String end_date;
        @Expose
        private String address_line2;
        @Expose
        private String landmark;
        @Expose
        private String fees;
        @Expose
        private boolean all_day;
        @Expose
        private String ttl;

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

        public String getTtl() {
            return ttl;
        }

        public void setAll_day(boolean all_day) {
            this.all_day = all_day;
        }

        public boolean isAll_day() {

            return all_day;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getLandmark() {

            return landmark;
        }

        public String getEvent_id() {
            return event_id;
        }

        public void setCategories(String category) {
            this.category = category;
        }


        public String getCategories() {

            return category;
        }

        public void setAddress_line1(String address_line1) {
            this.address_line1 = address_line1;
        }

        public void setAddress_line2(String address_line2) {
            this.address_line2 = address_line2;
        }

        public String getAddress_line1() {

            return address_line1;
        }

        public String getAddress_line2() {
            return address_line2;
        }


        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public void setEvent_details(String event_details) {
            this.description = event_details;
        }

        public void setFees(String fees) {
            this.fees = fees;
        }

        public String getStart_date() {
            return start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public String getEvent_details() {
            return description;
        }

        public String getFees() {
            return fees;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getTitle() {
            return title;
        }

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getImage_url() {
            return image_url;
        }
    }

    public class News {
        @Expose
        private ArrayList<singleNewsAbstract> news_all;
        @Expose
        private boolean success;
        @Expose
        private String message;
        @Expose
        private boolean eod;
        @Expose
        private String current_time_stamp;
        @Expose
        private boolean change_status;

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {

            return success;
        }

        public String getMessage() {
            return message;
        }

        public void setData_changed(boolean data_changed) {
            this.change_status = data_changed;
        }

        public boolean isData_changed() {
            return change_status;
        }

        public void setNews_all(ArrayList<singleNewsAbstract> news_all) {


            this.news_all = news_all;
        }

        public ArrayList<singleNewsAbstract> getNews_all() {
            return news_all;
        }

        public void setCurrent_time_stamp(String current_time_stamp) {

            this.current_time_stamp = current_time_stamp;
        }

        public String getCurrent_time_stamp() {

            return current_time_stamp;
        }

        public void setEod(boolean eod) {
            this.eod = eod;
        }

        public boolean isEod() {
            return eod;
        }

        public void setNews(ArrayList<singleNewsAbstract> news) {
            this.news_all = news;
        }

        public ArrayList<singleNewsAbstract> getNews() {

            return news_all;
        }
    }

    public class singleNewsAbstract {

        @Expose
        private String news_id;
        @Expose
        private String image_url;
        @Expose
        private String title;
        @Expose
        private String link;
        @Expose
        private String source_name;
        @Expose
        private String tags;
        @Expose
        private String period;
        @Expose
        private String categories;
        @Expose
        private String news_excerpt;
        @Expose
        private String datetime;

        public void setLink(String link) {
            this.link = link;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getCategories() {

            return categories;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getLink() {
            return link;
        }

        public String getTags() {
            return tags;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCategory(String category) {
            this.categories = category;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public void setNews_excerpt(String news_excerpt) {
            this.news_excerpt = news_excerpt;
        }

        public String getNews_id() {

            return news_id;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return categories;
        }

        public String getImage_url() {
            return image_url;
        }

        public String getPeriod() {
            return period;
        }

        public String getNews_excerpt() {
            return news_excerpt;
        }

        public void setSource_name(String source_name) {
            this.source_name = source_name;
        }

        public String getSource_name() {

            return source_name;
        }
    }

    public class SingleNews {

        @Expose
        private String lat;
        @Expose
        private String lon;
        @Expose
        private String locality;
        @Expose
        private String period;
        @Expose
        private String author;
        @Expose
        private String source_name;
        @Expose
        private String source_image_url;
        @Expose
        private String website;

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getWebsite() {
            return website;
        }

        public void setSource_name(String source_name) {
            this.source_name = source_name;
        }

        public String getSource_name() {
            return source_name;
        }

        public void setSource_image_url(String source_image_url) {
            this.source_image_url = source_image_url;
        }

        public String getSource_image_url() {

            return source_image_url;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setSource(String source) {
            this.source_name = source;
        }

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public String getLocality() {
            return locality;
        }

        public String getPeriod() {
            return period;
        }

        public String getAuthor() {
            return author;
        }

        public String getSource() {
            return source_name;
        }
    }

    public class Infos {
        @Expose
        private ArrayList<SingleInfo> infos;

        public void setInfos(ArrayList<SingleInfo> infos) {
            this.infos = infos;
        }

        public ArrayList<SingleInfo> getInfos() {
            return infos;
        }
    }

    public class SingleInfo {
        @Expose
        private String id;
        @Expose
        private String title;
        @Expose
        private String locality;
        @Expose
        private boolean delivery;
        @Expose
        private ArrayList<String> days_of_operation;
        @Expose
        private boolean if_24;
        @Expose
        private WorkingHours timings;
        @Expose
        private String lat;
        @Expose
        private String lon;
        @Expose
        private ArrayList<String> contact_number;
        @Expose
        private String website;
        @Expose
        private String address_line1;
        @Expose
        private String address_line2;
        @Expose
        private String address_line3;
        @Expose
        private String category;
        @Expose
        private String email;

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public void setDelivery(boolean delivery) {
            this.delivery = delivery;
        }

        public void setDays_of_operation(ArrayList<String> days_of_operation) {
            this.days_of_operation = days_of_operation;
        }

        public void setIf_24(boolean if_24) {
            this.if_24 = if_24;
        }

        public void setTimings(WorkingHours timings) {
            this.timings = timings;
        }

        public WorkingHours getTimings() {

            return timings;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public void setContact_number(ArrayList<String> contact_number) {
            this.contact_number = contact_number;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public void setAddress_line2(String address_line2) {
            this.address_line2 = address_line2;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getId() {

            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getLocality() {
            return locality;
        }

        public boolean isDelivery() {
            return delivery;
        }

        public ArrayList<String> getDays_of_operation() {
            return days_of_operation;
        }

        public boolean isIf_24() {
            return if_24;
        }

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public ArrayList<String> getContact_number() {
            return contact_number;
        }

        public String getWebsite() {
            return website;
        }

        public void setAddress_line1(String address_line1) {
            this.address_line1 = address_line1;
        }

        public void setAddress_line3(String address_line3) {
            this.address_line3 = address_line3;
        }

        public String getAddress_line3() {

            return address_line3;
        }

        public String getAddress_line1() {

            return address_line1;
        }

        public String getAddress_line2() {
            return address_line2;
        }

        public String getCategory() {
            return category;
        }

        public String getEmail() {
            return email;
        }
    }

    public class WorkingHours {
        @Expose
        private ArrayList<ArrayList<String>> monday;
        @Expose
        private ArrayList<ArrayList<String>> tuesday;
        @Expose
        private ArrayList<ArrayList<String>> wednesday;
        @Expose
        private ArrayList<ArrayList<String>> thursday;
        @Expose
        private ArrayList<ArrayList<String>> friday;
        @Expose
        private ArrayList<ArrayList<String>> saturday;
        @Expose
        private ArrayList<ArrayList<String>> sunday;

        public void setMonday(ArrayList<ArrayList<String>> monday) {
            this.monday = monday;
        }

        public void setTuesday(ArrayList<ArrayList<String>> tuesday) {
            this.tuesday = tuesday;
        }

        public void setWednesday(ArrayList<ArrayList<String>> wednesday) {
            this.wednesday = wednesday;
        }

        public void setThursday(ArrayList<ArrayList<String>> thursday) {
            this.thursday = thursday;
        }

        public void setFriday(ArrayList<ArrayList<String>> friday) {
            this.friday = friday;
        }

        public void setSaturday(ArrayList<ArrayList<String>> saturday) {
            this.saturday = saturday;
        }

        public void setSunday(ArrayList<ArrayList<String>> sunday) {
            this.sunday = sunday;
        }

        public ArrayList<ArrayList<String>> getMonday() {

            return monday;
        }

        public ArrayList<ArrayList<String>> getTuesday() {
            return tuesday;
        }

        public ArrayList<ArrayList<String>> getWednesday() {
            return wednesday;
        }

        public ArrayList<ArrayList<String>> getThursday() {
            return thursday;
        }

        public ArrayList<ArrayList<String>> getFriday() {
            return friday;
        }

        public ArrayList<ArrayList<String>> getSaturday() {
            return saturday;
        }

        public ArrayList<ArrayList<String>> getSunday() {
            return sunday;
        }
    }

    public class RegistedUserResult {
        @Expose
        public boolean success;

        public void setRegistered(boolean registered) {
            this.success = success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {

            return success;
        }

        public boolean getRegistered() {
            return success;
        }
    }

    public class AYNQuestions {
        @Expose
        ArrayList<AYNSingleQuestionAbstract> questions_all;
        @Expose
        String current_time_stamp;
        @Expose
        boolean success;
        @Expose
        String message;
        @Expose
        boolean change_status;

        public void setQuestions_all(ArrayList<AYNSingleQuestionAbstract> questions_all) {
            this.questions_all = questions_all;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<AYNSingleQuestionAbstract> getQuestions_all() {

            return questions_all;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public void setCurrent_time_stamp(String current_time_stamp) {

            this.current_time_stamp = current_time_stamp;
        }

        public void setChange_status(boolean change_status) {
            this.change_status = change_status;
        }

        public String getCurrent_time_stamp() {
            return current_time_stamp;
        }

        public boolean isChange_status() {
            return change_status;
        }
    }

    public class AYNSingleQuestionAbstract {

        @Expose
        private String question_id;
        @Expose
        private String content;
        @Expose
        private String firstname;
        @Expose
        private String period;
        @Expose
        private String thumbnail_url;
        @Expose
        private boolean user_upvoted;
        @Expose
        private int upvotes;
        @Expose
        private boolean user_downvoted;
        @Expose
        private int downvotes;
        @Expose
        private boolean user_answered;
        @Expose
        private int answers;
        @Expose
        private boolean shared;
        @Expose
        private int shares;


        public void setShares(int shares) {
            this.shares = shares;
        }

        public void setShared(boolean shared) {
            this.shared = shared;
        }

        public int getShares() {

            return shares;
        }

        public boolean isShared() {
            return shared;
        }

        public void setAyn_question_id(String ayn_question_id) {

            this.question_id = ayn_question_id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setQuestion_author(String question_author) {
            this.firstname = question_author;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }

        public void setUser_up_voted(boolean user_up_voted) {
            this.user_upvoted = user_up_voted;
        }

        public void setUp_voted(int up_voted) {
            this.upvotes = up_voted;
        }

        public void setUser_down_voted(boolean user_down_voted) {
            this.user_downvoted = user_down_voted;
        }

        public void setDown_voted(int down_voted) {
            this.downvotes = down_voted;
        }

        public void setAnswered(boolean answered) {
            this.user_answered = answered;
        }

        public void setAnswers(int answers) {
            this.answers = answers;
        }

        public String getAyn_question_id() {

            return question_id;
        }

        public String getContent() {
            return content;
        }

        public String getQuestion_author() {
            return firstname;
        }

        public String getPeriod() {
            return period;
        }

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public boolean isUser_up_voted() {
            return user_upvoted;
        }

        public int getUp_voted() {
            return upvotes;
        }

        public boolean isUser_down_voted() {
            return user_downvoted;
        }

        public int getDown_voted() {
            return downvotes;
        }

        public boolean isAnswered() {
            return user_answered;
        }

        public int getAnswers() {
            return answers;
        }
    }

    public class AYNPSinglePost {
    }

    public class AYNAnswers {
        @Expose
        private ArrayList<AYNAnswer> answers_all;
        @Expose
        private boolean success;
        @Expose
        private boolean change_status;
        @Expose
        private String current_time_stamp;
        @Expose
        private String message;


        public void setAnswers(ArrayList<AYNAnswer> answers) {
            this.answers_all = answers;
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean isChange_status() {
            return change_status;
        }

        public String getCurrent_time_stamp() {
            return current_time_stamp;
        }

        public String getMessage() {
            return message;
        }

        public void setSuccess(boolean success) {

            this.success = success;
        }

        public void setChange_status(boolean change_status) {
            this.change_status = change_status;
        }

        public void setCurrent_time_stamp(String current_time_stamp) {
            this.current_time_stamp = current_time_stamp;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<AYNAnswer> getAnswers() {

            return answers_all;
        }
    }

    public class AYNAnswer {

        @Expose
        private String answer_id;
        @Expose
        private String firstname;
        @Expose
        private String thumbnail_url;
        @Expose
        private String content;
        @Expose
        private boolean user_upvoted;
        @Expose
        private String upvotes;
        @Expose
        private boolean user_downvoted;
        @Expose
        private String downvotes;
        @Expose
        private String period;
        @Expose
        private String timestamp;
        @Expose
        private String date_added;
        @Expose
        private String status;


        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public void setDate_added(String date_added) {
            this.date_added = date_added;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimestamp() {

            return timestamp;
        }

        public String getDate_added() {
            return date_added;
        }

        public String getStatus() {
            return status;
        }

        public void setAyn_answer_id(String ayn_answer_id) {
            this.answer_id = ayn_answer_id;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setUser_up_voted(boolean user_up_voted) {
            this.user_upvoted = user_up_voted;
        }

        public void setUp_voted(String up_voted) {
            this.upvotes = up_voted;
        }

        public void setUser_down_voted(boolean user_down_voted) {
            this.user_downvoted = user_down_voted;
        }

        public void setDown_voted(String down_voted) {
            this.downvotes = down_voted;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getAyn_answer_id() {

            return answer_id;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getThumbnail_url() {
            return thumbnail_url;
        }

        public String getContent() {
            return content;
        }

        public boolean isUser_up_voted() {
            return user_upvoted;
        }

        public String getUp_voted() {
            return upvotes;
        }

        public boolean isUser_down_voted() {
            return user_downvoted;
        }

        public String getDown_voted() {
            return downvotes;
        }

        public String getPeriod() {
            return period;
        }
    }

    public class AYNAnswerVoteOrAnswer {
        @Expose
        private boolean success;
        @Expose
        private boolean opposite;
        @Expose
        private String message;

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {

            return message;
        }

        public void setOpposite(boolean opposite) {

            this.opposite = opposite;
        }

        public boolean isOpposite() {

            return opposite;
        }

        public void setSuccess(boolean success) {

            this.success = success;
        }

        public boolean isSuccess() {

            return success;
        }
    }

    public class AYNPushAnswer {
        @Expose
        private boolean success;
        @Expose
        private String message;
        @Expose
        private String answer_id;

        public String getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(String answer_id) {
            this.answer_id = answer_id;
        }

        public void setSuccess(boolean success) {

            this.success = success;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {

            return message;
        }

        public boolean isSuccess() {

            return success;
        }
    }

    public class HomeLocation {
        @Expose
        private boolean success;

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {

            return success;
        }
    }

    public Event returnEventObject() {
        return (new Event());
    }
}