package org.cohorte.studio.eclipse.ui.node.wizards;

import javax.inject.Inject;
import javax.inject.Named;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@Creatable
public class CNodeProjectDetailsPage extends WizardPage implements INodeProjectPage {
	
	/**
	 * Default page name.
	 */
	public static final String PAGE_NAME = "details"; //$NON-NLS-1$
	
	private IStructuredSelection pCurrentSellection;
	
	private Label pNameLabel;
	private Text pNameText;
	private Button pAutoStartButton;
	private Button pUseCacheButton;
	
	private Button pHttpButton;
	private Label pIpVersionLabel;
	private Combo pIpVersionCombo; 
	private Button pXmppButton;
	private Label pServerLabel;
	private Text pServerText;
	
	private Combo pRuntimeCombo;
	private Button pManageButton;
	
	private IRuntime[] pRuntimes;
	
	private boolean pShown; 
	
	@Nullable
	private INodeProjecrWizard pWizard;
	
	@Inject
	public CNodeProjectDetailsPage(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION)  IStructuredSelection aSellection) {
		super(PAGE_NAME);
		this.pCurrentSellection = aSellection;
		this.pShown = false;
		this.setTitle(CMessages.COHORTE_NODE_PROJECT);
		this.setDescription(CMessages.CREATE_NEW_NODE_PROJECT);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && !this.pShown) {
			this.pShown = true;
			INodeProjecrWizard wWizard = pWizard;
			if (this.pNameText != null && wWizard != null) {
				this.pNameText.setText(wWizard.getProjectName());
			}
		}
	}
	
	@Override
	public void createControl(Composite aParent) {
		Composite wComposite = new Composite(aParent, SWT.NULL);
		initializeDialogUnits(aParent);
		
		wComposite.setLayout(new GridLayout());
		wComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		setPageComplete(validatePage());
		// Show description on opening
        setErrorMessage(null);
        setMessage(null);


		createNodeGroup(wComposite);
		createTransportGroup(wComposite);
		createRuntimeGroup(wComposite);

		Dialog.applyDialogFont(wComposite);
		setControl(wComposite);

	}

	@Override
	public void updateModel(@NonNull INode aNode) {

	}

	private void createNodeGroup(@NonNull Composite aContainer) {
		Group wGroup = createGroup(aContainer, 2);
		wGroup.setText(CMessages.NODE);
	
		pNameLabel = createLabel(wGroup);
		pNameLabel.setText(CMessages.NAME);
		pNameText = createText(wGroup);
		
		pNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		});
		
		pAutoStartButton = createButton(wGroup, SWT.CHECK, 2, 0);
		pAutoStartButton.setText(CMessages.AUTOSTART);
		pAutoStartButton.setSelection(true);
		
		pUseCacheButton = createButton(wGroup, SWT.CHECK, 2, 0);
		pUseCacheButton.setText(CMessages.USE_CACHE);
		pUseCacheButton.setSelection(true);
		
	}

	private void createTransportGroup(@NonNull Composite aContainer) {
		Group wGroup = createGroup(aContainer, 2);
		wGroup.setText(CMessages.TRANSPORT);
		
		pHttpButton = createButton(wGroup, SWT.CHECK, 2, 0);
		pHttpButton.setText(CMessages.HTTP);
		pIpVersionLabel = createLabel(wGroup);
		pIpVersionLabel.setText(CMessages.IP_VERSION);
		pIpVersionCombo = createCombo(wGroup, 60, 30);
		pIpVersionCombo.setItems(new String[] {"4", "6"} );  //$NON-NLS-1$ //$NON-NLS-2$
		pIpVersionCombo.select(0);
		
		pXmppButton = createButton(wGroup, SWT.CHECK, 2, 0);
		pXmppButton.setText(CMessages.XMPP);
		pServerLabel = createLabel(wGroup);
		pServerLabel.setText(CMessages.SERVER);
		pServerText = createText(wGroup);
	}

	private void createRuntimeGroup(@NonNull Composite aContainer) {
		Group wGroup = createGroup(aContainer, 2);
		wGroup.setText(CMessages.COHORTE_RUNTIME);
	
		pRuntimeCombo = createCombo(wGroup);
		pRuntimeCombo.setItems(getRuntimeNames());
		pRuntimeCombo.select(getDefaultRuntimeIndex());
		pManageButton = createButton(wGroup, SWT.NONE, 1, 0);
		pManageButton.setText(CMessages.MANAGE);
		
	}
	
	@Inject
	public void populate(ICohortePreferences aPrefs) {
		this.pRuntimes = aPrefs.getRuntimes();
		if (pRuntimeCombo != null) {
			pRuntimeCombo.setItems(getRuntimeNames());
		}
	}
	
	private String[] getRuntimeNames() {
		if (this.pRuntimes == null) {
			return new String[0];
		} else {
			int wSize = this.pRuntimes.length;
			String[] wItems = new String[wSize];
			for (int i = 0; i < wSize; i++) {
				wItems[i] = this.pRuntimes[i].getName();
			}
			return wItems;
		}
	}
	
	private int getDefaultRuntimeIndex() {
		if (this.pRuntimes == null) {
			return -1;
		} else {
			int wSize = this.pRuntimes.length;
			for (int i = 0; i < wSize; i++) {
				if (this.pRuntimes[i].isDefault()) {
					return i;
				}
			}
			return -1;
		}
	}

	private boolean validatePage() {
		return true;
	}
	
	@Inject @Optional
	private void update(INodeProjecrWizard aWizard) {
		pWizard = aWizard;
	}

}
