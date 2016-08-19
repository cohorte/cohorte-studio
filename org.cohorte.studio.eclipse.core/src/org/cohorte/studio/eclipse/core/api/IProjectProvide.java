package org.cohorte.studio.eclipse.core.api;

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
