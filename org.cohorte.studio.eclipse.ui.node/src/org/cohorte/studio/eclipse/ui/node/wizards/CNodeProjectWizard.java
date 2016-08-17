package org.cohorte.studio.eclipse.ui.node.wizards;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.core.registry.CRegistrar;
import org.cohorte.studio.eclipse.ui.node.objects.CNode;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * New Cohorte application project wizard. 
 * 
 * @author Ahmad Shahwan
 *
 */
public class CNodeProjectWizard extends Wizard implements INodeProjecrWizard {
	
	/**
	 * Cohorte node.
	 */
	@NonNull
	private INode pCohorteNode;
	
	private CRegistrar pRegistrar;
	
	@Inject
	private CNodeProjectCreationPage pCreationPage;
	
	@Inject
	private CNodeProjectDetailsPage pDetailsPage;

	public CNodeProjectWizard() {
		pCohorteNode = new CNode();
		pRegistrar = new CRegistrar(PlatformUI.getWorkbench().getService(IEclipseContext.class));
		pRegistrar.register(this).as(INodeProjecrWizard.class);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection aSellection) {
		this.pCohorteNode = new CNode();
	}

	@Override
	public boolean performFinish() {
		IProjectDescription wDescription = getProject().getWorkspace().newProjectDescription(getProjectName());
		wDescription.setLocation(getLocationPath());
		getProject().create(null);
		return true;
	}
	
	@Override
	public void addPages() {
		this.addPage(this.pCreationPage);
		this.addPage(this.pDetailsPage);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		pRegistrar.clear();
	}
	
	@Override
	public INode getModel() {
		return this.pCohorteNode;
	}

	@Override
	public String getProjectName() {
		String wName = this.pCreationPage.getProjectName();
		return wName == null ? "" : wName; //$NON-NLS-1$
	}

	@Override
	public IPath getLocationPath() {
		IPath wPath = this.pCreationPage.getLocationPath();
		if (wPath == null) {
			throw new RuntimeException("Project path is null"); //$NON-NLS-1$
		}
		return wPath;
	}

	@Override
	public IProject getProject() {
		return this.pCreationPage.getProjectHandle();
	}

}
