package com.dating.reveal.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.dating.reveal.main.Const;
import com.dating.reveal.main.SoSoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.dating.reveal.utility.AlgorithmUtils;
import com.dating.reveal.utility.CheckUtils;
import com.dating.reveal.utility.FileUtils;

public class DBManager {
	private static final String DATABASE_NAME = "PhoneBook.db";
	private static final String CONTACTLIST_TABLE = "ContactList";
	private static final String CALLHISTORY_TABLE = "CallHistory";
	private static final String SMSHISTORY_TABLE = "SMSHistory";
	private static final String GROUP_TABLE = "GroupList";
	private static final String CHATHISTORY_TABLE = "ChatHistory";
	private static final String NOTIFICATIONLIST_TABLE = "NotificationList";
	
	private static Context m_context = null;
	
	static String[] contactlist_FieldNameArray = {"id", "name", "mobile", "reg_state", "thumb_image", "sinch_id",
													"pname", "email", "homeaddr", "housetel", "birthday", "class_id", 
													"group_id", "country_id", "photo_filename", "sync_flag", "username"};
	
	static String[] callhistory_FieldNameArray = {"id", "state", "starttime", "endtime", "to_mobile"};
	static String[] smshistory_FieldNameArray = {"id", "state", "sendtime", "msg_content", "to_mobile"};
	static String[] group_FieldNameArray = {"id", "name", "sync_flag"};

	public static void loadDB(Context context)
	{
		m_context = context;
		
		DatabaseManager db = new DatabaseManager(context);
		String path = db.getDatabaseFullPath(DATABASE_NAME);
		FileUtils.copyAssetFileToSDCard(context, "data/tijarti.db", path);

	}
	
	public static List<JSONObject> getContactListInfo(String userID, String filter, int groupID, int alphabet)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		String range [] = {
				"",
				"where (UPPER(pname) < 'D')",
				"where (UPPER(pname) >= 'D' and UPPER(pname) < 'G')",
				"where (UPPER(pname) >= 'G' and UPPER(pname) < 'J')",
				"where (UPPER(pname) >= 'J' and UPPER(pname) < 'M')",
				"where (UPPER(pname) >= 'M' and UPPER(pname) < 'P')",
				"where (UPPER(pname) >= 'P' and UPPER(pname) < 'T')",
				"where (UPPER(pname) >= 'T' and UPPER(pname) < 'W')",
				"where (UPPER(pname) >= 'W')"
				
				
		};
		Cursor cursor = null;
		
		String strQuery = "order by pname COLLATE NOCASE ASC";
		if( CheckUtils.isEmpty(filter) )
		{
			if( groupID >= 0 )
				strQuery = "where group_id = '" + groupID + "' " + strQuery;
			else
				strQuery = range[alphabet] + " " + strQuery;
		}
		else
		{
			if( groupID >= 0 )
				strQuery = "where (pname LIKE '%" + filter + "%' or mobile LIKE '%" + filter + "%' or email LIKE '%" + filter + "%')" + " and group_id = '" + groupID + "' " + strQuery;
			else
				strQuery = "where (pname LIKE '%" + filter + "%' or mobile LIKE '%" + filter + "%' or email LIKE '%" + filter + "%') " + strQuery;
		}
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		cursor = db.SearchRecordsWithRaw("*", CONTACTLIST_TABLE, strQuery);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static void replaceContactFromServer(JSONArray contactlist)
	{
		if( contactlist == null )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		db.DeleteAllRecords(CONTACTLIST_TABLE);
		
		for(int i = 0; i < contactlist.length(); i++ )
		{
			JSONObject data = contactlist.optJSONObject(i);
			if(data == null)
				continue;
			
			String mobile = data.optString("mobile", "");
			String username = SoSoUtils.getChatID(mobile);
//			ChatController.addContacts(username, "", data.optString("pname", ""));
			String[] strFieldValues = { "'" + data.optString("id", "1") + "'",
					"'" + data.optString("name", "") + "'",
					"'"+ mobile + "'",
					"'"+ "0" + "'",
					"'"+ "" + "'",
					"'"+ "" + "'",
					"'"+ data.optString("pname", "") + "'",
					"'"+ data.optString("email", "") + "'",
					"'"+ data.optString("homeaddr", "") + "'",
					"'"+ data.optString("housetel", "") + "'",
					"'"+ data.optString("birthday", "") + "'",
					"'"+ data.optString("class_id", "") + "'",
					"'"+ data.optString("group_id", "") + "'",
					"'"+ data.optString("country_id", "") + "'",
					"'"+ data.optString("photo_filename", "") + "'",
					"'0'", 
					"'"+ username + "'"};

			db.AddRecord(CONTACTLIST_TABLE, contactlist_FieldNameArray, strFieldValues);
		}


		
		db.CloseDatabase();
	}

	
	public static List<JSONObject> getContactListNotIncludeGroup(String userID, int groupID)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;		
	
		
		String strQuery = "where group_id <> '" + groupID + "' order by pname COLLATE NOCASE ASC";
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		Cursor cursor = null;
		cursor = db.SearchRecordsWithRaw("*", CONTACTLIST_TABLE, strQuery);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static List<JSONObject> getContactListIncludeGroup(String userID, int groupID)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;		
	
		
		String strQuery = "where group_id == '" + groupID + "' order by pname COLLATE NOCASE ASC";
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		Cursor cursor = null;
		cursor = db.SearchRecordsWithRaw("*", CONTACTLIST_TABLE, strQuery);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static void updateGroupID(DatabaseManager db, int id, int groupID)
	{
		if(db == null)
			return;
		
		if( db.IsDatabaseExists(DATABASE_NAME) == false )
			return;
		
		if( db.IsOpen() == false )
			return;
		
    	String strSetValue = "group_id = '" + groupID  + "'";
    
		String strQuery = "id = '" + id + "'";
		
		db.UpdateRecords(CONTACTLIST_TABLE, strSetValue, strQuery);

	}
	
	public static void removeGroupIDFromContactList(int groupID)
	{
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;		
		
    	String strSetValue = "group_id = '-1'";
    
		String strQuery = "group_id = '" + groupID + "'";
		
		db.UpdateRecords(CONTACTLIST_TABLE, strSetValue, strQuery);

		db.CloseDatabase();		

	}
	
	public static JSONObject getContactInfo(int id)
	{
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return null;
		
		Cursor cursor = null;
		
		String strQuery = "id = '" + id + "'";
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		cursor = db.SearchRecords("*", CONTACTLIST_TABLE, strQuery);
		
		JSONObject data = db.getFirstRecordData(cursor);
					
		db.CloseDatabase();
		
		return data;
	}
	
	public static JSONObject getContactInfo(DatabaseManager db, String mobile)
	{
		if(db == null)
			return null;
		
		if( db.IsDatabaseExists(DATABASE_NAME) == false )
			return null;
		
		if( db.IsOpen() == false )
			return null;
		
		Cursor cursor = null;
		
		String strQuery = "mobile = '" + mobile + "'";
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		cursor = db.SearchRecords("*", CONTACTLIST_TABLE, strQuery);
		
		JSONObject data = db.getFirstRecordData(cursor);
					
		return data;
	}
	
	public static List<JSONObject> getFavoriteContactListInfo(String userID)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;

		Cursor cursor = null;
		
		String strQuery = "where favourite = '1' order by pname COLLATE NOCASE ASC";
	
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		cursor = db.SearchRecordsWithRaw("*", CONTACTLIST_TABLE, strQuery);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static List<JSONObject> getFavoriteContactListNotInclude(String userID)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;

		Cursor cursor = null;
		
		String strQuery = "where favourite <> '1' order by pname COLLATE NOCASE ASC";
	
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		cursor = db.SearchRecordsWithRaw("*", CONTACTLIST_TABLE, strQuery);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static void updateFavoriteInfo(String userID, String id, int value)
	{
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
    	String strSetValue = "favourite = '" + value + "'";
    
		String strQuery = "id = '" + id + "'";
		
		db.UpdateRecords(CONTACTLIST_TABLE, strSetValue, strQuery);

		db.CloseDatabase();
	}
	

	
	public static List<JSONObject> getCallHistoryInfo(String userID)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String strQuery = "order by endtime desc";
		
//		if(CheckUtils.isEmpty(userID) == false )
//			strQuery = "userid = '" + userID + "'"; 		
		
		cursor = db.SearchRecordsWithRaw("*", CALLHISTORY_TABLE, strQuery);
		list = db.getRecordData(cursor);
				
		db.CloseDatabase();
		
		return list;
	}
	
	public static int addContactFromAddressBook(Activity activity, Uri contactData)
	{
		Cursor cursor =  null;
		
		String phoneNumber = "";
        List<String> allNumbers = new ArrayList<String>();
        int phoneIdx = 0;
        try {  
            String id = contactData.getLastPathSegment();
            cursor = activity.getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] { id }, null);
            phoneIdx = cursor.getColumnIndex(Phone.DATA);
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    phoneNumber = cursor.getString(phoneIdx);
                    allNumbers.add(phoneNumber);
                    cursor.moveToNext();
                }
            } else {
                //no results actions
            }  
        } catch (Exception e) {
           //error actions
        } finally {  
            if (cursor != null) {  
                cursor.close();
            }          
        }

        return -1;
	}
	
	public static void updateContact(JSONObject data, String id)
	{
		if( data == null || CheckUtils.isEmpty(id) )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;



    	String strSetValue = "";
		
    	String[] item = {
    			"pname", "name", "mobile", "email", "homeaddr", "housetel", "birthday", "country_id"
    	};
    	
    	for(int i = 0; i < item.length; i++ )
    	{
			strSetValue += item[i] + " = '" + data.optString(item[i], "") + "',";	
    	}
//      	strSetValue += "username = '" + Contact2WUtils.changeUserNameFormat(data.optString("mobile", "")) + "',";		        
    	strSetValue += "sync_flag = '2'";	// update contact	
			
		String strQuery = "id = '" + id + "'";
		
		db.UpdateRecords(CONTACTLIST_TABLE, strSetValue, strQuery);

		db.CloseDatabase();
	}
	
	public static void deleteContact(String id)
	{
		if( CheckUtils.isEmpty(id) )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;

		String strQuery = "id = '" + id + "'";
		
		db.DeleteRecords(CONTACTLIST_TABLE, strQuery);


		db.CloseDatabase();
	}
	
	public static void updateContactPhoto(String imgPath, String id)
	{
		if( CheckUtils.isEmpty(imgPath) )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
    	String strSetValue = "photo_filename = '" + imgPath + "'";
    
		String strQuery = "id = '" + id + "'";
		
		db.UpdateRecords(CONTACTLIST_TABLE, strSetValue, strQuery);

		db.CloseDatabase();
	}
	
	public static void updateContactPhotoList(JSONArray photoList)
	{
		if( CheckUtils.isEmpty(photoList) )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		
		for(int i = 0; i < photoList.length(); i++ )
		{
			JSONObject contact = photoList.optJSONObject(i);
			if( contact == null )
				continue;
			
			String strSetValue = "photo_filename = '" + contact.optString("photo_filename", "") + "'";
			String strQuery = "id = '" + contact.optString("id", "0") + "'";
			
			db.UpdateRecords(CONTACTLIST_TABLE, strSetValue, strQuery);
		}
    
		db.CloseDatabase();
	}
	
	public static DatabaseManager openDatabaseManager()
	{
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return null;
		
		return db;
	}
	
	public static void closeDatabaseManager(DatabaseManager db)
	{
		if(db == null)
			return;
		
		if( db.IsDatabaseExists(DATABASE_NAME) == false )
			return;
		
		if( db.IsOpen() == false )
			return;
		
		db.CloseDatabase();
	}
	
	public static boolean isExistContact(DatabaseManager db, String phoneNumber, String countryCode)
	{
		if(db == null)
			return false;
		
		if( db.IsDatabaseExists(DATABASE_NAME) == false )
			return false;
		
		if( db.IsOpen() == false )
			return false;
		
		if( CheckUtils.isEmpty(phoneNumber) )
			return false;
	
			
		if( CheckUtils.isEmpty(countryCode) )
			countryCode = "+60";
		
		String strQuery = "mobile = '" + countryCode + " " + phoneNumber + "'";
		
		Cursor cursor = db.SearchRecords(CONTACTLIST_TABLE, strQuery);
		List<JSONObject> list =  db.getRecordData(cursor);
	
		if( list == null || list.size() < 1 )
			return false;

		return true;
	}
	
	public static void addCallHistoryWithContactInfo(JSONObject data, int state, Date start, Date end, String mobile, int mobileNum)
	{
		if( data == null )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		if( CheckUtils.isEmpty(mobile) )
		{
			String number = "";
			if( mobileNum == 0 )	// mobile 
				number = data.optString("mobile", "");
			if( mobileNum == 1 )	// mobile 
				number = data.optString("housetel", "");

			String[] strFieldValues = { "'" + data.optString("id", "") + "'",
					"'"+ state + "'",
					"'"+ format.format(start) + "'",
					"'"+ format.format(end) + "'",
					"'"+ number + "'"};

			db.AddRecord(CALLHISTORY_TABLE, callhistory_FieldNameArray, strFieldValues);
		}
		else
		{
			String[] strFieldValues = { "-1",
					"'"+ state + "'",
					"'"+ format.format(start) + "'",
					"'"+ format.format(end) + "'",
					"'"+ mobile + "'"};

			db.AddRecord(CALLHISTORY_TABLE, callhistory_FieldNameArray, strFieldValues);			
		}


		
		db.CloseDatabase();
	}
	
	public static void deleteCallHistory(String mobile)
	{
		if( CheckUtils.isEmpty(mobile) )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;

		String strQuery = "to_mobile = '" + mobile + "'";
		
		db.DeleteRecords(CALLHISTORY_TABLE, strQuery);


		db.CloseDatabase();
	}
	
	public static void replaceCallHistory(JSONArray callhistoryList)
	{
		if( callhistoryList == null )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		db.DeleteAllRecords(CALLHISTORY_TABLE);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int state = 0;
		for(int i = 0; i < callhistoryList.length(); i++ )
		{
			JSONObject data = callhistoryList.optJSONObject(i);
			if( data == null )
				continue;
						
//			String from_mobile = data.optString("from_mobile", "1234567890");
//			String to_mobile = data.optString("to_mobile", "1234567890");
//			
//			if( self_mobile.equals(from_mobile) )
//				state = Const.CALL_OUT_OK;
//			
//			JSONObject contact = getContactInfo(db, from_mobile);
//			
//			
//			if( contact != null )
//			{
//				String number = "";
//				String[] strFieldValues = { "'" + contact.optString("contact_id", "") + "'",
//						"'" + contact.optString("contact_id", "") + "'",
//						"'"+ state + "'",
//						"'"+ format.format(new Date(contact.optLong("starttime", 0))) + "'",
//						"'"+ format.format(new Date(contact.optLong("endtime", 0))) + "'",
//						"'"+ number + "'"};
//
//				db.AddRecord(CALLHISTORY_TABLE, callhistory_FieldNameArray, strFieldValues);
//			}
//			else
//			{
//				String[] strFieldValues = { "-1",
//						"-1",
//						"'"+ state + "'",
//						"'"+ format.format(start) + "'",
//						"'"+ format.format(end) + "'",
//						"'"+ mobile + "'"};
//
//				db.AddRecord(CALLHISTORY_TABLE, callhistory_FieldNameArray, strFieldValues);			
//			}


		}		
		db.CloseDatabase();
	}
	
	
	
	public static List<JSONObject> getSMSHistoryInfo(String userID)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String strQuery = "order by sendtime desc";
		
		cursor = db.SearchRecordsWithRaw("*", SMSHISTORY_TABLE, strQuery);
		list = db.getRecordData(cursor);
				
		db.CloseDatabase();
		
		return list;
	}
	
	public static List<JSONObject> searchSMSHistoryInfo(String userID, String filter)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String whereClause = "";
		String[]whereArgs = null;
		
		if( CheckUtils.isEmpty(filter) == false )
		{
			whereClause = "msg_content LIKE ? or to_mobile LIKE ?";
			String[] args = {"%" + filter + "%", "%" + filter + "%"};
		}
		
		cursor = db.searchRecord(SMSHISTORY_TABLE, null, whereClause, whereArgs, null, "sendtime DESC", null);
		
		list = db.getRecordData(cursor);
				
		db.CloseDatabase();
		
		return list;
	}
	
	public static List<JSONObject> getSMSHistoryInfo(String userID, String mobile, int id, int count)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( m_context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String orderBy = "id DESC";
		String limit = "";
		
		if( id == 0 )	// latest
			limit = Integer.MAX_VALUE + "";
		else
			limit = id + "";
			
		String whereClause = "to_mobile = ? and id < ?";
		String[]whereArgs = {mobile, limit};
		
		cursor = db.searchRecord(SMSHISTORY_TABLE, null, whereClause, whereArgs, orderBy, count + "");
		
		list = db.getRecordData(cursor);
		db.CloseDatabase();
		
		return list;
	}
	
	public static void addSMSHistoryWithContactInfo(JSONObject data, String mobile, int mobileNum)
	{
		if( data == null )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		if( CheckUtils.isEmpty(mobile) )
		{
			String number = "";
			if( mobileNum == 0 )	// mobile 
				number = data.optString("mobile", "");
			if( mobileNum == 1 )	// mobile 
				number = data.optString("housetel", "");

			String[] strFieldValues = { "'" + data.optString("id", "") + "'",
					"'"+ data.optString("direction", "0")  + "'",
					"'"+ data.optString("sendtime", "") + "'",
					"'"+ data.optString("msg_content", "") + "'",
					"'"+ number + "'"};

			db.AddRecord(SMSHISTORY_TABLE, smshistory_FieldNameArray, strFieldValues);
		}
		else
		{
			String[] strFieldValues = { "'" + data.optString("id", "") + "'",
					"'"+ data.optString("direction", "0")  + "'",
					"'"+ data.optString("sendtime", "") + "'",
					"'"+ data.optString("msg_content", "") + "'",
					"'"+ mobile + "'"};

			db.AddRecord(SMSHISTORY_TABLE, smshistory_FieldNameArray, strFieldValues);			
		}


		
		db.CloseDatabase();
	}
	
	public static void deleteSMSHistory(String mobile)
	{
		if( CheckUtils.isEmpty(mobile) )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;

		String strQuery = "to_mobile = '" + mobile + "'";
		
		db.DeleteRecords(SMSHISTORY_TABLE, strQuery);


		db.CloseDatabase();
	}
	
	public static void replaceSMSHistory(JSONArray smshistoryList)
	{
		if( smshistoryList == null )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;

		db.DeleteAllRecords(SMSHISTORY_TABLE);
		
		db.CloseDatabase();
	}
	
	private static boolean isExistGroup(String name)
	{
		if( CheckUtils.isEmpty(name) )
			return false;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;
		
		String strQuery = "name = '" + name + "'";
		
		Cursor cursor = db.SearchRecords(GROUP_TABLE, strQuery);
		List<JSONObject> list =  db.getRecordData(cursor);
	
		if( list == null || list.size() < 1 )
			return false;

		return true;
					
	}
	public static int addGroup(String name)
	{
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;

		if( isExistGroup(name) )
			return -2;
		
		int id = db.getMaxValue(GROUP_TABLE, "id") + 1;
		
		String[] strFieldValues = {"'" + id + "'",
									"'" + name + "'",									
									"'1'" };	// Add Group

		db.AddRecord(GROUP_TABLE, group_FieldNameArray, strFieldValues);
		
		db.CloseDatabase();
		
		return id;
	}
	
	public static boolean deleteGroup(int id)
	{
		if( id < 1 )
			return false;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;

		String strQuery = "id = '" + id + "'";
		
		db.DeleteRecords(GROUP_TABLE, strQuery);

		db.CloseDatabase();
		
		return true;
	}
	
	public static boolean updateGroupName(int id, String groupName)
	{
		if( id < 1 )
			return false;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;

		String strSetValue = "name = '" + groupName + "'," +  "sync_flag = '0'";
	    
		String strQuery = "id = '" + id + "'";
		
		db.UpdateRecords(GROUP_TABLE, strSetValue, strQuery);
		
		db.CloseDatabase();
		
		return true;
	}
	
	public static boolean syncGroup(int id)
	{
		if( id < 1 )
			return false;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;

		String strSetValue = "sync_flag = '0'";
	    
		String strQuery = "id = '" + id + "'";
		
		db.UpdateRecords(GROUP_TABLE, strSetValue, strQuery);
		
		db.CloseDatabase();
		
		return true;
	}
	
	public static List<JSONObject> getGroupList()
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		String strQuery = "order by name COLLATE NOCASE ASC";
		Cursor cursor = db.SearchRecordsWithRaw("*", GROUP_TABLE, strQuery);
		list =  db.getRecordData(cursor);
	
		db.CloseDatabase();
		
		return list;

	}
	
	public static void replaceGroupListFromServer(JSONArray grouplist)
	{
		if( grouplist == null )
			return;
		
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		db.DeleteAllRecords(GROUP_TABLE);
		
		for(int i = 0; i < grouplist.length(); i++ )
		{
			JSONObject data = grouplist.optJSONObject(i);
			if(data == null)
				continue;
			
			String[] strFieldValues = { "'" + data.optString("id", "1") + "'",
										"'" + data.optString("name", "") + "'",
										"'0'" };

			db.AddRecord(GROUP_TABLE, group_FieldNameArray, strFieldValues);
		}


		
		db.CloseDatabase();
	}
	
	
	// Conatct List
	public static List<JSONObject> getContactList(Context context, String search, int category)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String orderBy = "nickname COLLATE NOCASE ASC";
		String whereClause = "group_type = '0'";
		String[]whereArgs = null;
		if( category == 0 )	// all contacts
		{
			if( CheckUtils.isEmpty(search) == false )
			{
				search = "%" + search + "%";
				whereClause += " and (nickname LIKE ? or username LIKE ? or email LIKE ?)";
				String[] args = { search, search, search };
				whereArgs = args;
			}
		}
		else if( category == 1 || category == 2 ) // doctors or patient
		{
			whereClause += " and role = ?";
			if( CheckUtils.isEmpty(search) == false )
			{
				search = "%" + search + "%";
				whereClause += " and (nickname LIKE ? or username LIKE ? or email LIKE ?)";
				if( category == 1 )
				{
					String[] args = { "1", search, search, search };
					whereArgs = args;	
				}
				else
				{
					String[] args = { "0", search, search, search,  };
					whereArgs = args;
				}
				
			}
			else
			{
				if( category == 1 )
				{
					String[] args = { "1" };
					whereArgs = args;	
				}
				else
				{
					String[] args = { "0" };
					whereArgs = args;
				}
			}
		}
		
		cursor = db.searchRecord(CONTACTLIST_TABLE, null, whereClause, whereArgs, orderBy, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static JSONObject getContact(Context context, String username, int group_type)
	{
		if( context == null )
			return new JSONObject();
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return new JSONObject();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		Cursor cursor = null;
		
		String whereClause = "mobile = ?";
//		String []whereArgs = {Contact2WUtils.changePhoneNumberFormat(username)};
		String[]whereArgs = {""};
		
		cursor = db.searchRecord(CONTACTLIST_TABLE, null, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		if( list == null || list.size() < 1 )
			return new JSONObject();
		
		return list.get(0);
	}
	
	public static boolean isExistContact(Context context, String username)
	{
		if( context == null || CheckUtils.isEmpty(username) )
			return false;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;
				
		
		String strQuery = "username = '" + username + "' and group_type = '0'";
		
		Cursor cursor = db.SearchRecords(CONTACTLIST_TABLE, strQuery);
		List<JSONObject> list =  db.getRecordData(cursor);
	
		if( list == null || list.size() < 1 )
		{
			db.CloseDatabase();
			return false;			
		}
		
		db.CloseDatabase();

		return true;
	}
	
	public static boolean isExistContact(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return false;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;
				
		String username = data.optString(Const.USERNAME, "");
		int group_type = data.optInt(Const.GROUP_TYPE, 0);
		if( CheckUtils.isEmpty(username) )
		{
			db.CloseDatabase();
			return false;
		}
	
			
		String strQuery = "username = '" + username + "' and group_type = '" + group_type +  "'";
		
		Cursor cursor = db.SearchRecords(CONTACTLIST_TABLE, strQuery);
		List<JSONObject> list =  db.getRecordData(cursor);
	
		if( list == null || list.size() < 1 )
		{
			db.CloseDatabase();
			return false;			
		}
		
		db.CloseDatabase();

		return true;
	}
	
	public static long addContact(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		long ret = -1;
		if( isExistContact(context, data) == true )
		{
			String username = data.optString(Const.USERNAME, "");
			int group_type = data.optInt(Const.GROUP_TYPE, 0);
			
			String whereClause = "username = ? and group_type = ?";
			String[] whereArgs = { username, group_type + ""};
			ret = updateRecord(context, CONTACTLIST_TABLE, data, whereClause, whereArgs);
		}
		else
		{
			ret = addRecord(context, CONTACTLIST_TABLE, data);	
		}
		
		return ret;
	}
	
	public static synchronized void updateContactList(Context context, List<JSONObject> list)
	{
		if( context == null || list == null )
			return;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		String whereClause = "username = ? and group_type = ?";
		
		SQLiteDatabase handle = db.getDB();
		if( handle == null )
		{
			db.CloseDatabase();
			return;
		}
		
		String[] whereArgs = { "", "0"};
		try {
			handle.beginTransaction();
			for(int i = 0; i < list.size(); i++ )
			{
				JSONObject data = list.get(i);
				if( data == null )
					continue;
				
				whereArgs[0] = data.optString(Const.USERNAME, "");
				
				
				Cursor cursor = handle.query(CONTACTLIST_TABLE, null, whereClause, whereArgs, null, null, null);
				
				ContentValues values = new ContentValues();
				
				Iterator<String> iter = data.keys();
				while (iter.hasNext()) {
				    String key = iter.next();
				    
				    try {
				    	String value = data.optString(key, "");
				    	values.put(key, value);		    	
				    } catch (Exception e) {
				        // Something went wrong!
				    }
				}
				
				if( cursor == null || cursor.getCount() < 1 ) // not exist
				{
					// create
					handle.insert(CONTACTLIST_TABLE, null, values);		
				} 
				else // exist
				{
					// update
					handle.update(CONTACTLIST_TABLE, values, whereClause, whereArgs);
				}						
			}
			handle.setTransactionSuccessful();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			handle.endTransaction();
		}
		
		db.CloseDatabase();
	}
		
	public static long updateNicknameInChatHistory(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		String username = data.optString(Const.USERNAME, "");
		int group_type = data.optInt(Const.GROUP_TYPE, 0);
		
		String whereClause = "to_id = ? and group_type = ?";
		String[] whereArgs = { username, group_type + ""};
		
		JSONObject nickname = new JSONObject();
		
		try {
			nickname.put(Const.NICKNAME, SoSoUtils.getDisplayName(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, nickname, whereClause, whereArgs);
	}
	
	public static long updateGroupContact(Context context, JSONObject data, String groupName)
	{
		if( context == null || data == null )
			return -1;
		
	
		String whereClause = "username = ? and group_type = 1";
		String[] whereArgs = { groupName};
		return updateRecord(context, CONTACTLIST_TABLE, data, whereClause, whereArgs);
	
	}
	
	public static void deleteContact(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return;
		
		String username = data.optString(Const.USERNAME, "");
		int group_type = data.optInt(Const.GROUP_TYPE, 0);
		
		String whereClause = "username = ? and group_type = ?";
		String[] whereArgs = { username, group_type + ""};

		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
	
		db.deleteRecord(CONTACTLIST_TABLE, whereClause, whereArgs);
		db.CloseDatabase();	
	}
	
	public static List<JSONObject> getGroupList(Context context, String search)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String orderBy = "username COLLATE NOCASE ASC";
		String whereClause = "group_type = '1'";
		String[]whereArgs = null;
		if( CheckUtils.isEmpty(search) == false )
		{
			whereClause += " and username = ?";
			String[] args = { search };
			whereArgs = args;
		}
				
		cursor = db.searchRecord(CONTACTLIST_TABLE, null, whereClause, whereArgs, orderBy, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static long addContactToGroup(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		if( isExistUserInGroup(context, data) == true )
		{
			String whereClause = "username = ? and group_name = ?";
			String[] whereArgs = { data.optString(Const.USERNAME, ""), data.optString(Const.GROUP_NAME, "") };
			return updateRecord(context, GROUP_TABLE, data, whereClause, whereArgs);
		}
		else
		{
			return addRecord(context, GROUP_TABLE, data);	
		}
	}
	
	public static List<JSONObject> deleteContactFromGroup(Context context, String roomname)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null || roomname == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		String whereClause = "group_name = ?";
		String[]whereArgs = {roomname};
		
		db.deleteRecord(GROUP_TABLE, whereClause, whereArgs);
								
		db.CloseDatabase();
		
		return list;
	}
	
	public static List<JSONObject> removeContactFromGroup(Context context, String username)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null || username == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		String whereClause = "username = ?";
		String[]whereArgs = {username};
		
		db.deleteRecord(GROUP_TABLE, whereClause, whereArgs);
								
		db.CloseDatabase();
		
		return list;
	}
	
	
	public static List<JSONObject> getContactInGroup(Context context, String roomname)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null || roomname == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		
		Cursor cursor = null;
		
		String whereClause = "group_name = ?";
		String[]whereArgs = {roomname};
		
		cursor = db.searchRecord(GROUP_TABLE, null, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static boolean isExistUserInGroup(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return false;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;
				
		String username = data.optString(Const.USERNAME, "");
		String roomname = data.optString(Const.GROUP_NAME, "");
		if( CheckUtils.isEmpty(username) || CheckUtils.isEmpty(roomname) )
		{
			db.CloseDatabase();
			return false;
		}
	
			
		String strQuery = "username = '" + username + "' and group_name = '" + roomname + "'";
		
		Cursor cursor = db.SearchRecords(GROUP_TABLE, strQuery);
		List<JSONObject> list =  db.getRecordData(cursor);
	
		if( list == null || list.size() < 1 )
		{
			db.CloseDatabase();
			return false;			
		}
		
		db.CloseDatabase();

		return true;
	}
	
	public static List<JSONObject> getGroupMembers(Context context, String groupName)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null || CheckUtils.isEmpty(groupName) )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
				
		String strQuery = "group_name = '" + groupName + "'";
		
		Cursor cursor = db.SearchRecords(GROUP_TABLE, strQuery);
		list =  db.getRecordData(cursor);
	
		db.CloseDatabase();

		return list;
	}
	
	
	// Chat History
	public static List<JSONObject> getChatHistory(Context context, String username, String search, int category)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		if( category < 0 )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String whereClause = "from_id = ?";
		String[]whereArgs = null;
	
		
		if( category == 1 ) // sms
		{	
			whereClause += " and group_type = 2";
			if( CheckUtils.isEmpty(search) == false )
			{
				whereClause += " and (to_id LIKE ? or nickname LIKE ? or body LIKE ?)";
				String[] args = {username, "%" + search + "%", "%" + search + "%", "%" + search + "%"};
				whereArgs = args;
			}
			else
			{
				String[] args = {username};
				whereArgs = args;			
			}
		}
		else // all history or doctor
		{
			whereClause += " and group_type = 0";
			
			if( CheckUtils.isEmpty(search) == false )
			{
				whereClause += " and (to_id LIKE ? or nickname LIKE ? or body LIKE ?) ";
				String[] args = {username, "%" + search + "%", "%" + search + "%", "%" + search + "%"};
				whereArgs = args;
			}
			else
			{	
				String[] args = {username};
				whereArgs = args;			
			}
		}
		
		String column [] = { Const.TO + " as username", "*", "sum(unread) as count" };
		cursor = db.searchRecord(CHATHISTORY_TABLE, column, whereClause, whereArgs, Const.TO, "id DESC", null);
		
		list = db.getRecordData(cursor);
		
		for(int i = 0; i < list.size(); i++ )
		{
			JSONObject data = list.get(i);
			if( data == null )
				continue;
			
			JSONObject contact = getContactInfo(db, "");
			AlgorithmUtils.bindJSONObject(data, contact);
			if( contact == null || CheckUtils.isEmpty(contact.optString("pname", "")) )
			{
				try {
					data.put("mobile", "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		db.CloseDatabase();
	
		return list;	
		
	}
	
	public static void deleteHistory(Context context, String from, String to, int group_type)
	{
		if( context == null )
			return;

		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return;
		
		String whereClause = "from_id = ? and to_id = ? and group_type = ?";
		String[]whereArgs = {from, to, group_type + ""};
				
		db.deleteRecord(CHATHISTORY_TABLE, whereClause, whereArgs);

		db.CloseDatabase();
	}
	public static int getUnreadTotalMessageCount(Context context, String username)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return 0;

		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return 0;
		
		Cursor cursor = null;
		
		String whereClause = "from_id = ?";
		String[]whereArgs = {username};
				
		String column [] = { "sum(unread) as count" };
		cursor = db.searchRecord(CHATHISTORY_TABLE, column, whereClause, whereArgs, null, null, null);
		
		list = db.getRecordData(cursor);
		
		db.CloseDatabase();
		
		if( list == null || list.size() < 1 )
			return 0;
		
		JSONObject result = list.get(0);
		if( result == null )
			return 0;
		
		return result.optInt("count", 0);
	}
	
	public static List<JSONObject> getIndividualChatHistory(Context context, String from, String to, int type, int id, int count)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String orderBy = "id DESC";
		String limit = "";
		
		if( id == 0 )	// latest
			limit = Integer.MAX_VALUE + "";
		else
			limit = id + "";
			
		String whereClause = "(from_id = ? and to_id = ?) and group_type = ? and id < ?";
		String[]whereArgs = {from, to, type + "", limit};
		
		cursor = db.searchRecord(CHATHISTORY_TABLE, null, whereClause, whereArgs, orderBy, count + "");
		
		list = db.getRecordData(cursor);
		
		if( type == 1 ) // group chatting
		{
//			for(int i = 0; i < com.dating.reveal.list.size(); i++ )
//			{
//				JSONObject data = com.dating.reveal.list.get(i);
//				if( data == null )
//					continue;
//				
//				int direction = data.optInt(Const.DIRECTION, 0);
//				if( direction == 0 ) // incoming
//				{
//					JSONObject contact = getContact(context, data.optString(Const.SENDER, "Unkwown"), 0);
//					if( contact == null )
//						continue;
//					try {
//						data.put(Const.AVASTAR, contact.optString(Const.AVASTAR, ""));
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}				
//			}				
		}
		
		db.CloseDatabase();
		
		return list;
	}
	
	public static JSONObject getChatContent(Context context, int id)
	{
		if( context == null )
			return new JSONObject();
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return new JSONObject();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		Cursor cursor = null;
		
		String whereClause = "id = ?";
		String[]whereArgs = {id + ""};
		
		cursor = db.searchRecord(CHATHISTORY_TABLE, null, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		if( list == null || list.size() < 1 )
			return new JSONObject();
		
		return list.get(0);
	}
	public static long addChat(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		
		return addRecord(context, CHATHISTORY_TABLE, data);
	}
	
	public static long setFlagReadChat(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		String whereClause = Const.ID + " = ?";
		String[] whereArgs = { data.optString(Const.ID, "") };
		
		JSONObject setdata = new JSONObject();
		
		try {
			data.put(Const.UNREAD, 0);
			setdata.put(Const.UNREAD, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, setdata, whereClause, whereArgs);
	}
	
	public static long setFlagSendChat(Context context, JSONObject data, int sent)
	{
		if( context == null || data == null )
			return -1;
		
		String whereClause = Const.ID + " = ?";
		String[] whereArgs = { data.optString(Const.ID, "") };
		
		try {
			data.put(Const.SENT, sent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, data, whereClause, whereArgs);
	}
	
	public static long setFlagFileState(Context context, long fileid, int sent)
	{
		if( context == null || fileid == 0 )
			return -1;
		
		String whereClause = Const.SENDER + " = ?";
		String[] whereArgs = { fileid + "" };
		
		JSONObject data = new JSONObject();
		
		try {
			data.put(Const.SENT, sent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, data, whereClause, whereArgs);
	}
	
	public static long setFilePath(Context context, long fileid, String path)
	{
		if( context == null || fileid == 0 )
			return -1;
		
		String whereClause = Const.SENDER + " = ?";
		String[] whereArgs = { fileid + "" };
		
		JSONObject data = new JSONObject();
		
		try {
			data.put(Const.BODY, path);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, data, whereClause, whereArgs);
	}
	
	public static JSONObject getFileChatID(Context context, long fileid)
	{
		if( context == null )
			return null;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return null;
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		Cursor cursor = null;
		
		String whereClause = "sender = ?";
		String[]whereArgs = {fileid + ""};
		
		cursor = db.searchRecord(CHATHISTORY_TABLE, null, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		if( list == null || list.size() < 1 )
			return null;
		
		return list.get(0);
	}
	
	public static long setFlagReadChatToAll(Context context, String username, int group_type)
	{
		if( context == null || CheckUtils.isEmpty(username) )
			return -1;
		
		String whereClause = Const.TO + " = ? and " + Const.GROUP_TYPE + " = ?";
		String[] whereArgs = { username, group_type + ""};
		
		JSONObject data = new JSONObject();
		try {
			data.put(Const.UNREAD, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return updateRecord(context, CHATHISTORY_TABLE, data, whereClause, whereArgs);
	}
	
	public static int getUnreadMessageCount(Context context, String username, boolean self)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return 0;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return 0;
		
		Cursor cursor = null;
		
		String whereClause = "";
		if( self == true )
			whereClause = "to_id = ?";
		else
			whereClause = "to_id != ?";
		String[]whereArgs = {username};
			
		String column [] = { "sum(unread) as count"};
		cursor = db.searchRecord(CHATHISTORY_TABLE, column, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
		
		if( list == null || list.size() < 1 )
			return 0;
		
		JSONObject data = list.get(0);
		int count = data.optInt("count", 0);
		
		return count;
	}
	
	public static long addRecord(Context context, String table, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;
		
		long result = db.addRecord(table, data);
        
		db.CloseDatabase();
		
		return result;
	}
	
	public static long updateRecord(Context context, String table, JSONObject data, String whereClause, String[]whereArgs)
	{
		if( context == null || data == null )
			return -1;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;
		
		long result = db.updateRecord(table, data, whereClause, whereArgs);
        
		db.CloseDatabase();
		
		return result;
	}

	
	public static List<JSONObject> getNotificationList(Context context, int id, int count)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String orderBy = "id DESC";
		String limit = "";
		
		if( id == 0 )	// latest
			limit = Integer.MAX_VALUE + "";
		else
			limit = id + "";
			
		String whereClause = "id < ?";
		String[]whereArgs = {limit};
		
		cursor = db.searchRecord(NOTIFICATIONLIST_TABLE, null, whereClause, whereArgs, orderBy, count + "");
		
		list = db.getRecordData(cursor);
		
		db.CloseDatabase();
		
		return list;
	}
	
	
	public static List<JSONObject> getUnsendChatHistory(Context context, String from, String to, int type)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor cursor = null;
		
		String whereClause = "(from_id = ? and to_id = ?) and group_type = ? and sent = 0 and direction = 1 and type = 0";
		String[]whereArgs = {from, to, type + ""};
		
		cursor = db.searchRecord(CHATHISTORY_TABLE, null, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
	
		db.CloseDatabase();
		
		return list;
	}
}
