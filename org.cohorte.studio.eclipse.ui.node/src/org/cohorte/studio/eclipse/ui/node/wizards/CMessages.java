package org.cohorte.studio.eclipse.ui.node.wizards;

import org.eclipse.osgi.util.NLS;

public class CMessages extends NLS {
	private static final String BUNDLE_NAME = "org.cohorte.studio.eclipse.ui.node.wizards.messages"; //$NON-NLS-1$
	public static String APPLICATION;
	public static String COHORTE_NODE_PROJECT;
	public static String CREATE_NEW_NODE_PROJECT;
	public static String NAME;
	public static String TOP_LEVEL_COMPOSER;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CMessages.class);
	}

	private CMessages() {
	}
}
