package com.similan.domain.entity.lead;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "ClickThroughLead")
@DiscriminatorValue("ClickThroughLead")
public class ClickThroughLead extends Lead {

}
