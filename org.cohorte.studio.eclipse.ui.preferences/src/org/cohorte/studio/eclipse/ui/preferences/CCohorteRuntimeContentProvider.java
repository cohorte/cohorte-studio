package org.cohorte.studio.eclipse.ui.preferences;

import java.util.Collection;

import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for Cohorte runtime objects.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CCohorteRuntimeContentProvider implements IStructuredContentProvider {

	public CCohorteRuntimeContentProvider() {
		super();
	}

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Collection<?> coll = (Collection<?>) inputElement;
		return coll.toArray(new IRuntime[0]);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// Do nothing
	}

}
