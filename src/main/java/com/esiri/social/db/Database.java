package com.esiri.social.db;

import java.util.List;

import com.esiri.social.model.Post;
import com.esiri.social.model.User;

public interface Database {

	List<Post> getUserMessages(String user);

	boolean addMessage(String user, String content);

	List<Post> getUserTimeline(String user);

	boolean follow(String userA, String userB);

	boolean unfollow(String userA, String userB);
	
	
}
