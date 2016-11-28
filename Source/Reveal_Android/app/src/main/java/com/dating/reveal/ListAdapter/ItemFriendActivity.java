package com.dating.reveal.ListAdapter;

/**
 * Created by JonIC on 2016-11-21.
 */
public class ItemFriendActivity {

    private String title; // user name
    private String description; // add match case: request state( match do request or you have to request) , add friend case:
    private String facebookid;  // user facebookid.
    private String name;
    private String profilephoto;

    public ItemFriendActivity(String title, String description, String facebookid, String username, String profilephoto ) {
        super();
        this.title = title;
        this.description = description;
        this.facebookid = facebookid;
        this.name = username;
        this.profilephoto = profilephoto;
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getFacebookid(){return facebookid;}
    public String getName(){return name;}
    public String getProfilephoto(){return profilephoto;}

}
