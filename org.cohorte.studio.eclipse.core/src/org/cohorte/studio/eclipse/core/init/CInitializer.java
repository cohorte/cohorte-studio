 
package org.cohorte.studio.eclipse.core.init;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.core.log.CLogger;
import org.cohorte.studio.eclipse.core.preferences.CPreferences;
import org.cohorte.studio.eclipse.core.registry.CRegistrar;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;

/**
 * Initialization component.
 * Register and unregister service components. 
 * 
 * @author Ahmad Shahwan
 *
 */
public class CInitializer {
	
	private CRegistrar pRegistrar;
	private ILogger pLog;
	
	/**
	 * Constructor.
	 * 
	 * @param log
	 * @param context
	 */
	@Inject
	public CInitializer(Logger log, IEclipseContext context) {
		this.pLog = new CLogger(log);
		this.pRegistrar = new CRegistrar(context);
	}

	/**
	 * On-application-start callback.
	 * 
	 * @param event
	 */
	@PostConstruct
	public void applicationStarted() {
		// Register Cohorte logger component
		this.pRegistrar.register(CLogger.class).as(ILogger.class);
		this.pLog.info("Cohorte logger registered.");
		
		// Register Cohorte preferences component
		this.pRegistrar.register(CPreferences.class).as(ICohortePreferences.class);
		this.pLog.info("Cohorte preferences registered.");
	}
	
	/**
	 * On-application-shutdown callback.
	 * 
	 * @param event
	 */
	@PreDestroy
	public void applicationShutdown() {
			this.pRegistrar.clear();
			this.pLog.info("Objects uninjected.");	
	}
}
