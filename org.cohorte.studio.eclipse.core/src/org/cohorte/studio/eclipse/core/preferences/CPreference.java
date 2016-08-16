package org.cohorte.studio.eclipse.core.preferences;

import java.io.IOException;

import org.cohorte.studio.eclipse.api.objects.IPreference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Preference item that is persisted as an Eclipse preference node.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CPreference implements IPreference {
	
	private Preferences pPref;

	public CPreference(Preferences aPref) {
		super();
		this.pPref = aPref;
	}

	@Override
	public void save() throws IOException {
		try {
			this.pPref.flush();
		} catch (BackingStoreException e) {
			throw new IOException("Cannot write preference.", e);
		}
	}

	@Override
	public void remove() throws IOException {
		try {
			this.pPref.removeNode();
		} catch (BackingStoreException e) {
			throw new IOException("Cannot remove preference.", e);
		}
	}
	
	/**
	 * Return string value.
	 * 
	 * @param key
	 * @return
	 */
	protected String get(String key) {
		return this.pPref.get(key, null);
	}
	
	/**
	 * Set value. To remove value for key, pass value as {@code null}.
	 * 
	 * @param key
	 * @param value
	 */
	protected void set(String key, String value) {
		if (value != null) {
			this.pPref.put(key, value);
		} else {
			this.pPref.remove(key);
		}
	}
	
	/**
	 * Whether a preference is defined.
	 * 
	 * @param key
	 * @return
	 */
	protected boolean has(String key) {
		return this.pPref.get(key, null) != null;
	}
	
	/**
	 * Preferences node.
	 * 
	 * @return
	 */
	protected Preferences getNode() {
		return this.pPref;
	}
}
