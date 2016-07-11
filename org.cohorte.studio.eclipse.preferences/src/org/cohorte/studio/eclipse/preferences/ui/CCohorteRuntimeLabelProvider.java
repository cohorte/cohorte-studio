package org.cohorte.studio.eclipse.preferences.ui;

import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.cohorte.studio.eclipse.utils.i18n.IInternationalizable;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class CCohorteRuntimeLabelProvider extends BaseLabelProvider implements
ITableLabelProvider, IColorProvider, IInternationalizable {

	public CCohorteRuntimeLabelProvider() {
		super();
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element == null) {
			return null;
		}
		IRuntime data = (IRuntime) element;
		switch (columnIndex) {
		case 0:
			return data.getName();
		case 1:
			return data.getVersion();
		case 2:
			return data.getPath();
		default:
			return null;
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

}
