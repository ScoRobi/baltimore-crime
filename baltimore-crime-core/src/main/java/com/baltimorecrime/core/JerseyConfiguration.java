package com.baltimorecrime.core;

import com.baltimorecrime.core.resource.MapResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by Scott Robinson on 7/10/16.
 */
@Component
public class JerseyConfiguration extends ResourceConfig {

  public JerseyConfiguration() {
    register(MapResource.class);
  }

}
