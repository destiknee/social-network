package com.esiri.social.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class User {
	private final String name;
	private final Set<User> following;
	private final List<Post> posts;
	
	public User(String name) {
		this.name = name;
		following = new LinkedHashSet<>();
		posts = new ArrayList<>();
		
	}
	
	public boolean follow(User u) {
		return following.add(u);
	}
	
	public boolean unfollow(User u) {
		return following.remove(u);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	/** compares using user names for simplicity, assuming two users will not have the same name */
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}else if (this == obj) {
			return true;
		}else if (obj.getClass() != getClass()) {
			return false;
		}
		User other = (User) obj;
		return name.equals(other.name);
	}
	
	public List<Post> getTimeline(){
		return following.parallelStream().map(u -> u.getPosts()).reduce(new ArrayList<>(), User::mergePosts, User::mergePosts);
	}
	
	/* merge list utility function merges an already sorted list of posts
	 * return a reverse chronologically sorted list containing posts from a and b*/
	private static List<Post> mergePosts(List<Post> a, List<Post> b){
		if(a == null) return b;
		if(b == null) return a;
		
		int i = 0, j = 0;
		Post p1, p2;
		List<Post> list = new ArrayList<>();
		while(i < a.size() && j < b.size()) {
			p1 = a.get(i); p2  = b.get(j);
			if(p1.compareTo(p2) < 0) {
				list.add(p1);
				i++;
			}else {
				list.add(p2);
				j++;
			}
		}
		
		List<Post> remaining = a;
		if(i == a.size()) {
			i = j;
			remaining = b;
		}
		
		for(;i < remaining.size(); i++) {
			list.add(remaining.get(i));
		}
		return list;
	}
	
	public List<Post> getPosts() {
		return posts;
	}

	public void addPost(Post post) {
		// add at the beginning of the List instead of end, to preserve chronology
		posts.add(0, post);
	}
	
	public String toString() {
		return name;
	}
}
