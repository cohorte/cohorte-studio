package org.cohorte.studio.eclipse.ui.node.wizards;

import org.cohorte.studio.eclipse.api.objects.INode;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@NonNullByDefault
public interface INodeProjectPage extends IWizardPage {
	
	default Label createLabel(Composite container) {
		Label label = new Label(container, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalIndent = 30;
		label.setLayoutData(gd);
		return label;
	}

	default Text createText(Composite container) {
		Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		text.setLayoutData(gd);
		return text;
	}
	
	default Button createButton(Composite container, int style, int span, int indent) {
		Button button = new Button(container, style);
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		gd.horizontalIndent = indent;
		button.setLayoutData(gd);
		return button;
	}
	
	default Combo createCombo(Composite container) {
		return new Combo(container, SWT.READ_ONLY | SWT.BORDER);
	}
	
	default Group createGroup(Composite container, int cols) {
		Group wGroup = new Group(container, SWT.NONE);
		GridLayout wLayout = new GridLayout();
		wLayout.numColumns = cols;
		wGroup.setLayout(wLayout);
		wGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return wGroup;
	}
	
	INode getModel();
}
