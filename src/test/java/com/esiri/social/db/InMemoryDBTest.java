package com.esiri.social.db;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.esiri.social.model.Post;

public class InMemoryDBTest {
	
	private Database underTest;
	
	
	@Test
	public void testGetUserMessages() {
		underTest = new InMemoryDB();
		
		// null user
		List<Post> posts = underTest.getUserMessages(null);
		Assert.assertTrue(posts == null);
		
		//  non existent user
		posts = underTest.getUserMessages("bob");
		Assert.assertTrue(posts == null);
		
		// existing user
		String content = "first post";
		underTest.addMessage("bob", content);
		
		posts = underTest.getUserMessages("bob");
		Assert.assertTrue(!posts.isEmpty());
		Assert.assertTrue(posts.size() == 1);
		Assert.assertTrue(posts.get(0).getContent().equals(content));
	}
	
	
	
	@Test
	public void testAddMessage() {
		underTest = new InMemoryDB();
		
		// null message and invalid user
		boolean result = underTest.addMessage("bob", null);
		Assert.assertTrue(result == false);
		
		// valid message and valid user
		result = underTest.addMessage("bob", "first post");
		Assert.assertTrue(result == true);
		List<Post> posts = underTest.getUserMessages("bob");
		Assert.assertTrue(!posts.isEmpty());
		Assert.assertTrue(posts.size() == 1);
		Assert.assertTrue(posts.get(0).getContent().equals("first post"));
		
		// null message and valid user
		result = underTest.addMessage("bob", null);
		Assert.assertTrue(result == false);
	}

	@Test
	public void testGetUserTimeline() {
		underTest = new InMemoryDB();
		
		// null user
		List<Post> result = underTest.getUserTimeline(null);
		Assert.assertTrue(result == null);
		
		// nonexistent user
		result = underTest.getUserTimeline("bob");
		Assert.assertTrue(result == null);
		
		// existing user not following any user
		underTest.addMessage("bob", "first post");
		result = underTest.getUserTimeline("bob");
		Assert.assertTrue(result.isEmpty());
		
		// existing user following other user
		underTest.addMessage("kate", "kates first post");
		underTest.follow("bob", "kate");
		result = underTest.getUserTimeline("bob");
		Assert.assertTrue(!result.isEmpty());
		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getContent().equals("kates first post"));
	}

	@Test
	public void testFollow() {
		underTest = new InMemoryDB();
		
		// null users 
		boolean result = underTest.follow(null, null);
		Assert.assertTrue(result == false);
		
		// one null user
		result = underTest.follow("bob", null);
		Assert.assertTrue(result == false);
		
		result = underTest.follow(null, "bob");
		Assert.assertTrue(result == false);
		
		// non null unregistered user
		result = underTest.follow("bob", "kate");
		Assert.assertTrue(result == false);
		
		// one registered user and one unregistered user
		underTest.addMessage("bob", "first post");
		
		result = underTest.follow("bob", "kate");
		Assert.assertTrue(result == false);
		
		result = underTest.follow("kate", "bob");
		Assert.assertTrue(result == false);
		
		// two valid users
		underTest.addMessage("kate", "kates first post");
		
		result = underTest.follow("bob", "kate");
		Assert.assertTrue(result == true);
		
		result = underTest.follow("kate", "bob");
		Assert.assertTrue(result == true);
		
		// user trying to follow him/herself
		result = underTest.follow("kate", "kate");
		Assert.assertTrue(result == false);
	}

	@Test
	public void testUnFollow() {
		underTest = new InMemoryDB();
		
		// null users 
		boolean result = underTest.unfollow(null, null);
		Assert.assertTrue(result == false);
		
		// one null user
		result = underTest.unfollow("bob", null);
		Assert.assertTrue(result == false);
		
		result = underTest.unfollow(null, "bob");
		Assert.assertTrue(result == false);
		
		// non null unregistered user
		result = underTest.unfollow("bob", "kate");
		Assert.assertTrue(result == false);
		
		// one registered user and one unregistered user
		underTest.addMessage("bob", "first post");
		
		result = underTest.unfollow("bob", "kate");
		Assert.assertTrue(result == false);
		
		result = underTest.unfollow("kate", "bob");
		Assert.assertTrue(result == false);
		
		// two valid users not following 
		underTest.addMessage("kate", "kates first post");
		
		result = underTest.unfollow("bob", "kate");
		Assert.assertTrue(result == false);
		
		result = underTest.unfollow("kate", "bob");
		Assert.assertTrue(result == false);
		
		// two valid users following each other
		underTest.follow("bob", "kate");
		underTest.follow("kate", "bob");
		
		result = underTest.unfollow("bob", "kate");
		Assert.assertTrue(result == true);
		
		result = underTest.unfollow("kate", "bob");
		Assert.assertTrue(result == true);
		
		// user trying to unfollow him/herself
		result = underTest.unfollow("kate", "kate");
		Assert.assertTrue(result == false);
	}

}
