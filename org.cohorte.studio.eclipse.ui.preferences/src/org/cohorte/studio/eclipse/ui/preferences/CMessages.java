package org.cohorte.studio.eclipse.ui.preferences;

import org.eclipse.osgi.util.NLS;

public class CMessages extends NLS {
	private static final String BUNDLE_NAME = "org.cohorte.studio.eclipse.ui.preferences.messages"; //$NON-NLS-1$
	public static String ADD;
	public static String ADD_NEW_COHORTE_RUNTIME;
	public static String COHORTE_RUNTIME;
	public static String EDIT;
	public static String NAME;
	public static String PATH;
	public static String REMOVE;
	public static String SET_DEFAULT;
	public static String VERSION;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CMessages.class);
	}

	private CMessages() {
	}
}
