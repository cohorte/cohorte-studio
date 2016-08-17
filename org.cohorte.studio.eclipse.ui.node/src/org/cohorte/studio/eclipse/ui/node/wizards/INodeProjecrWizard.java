package org.cohorte.studio.eclipse.ui.node.wizards;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.INewWizard;

/**
 * New Chorte Node project wizard.
 * 
 * @author Ahmad Shahwan
 *
 */
public interface INodeProjecrWizard extends INewWizard {
	
	@NonNull
	INode getModel();
	
	@NonNull
	String getProjectName();
	
	@NonNull
	public IPath getLocationPath();

	public IProject getProject();
}
