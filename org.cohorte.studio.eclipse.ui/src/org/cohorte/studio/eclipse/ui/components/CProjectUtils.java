package org.cohorte.studio.eclipse.ui.components;

import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.ui.CActivator;
import org.cohorte.studio.eclipse.ui.api.IProjectUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**
 * Project utilities component.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CProjectUtils implements IProjectUtils {
	
	@Inject
	private ILogger pLogger;
	
	@Nullable
	private IConfigurationElement pConfElement;
	
	@Nullable
	private IWorkbench pWorkbench;

	@Inject
	public CProjectUtils() {
	}
	
	@Override
	public void addToWorkingSets(@NonNull IProject wProject, IWorkingSet[] aSets) {
		IWorkbench wWorkbench = this.pWorkbench;
		if (wWorkbench != null) {
			wWorkbench.getWorkingSetManager().addToWorkingSets(wProject, aSets);
		} else {
			this.pLogger.warning("Workbench is null."); //$NON-NLS-1$
		}
	}
	
	@Override
	public boolean updatePerspective() {
		/**
		 * @see org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard#updatePerspective(IConfigurationElement)
		 */
		
		/** Do not change perspective if the configuration element is not specified. */
		IConfigurationElement wConfElement = this.pConfElement;
		if (wConfElement == null) {
			pLogger.info("No perspective change: null conf element."); //$NON-NLS-1$
			return false;
		}

		/** Read the requested perspective ID to be opened. */
		String finalPerspId = wConfElement.getAttribute("finalPerspective"); //$NON-NLS-1$
		if (finalPerspId == null) {
			pLogger.info("No perspective change: no finalPerspective in conf."); //$NON-NLS-1$
			return false;
		}

		/** Map perspective id to descriptor. */
		IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();

		IPerspectiveDescriptor finalPersp = reg.findPerspectiveWithId(finalPerspId);

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {

			// prompt the user to switch
			if (!confirmPerspectiveSwitch(window, finalPersp)) {
				pLogger.info("No perspective change: user canceled."); //$NON-NLS-1$
				return false;
			}
		}

		int workbenchPerspectiveSetting = CActivator.getDefault().getPreferenceStore()
				.getInt("OPEN_PERSPECTIVE_MODE"); //$NON-NLS-1$

		/** open perspective in new window setting */
		if (workbenchPerspectiveSetting == 2) {
			try {
				PlatformUI.getWorkbench().openWorkbenchWindow(finalPerspId, ResourcesPlugin.getWorkspace().getRoot());
			} catch (WorkbenchException e) {
				pLogger.error(e, "No perspective change: WorkbenchException occured."); //$NON-NLS-1$
				return false;
			}
			pLogger.info("Perspective changed."); //$NON-NLS-1$
			return true;
		}

		/** replace active perspective setting otherwise */
		if (window == null) {
			pLogger.info("No perspective change: null window."); //$NON-NLS-1$
			return false;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			pLogger.info("No perspective change: user page."); //$NON-NLS-1$
			return false;
		}

		/** Set the perspective. */
		page.setPerspective(finalPersp);
		pLogger.info("Perspective changed."); //$NON-NLS-1$
		return true;
	}

	private boolean confirmPerspectiveSwitch(IWorkbenchWindow window, IPerspectiveDescriptor finalPersp) {
		return true;
	}
	
	@Inject
	private void setConfiguration(@Optional IConfigurationElement aConfElement) {
		this.pConfElement = aConfElement;
	}
	
	@Inject
	private void setWorkbench(@Optional IWorkbench aWorkbench) {
		this.pWorkbench = aWorkbench;
	}

}
