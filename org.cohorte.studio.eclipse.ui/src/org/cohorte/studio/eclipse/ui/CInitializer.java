 
package org.cohorte.studio.eclipse.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.core.api.CRegistrar;
import org.cohorte.studio.eclipse.ui.api.IProjectUtils;
import org.cohorte.studio.eclipse.ui.components.CProjectUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;

public class CInitializer {
	private @NonNull CRegistrar pRegistrar;
	
	/**
	 * Constructor.
	 * 
	 * @param log
	 * @param context
	 */
	@Inject
	public CInitializer(IEclipseContext context) {
		this.pRegistrar = new CRegistrar(context);
	}

	/**
	 * On-application-start.
	 */
	@PostConstruct
	public void applicationStarted() {
		this.pRegistrar.register(CProjectUtils.class).as(IProjectUtils.class);
	}
	
	/**
	 * On-application-shutdown.
	 */
	@PreDestroy
	public void applicationShutdown() {
			this.pRegistrar.clear();
	}
}
