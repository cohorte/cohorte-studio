package org.cohorte.studio.eclipse.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class CActivator implements BundleActivator {
	
	/**
	 * Plugin ID.
	 */
	public static final String PLUGIN_ID = FrameworkUtil.getBundle(CActivator.class).getSymbolicName();
	
	// The shared instance
	private static CActivator plugin;

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		CActivator.context = bundleContext;
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		CActivator.context = null;
		plugin = null;
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CActivator getDefault() {
		return plugin;
	}

}
