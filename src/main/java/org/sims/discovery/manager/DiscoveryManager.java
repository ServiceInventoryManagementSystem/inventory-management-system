package org.sims.discovery.manager;

import io.reactivex.disposables.Disposable;
import org.sims.discovery.IDiscoveryService;
import org.sims.discovery.IDiscoveryService.DiscoverySettings;
import org.sims.discovery.models.IService;

import java.util.*;
import java.util.stream.Collectors;


// Class that manages multiple discovery mechanisms
public class DiscoveryManager{
  

  List<IDiscoveryService> discoveryServices = new ArrayList<IDiscoveryService>();
  Set<Class<? extends IDiscoveryService>> serviceClasses = new HashSet<Class<? extends IDiscoveryService>>();

  HashMap<Class<? extends IDiscoveryService>, DiscoverySettings> serviceSettings = new HashMap<Class<? extends IDiscoveryService>, DiscoverySettings>();
  // Map IService to database entry 
  Map<IService, Long> databaseMap = new HashMap<IService, Long>(50);
  
  // Map UUID to IService
  Map<String, IService> serviceMap = new HashMap<String, IService>(50);

  //List of disposable subscriptions
  List<Disposable> subscriptions = new ArrayList<Disposable>();
  

  private float probeInterval;
  private Thread probingThread;
  private boolean runProbe = false;

  private IResourceManager resourceManager;

  // is the manger initialized?
  private boolean init = false;

  // probeInterval is a prefered value
  public DiscoveryManager(IResourceManager resourceManager, float probeInterval){
    this.resourceManager = resourceManager;
    this.probeInterval = probeInterval;
  }
  
  public DiscoveryManager(IResourceManager resourceManager){
    this(resourceManager, 10);
  }

  // Register a discovery mechanism to be used, if manager is already initialized all otehr mechanisms
  // have to be disposed before calling initAll
  public void registerDiscovery(Class<? extends IDiscoveryService> serviceClass, DiscoverySettings settings){
    
    serviceClasses.add(serviceClass);
    serviceSettings.put(serviceClass, settings);
  }

  public void registerDiscovery(Class<? extends IDiscoveryService> serviceClass){
    registerDiscovery(serviceClass, null);
  }

  public void initAll(){
    if(init){
      throw new IllegalStateException("Manager has already been initialized");
    }

    this.resourceManager.getOwnedServices();


    for(Class<? extends IDiscoveryService> discovery : serviceClasses){
      try{
        IDiscoveryService service = discovery.getConstructor(new Class[]{DiscoverySettings.class}).newInstance(serviceSettings.get(discovery));
        discoveryServices.add(service);
        subscriptions.add(service.serviceAdded().subscribe(this::addService));
        subscriptions.add(service.serviceUpdated().subscribe(this::updateService));
        subscriptions.add(service.serviceRemoved().subscribe(this::removeService));

      }catch(Exception e) {
        System.err.println(e);
      }
    }
    init = true;
    // Set up probing thread
    setUpProbing();
  }

  private void setUpProbing(){
    if(probingThread != null){
      stopProbing();
    }
    runProbe = true;
    probingThread = new Thread(){
      public void run(){
        try{
          while(runProbe){
            probeAll();
            sleep((long) (1000*probeInterval));
          }
        } catch(Exception e) {
          System.err.println(e);
          stopProbing();
        }
      }
    };
    probingThread.start();
  }

  private void stopProbing(){
    runProbe = false;
    probingThread = null;
  }

  private void probeAll(){
    for(IDiscoveryService service : discoveryServices){
      service.probeServices().subscribe((List<IService> services) -> {
        services.forEach((IService s) -> {
          this.addService(s);
        });
      });
    }
  }

  public void startAll(){
    for(IDiscoveryService service : discoveryServices){
      service.start();
    }
  }

  public void stopAll(){
    for(IDiscoveryService service : discoveryServices){
      service.stop();
    }
  }

  // Disposes all service discovery
  public void disposeAll(){
    init = false;
    stopProbing();
    for(Disposable subscription : subscriptions){
      subscription.dispose();
    }
    
    for(IDiscoveryService service : discoveryServices){
      service.dispose();
    }
    discoveryServices = new ArrayList<IDiscoveryService>(discoveryServices.size());
  }

  //Returns a list of active discovery services
  public List<IDiscoveryService> getRunning(){
    return discoveryServices.stream()
      .filter((IDiscoveryService e) -> e.isRunning())
      .collect(Collectors.toList());
  }

  public String addService(IService service){
    System.out.println("Adding service... " + service.getName());
    resourceManager.save(service).subscribe((IService s) -> {
      System.out.println("Service saved to db id = " + s.getId() + " " + s.getLocalReference());
      serviceMap.put(service.getLocalReference(), service);
    });
    return "";
  }

  public void updateService(IService service){
    System.out.println("Updating service...");
    addService(service);
  }

  public void removeService(IService service){
    System.out.println("Removing service... " + service.getName());
    resourceManager.removeService(service);
  }

}