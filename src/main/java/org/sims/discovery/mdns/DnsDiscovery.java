package org.sims.discovery.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.DNSCache;
import javax.jmdns.impl.DNSEntry;
import javax.jmdns.impl.JmDNSImpl;

import org.sims.discovery.IDiscoveryService;
import org.sims.discovery.models.IService;;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class DnsDiscovery implements IDiscoveryService, ServiceListener{
  private JmDNSImpl jmdns;
  
  private boolean alive = false;
  private boolean running = false;
  private Subject<IService> dnsSubject;
  private Subject<IService> serviceAddSubject = PublishSubject.create();
  private Subject<IService> serviceRemoveSubject = PublishSubject.create();
  
  
  public DnsDiscovery(){
    alive = true;
    try{
      jmdns = new JmDNSImpl(InetAddress.getLocalHost(), "test");
    } catch(Exception e){
      running = false;
      System.err.println(e);
    }
  }
  
  public Observable<IService> serviceAdded(){
    return serviceAddSubject.distinct((IService s) -> {
      return s.getUUID();
    });
  } // emitts when service is created

  public Observable<IService> serviceRemoved(){
    return serviceRemoveSubject.distinct((IService s) -> {
      return s.getUUID();
    });
  } // emitts when service no longer exists

  public Observable<IService> serviceUpdated(){
    throw new UnsupportedOperationException("This method is not implemented");
  }
  



  public Single<List<IService>> probeServices(){
    if(!alive){
      return Single.just(new ArrayList<IService>(0));
    }
    if(dnsSubject == null){
      dnsSubject = PublishSubject.create();
      if(!jmdns.isProbing()){
        jmdns.startProber();
      }
      Thread t = new Thread(){
        public void run(){
          try{
          
            while(jmdns.isProbing()){
              sleep(100);
            }
          }catch(Exception e){
            System.err.println(e);
          }
          for(ServiceInfo info : jmdns.list("_http._tcp.local.")){
            DnsService service = new DnsService(info);
            dnsSubject.onNext(service);
          }
          dnsSubject.onComplete();
          dnsSubject = null;
        }
      };
      t.start();
    }

    
    
    return dnsSubject.buffer(Integer.MAX_VALUE).first(new ArrayList<IService>(0));
  } 
  
  public void start(){
    jmdns.addServiceListener("_http._tcp.local.", this);
    running = true;
  }

  public void stop(){
    jmdns.removeServiceListener("_http._tcp.local.", this);
    running = false;
  }

  public boolean isRunning(){
    return running;
  }

  public void dispose(){
    //try{
    jmdns.unregisterAllServices();
    jmdns.close();
    alive = false;
    /*} catch(IOException e){
      System.err.println(e);
    }*/
  }


  public void serviceAdded(ServiceEvent event){
    System.out.println("Service added");
    System.out.println(event.getInfo());
    System.out.println("----------------------------------");
  }

  public void serviceRemoved(ServiceEvent event){
    System.out.println("Service removed");
    System.out.println(event.getInfo());
    System.out.println("----------------------------------");
  }

  public void serviceResolved(ServiceEvent event){
    System.out.println("Service resolved");
    System.out.println(String.format("Domain: %s", event.getInfo().getDomain()));
    System.out.println(String.format("Urls: %s", event.getInfo().getURLs()[0]));
    System.out.println(String.format("Key: %s", event.getInfo().getKey()));
    System.out.println(String.format("Name: %s", event.getInfo().getName()));
    System.out.println(String.format("Nice: %s", event.getInfo().getNiceTextString()));
    System.out.println(String.format("Path: %s", event.getInfo().getPropertyString("path")));
    System.out.println(String.format("Server: %s", event.getInfo().getServer()));
    System.out.println("----------------------------------");
  }

  public String getTypeDescriptor(){
    return "MDNS";
  }
}