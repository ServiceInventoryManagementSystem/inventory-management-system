import org.sims.discovery.models.IService;
import org.sims.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import io.reactivex.Observable;
import io.reactivex.Single;

public class HybernateResourceManager implements IResourceManager{
  @Autowired
  ServiceRepository serviceRepo;

  public Single<IService> save(IService service){
    return null;
  }


  public Single<IService> getById(String id){
    return null;
  }

}