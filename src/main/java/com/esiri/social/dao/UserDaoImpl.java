package com.esiri.social.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esiri.social.db.Database;
import com.esiri.social.model.Post;

@Component
public class UserDaoImpl implements UserDao{
	
	private Database db;
	
	@Autowired
	public UserDaoImpl(Database db) {
		this.db = db;
	}
	
	@Override
	public List<Post> getUserMessages(String user) {
		return db.getUserMessages(user);
	}

	@Override
	public boolean addMessage(String user, String content) {
		return db.addMessage(user, content);
	}

	@Override
	public List<Post> getUserTimeline(String user) {
		return db.getUserTimeline(user);
	}

	@Override
	public boolean follow(String userA, String userB) {
		return db.follow(userA, userB);
	}

	@Override
	public boolean unfollow(String userA, String userB) {
		return db.unfollow(userA, userB);
	}

}
