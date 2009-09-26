package org.swid.actions;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.swid.editors.SwidEditorMessages;

/**
 * @author venkatesh
 */
public class SwidAddRemoteSchemas extends TextEditorAction
{

    /**
     * @param bundle
     * @param prefix
     * @param editor
     */
    public SwidAddRemoteSchemas()
    {
        super(SwidEditorMessages.getResourceBundle(), null, null);
        setText("Add Remote Schemas");
        setEnabled(true);
    }

    @Override
    public void run()
    {
        ITextEditor editor = this.getTextEditor();
        IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
        InputDialog dialog =
            new InputDialog(this.getTextEditor().getEditorSite().getShell(), "Schema URL", "Enter required URL",
                "http://xmlns.com/foaf/spec/20071002.rdf", new IInputValidator()
                {
                    @Override
                    public String isValid(String newText)
                    {
                        if ((newText.startsWith("http://") || newText.startsWith("ftp://"))
                            && (newText.endsWith(".rdfs") || newText.endsWith(".rdf") || newText.endsWith(".owl")))
                            return null;
                        else
                            return "Should be of the form '(http|ftp)://....(.rdfs|.rdf|.owl)'";
                    }
                });
        dialog.create();
        dialog.open();
        doc.set(doc.get() + "\n%Load " + dialog.getValue());
    }
}
