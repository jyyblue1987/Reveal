package com.dating.reveal.commom;

/**
 * Reveal Created by spider on 10/20/16.
 */
public class Member {
    private String name; // facebookid of the user who is match with you.
    private String facebookid;
    private String profilephoto;

    public String getFacebookid(){return facebookid;}
    public String getProfilephoto(){return profilephoto;}
    public String getName() {
        return name;
    }

    public void setFacebookid(String facebookid){this.facebookid =facebookid;}
    public void setProfilephoto(String profilephoto){this.profilephoto = profilephoto;}
    public void setName(String name) {
        this.name = name;
    }

}
