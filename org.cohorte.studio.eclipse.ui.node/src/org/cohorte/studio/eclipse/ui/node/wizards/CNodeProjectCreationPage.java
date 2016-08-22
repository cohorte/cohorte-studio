package org.cohorte.studio.eclipse.ui.node.wizards;

import javax.inject.Inject;
import javax.inject.Named;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.objects.INode;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * New node project creation wizard page.
 * 
 * @author Ahmad Shahwan
 *
 */
@Creatable
public class CNodeProjectCreationPage extends WizardNewProjectCreationPage implements INodeProjectPage {
	
	/**
	 * Default page name.
	 */
	public static final String PAGE_NAME = "creation"; //$NON-NLS-1$
	
	private IStructuredSelection pCurrentSellection;

	private Button pComposerButton;
	private Label pAppNameLabel;
	private Text pAppNameText;
	
	@Inject
	public CNodeProjectCreationPage(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection aSellection) {
		super(PAGE_NAME);
		this.pCurrentSellection = aSellection;
		this.setTitle(CMessages.COHORTE_NODE_PROJECT);
		this.setDescription(CMessages.CREATE_NEW_NODE_PROJECT);
	}
	
	@Override
	public void createControl(Composite aParent) {
		super.createControl(aParent);
		Composite wControl = (Composite) getControl();
		GridLayout wLayout = new GridLayout();
		wControl.setLayout(wLayout);

		createApplicationGroup(wControl);
		createWorkingSetGroup(wControl, this.pCurrentSellection, new String[] {
				"org.eclipse.jdt.ui.JavaWorkingSetPage", //$NON-NLS-1$
				"org.eclipse.ui.resourceWorkingSetPage"  //$NON-NLS-1$
		});

		Dialog.applyDialogFont(wControl);
		setControl(wControl);
	}

	protected void createApplicationGroup(Composite aContainer) {
		Group wGroup = new Group(aContainer, SWT.NONE);
		wGroup.setText(CMessages.APPLICATION);
		GridLayout wLayout = new GridLayout();
		wLayout.numColumns = 2;
		wGroup.setLayout(wLayout);
		wGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
		pAppNameLabel = createLabel(wGroup);
		pAppNameLabel.setText(CMessages.NAME);
		pAppNameText = createText(wGroup);
		
		pAppNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		});
		
		pComposerButton = createButton(wGroup, SWT.CHECK, 2, 0);
		pComposerButton.setText(CMessages.TOP_LEVEL_COMPOSER);
		pComposerButton.setSelection(true);
	}

	@Override
	public void updateModel(@NonNull INode aNode) {
		String wName = this.pAppNameText.getText();
		if (wName == null) wName = ""; //$NON-NLS-1$
		aNode.setApplicationName(wName);
		aNode.setComposer(this.pComposerButton.getSelection());
	}
}
