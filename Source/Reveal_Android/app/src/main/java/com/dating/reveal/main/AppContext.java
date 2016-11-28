package com.dating.reveal.main;

import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;

import org.json.JSONObject;

import com.dating.reveal.utility.DataUtils;

public class AppContext {
	private static JSONObject g_MyProfile = new JSONObject();
	public static String g_UserID = "1";
	private static JSONObject g_UserProfileInfo = new JSONObject();
	private static final String PROFILE_KEY = "profile_key";


	
	public static void setProfile(JSONObject profile)
	{
		if( profile != null)
			g_MyProfile = profile;
	}
	
	public static void initUserInfo(String id, JSONObject profile, final ResultCallBack callback)
	{
		g_UserID = id + "";	
		setUserProfileInfo(id, profile);
		
		String prevUserID = DataUtils.getPreference(Const.PREV_USER_ID, "-1");
		if( prevUserID.equals(g_UserID) )		// same account login
		{
			updateContactPhotoList();
			if( callback != null )
			{
				LogicResult result = new LogicResult();
				result.mResult = LogicResult.RESULT_OK;
				callback.doAction(result);
			}
			return;
		}
		
		restoreUserInfo(callback);	
	}
	
	public static void restoreUserInfo(final ResultCallBack callback)
	{		

	}

	public static void updateContactPhotoList()
	{
//		List<JSONObject> contactList = DBManager.getContactListInfo(g_UserID, "", -1, 0);
//		ServerManager.getContactPhotoList(g_UserID, contactList, new ResultCallBack() {
//			
//			@Override
//			public void doAction(LogicResult result) {
//				if( result.mResult != LogicResult.RESULT_OK )				
//					return;
//				
//				DBManager.updateContactPhotoList(result.getArray());
//			}
//		});
	}

	public static void setUserProfileInfo(String userID, JSONObject userinfo)
	{		
		DataUtils.savePreference(userID + PROFILE_KEY, userinfo.toString());
		g_UserProfileInfo = userinfo;
	}

	public static JSONObject getProfile()
	{
		return g_MyProfile;
	}
	
	public static String getUserID()
	{
		if( g_MyProfile == null )
			return "0";
		
		return g_MyProfile.optString("id", "0");
	}
	
}
