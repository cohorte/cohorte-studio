package org.cohorte.studio.eclipse.ui.api;

import org.cohorte.studio.eclipse.core.api.CRegistrar;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.ui.PlatformUI;

/**
 * Shorthand for a register that finds its way to Eclipse context.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CUIRegistrar extends CRegistrar {

	/**
	 * No-args constructor.
	 */
	public CUIRegistrar() {
		super(PlatformUI.getWorkbench().getService(IEclipseContext.class));
	}
	
	/**
	 * Clear before garbage collection.
	 */
	@Override
	public void finalize() {
		this.clear();
	}
}
