package com.esiri.social.db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.esiri.social.config.Config;
import com.esiri.social.model.Post;
import com.esiri.social.model.User;

@Component
public class InMemoryDB implements Database {

	private Map<String, User> users;
	
	public InMemoryDB(Map<String, User> users) {
		this.users = users;
	}
	
	public InMemoryDB() {
		this(new ConcurrentHashMap<String, User>());
	}
	
	
	@Override
	public List<Post> getUserMessages(String user) {
		if (user == null) { 
			return null;
		}
		
		user = normalise(user);
		User u = users.get(user);
		if (u == null) {
			return null;
		}
		return u.getPosts();
	}

	@Override
	public boolean addMessage(String user, String content) {
		if(user == null || content == null) {
			return false;
		}
		
		if (content.length() == 0 || content.length() > Config.MAX_POST_LENGTH) {
			return false;
		}
		
		user = normalise(user);
		User u = users.get(user);
		if (u == null) {
			u = new User(user);
			users.put(user, u);
		}
		
		u.addPost(new Post(u, content));
		return true;
	}

	@Override
	public List<Post> getUserTimeline(String user) {
		
		if(user == null) {
			return null;
		}
		
		user = normalise(user);
		User a = users.get(user);
		if(a == null) {
			return null;
		}
		return a.getTimeline();		
	}

	@Override
	public boolean follow(String userA, String userB) {
		
		if(userA == null || userB == null) {
			return false;
		}
		
		userA = normalise(userA);
		userB = normalise(userB);
		
		// user cannot follow himself/herself
		if(userA.equals(userB)) {
			return false;
		}
		
		User a = users.get(userA);
		User b = users.get(userB);
		if (a == null || b == null) {
			return false;
		}
		return a.follow(b);
	}

	@Override
	public boolean unfollow(String userA, String userB) {
		
		if(userA == null || userB == null) {
			return false;
		}
		
		userA = normalise(userA);
		userB = normalise(userB);
		
		// user cannot unfollow himself/herself
		if(userA.equals(userB)) {
			return false;
		}
		
		User a = users.get(userA);
		User b = users.get(userB);
		if (a == null || b == null) {
			return false;
		}
		return a.unfollow(b);
	}
	
	private String normalise(String value) {
		return value.toLowerCase().trim();
	}

}
