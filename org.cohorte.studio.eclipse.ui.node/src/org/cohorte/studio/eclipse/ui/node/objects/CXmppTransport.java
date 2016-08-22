package org.cohorte.studio.eclipse.ui.node.objects;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.annotations.Nullable;
import org.cohorte.studio.eclipse.api.objects.IXmppTransport;

/**
 * XMPP transport object.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CXmppTransport implements IXmppTransport {
	
	/**
	 * Protocol name.
	 */
	public static final @NonNull String NAME = "xmpp"; //$NON-NLS-1$

	private @NonNull String pHostname;
	private @Nullable String pUsername;
	private @Nullable String pPassword;
	private int pPort;
	
	/**
	 * Constructor.
	 * 
	 * @param aHostname hostname.
	 */
	public CXmppTransport(@NonNull String aHostname) {
		this.pHostname = aHostname;
	}

	@Override
	public @NonNull String getName() {
		return NAME;
	}

	@Override
	public @NonNull String getHostname() {
		return this.pHostname;
	}

	@Override
	public void setHostname(@NonNull String aValue) {
		this.pHostname = aValue;
	}

	@Override
	public String getUsername() {
		return this.pUsername;
	}

	@Override
	public void setUsername(String aValue) {
		this.pUsername = aValue;
	}

	@Override
	public String getPassword() {
		return this.pPassword;
	}

	@Override
	public void setPassword(String aValue) {
		this.pPassword = aValue;
	}

	@Override
	public int getPort() {
		return this.pPort;
	}

	@Override
	public void setPort(int aValue) {
		this.pPort = aValue;
	}

}
