package org.cohorte.studio.eclipse.core.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;


/**
 * Preferences manager.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CPreferences implements ICohortePreferences {
	
	private static final String ROOT_NODE_ID = "org.cohorte.studio.eclipse";
	private static final String RUNTIMES = "runtimes";
	
	@Inject
	public CPreferences(Logger aLogger) {
		this.pLogger = aLogger;
		System.out.println("Init Chrt preferences");
	}
	
	private Logger pLogger;
	
	@Override
	public IRuntime[] getRuntimes() {
		Preferences wPrefs = getNode(RUNTIMES);
		List<IRuntime> wRuntimes = new ArrayList<>();
		try {
			String[] wChildren = wPrefs.childrenNames();
			for (int i = 0; i < wChildren.length; i++) {
				String wName = wChildren[i];
				Preferences wChild = wPrefs.node(wName);
				IRuntime wRuntime = new CRuntimePreference(wChild);
				wRuntimes.add(wRuntime);
			}
		} catch (BackingStoreException e) {
			this.pLogger.error(e, "Error reading child names.");
		}
		return wRuntimes.toArray(new IRuntime[0]); 
	}

	public IRuntime getDefaultRuntime() {
		Preferences wPrefs = getNode(RUNTIMES);
		try {
			String[] wChildren = wPrefs.childrenNames();
			for (int i = 0; i < wChildren.length; i++) {
				IRuntime wRuntime = new CRuntimePreference(wPrefs.node(wChildren[i]));
				if (wRuntime.isDefault()) {
					return wRuntime;
				}
			}
		} catch (BackingStoreException e) {
			this.pLogger.error(e, "Error reading child names.");
		}
		return null; 
	}
	
	
	@Override
	public IRuntime createRuntime() {
		Preferences wPrefs = getNode(RUNTIMES);
		String wName = UUID.randomUUID().toString();
		return new CRuntimePreference(wPrefs.node(wName));
	}

	@Override
	public void flush() throws IOException {
		try {
			getRootNode().flush();
		} catch (BackingStoreException e) {
			this.pLogger.error(e, "Error writing preferences.");
			throw new IOException("Cannot persist changes to preferences.", e);
		}		
	}

	protected IEclipsePreferences getRootNode() {
		return InstanceScope.INSTANCE.getNode(ROOT_NODE_ID);
	}
	
	protected Preferences getNode(String aPath) {
		return getRootNode().node(aPath);
	}
	
	@Execute
	public void execute() {}

}
