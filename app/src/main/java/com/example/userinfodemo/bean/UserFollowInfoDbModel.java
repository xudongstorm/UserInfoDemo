package com.example.userinfodemo.bean;

/**
 * 用户Follow数据库DB实体类
 */
public class UserFollowInfoDbModel {

    private String login;
    private String followers;   //存储对应followers的json字符串，以“、”分割，如{"login" : "dong", "avatarUrl" : "xxx}
    private String following;   //存储对应following的json字符串，以“、”分割，如{"login" : "dong", "avatarUrl" : "xxx}


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
