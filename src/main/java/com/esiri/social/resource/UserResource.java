package com.esiri.social.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.esiri.social.config.Config;
import com.esiri.social.dao.UserDao;
import com.esiri.social.model.Post;

@Path("users")
public class UserResource {

	private UserDao userDao;
	
	@Autowired
	public UserResource(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@GET
	@Path("/{userName}/messages")
	@Produces("application/json")
	public Response getUserMessages(@PathParam("userName") String userName) {
		List<Post> userPosts = userDao.getUserMessages(userName);
		
		if(userPosts == null) {
			return Response.status(Status.NOT_FOUND).entity(userName+ " not found").build();
		}else if(userPosts.isEmpty()) {
			return Response.status(Status.OK).entity(userName+ " has not posted").build();
		}
		
		return Response.ok(userPosts).build();
	}
	
	@POST
	@Path("/{userName}/messages")
	@Consumes("application/x-www-form-urlencoded")
	public Response addMessage(@PathParam("userName") String userName, @FormParam("content") String content) {
		
		if(userName == null ||content == null || content.equals("")) {
			return Response.status(Status.BAD_REQUEST).entity("no content specified").build();
		}
		
		if (content.length() > Config.MAX_POST_LENGTH) {
			return Response.status(Status.BAD_REQUEST).entity("please reduce content size").build();
		}
		
		boolean result  = userDao.addMessage(userName, content);
		return result? Response.created(null).entity("successfully posted message by "+userName).build(): Response.
				status(Status.INTERNAL_SERVER_ERROR).entity("an error occured posting message, please try again later").build();	
	}
	
	@GET
	@Path("/{userName}/timeline")
	@Produces("application/json")
	public Response getUserTimeline(@PathParam("userName") String userName) {
		
		List<Post> timeline = userDao.getUserTimeline(userName);
		if(timeline == null) {
			return Response.status(Status.NOT_FOUND).entity("user not registered").build();
		}

		return Response.ok().entity(timeline).build();	
	}
	
	@POST
	@Path("/{follower}/{followee}")
	@Produces("text/plain")
	public Response followUser(@PathParam("follower") String follower, @PathParam("followee") String followee) {
		boolean result = userDao.follow(follower, followee);
		if(result) {
			return Response.created(null).entity(follower+" following "+followee).build();
		}else {
			return Response.status(Status.BAD_REQUEST).entity("could not complete follow request").build();
		}
	}
	
	@DELETE
	@Path("/{follower}/{followee}")
	public Response unfollowUser(@PathParam("follower") String follower, @PathParam("followee") String followee) {
		boolean result = userDao.unfollow(follower, followee);
		if(result) {
			return Response.ok().entity(follower+" unfollowed "+followee).build();
		}else {
			return Response.status(Status.BAD_REQUEST).entity("could not complete unfollow request").build();
		}
	}
}
