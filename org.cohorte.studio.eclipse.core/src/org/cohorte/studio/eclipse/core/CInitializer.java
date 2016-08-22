 
package org.cohorte.studio.eclipse.core;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.core.api.CRegistrar;
import org.cohorte.studio.eclipse.core.api.IProjectFactory;
import org.cohorte.studio.eclipse.core.preferences.CPreferences;
import org.cohorte.studio.eclipse.core.project.CProjectFactory;
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
	
	private @NonNull CRegistrar pRegistrar;
	private @NonNull ILogger pLog;
	
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
	 * On-application-start.
	 */
	@PostConstruct
	public void applicationStarted() {
		// Register Cohorte logger component
		this.pRegistrar.register(this.pLog).as(ILogger.class);
		this.pLog.info("Cohorte logger registered.");
		
		// Register Cohorte preferences component
		this.pRegistrar.register(CPreferences.class).as(ICohortePreferences.class);
		this.pLog.info("Cohorte preferences registered.");
		
		// Register Cohorte project factory
		this.pRegistrar.register(CProjectFactory.class).as(IProjectFactory.class);
		this.pLog.info("Cohorte project factory registered.");
	}
	
	/**
	 * On-application-shutdown.
	 */
	@PreDestroy
	public void applicationShutdown() {
			this.pRegistrar.clear();
			this.pLog.info("Cohorte components unregistered.");	
	}
}
