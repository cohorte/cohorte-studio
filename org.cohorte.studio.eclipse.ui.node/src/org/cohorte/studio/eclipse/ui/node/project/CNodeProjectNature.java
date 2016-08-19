package org.cohorte.studio.eclipse.ui.node.project;

import org.cohorte.studio.eclipse.ui.node.CActivator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class CNodeProjectNature implements IProjectNature {
	
	public static final String ID = String.format(
			"%s.%s", //$NON-NLS-1$
			CActivator.getDefault().getBundle().getSymbolicName(),
			"NodeNature");  //$NON-NLS-1$ 
	
	private IProject pProject;

	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		return this.pProject;
	}

	@Override
	public void setProject(IProject project) {
		this.pProject = project;
	}

}
