package vun.zisplaymerchant.models;

import android.location.Location;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.loopback.User;

/**
 * Created by ashok on 1/29/15.
 */
public class AppUser {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public String getWhatsappNo() {
        return whatsappNo;
    }
    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }
    public String getIsVerified() {
        return isVerified;
    }
    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }
    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    String id;
    String name;
    String title;
    String mobileNo;
    String accessToken;
    String email;
    Location location;
    String whatsappNo;
    String isVerified;
    String address;
    String city;
    String state;
    String Country;
    String locality;
    String password;
    String imageId;
    boolean isMerchant;
    String merchantId;
    int unreadMessage=0;
    String facebookId="";
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public int getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(int unreadMessage) {
        this.unreadMessage = unreadMessage;
    }



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public boolean isMerchant() {
        return isMerchant;
    }

    public void setMerchant(boolean isMerchant) {
        this.isMerchant = isMerchant;
    }



}