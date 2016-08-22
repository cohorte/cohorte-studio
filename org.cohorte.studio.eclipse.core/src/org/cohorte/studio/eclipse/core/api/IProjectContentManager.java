package org.cohorte.studio.eclipse.core.api;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.objects.IProjectModel;
import org.eclipse.core.resources.IProject;

/**
 * Generic project content management service.
 * 
 * @author Ahmad Shahwan
 *
 */
public interface IProjectContentManager<T extends IProjectModel> {
	
	/**
	 * Create project content.
	 * 
	 * @param aProject
	 * @param aModel TODO
	 * @throws Exception 
	 */
	void populate(final @NonNull IProject aProject, @NonNull T aModel) throws Exception;

}
