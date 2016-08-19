package org.cohorte.studio.eclipse.ui.api;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.annotation.NonNull;
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