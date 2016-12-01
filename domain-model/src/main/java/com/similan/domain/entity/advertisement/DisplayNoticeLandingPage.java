package com.similan.domain.entity.advertisement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "DisplayNoticeLandingPage")
public class DisplayNoticeLandingPage {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  
  @Column(columnDefinition = "TEXT")
  private String text;
  
  @Column
  private String url;

}
