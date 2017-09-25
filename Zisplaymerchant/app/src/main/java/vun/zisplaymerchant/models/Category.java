package vun.zisplaymerchant.models;

/**
 * Created by ashok on 25/3/15.
 */
public class Category {
    String id="";
    String title="";
    String gender="";
    String image="";
    String parentCatagoryId="";
    boolean deleted=false;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParentCatagoryId() {
        return parentCatagoryId;
    }

    public void setParentCatagoryId(String parentCatagoryId) {
        this.parentCatagoryId = parentCatagoryId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    String created="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    @Override
    public String toString()
    {
        return title;
    }


}
