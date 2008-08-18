package org.swid.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class semEditor extends TextEditor {

	private ColorManager colorManager;

	public semEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
