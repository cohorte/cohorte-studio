package org.cohorte.studio.eclipse.preferences.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.cohorte.studio.eclipse.api.managers.ICohortePreferences;
import org.cohorte.studio.eclipse.api.managers.ILogger;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.cohorte.studio.eclipse.utils.i18n.IInternationalizable;
import org.eclipse.jface.viewers.ColumnPixelData;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A composite holding the list and controls of Cohorte available runtimes.
 */
public class CCohorteRuntimeComposite extends Composite implements IInternationalizable {

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
		this.pLabel.setText(i("Cohorte runtimes"));
		this.pLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));

		Table wTable = new Table(this,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		wTable.setHeaderVisible(true);
		wTable.setLinesVisible(true);
		wTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));

		this.pViewer = new TableViewer(wTable);
		ITableLabelProvider labelProvider = new CCohorteRuntimeLabelProvider();
		IStructuredContentProvider contentProvider = new CCohorteRuntimeContentProvider();
		String[] titles = { i("Name"), i("Version"), i("Path") };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableViewerColumn(this.pViewer, SWT.NONE).getColumn();
			column.setText(titles[i]);
			column.setResizable(true);
		}
		pViewer.setContentProvider(contentProvider);
		pViewer.setLabelProvider(labelProvider);

		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnPixelData(24));
		tableLayout.addColumnData(new ColumnWeightData(50, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(50, 50, true));
		wTable.setLayout(tableLayout);

		this.pAddButton = createButton(i("Add"));
		this.pEditButton = createButton(i("Edit"));
		this.pRemoveButton = createButton(i("Remove"));
		this.pSetDefaultButton = createButton(i("Set Default"));

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
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		pViewer.getTable().setEnabled(enabled);
		enableButtons();
	}

	protected void enableButtons() {
		if (this.getEnabled()) {
			boolean wRemovable = isSelectionRemovable();
			pAddButton.setEnabled(true);
			pEditButton.setEnabled(true);
			pRemoveButton.setEnabled(wRemovable);
			pSetDefaultButton.setEnabled(wRemovable);
		} else {
			pAddButton.setEnabled(false);
			pEditButton.setEnabled(false);
			pRemoveButton.setEnabled(false);
			pSetDefaultButton.setEnabled(false);
		}
	}

	private boolean isSelectionRemovable() {
		IStructuredSelection selection = (IStructuredSelection) pViewer
				.getSelection();
		Iterator<?> iterator = selection.iterator();
		boolean wRemovable = iterator.hasNext();
		while (iterator.hasNext()) {
			if (((IRuntime) iterator.next()).isDefault()) {
				wRemovable = false;
			}
		}
		return wRemovable;
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
			pRuntimes.remove(data);
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

	private IRuntime promptForRuntime(IRuntime selectedRutime) {
		this.pLog.info("Prompt runtime.");
		CRuntimeDialog wDialog = new CRuntimeDialog(
				getShell(), i("Add new Cohorte Runtime"), selectedRutime, this.pPrefs);
		
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



	public void performApply() {
		/**
		 * TODO
		 */
	}

	public void refresh() {
		pViewer.refresh();
	}
	
	@PostConstruct
	public void initializeValues() {
		pRuntimes = Arrays.asList(this.pPrefs.getRuntimes());
		pViewer.setInput(pRuntimes);
		enableButtons();
	}
}

