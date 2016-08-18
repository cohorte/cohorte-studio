package org.cohorte.studio.eclipse.ui.node.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.core.registry.CRegistrar;
import org.cohorte.studio.eclipse.ui.node.objects.CNode;
import org.cohorte.studio.eclipse.ui.node.project.CNodeProjectNature;
import org.cohorte.studio.eclipse.ui.node.utils.CProjectUtils;
import org.eclipse.core.resources.IProject;
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
	
	@Inject
	private CProjectUtils pUtils; 

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
		IProject wProject = this.pUtils.createProject(this, new String [] { CNodeProjectNature.ID });
		if (wProject == null) return false;
		this.pUtils.addToWorkingSets(wProject, this.pCreationPage.getSelectedWorkingSets());
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
	public URI getLocationUri() {
		/**
		 * @see org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard#createProject()
		 */
		if (!pCreationPage.useDefaults()) {
			return pCreationPage.getLocationURI();
		} else {
			return null;
		}
	}

	@Override
	public IProject getProject() {
		return this.pCreationPage.getProjectHandle();
	}

}
