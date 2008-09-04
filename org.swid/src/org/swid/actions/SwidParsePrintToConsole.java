package org.swid.actions;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.swid.editors.SwidEditorMessages;

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
		String ns = "http://code.google.com/p/swid/resource#";
		InputDialog dialog_ns = new InputDialog(this.getTextEditor().getEditorSite().getShell(),"Namespace","Enter Required namespace",ns,new IInputValidator() {
			@Override
			public String isValid(String newText) {
				if(newText.startsWith("http://") && newText.endsWith("#"))
					return null;
				else
					return "Should be of the form 'http://..../...#'";
			} });
		dialog_ns.create();
		dialog_ns.open();
		ns = dialog_ns.getValue();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = doc.get();
		FileDialog dialog = new FileDialog(this.getTextEditor().getEditorSite().getShell(),SWT.MULTI);
		//TODO Preferences..Set Reasoning option.
		dialog.setFilterExtensions(new String[] {"*.rdf","*.owl"});
		dialog.setText("Choose RDF file(s) to load..");
		dialog.open();
		String[] names = dialog.getFileNames();
		String filterpath = dialog.getFilterPath()+"/";
		//FIXME: The below method of sending the absolute path of directory is clumsy.. note debug does give /home/gen/.. in character array!
		//SwidFunctionalities.parseAndPrint(text,filterpath,names, ns);
		SwidFunctionalities.parseAndPrint(text,filterpath,names, ns);
	}

}