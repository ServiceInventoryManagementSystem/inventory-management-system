package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Note {
  @Id
  @ApiModelProperty(notes="Id of the note.")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

//  @ApiModelProperty(notes="Author of the note.")
  private String author;
//  @ApiModelProperty(notes="Date of the note.")
  private String date;
//  @ApiModelProperty(notes="Text of the note.")
  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_id")
  private Service service;

  public Note() {
  }

  @JsonIgnore
  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
