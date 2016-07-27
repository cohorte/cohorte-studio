package org.cohorte.studio.eclipse.ui.preferences;

import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * Cohorte Runtime preference page.
 */
public class CPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private CCohorteRuntimeComposite pRuntimeComposite;
	
	/**
	 * Injection context.
	 */
	private IEclipseContext pContext;
	
	private ILogger pLog;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		this.pContext = PlatformUI.getWorkbench().getService(IEclipseContext.class);
		this.pLog = this.pContext.get(ILogger.class);
		createCohorteRuntimeComposite(composite);

		applyDialogFont(composite);
		
		if (this.pLog != null) this.pLog.info("Cohorte preference content composite created.");
		return composite;
	}

	private void createCohorteRuntimeComposite(Composite parent) {
		pRuntimeComposite = new CCohorteRuntimeComposite(parent, SWT.NONE);
		pRuntimeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ContextInjectionFactory.inject(this.pRuntimeComposite, this.pContext);
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	@Override
	protected void performApply() {
		performOk();
	}

	@Override
	protected void performDefaults() {
	}

	@Override
	public boolean performOk() {
		if (this.pRuntimeComposite != null) {
			return this.pRuntimeComposite.apply();
		} else {
			return false;
		}
	}
	
	@Override
	public boolean performCancel() {
		if (this.pRuntimeComposite != null) {
			return this.pRuntimeComposite.cancel();
		}
		return true;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (this.pContext != null && this.pRuntimeComposite != null) {
			ContextInjectionFactory.uninject(this.pRuntimeComposite, this.pContext);
		}
		if (this.pLog != null) this.pLog.info("Cohorte preference page disposed.");
	}

}