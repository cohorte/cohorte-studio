package org.cohorte.studio.eclipse.ui.node.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.annotations.Nullable;
import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.objects.IHttpTransport;
import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.cohorte.studio.eclipse.api.objects.ITransport;
import org.cohorte.studio.eclipse.api.objects.IXmppTransport;
import org.cohorte.studio.eclipse.core.api.CUtl;
import org.cohorte.studio.eclipse.ui.node.objects.CHttpTransport;
import org.cohorte.studio.eclipse.ui.node.objects.CXmppTransport;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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
	private Label pHostLabel;
	private Text pHostText;
	private Label pPortLabel;
	private Text pPortText;
	private Button pAnonymousButton;
	private Label pUsernameLabel;
	private Text pUsernameText;
	private Label pPasswordLabel;
	private Text pPasswordText;
	
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
		
		setPageComplete(true);
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
		aNode.setName(CUtl.either(this.pNameText.getText()).or(String::new));
		aNode.setAutoStart(this.pAutoStartButton.getSelection());
		aNode.setUseCache(this.pUseCacheButton.getSelection());
		List<ITransport> wTransports = new ArrayList<>(2);
		if (this.pHttpButton.getSelection()) {
			IHttpTransport wTransport = new CHttpTransport();
			if (pIpVersionCombo.getSelectionIndex() == 1) {
				wTransport.setVersion(IHttpTransport.EVersion.IPV6);
			}
			wTransports.add(wTransport);
		}
		if (this.pXmppButton.getSelection()) {
			IXmppTransport wTransport = new CXmppTransport(CUtl.either(this.pHostText.getText()).or(String::new));
			String wPort = CUtl.either(this.pPortText.getText()).or(String::new).trim();
			if (!wPort.isEmpty()) {
				try {
					wTransport.setPort(Integer.parseInt(wPort));
				} catch (NumberFormatException e) {/* Keep defaults */}
			}
			if (!pAnonymousButton.getSelection()) {
				wTransport.setUsername(CUtl.either(this.pUsernameText.getText()).or(String::new));
				wTransport.setPassword(CUtl.either(this.pPasswordText.getText()).or(String::new));
			}
			wTransports.add(wTransport);
		}
		ITransport[] wTrasnportsArray = new ITransport[wTransports.size()];
		wTransports.toArray(wTrasnportsArray);
		aNode.setTransports(wTrasnportsArray);
		IRuntime[] wRuntimes = this.pRuntimes;
		int wIndex = this.pRuntimeCombo.getSelectionIndex();
		if (wRuntimes != null && wIndex < wRuntimes.length) {
			aNode.setRuntime(wRuntimes[wIndex]);
		}
		
	}

	private void createNodeGroup(@NonNull Composite aContainer) {
		Group wGroup = createGroup(aContainer, 2);
		wGroup.setText(CMessages.NODE);
	
		pNameLabel = createLabel(wGroup);
		pNameLabel.setText(CMessages.NAME);
		pNameText = createText(wGroup);
		
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
		
		pHttpButton = createButton(wGroup, SWT.CHECK, 3, 0);
		pHttpButton.setText(CMessages.HTTP);
		pHttpButton.setSelection(false);
		pIpVersionLabel = createLabel(wGroup);
		pIpVersionLabel.setText(CMessages.IP_VERSION);
		pIpVersionCombo = createCombo(wGroup, 60, 30);
		pIpVersionCombo.setItems(new String[] {new Integer(4).toString(), new Integer(6).toString()} );
		pIpVersionCombo.select(0);
		
		pXmppButton = createButton(wGroup, SWT.CHECK, 3, 0);
		pXmppButton.setText(CMessages.XMPP);
		pXmppButton.setSelection(false);
		pHostLabel = createLabel(wGroup);
		pHostLabel.setText(CMessages.SERVER_HOSTNAME);
		pHostText = createText(wGroup);
		pPortLabel = createLabel(wGroup);
		pPortLabel.setText(CMessages.SERVER_PORT);
		pPortText = createText(wGroup);
		/* only accept digits */ 
		pPortText.addListener(SWT.Verify, aEvent -> {
			aEvent.doit = aEvent.text.matches("[0-9]+"); //$NON-NLS-1$
		});
		pAnonymousButton = createButton(wGroup, SWT.CHECK, 2, 30);
		pAnonymousButton.setText(CMessages.ANONYMOUS_LOGIN);
		pAnonymousButton.addListener(SWT.Selection, aEvent -> {
			boolean wEnabled = !pAnonymousButton.getSelection();
			Arrays.stream(new Text[] { pUsernameText, pPasswordText }).forEach(aControl -> {
				aControl.setEditable(wEnabled);
				aControl.setEnabled(wEnabled);
			});
		});
		pAnonymousButton.setSelection(true);
		pUsernameLabel = createLabel(wGroup);
		pUsernameLabel.setText(CMessages.USERNAME);
		pUsernameText = createText(wGroup);
		pUsernameText.setEditable(false);
		pPasswordLabel = createLabel(wGroup);
		pPasswordLabel.setText(CMessages.PASSWORD);
		pPasswordText = createText(wGroup);
		pPasswordText.setEditable(false);
		pPasswordText.setEchoChar('*');
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
	
	@Inject @Optional
	private void update(INodeProjecrWizard aWizard) {
		pWizard = aWizard;
	}

}
