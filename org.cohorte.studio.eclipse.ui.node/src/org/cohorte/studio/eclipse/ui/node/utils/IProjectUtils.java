package org.cohorte.studio.eclipse.ui.node.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IWorkingSet;

public interface IProjectUtils {

	/**
	 * Create new project, with the given natures.
	 * 
	 * @param aProvider
	 * @param aNatures
	 * @return
	 */
	@Nullable IProject createProject(@NonNull IProjectProvide aProvider);

	/**
	 * Create new generic project.
	 * 
	 * @param aProvider
	 * @return
	 */
	@Nullable IProject createProject(@NonNull IProjectProvide aProvider, String @Nullable [] aNatures);
	
	/**
	 * Add project to working sets.
	 * 
	 * @param wProject
	 * @param aSets
	 */
	void addToWorkingSets(@NonNull IProject wProject, IWorkingSet[] aSets);
}