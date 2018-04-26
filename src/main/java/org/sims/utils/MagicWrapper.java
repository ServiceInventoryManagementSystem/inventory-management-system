package org.sims.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sims.discovery.models.IHasId;
import org.sims.discovery.models.IService;


public class MagicWrapper implements InvocationHandler{

  private Object[] objects;
  private boolean continueIfNull = false;

  public MagicWrapper(Object... objects){
    this.objects = objects;
  }

  public MagicWrapper(boolean continueIfNull, Object... objects){
    this(objects);
    this.continueIfNull = continueIfNull;
  }
  

  private Object _invoke(Object proxy, Object who, Method m, Object[] args) 
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    return m.invoke(who, args);
  }


  public Object invoke(Object proxy, Method m, Object[] args){
    for(int i = 0; i< objects.length; i++){
      try{
        Object obj = _invoke(proxy, objects[i], m, args); 
        if(obj == null){
          obj = "null";
          if(continueIfNull){
            continue;
          }
        }
        return obj;
      } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
        continue;
      }
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public static <T> T createProxy(Class<? extends T> type, Boolean continueIfNull, Object... objects) throws IllegalArgumentException{
    return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{IHasId.class, type}, new MagicWrapper(continueIfNull, objects)));
  }

  public static <T> T createProxy(Class<? extends T> type, Object... objects) throws IllegalArgumentException{
    return createProxy(type, false, objects);
  }
}