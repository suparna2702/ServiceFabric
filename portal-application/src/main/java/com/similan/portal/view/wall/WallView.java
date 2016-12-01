package com.similan.portal.view.wall;

import java.util.List;

import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;

public interface WallView<WallContainer extends IWallContainerKey> extends
    WallPostingView {

  List<WallEntryDto<WallContainer>> getWallEntries();
  
  void post();
  
  String getPostContent();

}
