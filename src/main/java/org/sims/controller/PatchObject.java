package org.sims.controller;


import org.springframework.util.StringUtils;

public class PatchObject {

  public String path;
  public Object value;
  public String op;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    String p = path.substring(1);
    this.path = StringUtils.capitalize(p);

  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }
}
