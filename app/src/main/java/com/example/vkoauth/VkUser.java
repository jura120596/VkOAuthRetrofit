package com.example.vkoauth;

import java.io.Serializable;

public class VkUser implements Serializable {
    public String id;
    public String first_name;
    public String last_name;
    public String screen_name;
    public String sex;
    public String bdate;
    public String photo_big;

    @Override
    public String toString() {
        return "VkUser{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", sex='" + sex + '\'' +
                ", bdate='" + bdate + '\'' +
                ", photo_big='" + photo_big + '\'' +
                '}';
    }
}
