package vun.zisplay.models;



/**
 * Created by ashok on 1/29/15.
 */
public class Merchant {


    String id;
    String name;
    String title;
    String mobileNo;
    String facebookPageId;
    String email;
    boolean chatEnabled;
    boolean paymentEnabled;
    String whatsAppNo;
    boolean isVerified;

    Location location;
//    String location;
    String address;
    String locality;
    String city;
    String state;
    String country;
    String imageId;
    String LogoId;
    boolean homeDelivery;
    boolean nationalDelivery;
    boolean internationalDelivery;
    String website;
    int rating;
    String vat;
    String cst;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFacebookPageId() {
        return facebookPageId;
    }

    public void setFacebookPageId(String facebookPageId) {
        this.facebookPageId = facebookPageId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    public boolean isPaymentEnabled() {
        return paymentEnabled;
    }

    public void setPaymentEnabled(boolean paymentEnabled) {
        this.paymentEnabled = paymentEnabled;
    }

    public String getWhatsAppNo() {
        return whatsAppNo;
    }

    public void setWhatsAppNo(String whatsAppNo) {
        this.whatsAppNo = whatsAppNo;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

//    public Location getLocation() {
//        return location;
//    }
//
//    public void setLocation(Location location) {
//        this.location = location;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLogoId() {
        return LogoId;
    }

    public void setLogoId(String logoId) {
        LogoId = logoId;
    }

    public boolean isHomeDelivery() {
        return homeDelivery;
    }

    public void setHomeDelivery(boolean homeDelivery) {
        this.homeDelivery = homeDelivery;
    }

    public boolean isNationalDelivery() {
        return nationalDelivery;
    }

    public void setNationalDelivery(boolean nationalDelivery) {
        this.nationalDelivery = nationalDelivery;
    }

    public boolean isInternationalDelivery() {
        return internationalDelivery;
    }

    public void setInternationalDelivery(boolean internationalDelivery) {
        this.internationalDelivery = internationalDelivery;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }






}
