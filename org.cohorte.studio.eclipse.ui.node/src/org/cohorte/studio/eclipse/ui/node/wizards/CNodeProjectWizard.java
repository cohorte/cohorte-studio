package org.cohorte.studio.eclipse.ui.node.wizards;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.core.node.CNode;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * New Cohorte application project wizard. 
 * 
 * @author Ahmad Shahwan
 *
 */
public class CNodeProjectWizard extends Wizard implements INewWizard {
	
	@Nullable
	private IStructuredSelection pSellection;
	
	/**
	 * Cohorte node.
	 */
	@NonNull
	private INode pCohorteNode;
	
	@NonNull
	private IEclipseContext pContext;
	
	private IWizardPage pCreationPage;
	private IWizardPage pDetailsPage;

	public CNodeProjectWizard() {
		this.pCohorteNode = new CNode();
		@Nullable IEclipseContext wContext = PlatformUI.getWorkbench().getService(IEclipseContext.class);
		this.pContext = wContext;
	}

	@Override
	synchronized public void init(IWorkbench workbench, IStructuredSelection aSellection) {
		this.pCohorteNode = new CNode();
		this.pSellection = aSellection;
		this.pCreationPage = new CNodeProjectCreationPage(this.pCohorteNode, getSellection());
		this.pDetailsPage = new CNodeProjectDetailsPage(this.pCohorteNode, getSellection());
		ContextInjectionFactory.inject(this.pCreationPage, this.pContext);
		ContextInjectionFactory.inject(this.pDetailsPage, this.pContext);
	}

	@Override
	public boolean performFinish() {
		return false;
	}
	
	@Override
	public void addPages() {
		this.addPage(this.pCreationPage);
		this.addPage(this.pDetailsPage);
	}
	
	@Override
	synchronized public void dispose() {
		super.dispose();
		if (this.pCreationPage != null) ContextInjectionFactory.uninject(this.pCreationPage, this.pContext);
		if (this.pDetailsPage != null) ContextInjectionFactory.uninject(this.pDetailsPage, this.pContext);
	}

	protected IStructuredSelection getSellection() {
		return this.pSellection;
	}

}
