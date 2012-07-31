package org.hbilab.daumapi;

import android.graphics.Bitmap;

public class BookItem {
	private String title; //title
	private String link; //link
	private String image; //cover_s_url
	private String price; //sale_price
	
	public BookItem(String title, String link, String image, String price) {
		super();
		this.title = title;
		this.link = link;
		this.image = image;
		this.price = price;
	}
	
	
	public BookItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
