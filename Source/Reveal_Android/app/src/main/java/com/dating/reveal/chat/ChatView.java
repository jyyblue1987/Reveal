package com.dating.reveal.chat;

import org.json.JSONObject;

import java.util.List;


public interface ChatView {
	public void showChatHistory(List<JSONObject> list);
	public void addChat(JSONObject message);
	public void addChatHistory(List<JSONObject> list);
	public void displayUnreadMessageCount(int count);
}
