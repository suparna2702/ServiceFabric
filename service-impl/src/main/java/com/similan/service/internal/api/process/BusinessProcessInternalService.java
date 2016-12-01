package com.similan.service.internal.api.process;

import com.similan.domain.entity.process.BusinessProcess;
import com.similan.service.internal.impl.process.BusinessProcessActions;

public interface BusinessProcessInternalService {
  void deployModified();
  BusinessProcessActions<?> createActions(BusinessProcess process);

  <PROCESS extends BusinessProcess> void initiate(PROCESS inputs);

  <PROCESS extends BusinessProcess> void resume(PROCESS inputs);
}
