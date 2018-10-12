package com.esiri.social.dao;

import java.util.List;

import com.esiri.social.model.Post;

public interface UserDao {
	
	public List<Post> getUserMessages(String user);
	
	public boolean addMessage(String user, String content);
	
	public List<Post> getUserTimeline(String user);
	
	public boolean follow(String userA, String userB);
	
	public boolean unfollow(String userA, String userB);
}
