package com.dating.reveal.chat;



public interface ImageSelectView {
	public void doUploadImageWithCrop();
	public void doUploadImageWithCrop(int x, int y, int gravity);
	public void doUploadImage();
	public void onTakePicture(String path, String tag, int source);
}
