package org.cohorte.studio.eclipse.preferences.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbench;

/**
 * Cohorte Runtime preference page.
 */
public class CPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private CCohorteRuntimeComposite pRuntimeComposite;
	
	/**
	 * Injection context.
	 */
	private IEclipseContext pContext;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		createCohorteRuntimeComposite(composite);

		applyDialogFont(composite);
		initializeValues();

		return composite;
	}

	private void createCohorteRuntimeComposite(Composite parent) {
		pRuntimeComposite = new CCohorteRuntimeComposite(parent, SWT.NONE);
		pRuntimeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.pContext = PlatformUI.getWorkbench().getService(IEclipseContext.class);
		ContextInjectionFactory.inject(this.pRuntimeComposite, this.pContext);
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	@Override
	protected void performApply() {
		refresh();
	}

	@Override
	protected void performDefaults() {
	}

	@Override
	public boolean performOk() {
		performApply();
		return super.performOk();
	}

	private void initializeValues() {
	}

	private void refresh() {
		pRuntimeComposite.refresh();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (this.pContext != null && this.pRuntimeComposite != null) {
			ContextInjectionFactory.uninject(this.pRuntimeComposite, this.pContext);
		}
	}

}