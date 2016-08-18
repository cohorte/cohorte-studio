package org.cohorte.studio.eclipse.ui.node.utils;

import java.net.URI;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;

@Creatable
public class CProjectUtils implements IProjectUtils {
	
	@Inject
	private ILogger pLogger;
	
	@Inject
	private IWorkspace pWorkspace;
	
	@Inject
	private IWorkbench pWorkbench;

	@Inject
	public CProjectUtils() {
	}
	
	/**
	 * Create new project, with the given natures.
	 * 
	 * @param aProvider
	 * @param aNatures
	 * @return
	 */
	@Override
	public @Nullable IProject createProject(@NonNull IProjectProvide aProvider, String @Nullable [] aNatures) {
		IProject wProject = aProvider.getProject();
		if (wProject == null) {
			pLogger.error("Project null."); //$NON-NLS-1$
			return null;
		}
		IProgressMonitor wMonitor = new NullProgressMonitor();
		IProjectDescription wDescription = wProject.getWorkspace().newProjectDescription(aProvider.getProjectName());
		
		/**
		 * Null if default.
		 */
		URI wLocation = aProvider.getLocationUri();
		wDescription.setLocationURI(wLocation);
		
		try {
			if (aNatures != null) {
				if (this.pWorkspace.validateNatureSet(aNatures).getCode() == IStatus.OK) {
					wDescription.setNatureIds(aNatures);
				} else {
					pLogger.error("Invalid project nature combination."); //$NON-NLS-1$
					return null;
				}
			}

			wProject.create(wDescription, wMonitor);
		} catch (CoreException e) {
			pLogger.error(e, "Core Exception occured."); //$NON-NLS-1$
			return null;
		}
		return wProject;
	}
	
	/**
	 * Create new generic project.
	 * 
	 * @param aProvider
	 * @return
	 */
	@Override
	public @Nullable IProject createProject(@NonNull IProjectProvide aProvider) {
		return this.createProject(aProvider, null);
	}

	@Override
	public void addToWorkingSets(@NonNull IProject wProject, IWorkingSet[] aSets) {
		this.pWorkbench.getWorkingSetManager().addToWorkingSets(wProject, aSets);
		
	}

}
