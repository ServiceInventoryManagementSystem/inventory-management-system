package org.sims.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import java.io.IOException;

public class JsonMergePatchUtils {

  public static <T> T mergePatch(T t, String patch, Class<T> clazz) throws IOException, JsonPatchException {
    System.out.println("Entered JsonMergePatchUtils");
    ObjectMapper mapper = new ObjectMapper();
    System.out.println("Created object mapper");
    System.out.println(t);

    SimpleFilterProvider filters;
    filters = (new SimpleFilterProvider()).addFilter("org.sims.model.Service",
            SimpleBeanPropertyFilter.serializeAll());
    JsonNode node = mapper.convertValue(filters, JsonNode.class);
    System.out.println("    JsonNode node = mapper.convertValue(t, JsonNode.class);\n");
    JsonNode patchNode = mapper.readTree(patch);
    System.out.println("    JsonNode patchNode = mapper.readTree(patch);\n");
    JsonMergePatch mergePatch = JsonMergePatch.fromJson(patchNode);
    System.out.println("    JsonMergePatch mergePatch = JsonMergePatch.fromJson(patchNode);\n");
    node = mergePatch.apply(node);
    System.out.println("    node = mergePatch.apply(node);\n");
    return mapper.treeToValue(node, clazz);
  }
}
