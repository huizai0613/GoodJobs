package cn.goodjobs.common.entity;

import java.io.Serializable;

/**
 * Created by 王刚 on 2015/12/23 0023.
 */
public class LoginInfo implements Serializable {
    public String userName;
    public String passWord;
    public boolean isAutoLogin;
    public long loginTime; // 登录时间
    public String userID; // 用户ID

    public LoginInfo() {}

    public LoginInfo(String userName, String passWord, boolean isAutoLogin, long loginTime, String userID) {
        this.userName = userName;
        this.passWord = passWord;
        this.isAutoLogin = isAutoLogin;
        this.loginTime = loginTime;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", isAutoLogin=" + isAutoLogin +
                ", loginTime=" + loginTime +
                ", userID='" + userID + '\'' +
                '}';
    }
}
