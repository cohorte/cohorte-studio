package org.cohorte.studio.eclipse.core.api;

import java.net.URI;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.annotations.Nullable;
import org.eclipse.core.resources.IProject;

/**
 * Project provider interface.
 * An object of this type holds necessary information to create a project.
 * 
 * @author Ahmad Shahwan
 *
 */
public interface IProjectProvide {
	/**
	 * Project name.
	 * 
	 * @return
	 */
	@NonNull String getProjectName();
	
	/**
	 * Project URI if one has been provided, or null if defaults are assumed.
	 * 
	 * @return
	 */
	@Nullable URI getLocationUri();

	/**
	 * Project handler.
	 * 
	 * @return
	 */
	@Nullable IProject getProject();
}
