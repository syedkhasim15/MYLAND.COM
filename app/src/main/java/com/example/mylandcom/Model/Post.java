package com.example.mylandcom.Model;


import java.security.PublicKey;
import java.util.Date;
public class Post extends PostId {

    private String Img_type,caption,image,lat,lng,details,sq_feet;
    private Date time;


    public String getImage() {
        return image;
    }

    public String getImg_type(){return Img_type;}

    public String getCaption() {
        return caption;
    }

    public String getLat() {return lat;}

    public String getLng(){return lng;}

    public String getDetails(){return details;}

    public String getSq_feet(){return sq_feet;}

    public Date getTime() {
        return time;
    }
}