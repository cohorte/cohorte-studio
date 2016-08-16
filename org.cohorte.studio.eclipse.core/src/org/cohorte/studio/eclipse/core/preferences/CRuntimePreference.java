package org.cohorte.studio.eclipse.core.preferences;

import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.osgi.service.prefs.Preferences;

/**
 * Cohorte runtime preferences.
 * An object of this type persists a Cohorte runtime as a preferences node.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CRuntimePreference extends CPreference implements IRuntime {
	
	private static final String NAME = "name";
	private static final String PATH = "path";
	private static final String VERSION = "version";
	private static final String DEFAULT = "default";
	
	public CRuntimePreference(Preferences aPref) {
		super(aPref);
	}
	
	@Override
	public String getName() {
		return get(NAME);
	}
	
	@Override
	public void setName(String title) {
		set(NAME, title);
	}
	
	@Override
	public String getVersion() {
		return get(VERSION);
	}
	
	@Override
	public void setVersion(String version) {
		set(VERSION, version);
	}
	
	@Override
	public String getPath() {
		return get(PATH);
	}
	
	@Override
	public void setPath(String path) {
		set(PATH, path);
	}
	
	@Override
	public boolean isDefault() {
		return has(DEFAULT);
	}
	
	@Override
	public void setDefault(boolean aValue) {
		set(DEFAULT, aValue ? "true" : null);
	}	
}
