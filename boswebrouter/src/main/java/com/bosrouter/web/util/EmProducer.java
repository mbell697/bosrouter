package com.bosrouter.web.util;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jboss.solder.core.ExtensionManaged;

public class EmProducer {
	
	@ExtensionManaged
	@Produces
	@Default
	@PersistenceUnit(unitName = "bosrouter")
	@ConversationScoped
	EntityManagerFactory em;
	
	public EmProducer() {
		
	}
}