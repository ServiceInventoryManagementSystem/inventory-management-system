package org.sims.discovery.ws;

import com.ms.wsdiscovery.WsDiscoveryConstants;
import com.ms.wsdiscovery.WsDiscoveryServer;
import com.ms.wsdiscovery.exception.WsDiscoveryException;
import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;
import com.ms.wsdiscovery.servicedirectory.interfaces.IWsDiscoveryServiceDirectory;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

import org.hibernate.cfg.annotations.Nullability;
import org.sims.discovery.IDiscoveryService;
import org.sims.discovery.models.IService;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;


public class WsDiscovery implements IDiscoveryService{
  private WsDiscoveryServer server;
  
  private Subject<IService> wsSubject;
  private boolean alive = false;
  
  private Subject<IService> serviceAddSubject = PublishSubject.create();
  private Subject<IService> serviceRemoveSubject = PublishSubject.create();
  private Subject<IService> serviceUpdateSubject = PublishSubject.create();
  
  private HashMap<String, IService> serviceMap = new HashMap<String, IService>();


  private boolean run = false;
  private Thread notifyThread;
  private Thread probeThread;
  private WsSettings settings;
  public WsDiscovery(DiscoverySettings settings){
    WsDiscoveryConstants.loggerLevel = Level.OFF;
    this.settings = (WsSettings)settings;
    try{
      server = new WsDiscoveryServer();
      server.start();
      alive = true;
    } catch (WsDiscoveryException e) {
      System.err.println(e);
    }
  }
  public Observable<IService> serviceAdded(){
    return serviceAddSubject;/*.distinct((IService s) -> {
      return s.getLocalReference();
    });*/
  }
  
  public Observable<IService> serviceRemoved(){
    return serviceRemoveSubject;/*.distinct((IService s) -> {
      return s.getLocalReference();
    });*/
  }

  public Observable<IService> serviceUpdated(){
    return serviceUpdateSubject;
  }

  public boolean isRunning(){
    return run;
  }

  public void start(){
    if(notifyThread != null){
      stop();
    }
    run = true;
    if(alive){
      try{
        probeServices();
      } catch(Exception e){
        System.err.println(e);
      }
      
      notifyThread = new Thread(){
        public void run(){
          while(run){
            HashMap<String, WsDiscoveryService> activeServices = new HashMap<String, WsDiscoveryService>(serviceMap.size());
            try{
              IWsDiscoveryServiceDirectory directory = server.getServiceDirectory();
              for(WsDiscoveryService service : directory.matchAll()){
                String UUID = service.getEndpointReference().getAddress().toString();
                // Check if the given service actually exists
                if(directory.findService(UUID) == null){
                  // Remove the service from the service directory if it doesn't
                  directory.remove(UUID);
                }else{
                  activeServices.put(UUID, service);
                }
              }


              Set<String> deadServices = new HashSet<String>(serviceMap.keySet());
              deadServices.removeAll(activeServices.keySet());
              
              Set<String> newServices = new HashSet<String>(activeServices.keySet());
              newServices.removeAll(serviceMap.keySet());

              for(String newService : newServices){
                IService service = new WsService(activeServices.get(newService));
                serviceMap.put(newService, service);
                serviceAddSubject.onNext(service);
              }

              for(String deadService : deadServices){
                IService service = serviceMap.get(deadService);
                serviceMap.remove(deadService);
                serviceRemoveSubject.onNext(service);
              }

              sleep(1000);
            } catch(Exception e){
              System.err.println(e);
            }
          }
        }
      };
      notifyThread.start();
    }
  }

  public void stop(){
    if(notifyThread != null){
      run = false;
    }
  }


  public Single<List<IService>> probeServices(){
    if(!alive){
      return Single.just(new ArrayList<IService>(0));
    }
    if(wsSubject == null && probeThread == null){
      final Subject<IService> wssub = ReplaySubject.create();
      wsSubject = wssub;
      try{
        //Clear out service inventory
        server.getServiceDirectory().clear();
        //Send out a probe message
        server.probe();
      } catch(Exception e){
        System.err.println(e);
      }
      /* Create thread that waits 2 seconds for services to accumelate */
      
      final Thread t = new Thread(){
        public void run(){
          try{
            sleep(2000);
            
            for(WsDiscoveryService service : server.getServiceDirectory().matchAll()){
              IService iservice = new WsService(service);
              wssub.onNext(iservice);
            }
          } catch(Exception e){
            System.err.println(e);
          }
          wssub.onComplete();
          wsSubject = null;
          probeThread = null;
        }
      };
      t.start();
      probeThread = t;
    }

    return wsSubject.buffer(Integer.MAX_VALUE).first(new ArrayList<IService>(0));
  }


  public void dispose(){
    stop();
    alive = false;
    try{
      server.done();
    } catch(WsDiscoveryException e){

    }
  }

  public String getTypeDescriptor(){
    return "WS-DISCOVERY";
  }

  static public class WsSettings extends DiscoverySettings{
    private InetAddress  host; 
    public WsSettings(InetAddress host){
      this.host = host;
    }

    public WsSettings() throws UnknownHostException{
      this(Inet4Address.getLocalHost());
    }
  }

}