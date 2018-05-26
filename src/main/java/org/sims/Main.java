package org.sims;

import org.sims.discovery.manager.DiscoveryManager;
import org.sims.discovery.manager.HybernateResourceManager;
import org.sims.discovery.mdns.DnsDiscovery;
import org.sims.discovery.ws.WsDiscovery;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.*;
import java.util.Enumeration;

@EnableJpaAuditing
@SpringBootApplication
public class Main {
  public static void main(String[] args) {

    ApplicationContext ctx = SpringApplication.run(Main.class, args);

   
    HybernateResourceManager resourceManager = new HybernateResourceManager();
    
    SimsConfig config = new SimsConfig(ctx.getEnvironment());
    
    ctx.getAutowireCapableBeanFactory().autowireBeanProperties(
      resourceManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true
    );
    
    DiscoveryManager manager = new DiscoveryManager(resourceManager, config.probeInterval);

    WsDiscovery.WsSettings wsSettings = new WsDiscovery.WsSettings(config.address);
    DnsDiscovery.DnsSettings dnsSettings = new DnsDiscovery.DnsSettings(config.address, ctx.getEnvironment());

    manager.registerDiscovery(WsDiscovery.class, wsSettings);
    manager.registerDiscovery(DnsDiscovery.class, dnsSettings);

    manager.initAll();
    manager.startAll();
  }

  public static class SimsConfig{
    public InetAddress address;
    public int probeInterval = 10;
    public SimsConfig(Environment env){
      NetworkInterface networkInterface = null;
      try{
        address = Inet4Address.getLocalHost();
        networkInterface = NetworkInterface.getByInetAddress(address);
      }catch(SocketException | UnknownHostException e){
        System.err.println("Could not get addr or if");
        System.err.println(e);
        System.exit(-1);
      }
  
      String iface = env.getProperty("sims.bindIf");
      String ip = env.getProperty("sims.bindIp");
      
      try{
        if(!iface.isEmpty()){
          networkInterface = NetworkInterface.getByName(iface);
          Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
          while(addresses.hasMoreElements()){
            InetAddress addr = addresses.nextElement();
            if(!addr.isLoopbackAddress()){
              address = addr;
              break;
            }
          }
        }
  
        if(!ip.isEmpty()){
          address = Inet4Address.getByName(ip);
        }
  
      }catch(SocketException | UnknownHostException e){
        System.err.println(e);
      }

      probeInterval = env.getProperty("sims.probeInterval", Integer.class, 10);
    }
  }

}

