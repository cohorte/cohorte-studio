package org.cohorte.studio.eclipse.ui.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * Add or edit a Cohorte runtime.
 * 
 * @author Ahmad Shahwan
 *
 */
public class CRuntimeDialog extends StatusDialog {

	private IRuntime pData;
	private ICohortePreferences pPrefs;
	private File pDir;

	private Label pNameLabel;
	private Text pNameText;
	private Label pPathLabel;
	private Text pPathText;
	private Button pWorkspace;
	private Button pFilesystem;

	private static final String CONF_DIR = "conf"; //$NON-NLS-1$
	private static final String VER_FILE = "version.js"; //$NON-NLS-1$
	private static final String WS_LOC_FORMAT = "${workspace_loc:%s}"; //$NON-NLS-1$
	private static final String VER_FORMAT = "%s *"; //$NON-NLS-1$
	private static final String STAGE = "stage"; //$NON-NLS-1$
	private static final String VERSION = "version"; //$NON-NLS-1$
	private static final String RELEASE = "release"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	
	/**
	 * Edit dialog constructor.
	 * 
	 * @param parent
	 * @param aTitle
	 * @param aData		the runtime to edit.
	 * @throws IOException 
	 * @throws XItemExistsException 
	 */
	public CRuntimeDialog(Shell parent, String aTitle, IRuntime aData, ICohortePreferences aPrefs) {
		super(parent);
		this.pData = aData;
		this.setTitle(aTitle);
		this.pPrefs = aPrefs;
	}

	/**
	 * Obtain newly created or edited runtime.
	 * 
	 * @return
	 */
	public IRuntime getValue() {
		return pData;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		pNameLabel = new Label(composite, SWT.NONE);
		pNameLabel.setText(CMessages.NAME);
		pNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		pNameText = new Text(composite, SWT.BORDER);
		pNameText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));

		pPathLabel = new Label(composite, SWT.NONE);
		pPathLabel.setText(CMessages.PATH);
		pPathLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		pPathText = new Text(composite, SWT.BORDER);
		GridData wLayout = new GridData(SWT.FILL, SWT.TOP, true, false);
		wLayout.widthHint = 255;
		pPathText.setLayoutData(wLayout);
		
		pWorkspace = new Button(composite, SWT.BORDER);
		pWorkspace.setText(CMessages.WORKSPACE);
		pWorkspace.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		pFilesystem = new Button(composite, SWT.BORDER);
		pFilesystem.setText(CMessages.FILESYSTEM);
		pFilesystem.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		ModifyListener validationListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateStatus();
			}
		};
		pNameText.addModifyListener(validationListener);
		pPathText.addModifyListener(validationListener);
		
		this.pWorkspace.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				openWorkspace();				
			}
		});
		
		this.pFilesystem.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				openFilesystem();			
			}
		});

		Dialog.applyDialogFont(composite);
		applyData();
		pPathText.setFocus();
		return composite;
	}

	private void openWorkspace() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
			this.getShell(),
			ResourcesPlugin.getWorkspace().getRoot(),
			true,
			CMessages.CHOOSE_LOCATION_WORKSPACE
		);
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 0) {
				return;
			}
			IPath path = (IPath) result[0];
			this.pPathText.setText(String.format(WS_LOC_FORMAT, path.makeRelative().toString()));
		}
	}
	
	/**
	 * Browse file system.
	 */
	private void openFilesystem() {
		DirectoryDialog dialog = new DirectoryDialog(this.getShell());
		dialog.setFilterPath(this.pPathText.getText().trim());
		dialog.setText(CMessages.PATH_TO_RUNTIME);
		dialog.setMessage(CMessages.CHOOSE_LOCATION_FILESYSTEM);
		String result = dialog.open();
		if (result != null) {
			this.pPathText.setText(result);
		}
	}

	@Override
	public void create() {
		super.create();
		validate();
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	private String toString(String str) {
		return str == null ? EMPTY_STRING : str;
	}

	private void applyData() {
		if (pData != null) {
			pNameText.setText(toString(pData.getName()));
			pPathText.setText(toString(pData.getPath()));
		}
	}

	private boolean validate() {
		String wPath = null;
		String wMsg = null;
		try {
			wPath = VariablesPlugin
					.getDefault()
					.getStringVariableManager()
					.performStringSubstitution(pPathText.getText(), false);
		} catch (CoreException e) {
			wMsg = CMessages.CANNOT_EXPANT;
		}
		if (wPath != null) {
			this.pDir = new File(wPath);
			if (!pDir.isDirectory()) {
				wMsg = CMessages.INVALIDE_DIRECTORY;
			}
		}
		if (EMPTY_STRING.equals(pNameText.getText())) {
			wMsg = CMessages.INVALIDE_NAME;
		}
		if (wMsg != null) {
			updateStatus(new Status(IStatus.ERROR, CActivator.PLUGIN_ID, IStatus.OK, wMsg, null));
			return false;
		} else {
			return true;
		}
	}
	
	private String parseVersion() {
		String wVersion = null;
		File wVerFile = new File(new File(this.pDir, CONF_DIR), VER_FILE);
		if (wVerFile.isFile()) {
			try (JsonReader wReader = Json.createReader(new FileReader(wVerFile))) {
				JsonObject wVerObject = wReader.readObject();
				if (wVerObject.containsKey(VERSION)) {
					String wVer = wVerObject.getString(VERSION);
					boolean wRelease = true;
					if (wVerObject.containsKey(STAGE)) {
						wRelease = RELEASE.equalsIgnoreCase(wVerObject.getString(STAGE));
					}
					wVersion = wRelease ? wVer : String.format(VER_FORMAT, wVer);
				}
			} catch (FileNotFoundException e) {
			}
		}
		if (wVersion == null) {
			wVersion = CMessages.N_A;
		}
		return wVersion;		
	}

	@Override
	protected void okPressed() {
		if (validate()) {
			if (this.pData == null) {
				try {
					this.pData = this.pPrefs.createRuntime();
				} catch (IOException e) {
					MessageBox wDialog = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
					wDialog.setText(CMessages.ERROR_CREATING_RUNTIME);
					wDialog.setMessage(e.getMessage());
					wDialog.open();
					return;
				}
			}
			String wString;
			wString = pNameText.getText();
			if (wString == null) wString = ""; //$NON-NLS-1$
			this.pData.setName(wString);
			wString = pPathText.getText();
			if (wString == null) wString = ""; //$NON-NLS-1$
			this.pData.setPath(wString);
			this.pData.setVersion(this.parseVersion());
			super.okPressed();
		}
	}

	protected void updateStatus() {
		if (validate()) {
			updateStatus(Status.OK_STATUS);
		}
	}

}
