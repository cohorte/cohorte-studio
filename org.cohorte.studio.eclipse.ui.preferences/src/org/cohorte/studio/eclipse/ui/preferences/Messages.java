package org.cohorte.studio.eclipse.ui.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.cohorte.studio.eclipse.ui.preferences.messages"; //$NON-NLS-1$
	public static String CHOOSE_LOCATION_FILESYSTEM;
	public static String CHOOSE_LOCATION_WORKSPACE;
	public static String ERROR_CREATING_RUNTIME;
	public static String FILESYSTEM;
	public static String INVALIDE_DIRECTORY;
	public static String INVALIDE_NAME;
	public static String N_A;
	public static String NAME;
	public static String PATH;
	public static String PATH_TO_RUNTIME;
	public static String WORKSPACE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
