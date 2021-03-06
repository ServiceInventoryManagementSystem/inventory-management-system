package org.sims.discovery.mdns;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.sims.discovery.IDiscoveryService;
import org.sims.discovery.models.IService;
import org.springframework.core.env.Environment;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.JmDNSImpl;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class DnsDiscovery implements IDiscoveryService, ServiceListener{
  private JmDNSImpl jmdns;
  
  private boolean alive = false;
  private boolean running = false;
  private Subject<IService> dnsSubject;
  private Thread probeThread;
  private Subject<IService> serviceAddSubject = PublishSubject.create();
  private Subject<IService> serviceUpdateSubject = PublishSubject.create();
  private Subject<IService> serviceRemoveSubject = PublishSubject.create();
  
  private DnsSettings settings;
  
  public DnsDiscovery(DiscoverySettings settings){
    
    this.settings = (DnsSettings)settings;
    alive = true;
    try{
      jmdns = new JmDNSImpl(this.settings.host, this.settings.name);
    } catch(Exception e){
      running = false;
      System.err.println(e);
    }
  }
  
  public Observable<IService> serviceAdded(){
    return serviceAddSubject;/*.distinct((IService s) -> {
      return s.getLocalReference();
    });*/
  } // emitts when service is created

  public Observable<IService> serviceRemoved(){
    return serviceRemoveSubject;/*.distinct((IService s) -> {
      return s.getLocalReference();
    });*/
  } // emitts when service no longer exists

  public Observable<IService> serviceUpdated(){
    return serviceUpdateSubject;
  }
  



  public Single<List<IService>> probeServices(){
    if(!alive){
      return Single.just(new ArrayList<IService>(0));
    }
    if(dnsSubject == null && probeThread == null){
      final Subject<IService> dnssub = PublishSubject.create();
      dnsSubject = dnssub;
      if(!jmdns.isProbing()){
        jmdns.startProber();
      }
      final Thread t = new Thread(){
        public void run(){
          try{
          
            while(jmdns.isProbing()){
              sleep(500);
            }
          }catch(Exception e){
            System.err.println(e);
          }
          for(String type : settings.types){
            for(ServiceInfo info : jmdns.list(type)){
              DnsService service = new DnsService(info);
              dnssub.onNext(service);
            }
          }
          dnssub.onComplete();
          dnsSubject = null;
          probeThread = null;
        }
      };
      t.start();
      probeThread = t; 
    }

    
    
    return dnsSubject.buffer(Integer.MAX_VALUE).first(new ArrayList<IService>(0));
  } 
  
  public void start(){
    for(String type : settings.types){
      jmdns.addServiceListener(type, this);
    }
    running = true;
  }

  public void stop(){
    for(String type : settings.types){
      jmdns.removeServiceListener(type, this);
    }
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
    DnsService service = new DnsService(event.getInfo());
    serviceAddSubject.onNext(service);
  }

  public void serviceRemoved(ServiceEvent event){
    System.out.println("Service removed");
    System.out.println(event.getInfo());
    System.out.println("----------------------------------");
    DnsService service = new DnsService(event.getInfo());

    serviceRemoveSubject.onNext(service);
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
    // Should use serviceUpdateSubject instead
    DnsService service = new DnsService(event.getInfo());
    serviceUpdateSubject.onNext(service);
  }

  public String getTypeDescriptor(){
    return "MDNS";
  }


  static public class DnsSettings extends DiscoverySettings{
    private String[] types;
    private InetAddress host;
    private String name = "DnsDiscovery";
    

    public DnsSettings(InetAddress host){
      this(host, "DnsDiscovery", "_http._tcp.local.");
    }

    public DnsSettings(InetAddress host, String name, String... types){
      this.host = host;
      this.name = name;
      this.types = types;
    }

    public DnsSettings() throws UnknownHostException{
      this("_http._tcp.local.");
    }

    public DnsSettings(String... types) throws UnknownHostException{
      this(Inet4Address.getLocalHost(), "DnsDiscovery", types);
    }

    public DnsSettings(InetAddress host, Environment env){
      String typeString = env.getProperty("sims.mdns.types");
      if(typeString != null){
        types = typeString.split("\\s*[,|]\\s*");
      }else {
        types = new String[]{"_http._tcp.local."};
      }
      this.host = host;
    }
  }
}