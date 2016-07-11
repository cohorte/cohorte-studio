 
package org.cohorte.studio.eclipse.preferences.handlers;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.preferences.Activator;
import org.cohorte.studio.eclipse.preferences.ui.CPreferencePage;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.log.Logger;

/**
 * Injection handler.
 * 
 * @see <a href="https://github.com/opcoach/e4Preferences">opcoach example</a>.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CInitHandler {
	
	private static final String XP_PREFERENCE_PAGES = "org.eclipse.ui.preferencePages";
	private static final String CLASS = "class";
	
	@Inject
	public CInitHandler(IEclipseContext aContext, IExtensionRegistry aRegistry, Logger aLog) {
		IContributionFactory wFactory = aContext.get(IContributionFactory.class);
		IConfigurationElement[] wEls = aRegistry.getConfigurationElementsFor(XP_PREFERENCE_PAGES);
		for (IConfigurationElement wEl : wEls) {
			if (wEl.getAttribute(CLASS) != null)
			{
				try {
					String wBundleId= wEl.getNamespaceIdentifier();
					if (Activator.PLUGIN_ID.equals(wBundleId)) {
						String prefPageURI = getClassURI(wBundleId, wEl.getAttribute(CLASS));
						Object object = wFactory.create(prefPageURI, aContext);
						aLog.info("Applying injection on preference page.");
						ContextInjectionFactory.inject(object, aContext);
					}
				} catch (ClassNotFoundException e) {
					aLog.error(e, "Error parsing extensions for injection.");
					continue;
				}
			}			
		}		
	}
	
	@Execute
	public void execute() {
		
	}
	
	private static String getClassURI(String definingBundleId, String spec) throws ClassNotFoundException
	{
		if (spec.startsWith("platform:")) {
			return spec;
		}
		return String.format("bundleclass://%s/%s", definingBundleId, spec);
	}
		
}