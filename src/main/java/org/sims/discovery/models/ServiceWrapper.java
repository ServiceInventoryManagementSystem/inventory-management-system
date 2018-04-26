package org.sims.discovery.models;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.sims.utils.MagicWrapper;

public class ServiceWrapper{
  
  IService combined;
  public static IService combine(IService... services){
    return MagicWrapper.createProxy(IService.class, true, (Object[])services);
  }
}

