package com.similan.adminapp.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;

import com.similan.service.api.wall.dto.basic.WallEntryStatisticsDto;

@ViewScoped
@ManagedBean(name = "wallEventStatisticsView")
@Slf4j
public class WallEventStatisticsView extends BaseAdminView {

  private static final long serialVersionUID = 1L;

  private WallEntryStatisticsDto wallStatistics;

  @PostConstruct
  public void init() {
    log.info("Initializing WallEventView");
    wallStatistics = this.getMemberAdminService().getBasicStatistics();
  }

  public WallEntryStatisticsDto getWallStatistics() {
    return wallStatistics;
  }

}
