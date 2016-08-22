package org.cohorte.studio.eclipse.ui.node.wizards;

import org.eclipse.osgi.util.NLS;

public class CMessages extends NLS {
	private static final String BUNDLE_NAME = "org.cohorte.studio.eclipse.ui.node.wizards.messages"; //$NON-NLS-1$
	public static String ANONYMOUS_LOGIN;
	public static String APPLICATION;
	public static String AUTOSTART;
	public static String COHORTE_NODE_PROJECT;
	public static String COHORTE_RUNTIME;
	public static String CREATE_NEW_NODE_PROJECT;
	public static String HTTP;
	public static String IP_VERSION;
	public static String MANAGE;
	public static String NAME;
	public static String NODE;
	public static String PASSWORD;
	public static String SERVER;
	public static String SERVER_HOSTNAME;
	public static String SERVER_PORT;
	public static String TOP_LEVEL_COMPOSER;
	public static String TRANSPORT;
	public static String USE_CACHE;
	public static String USERNAME;
	public static String XMPP;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CMessages.class);
	}

	private CMessages() {
	}
}
