package com.dating.reveal.commom;

/**
 * Reveal Created by spider on 10/20/16.
 */
public class Comment {
    private String name; // facebookid of the message sender
    private String comment; // message content
    private String profilephoto; // profile photo path.
    private String facebookid;

    public String getFacebookid(){return facebookid;}
    public void setFacebookid(String facebookid){this.facebookid = facebookid;}

    public String getProfilephoto(){return profilephoto;}
    public void setProfilephoto(String profilephoto){this.profilephoto = profilephoto;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
