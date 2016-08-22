package org.cohorte.studio.eclipse.ui.api;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkingSet;

public interface IProjectUtils {


	
	/**
	 * Add project to working sets.
	 * 
	 * @param wProject
	 * @param aSets
	 */
	void addToWorkingSets(@NonNull IProject wProject, IWorkingSet[] aSets);
	
	/**
	 * Update perspective.
	 * 
	 * @return
	 */
	public boolean updatePerspective();
}