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
	private static final String COPY_NODE_ID = "org.cohorte.studio.eclipse.temp";
	private static final String RUNTIMES = "runtimes";
	private IEclipsePreferences pWorkingCopy;
	
	@Inject
	private ILogger pLogger;

	public CPreferences() {
	}
	
	@Override
	public IRuntime[] getRuntimes() {
		List<IRuntime> wRuntimes = new ArrayList<>();
		try {
			Preferences wPrefs = getNode(RUNTIMES);
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

	@Override
	public IRuntime getDefaultRuntime() {
		try {
			Preferences wPrefs = getNode(RUNTIMES);
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
	public IRuntime createRuntime() throws IOException {
		Preferences wPrefs;
		try {
			wPrefs = getNode(RUNTIMES);
			String wName = UUID.randomUUID().toString();
			return new CRuntimePreference(wPrefs.node(wName));
		} catch (BackingStoreException e) {
			this.pLogger.error("Error obtaining runtime preferences.");
			throw new IOException("Exception while obtaining runtime preferences.", e);
		}
	}

	@Override
	synchronized public void rollback() throws IOException {
		if (this.pWorkingCopy != null) {
			try {
				Preferences parent = this.pWorkingCopy.parent();
				this.pWorkingCopy.removeNode();
				this.pWorkingCopy = null;
				parent.flush();
				this.pLogger.info("Preference changes rolled back.");
			} catch (BackingStoreException e) {
				this.pLogger.error(e, "Error rolling back preferences.");
				throw new IOException("Cannot delete temporary preferences.", e);
			}
		}
		this.pLogger.error("Preference changes rolled back.");
	}
	
	@Override
	synchronized public void commit() throws IOException {
		try {
			if (this.pWorkingCopy != null) {
				IEclipsePreferences root = InstanceScope.INSTANCE.getNode(ROOT_NODE_ID);
				this.purge(root);
				this.copy(this.pWorkingCopy, root);
				this.pWorkingCopy.removeNode();
				this.pWorkingCopy = null;
				root.flush();
				this.pLogger.info("Preference changes commited.");
			}
		} catch (BackingStoreException e) {
			this.pLogger.error(e, "Error commiting preferences.");
			throw new IOException("Cannot persist changes to preferences.", e);
		}		
	}
	
	protected void purge(Preferences prefs) throws BackingStoreException {
		try {
			for (String key : prefs.keys()) {
				prefs.remove(key);
			}
			for (String name : prefs.childrenNames()) {
				prefs.node(name).removeNode();
			}
		} catch (BackingStoreException e) {
			this.pLogger.error("Error purging node.");
			throw e;
		}
	}
	
	protected void copy(Preferences source, Preferences dest) throws BackingStoreException {
		try {
			for (String key : source.keys()) {
				String value = source.get(key, "");
				dest.put(key, value);
			}
			for (String name : source.childrenNames()) {
				Preferences child = source.node(name);
				Preferences copy = dest.node(name);
				copy(child, copy);
			}
		} catch (BackingStoreException e) {
			this.pLogger.error("Error copying nodes.");
			throw e;
		}
	}
	
	protected IEclipsePreferences getRootNode() throws BackingStoreException {
		if (this.pWorkingCopy == null) {
			IEclipsePreferences root = InstanceScope.INSTANCE.getNode(ROOT_NODE_ID);
			this.pWorkingCopy = InstanceScope.INSTANCE.getNode(COPY_NODE_ID);
			try {
				purge(this.pWorkingCopy);
				copy(root, this.pWorkingCopy);
			} catch (BackingStoreException e) {
				this.pLogger.error("Error creating temporary preferences.");
				throw e;
			}			
		}
		return this.pWorkingCopy;
	}
	
	protected Preferences getNode(String aPath) throws BackingStoreException {
		return getRootNode().node(aPath);
	}
}
