package org.cohorte.studio.eclipse.ui.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A composite holding the list and controls of Cohorte available runtimes.
 */
public class CCohorteRuntimeComposite extends Composite {

	private Label pLabel;
	TableViewer pViewer;
	private Button pAddButton;
	private Button pEditButton;
	private Button pRemoveButton;
	private Button pSetDefaultButton;

	private List<IRuntime> pRuntimes;

	@Inject
	private ICohortePreferences pPrefs;
	
	@Inject
	private ILogger pLog;

	CCohorteRuntimeComposite(Composite parent, int style) {
		super(parent, style);
		createWidgets();
	}

	protected void createWidgets() {
		setLayout(new GridLayout(2, false));

		this.pLabel = new Label(this, SWT.NONE);
		this.pLabel.setText("Cohorte runtimes");
		this.pLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));

		Table wTable = new Table(this,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		wTable.setHeaderVisible(true);
		wTable.setLinesVisible(true);
		wTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));

		this.pViewer = new TableViewer(wTable);
		ITableLabelProvider labelProvider = new CCohorteRuntimeLabelProvider();
		IStructuredContentProvider contentProvider = new CCohorteRuntimeContentProvider();
		String[] titles = { "Name", "Version", "Path" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableViewerColumn(this.pViewer, SWT.NONE).getColumn();
			column.setText(titles[i]);
			column.setResizable(true);
		}
		pViewer.setContentProvider(contentProvider);
		pViewer.setLabelProvider(labelProvider);

		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(40, 25, true));
		tableLayout.addColumnData(new ColumnWeightData(20, 0, true));
		tableLayout.addColumnData(new ColumnWeightData(40, 0, true));
		wTable.setLayout(tableLayout);

		this.pAddButton = createButton("Add");
		this.pEditButton = createButton("Edit");
		this.pRemoveButton = createButton("Remove");
		this.pSetDefaultButton = createButton("Set Default");

		pViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				enableButtons();
			}
		});

		pViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				editSelection();
			}
		});
		pAddButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addRuntime();
			}
		});
		pEditButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editSelection();
			}
		});
		pRemoveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeSelection();
			}
		});
		pSetDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setDefault();
			}
		});
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		pViewer.getTable().setEnabled(enabled);
		enableButtons();
	}

	protected void enableButtons() {
		if (this.getEnabled()) {
			boolean wSelected = isRuntimeSelected();
			boolean wNotDefault = isDefaultSelected();
			pAddButton.setEnabled(true);
			pEditButton.setEnabled(wSelected);
			pRemoveButton.setEnabled(wNotDefault);
			pSetDefaultButton.setEnabled(wNotDefault);
		} else {
			pAddButton.setEnabled(false);
			pEditButton.setEnabled(false);
			pRemoveButton.setEnabled(false);
			pSetDefaultButton.setEnabled(false);
		}
	}

	private boolean isDefaultSelected() {
		Iterator<?> iterator = ((IStructuredSelection) pViewer.getSelection()).iterator();
		boolean wRemovable = iterator.hasNext();
		while (iterator.hasNext()) {
			if (((IRuntime) iterator.next()).isDefault()) {
				wRemovable = false;
			}
		}
		return wRemovable;
	}
	
	private boolean isRuntimeSelected() {
		return !pViewer.getSelection().isEmpty();
	}

	protected void addRuntime() {
		this.pLog.info("Add runtime callback.");
		if (pRuntimes == null) return;
		IRuntime wRuntime = promptForRuntime(null);
		if (wRuntime != null) {
			pRuntimes.add(0, wRuntime);
			this.pViewer.refresh();
		}
	}

	protected void removeSelection() {
		this.pLog.info("Remove selection callback.");
		if (pRuntimes == null) return;
		IStructuredSelection selection = (IStructuredSelection) pViewer.getSelection();
		Iterator<?> it = selection.iterator();
		while (it.hasNext()) {
			IRuntime data = (IRuntime) it.next();
			try {
				data.remove();
				pRuntimes.remove(data);
			} catch (IOException e) {
				MessageBox wDialog = new MessageBox(this.getShell(), SWT.ICON_ERROR | SWT.OK);
				wDialog.setText("Error deleting runtime");
				wDialog.setMessage(e.getMessage());
				wDialog.open();
				continue;
			}
		}
		pViewer.refresh();
	}

	protected void editSelection() {
		this.pLog.info("Edit selection callback.");
		IStructuredSelection selection = (IStructuredSelection) pViewer.getSelection();
		Object[] selectedItems = selection.toArray();
		if (selectedItems.length == 0) return;
		IRuntime wRuntime = promptForRuntime((IRuntime) selectedItems[0]);
		if (wRuntime != null) {
			pViewer.refresh();
		}
	}
	
	protected void setDefault() {
		this.pLog.info("Set default callback.");
		IStructuredSelection selection = (IStructuredSelection) pViewer.getSelection();
		Object[] selectedItems = selection.toArray();
		if (selectedItems.length == 0) return;
		IRuntime wRuntime = (IRuntime) selectedItems[0];
		if (wRuntime != null) {
			IRuntime wDefault = this.pPrefs.getDefaultRuntime();
			if (wDefault != null) {
				wDefault.setDefault(false);
			}
			wRuntime.setDefault(true);
			pViewer.refresh();
		}
	}

	private IRuntime promptForRuntime(IRuntime selectedRutime) {
		this.pLog.info("Prompt runtime.");
		CRuntimeDialog wDialog = new CRuntimeDialog(
				getShell(), "Add new Cohorte Runtime", selectedRutime, this.pPrefs);
		
		int result = wDialog.open();
		if (result != Window.CANCEL) {
			return wDialog.getValue();
		}
		return null;
	}

	private Button createButton(String message) {
		Button button = new Button(this, SWT.PUSH);
		button.setText(message);
		button.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		return button;
	}

	public boolean cancel() {
		try {
			this.pPrefs.rollback();
		} catch (IOException e) {
			this.pLog.error(e, "Error cancelling preference changes.");
		}
		return true;
	}

	public boolean apply() {
		try {
			this.pPrefs.commit();
			return true;
		} catch (IOException e) {
			this.pLog.error(e, "Could not persist preferences.");
			return false;
		}
	}
	
	@PostConstruct
	public void initializeValues() {
		IRuntime[] wRuntimes = this.pPrefs.getRuntimes();
		pRuntimes = new ArrayList<>(wRuntimes.length);
		for (IRuntime wRuntime : wRuntimes) pRuntimes.add(wRuntime);
		pViewer.setInput(pRuntimes);
		enableButtons();
	}
}

