package org.cohorte.studio.eclipse.utils.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Internationalization manager.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CInternationalizationManager {
	
	public static final String BASE_NAME = "i18n";
	
	private ResourceBundle pResource;
	private static CInternationalizationManager sInstance;
	
	static {
		sInstance = new CInternationalizationManager();
	}
	
	public CInternationalizationManager() {
		try {
			this.pResource = ResourceBundle.getBundle(BASE_NAME);
		} catch (MissingResourceException e) {
			this.pResource = null;
		}
	}
	
	public String getText(String aMsgId) {
		if (this.pResource != null && this.pResource.containsKey(aMsgId)) {
			return this.pResource.getObject(aMsgId).toString();
		} else {
			return aMsgId;
		}
	}
	
	public static CInternationalizationManager getInstance() {
		return sInstance;
	}
}
