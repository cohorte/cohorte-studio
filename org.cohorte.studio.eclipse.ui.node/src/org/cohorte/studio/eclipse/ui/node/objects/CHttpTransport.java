package org.cohorte.studio.eclipse.ui.node.objects;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.objects.IHttpTransport;

/**
 * HTTP protocol object.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CHttpTransport implements IHttpTransport {
	
	/**
	 * Protocol name.
	 */
	public static final @NonNull String NAME = "http"; //$NON-NLS-1$
	
	private @NonNull EVersion pVersion = EVersion.IPV4;

	/**
	 * Constructor.
	 */
	public CHttpTransport() {
	}

	@Override
	public @NonNull String getName() {
		return NAME;
	}

	@Override
	public EVersion getVersion() {
		return this.pVersion;
	}

	@Override
	public void setVersion(EVersion aValue) {
		this.pVersion = aValue;

	}

}
