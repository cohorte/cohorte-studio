package org.cohorte.studio.eclipse.preferences.ui;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * Cohorte Runtime preference page.
 */
public class CPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private CCohorteRuntimeComposite pRuntimeComposite;
	
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
	}

	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	protected void performApply() {
		refresh();
	}

	protected void performDefaults() {
	}

	public boolean performOk() {
		performApply();
		return super.performOk();
	}

	private void initializeValues() {
	}

	private void refresh() {
		pRuntimeComposite.refresh();
	}
	
	@Inject
	public void setPreferences(ICohortePreferences aPrefs) {
		if (pRuntimeComposite != null) {
			pRuntimeComposite.setPreferences(aPrefs);
		}
	}
}