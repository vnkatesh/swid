package org.swid.actions;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.swid.editors.SwidEditorMessages;
import org.swid.actions.SwidFunctionalities;

/**
 * @author venkatesh
 *
 */
public class SwidParsePrintToConsole extends TextEditorAction {

	/**
	 * @param bundle
	 * @param prefix
	 * @param editor
	 */
	public SwidParsePrintToConsole() {
		super(SwidEditorMessages.getResourceBundle(),null,null);
		setText("PrintToConsole");
		setEnabled(true);
	}
	
	@Override
	public void run()
	{
		ITextEditor editor = this.getTextEditor();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = doc.get();
		SwidFunctionalities.parseAndPrint(text);
	}

}