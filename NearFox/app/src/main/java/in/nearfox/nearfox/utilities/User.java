package in.nearfox.nearfox.utilities;

/**
 * Created by tangbang on 6/18/2015.
 */
public class User {

    private String username ;
    private String link ;
    private String email ;
    private String image_url ;
    private String email_name ;
    private String gcm_reg_id ;
    private Double lat ;
    private Double lon ;
    private String device_id ;

    public User(){
    }

    public User(String username, String link, String email, String image_url, String email_name, String gcm_reg_id, Double lat, Double lon, String device_id) {
        this.username = username ;
        this.link = link ;
        this.email = email ;
        this.image_url = image_url ;
        this.email_name = email_name ;
        this.gcm_reg_id = gcm_reg_id ;
        this.lat = lat ;
        this.lon = lon ;
        this.device_id = device_id ;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setEmail_name(String email_name) {
        this.email_name = email_name;
    }

    public void setGcm_reg_id(String gcm_reg_id) {
        this.gcm_reg_id = gcm_reg_id;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUsername() {

        return username;
    }

    public String getLink() {
        return link;
    }

    public String getEmail() {
        return email;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getEmail_name() {
        return email_name;
    }

    public String getGcm_reg_id() {
        return gcm_reg_id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getDevice_id() {
        return device_id;
    }

    public User(String username, String link, String email, String image_url, String email_name, String gcm_reg_id, String device_id) {
        this.username = username ;
        this.link = link ;
        this.email = email ;
        this.image_url = image_url ;
        this.email_name = email_name ;

        this.gcm_reg_id = gcm_reg_id ;
        this.device_id = device_id ;
    }
}
