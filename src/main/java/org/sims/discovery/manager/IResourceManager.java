package org.sims.discovery.manager;

import java.util.HashMap;
import java.util.List;

import org.sims.discovery.models.IService;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

// Interface for resource manager, provides a way to save and fetch active services
public interface IResourceManager{
  public Single<IService> save(IService service);
  public Single<IService> getById(String id);
  public Single<List<IService>> getByIds(String... id);
  public Completable removeService(IService service);
  public Completable removeService(String id);
  // Return a list of services managed by this system
  public Single<List<IService>> getOwnedServices();
  public void stopWatching(IService service);
  public void stopWatching(String id);

  // Finish anything important
  public void dispose();
}