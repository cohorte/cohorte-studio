package org.cohorte.studio.eclipse.ui.node.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.core.api.IProjectFactory;
import org.cohorte.studio.eclipse.ui.api.CUIRegistrar;
import org.cohorte.studio.eclipse.ui.api.IProjectUtils;
import org.cohorte.studio.eclipse.ui.node.objects.CNode;
import org.cohorte.studio.eclipse.ui.node.project.CNodeContentManager;
import org.cohorte.studio.eclipse.ui.node.project.CNodeProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

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
	private @NonNull INode pCohorteNode;
	
	private @NonNull CUIRegistrar pRegistrar;
	
	@Inject
	private CNodeProjectCreationPage pCreationPage;
	
	@Inject
	private CNodeProjectDetailsPage pDetailsPage;
	
	@Inject
	private IProjectFactory pFactory;
	
	@Inject
	private IProjectUtils pUtils; 
	
	@Inject
	private CNodeContentManager pContentManager;
	
	@Inject
	private ILogger pLogger;
	
	public CNodeProjectWizard() {
		pCohorteNode = new CNode();
		pRegistrar = new CUIRegistrar();
		pRegistrar.register(this).as(INodeProjecrWizard.class);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection aSellection) {
		this.pCohorteNode = new CNode();
	}

	@Override
	public boolean performFinish() {
		this.pCreationPage.updateModel(this.pCohorteNode);
		this.pDetailsPage.updateModel(this.pCohorteNode);
		IProject wProject = this.pFactory.createProject(this, new String [] { CNodeProjectNature.ID });
		if (wProject == null) {
			this.pLogger.warning("Project is null."); //$NON-NLS-1$
			return false;
		}
		try {
			wProject.open(null);
			this.pContentManager.populate(wProject, this.getModel());
		} catch (CoreException e) {
			this.pLogger.error(e, "Error populating Cohorte node project."); //$NON-NLS-1$
		}
		this.pUtils.addToWorkingSets(wProject, this.pCreationPage.getSelectedWorkingSets());
		this.pUtils.updatePerspective();
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
