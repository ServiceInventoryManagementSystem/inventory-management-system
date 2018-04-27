package org.sims.discovery.manager;

import java.util.List;

import org.sims.discovery.models.IService;

import io.reactivex.Observable;
import io.reactivex.Single;

// Interface for resource manager, provides a way to save and fetch active services
public interface IResourceManager{
  public Single<String> save(IService service);
  public Single<IService> getById(String id);
  public Single<List<IService>> getByIds(String... id);

  // Return a list of services managed by this system
  public Single<List<IService>> getOwnedServices();

  // Finish anything important
  public void dispose();
}