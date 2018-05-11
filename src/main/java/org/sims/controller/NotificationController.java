package org.sims.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class NotificationController {

  @PostMapping("/hub")
  public void registerListener(@RequestBody String requestBody) {

    MultiValueMap<String, String> callback = new LinkedMultiValueMap<>();
    if(requestBody != null) {
      callback.add("callback", requestBody);
    }

    System.out.println(requestBody);
    System.out.println(callback);

    ObjectMapper mapper = new ObjectMapper();

    TypeReference<HashMap<String, String>> typeRef
            = new TypeReference<HashMap<String, String>>() {};
    Map<String, String> map;
    try {
      map = mapper.readValue(requestBody, typeRef);
      System.out.println(map);
      System.out.println(map.get("callback"));
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

}
