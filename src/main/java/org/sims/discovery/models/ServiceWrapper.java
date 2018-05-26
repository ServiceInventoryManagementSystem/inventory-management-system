package org.sims.discovery.models;

import org.sims.utils.MagicWrapper;

public class ServiceWrapper{
  public static IService combine(IService... services){
    return MagicWrapper.createProxy(IService.class, true, (Object[])services);
  }
}

