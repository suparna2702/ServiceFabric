package com.similan.domain.repository.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.advertisement.DisplayNoticeLandingPage;
import com.similan.domain.repository.advertisement.jpa.JpaDisplayNoticeLandingPageRepository;

@Repository
public class DisplayNoticeLandingPageRepository {

  @Autowired
  private JpaDisplayNoticeLandingPageRepository repository;

  public DisplayNoticeLandingPage save(DisplayNoticeLandingPage page) {
    return repository.save(page);
  }
  
  public DisplayNoticeLandingPage findOne(Long id){
    return this.repository.findOne(id);
  }

  public DisplayNoticeLandingPage create(String text, String url) {
    DisplayNoticeLandingPage retPage = new DisplayNoticeLandingPage();
    retPage.setText(text);
    retPage.setUrl(url);
    return retPage;
  }

}
