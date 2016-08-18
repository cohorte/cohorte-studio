package org.cohorte.studio.eclipse.ui.node.wizards;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.ui.node.utils.IProjectProvide;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ui.INewWizard;

/**
 * New Chorte Node project wizard.
 * 
 * @author Ahmad Shahwan
 *
 */
public interface INodeProjecrWizard extends INewWizard, IProjectProvide {
	
	@NonNull
	INode getModel();
	

}
