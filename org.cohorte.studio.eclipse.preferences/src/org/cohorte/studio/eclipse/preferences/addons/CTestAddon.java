 
package org.cohorte.studio.eclipse.preferences.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import org.eclipse.e4.core.services.events.IEventBroker;

public class CTestAddon {
	
	public CTestAddon() {
		// TODO Auto-generated constructor stub
		System.out.println("ADDON CREATED!!!!!!!!!");
	} 

	@Inject
	@Optional
	public void applicationStarted(
			@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {
		System.out.println("ADDON INVOKED!!!!!!!!!");
	}

}
