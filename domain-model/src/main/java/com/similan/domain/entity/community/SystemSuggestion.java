package com.similan.domain.entity.community;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SystemSuggestion")
@DiscriminatorValue("SystemSuggestion")
public class SystemSuggestion extends Suggestion {

}
