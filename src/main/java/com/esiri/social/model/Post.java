package com.esiri.social.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Post implements Comparable<Post>{
	private String content;
	private User user; 
	private Instant timeStamp;
	
	public Post(User user, String content) {
		this.content = content;
		this.user = user;
		this.timeStamp = Instant.now();
	}
	
	public String getContent() {
		return content;
	}
	
	public String getUser() {
		return user.toString();
	}
	
	public String getDate() {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(timeStamp, ZoneOffset.UTC);
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
	}
	
	@Override
	public String toString() {
		return "{ user: \""+ user  + "\" , post: \""+ content +"\" , time: "+ getDate()+ "}";
	}

	public void setInstant(Instant instant) {
		timeStamp = instant;
	}
	
//	public Instant getInstant() {
//		return timeStamp;
//	}

	@Override
	public int compareTo(Post p2) {
		return p2.timeStamp.compareTo(this.timeStamp);
	}
}
