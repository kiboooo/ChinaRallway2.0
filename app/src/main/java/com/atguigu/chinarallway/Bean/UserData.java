package com.atguigu.chinarallway.Bean;

/**
 * Created by Kiboooo on 2017/9/12.
 */

public class UserData {
    String uid;
    String uname;
    String roleName;
    String priv;
    int roleId;
    int state;

    public void setUid(String uid) {

        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setPriv(String priv) {
        this.priv = priv;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUid() {

        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getPriv() {
        return priv;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getState() {
        return state;
    }

    public UserData(){}

    public UserData(String uid, String uname, String roleName, String priv, int roleId, int state) {
        this.uid = uid;
        this.uname = uname;
        this.roleName = roleName;
        this.priv = priv;
        this.roleId = roleId;
        this.state = state;
    }

}
