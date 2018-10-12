package com.esiri.social.resource;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esiri.social.dao.UserDao;
import com.esiri.social.dao.UserDaoImpl;
import com.esiri.social.db.Database;
import com.esiri.social.db.InMemoryDB;
import com.esiri.social.model.Post;

public class UserResourceTest {
	
	
	private UserResource underTest;
	private Database db;
	
	@Before
	public void setUpResource() {
		db = new InMemoryDB();
		UserDao ud = new UserDaoImpl(db);
		underTest = new UserResource(ud);
	}

	@Test
	public void testGetUserMessagesNullUser() {
		Response result = underTest.getUserMessages(null);
		Assert.assertTrue(result.getStatus() == Status.NOT_FOUND.getStatusCode());
	}
	
	@Test
	public void testGetUserMessagesEmptyString() {
		Response result = underTest.getUserMessages(null);
		Assert.assertTrue(result.getStatus() == Status.NOT_FOUND.getStatusCode());
	}
	
	@Test
	public void testGetUserMessagesNonExistentUser() {
		Response result = underTest.getUserMessages("bob");
		Assert.assertTrue(result.getStatus() == Status.NOT_FOUND.getStatusCode());
	}
	
	@Test
	public void testGetUserMessagesExistingUser() {
		String content = "first post"; 
		db.addMessage("bob", content);
		Response result = underTest.getUserMessages("bob");
		Assert.assertTrue(result.getStatus() == Status.OK.getStatusCode());
		
		@SuppressWarnings("unchecked")
		List<Post> resultList = (List<Post>) result.getEntity();
		Assert.assertTrue(resultList.get(0).getContent().equals(content));
	}
	
	@Test
	public void testAddMessageUserNameNullContentNull() {
		
		Response result = underTest.addMessage("bob", "");
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		
		result = underTest.addMessage("bob", null);
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		result = underTest.addMessage(null, "valid content");
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		result = underTest.addMessage("bob", "valid content");
		Assert.assertTrue(result.getStatus() == Status.CREATED.getStatusCode());
	}

	@Test
	public void testGetUserTimelineInValidAndNonExistentUser() {
		
		//test non existent bob
		Response result = underTest.getUserTimeline("bob");
		Assert.assertTrue(result.getStatus() == Status.NOT_FOUND.getStatusCode());
		
		//test null user
		result = underTest.getUserTimeline(null);
		Assert.assertTrue(result.getStatus() == Status.NOT_FOUND.getStatusCode());
		
	}

	@Test
	public void testGetUserTimelineWithNoFollowing() {
		//add user in db
		db.addMessage("bob", "first post");
		
		Response result = underTest.getUserTimeline("bob");
		Assert.assertTrue(result.getStatus() == Status.OK.getStatusCode());
		
		@SuppressWarnings("unchecked")
		List<Post> resultList = (List<Post>) result.getEntity();
		Assert.assertTrue(resultList.isEmpty());
	}
	
	@Test
	public void testGetUserTimelineWithOneOrMoreFollowing() {
		//add valid users in db
		db.addMessage("bob", "first post");
		String maryContent = "marys first post";
		db.addMessage("mary", maryContent);
		db.follow("bob", "mary");
		
		Response result = underTest.getUserTimeline("bob");
		Assert.assertTrue(result.getStatus() == Status.OK.getStatusCode());

		@SuppressWarnings("unchecked")
		List<Post> resultList = (List<Post>) result.getEntity();
		Assert.assertTrue(resultList.size()==1);
		Assert.assertTrue(resultList.get(0).getContent().equals(maryContent));
	}
	
	@Test
	public void testFollowUserWithNullFollowerAndFollowew() {
		Response result = underTest.followUser(null, null);
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		result = underTest.followUser("bob", null);
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		result = underTest.followUser(null, "bob");
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void testFollowUserWithValidFollowerAndFollowee() {
		//add valid users in db
		db.addMessage("bob", "first post");
		String maryContent = "marys first post";
		db.addMessage("mary", maryContent);
		
		Response result = underTest.followUser("bob", "mary");
		Assert.assertTrue(result.getStatus() == Status.CREATED.getStatusCode());
	}

	@Test
	public void testUnFollowUserWithNullFollowerAndFollowew() {
		Response result = underTest.unfollowUser(null, null);
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		result = underTest.unfollowUser("bob", null);
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
		
		result = underTest.unfollowUser(null, "bob");
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void testUnFollowUserWithValidFollowerAndFollowee() {
		//add valid users in db
		db.addMessage("bob", "first post");
		String maryContent = "marys first post";
		db.addMessage("mary", maryContent);
		db.follow("bob", "mary"); //bob initially following mary
		
		//first unfollow attempt should be fine
		Response result = underTest.unfollowUser("bob", "mary");
		Assert.assertTrue(result.getStatus() == Status.OK.getStatusCode());
		
		//second attempt at unnfollowing an already unfollowed user
		result = underTest.unfollowUser("bob", "mary");
		Assert.assertTrue(result.getStatus() == Status.BAD_REQUEST.getStatusCode());
	}


}
