package org.cohorte.studio.eclipse.core.api;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.annotations.Nullable;
import org.eclipse.core.resources.IProject;

/**
 * Project factory service.
 * 
 * @author Ahmad Shahwan
 *
 */
public interface IProjectFactory {
	
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

}
