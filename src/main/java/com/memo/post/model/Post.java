package com.memo.post.model;

import java.util.Date;

public class Post {
	private int id;
	private int userId;
	private int subject;
	private int content;
	private int imagePath;
	private Date createdAt;
	private Date updatedAt;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public int getContent() {
		return content;
	}
	public void setContent(int content) {
		this.content = content;
	}
	public int getImagePath() {
		return imagePath;
	}
	public void setImagePath(int imagePath) {
		this.imagePath = imagePath;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAttedAt() {
		return updatedAt;
	}
	public void setUpdatedAttedAt(Date updatedAttedAt) {
		this.updatedAt = updatedAt;
	}
}
