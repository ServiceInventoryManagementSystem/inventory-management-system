package org.sims.discovery;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.sims.discovery.models.IService;

import java.util.List;

public interface IDiscoveryService{

  // emitts when service is discovered
  public Observable<IService> serviceAdded();
  // emitts when service is updated
  public Observable<IService> serviceUpdated();
  // emitts when service no longer available
  public Observable<IService> serviceRemoved();
  // Probes one time and returns services found
  public Single<List<IService>> probeServices();

  //Returns true if service discovery has started an is running
  public boolean isRunning();
  // Should start notifying
  public void start(); // Starts 'discovery
  // Should stop notifying
  public void stop();
  // Should dispose of all resources used by the service discoverer, should not be able to recover from this
  public void dispose(); 

  // String that describes what kind of service this service discovery class will emitt
  public String getTypeDescriptor();


  public class DiscoverySettings{
    public DiscoverySettings(){}
  }
}