package org.sims.controller;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import org.sims.model.Hub;
import org.sims.model.QHub;
import org.sims.repository.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class HubController {

  private HubRepository hubRepository;

  @Autowired
  public HubController(HubRepository hubRepository) {
    this.hubRepository = hubRepository;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Saves the callback address in the hub database")
  @PostMapping("/hub")
  public Hub registerListener(@RequestBody Hub hub) {
    return hubRepository.save(hub);
  }

  @GetMapping("/hub")
  @ApiOperation("Returns a list of all listeners")
  public List<Hub> getHubs() {
    return hubRepository.findAll();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation("Deletes a listener from the hub database")
  @DeleteMapping("/hub/{id}")
  public void unregisterListener(@PathVariable String id) {
    QHub qHub = QHub.hub;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qHub.id.eq(id));
    Optional<Hub> optionalHub = hubRepository.findOne(p);
    if(!optionalHub.isPresent()) {
      throw new ResourceNotFoundException();
    }
    hubRepository.deleteById(id);
  }

}
