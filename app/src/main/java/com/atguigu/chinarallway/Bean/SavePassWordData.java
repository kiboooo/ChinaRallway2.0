package com.atguigu.chinarallway.Bean;

/**
 * Created by Kiboooo on 2017/9/13.
 */

public class SavePassWordData {
    private String Password;
    private UserData userData;

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {

        return userData;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {

        return Password;
    }
}
