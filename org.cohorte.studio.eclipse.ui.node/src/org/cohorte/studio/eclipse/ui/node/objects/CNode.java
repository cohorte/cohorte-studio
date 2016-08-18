package org.cohorte.studio.eclipse.ui.node.objects;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.cohorte.studio.eclipse.api.objects.ITransport;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Cohorte node object.
 * 
 * @author Ahmad Shahwan
 *
 */
@NonNullByDefault
public class CNode implements INode {

	private String pName;
	private boolean pAutoStart;
	private boolean pUseCache;
	private ITransport[] pTransports;
	@Nullable
	private IRuntime pRuntime;
	private String pAppName;
	private boolean pComposer;
	
	public CNode() {
		this.pName = ""; //$NON-NLS-1$
		this.pTransports = new ITransport[0];
		this.pAppName = ""; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return this.pName;
	}

	@Override
	public void setName(String aName) {
		this.pName = aName;

	}

	@Override
	public boolean isAutoStart() {
		return this.pAutoStart;
	}

	@Override
	public void setAutoStart(boolean aAutoStart) {
		this.pAutoStart = aAutoStart;
	}

	@Override
	public boolean isUseCache() {
		return this.pUseCache;
	}

	@Override
	public void setUseCache(boolean aUseCache) {
		this.pUseCache = aUseCache;
	}
	
	@Override
	public ITransport[] getTransports() {
		return this.pTransports;
	}

	@Override
	public void setTransports(ITransport[] aTransports) {
		this.pTransports = aTransports;
	}

	@Nullable
	@Override
	public IRuntime getRuntime() {
		return this.pRuntime;
	}

	@Override
	public void setRuntime(IRuntime aRuntime) {
		this.pRuntime = aRuntime;
	}

	@Override
	public String getApplicationName() {
		return this.pAppName;
	}

	@Override
	public void setApplicationName(String aAppName) {
		this.pAppName = aAppName;
	}

	@Override
	public boolean isComposer() {
		return this.pComposer;
	}

	@Override
	public void setComposer(boolean aComposer) {
		this.pComposer = aComposer;
	}

}