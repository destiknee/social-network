package com.esiri.social.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.esiri.social.resource.UserResource;

@Component
public class SocialNetworkConfig extends ResourceConfig{

	public SocialNetworkConfig () {
		register(UserResource.class);
	}
}
