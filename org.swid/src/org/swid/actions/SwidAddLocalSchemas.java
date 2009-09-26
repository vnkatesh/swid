package org.swid.actions;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.swid.editors.SwidEditorMessages;

/**
 * @author venkatesh
 */
public class SwidAddLocalSchemas extends TextEditorAction
{

    /**
     * @param bundle
     * @param prefix
     * @param editor
     */
    public SwidAddLocalSchemas()
    {
        super(SwidEditorMessages.getResourceBundle(), null, null);
        setText("Add Local Schemas");
        setEnabled(true);
    }

    @Override
    public void run()
    {
        ITextEditor editor = this.getTextEditor();
        IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
        FileDialog dialog = new FileDialog(this.getTextEditor().getEditorSite().getShell(), SWT.MULTI);
        // TODO Preferences..Set Reasoning option.
        dialog.setFilterExtensions(new String[] {"*.rdf", "*.owl", "*.rdfs"});
        dialog.setText("Choose Schema file(s) to load..");
        dialog.open();
        String[] names = dialog.getFileNames();
        String filterpath = dialog.getFilterPath() + "/";
        for (String name : names) {
            doc.set(doc.get() + "\n%Load file://" + filterpath + name);
        }
    }
}
