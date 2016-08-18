package org.cohorte.studio.eclipse.ui.node.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.core.registry.CRegistrar;
import org.cohorte.studio.eclipse.ui.node.Activator;
import org.cohorte.studio.eclipse.ui.node.objects.CNode;
import org.cohorte.studio.eclipse.ui.node.project.CNodeProjectNature;
import org.cohorte.studio.eclipse.ui.node.utils.CProjectUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

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
	
	@Inject @Optional
	private IConfigurationElement pConfElement;

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
		updatePerspective();
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
	
	private boolean updatePerspective() {
		/**
		 * @see org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard#updatePerspective(IConfigurationElement)
		 */
		
		/** Do not change perspective if the configuration element is not specified. */
		if (pConfElement == null) return false;

		/** Read the requested perspective ID to be opened. */
		String finalPerspId = pConfElement.getAttribute("finalPerspective"); //$NON-NLS-1$
		if (finalPerspId == null) return false;

		/** Map perspective id to descriptor. */
		IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();

		IPerspectiveDescriptor finalPersp = reg.findPerspectiveWithId(finalPerspId);

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {

			// prompt the user to switch
			if (!confirmPerspectiveSwitch(window, finalPersp)) return false;
		}

		int workbenchPerspectiveSetting = Activator.getDefault().getPreferenceStore()
				.getInt("OPEN_PERSPECTIVE_MODE"); //$NON-NLS-1$

		/** open perspective in new window setting */
		if (workbenchPerspectiveSetting == 2) {
			try {
				PlatformUI.getWorkbench().openWorkbenchWindow(finalPerspId, ResourcesPlugin.getWorkspace().getRoot());
			} catch (WorkbenchException e) {
				return false;
			}
			return true;
		}

		/** replace active perspective setting otherwise */
		if (window == null) return false;
		IWorkbenchPage page = window.getActivePage();
		if (page == null) return false;

		/** Set the perspective. */
		page.setPerspective(finalPersp);
		return true;
	}

	private boolean confirmPerspectiveSwitch(IWorkbenchWindow window, IPerspectiveDescriptor finalPersp) {
		// TODO Auto-generated method stub
		return true;
	}

}
