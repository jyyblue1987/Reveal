package com.dating.reveal.main;

public class Const {
	public static boolean ANALYTICS = true;
	
	public static final String LANGUAGE = "language";

	public static final String LOGIN_OK = "login_ok";
	public static final String ADMIN = "bearer";
	public static final	int			NOT_LOGIN 	= 1;
	public static final	int			NOT_VERIFY 		= 2;
	public static final	int			LOGIN_SUCESS	= 3;
	public static final String MANAGER	 = "";

	public static final String USER_URL = "user_url";

	// Login
	public static final String PREV_USER_ID = "prev_user_id";

	public static final String PROFILE = "profile";
	public static final String PRE_EMAIL = "pre_user";
	// preference key
	public static final String GCM_PUSH_KEY = "gcm_push_key";
	public static final String VCODE = "vcode";
	
	public static final String ID = "id";
	public static final String FULLNAME = "fullname";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String CONTACT= "contact";
	public static final String THUMBNAIL = "thumbnail";
	public static final String DESC = "desc";
	public static final String RATING = "rating";
	public static final String DOMAIN = "domain";
	
	public static final String CONTENT = "content";
	public static final String UPLOAD_PATH = "upload_path";
	
	
	// Record 
	public static final String RECORD_START = "record_start";
	public static final String RECORD_STOP = "record_stop";

	// Complete Registration
	public static final String TITLE = "title";
	public static final String FIRST_NAME = "first_name";
	public static final String MIDDLE_NAME = "middle_name";
	public static final String LAST_NAME = "last_name";
	public static final String ADDRESS = "address";
	public static final String POST_CODE = "post_code";
	public static final String BRANCH = "branch";
	public static final String BRANCHID = "branch_id";
	public static final String PHONE_NUMBER = "phone_number";

	
	
	// Connect State
	public static final int NOT_CONNECTED = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECTED = 2;
	public static final int AUTHENTICATING = 3;
	public static final int AUTHENTICATED = 4;
	
	public static final String SMACK = "tijarti_smack";
	
	// Broadcast message
	public static final String RECEIVE_MESSAGE_ACTION = "receive_message";
	public static final String EXTRA_MESSAGE = "extra_message";
	public static final String ROSTER_CHANGE_ACTION = "roster_change";
	public static final String NOTIFICATON_RECEIVE_ACTION = "notification_receive";
	public static final String UPDATE_MESSAGE_ACTION = "update_message";
	
	public static final String ROSTER_INFO_CHANGE_ACTION = "roster_info_change";
	
	// Chat history table
	public static final String NICKNAME = "nickname";
	public static final String FROM = "from_id";
	public static final String TO = "to_id";
	public static final String BODY = "body";
	public static final String TYPE = "type";
	public static final String UNREAD = "unread";
	public static final String SENT = "sent";
	public static final String DIRECTION = "direct";
	public static final String GROUP_TYPE = "group_type";
	public static final String DATE = "created";
	public static final String DISP_DATE = "displayed";
	public static final String SENDER = "sender"; // only valid in group chatting
	public static final String SMS_CHAT = "sms_chat";

	public static final String ROLE = "role";
	
	public static final String PREFIX = "tijarti_";
	
	public static final String FAVORITE = "favorite";
	public static final String CREATOR = "creator";
	public static final String COMPANY = "company";
	public static final String POSTAL_CODE = "postal";
	public static final String AVASTAR = "avastar";
	public static final String REALNAME = "name";
	public static final String CONTACT_COUNT = "contact_count";
	public static final String GROUP_NAME = "group_name";
// my program
    public static final String GET_FACEBOOKID = "id";
	public static final String AGE = "age";
	public static final String GENDER = "gender";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String TOKEN="token";

	public static final String MAXAGE = "max_age";
	public static final String MINAGE = "min_age";
	public static final String MAXRATE  ="max_rate";
	public static final String MINRATE  ="min_rate";
	public static final String DISTANCE  ="distance";
	public static final String REPORT  ="report";
	public static final String RATEPHOTO  ="ratephoto";
	public static final String RESPONSE_MATCHING  ="response_matching_request";
	public static final String NOTIFICATION_SIZE  ="notification_size";
	public static final String NOTIFICATION_CONTENT  ="notification_content";

	public static final String FACEBOOKID = "facebookid";
	public static final String PHOTO_PATH  ="photopath";
	public static final String ABOUT_PHOTO  ="mycomment";
	public static final String SIZE_COMMENT  ="commentnum";
	public static final String SIZE_LIKE  ="likenum";
	public static final String RATE_SUM  ="ratesum";
	public static final String RATE_NUMBER  ="ratenumber";
	public static final String COMMENT_CONTENT  ="commentcon";
	public static final String LIKE_CONTENT  ="likefacebookid";
	// notification page save this value( try matching person's facebookid, that is sender
	// and delete that notification form notification array.
	// in raing page get this value,get the person's profile photo. evaluate the photo ( evaluating apply to only one photo.)
	public static final String TRY_MATCH_PERSON  ="TRY_MATCH_PERSON";
	public static final String FRIENDREQUEST  ="requestfriend";
	public static final String BLOCK = "BLOCK";
	public static final String USE_APP_FRIENDS = "USE_APP_FRIENDS";
	public static final String FACEBOOK_FRIENDS = "FACEBOOK_FRIENDS";
	public static final String BIRTHDAY = "birthday";
	public static final String INPUT_AGE = "input_age";
	public static final String ULIMIT_AGE = "upper_limit_age";
	public static final String LLIMIT_AGE = "lower_limit_age";

//	public static final String GROUP_NAME = "group_name";


}
