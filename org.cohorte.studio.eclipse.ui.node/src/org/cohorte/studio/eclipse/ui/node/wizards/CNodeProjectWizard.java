package org.cohorte.studio.eclipse.ui.node.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.core.registry.CRegistrar;
import org.cohorte.studio.eclipse.ui.node.nature.CNodeProjectNature;
import org.cohorte.studio.eclipse.ui.node.objects.CNode;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
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
		IProject wProject = getProject();
		IProgressMonitor wMonitor = new NullProgressMonitor();
		IProjectDescription wDescription = wProject.getWorkspace().newProjectDescription(getProjectName());
		
		/**
		 * @see org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard#createProject()
		 */
		URI wLocation = null;
		if (!pCreationPage.useDefaults()) {
			wLocation = pCreationPage.getLocationURI();
		}
		wDescription.setLocationURI(wLocation);
		
		String[] wNatures = wDescription.getNatureIds();
		String[] wNewNatures = new String[wNatures.length + 1];
		System.arraycopy(wNatures, 0, wNewNatures, 0, wNatures.length);
		wNewNatures[wNatures.length] = CNodeProjectNature.ID;
		IWorkspace wWorkspace = ResourcesPlugin.getWorkspace();
		if (wWorkspace.validateNatureSet(wNewNatures).getCode() == IStatus.OK) {
			wDescription.setNatureIds(wNewNatures);
		} else {
			return false;
		}
		try {
			wProject.create(wDescription, wMonitor);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
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
