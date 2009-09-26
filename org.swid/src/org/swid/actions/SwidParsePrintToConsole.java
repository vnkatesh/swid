package org.swid.actions;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.ontoware.rdf2go.model.Syntax;
import org.swid.editors.SwidEditorMessages;

/**
 * @author venkatesh
 */
public class SwidParsePrintToConsole extends TextEditorAction
{

    /**
     * @param bundle
     * @param prefix
     * @param editor
     */
    public SwidParsePrintToConsole()
    {
        super(SwidEditorMessages.getResourceBundle(), null, null);
        setText("Print to Console");
        setEnabled(true);
    }

    @Override
    public void run()
    {
        ITextEditor editor = this.getTextEditor();
        String ns = "http://code.google.com/p/swid/resource#";
        InputDialog dialog_ns =
            new InputDialog(this.getTextEditor().getEditorSite().getShell(), "Namespace", "Enter Required namespace",
                ns, new IInputValidator()
                {
                    @Override
                    public String isValid(String newText)
                    {
                        if (newText.startsWith("http://") && newText.endsWith("#"))
                            return null;
                        else
                            return "Should be of the form 'http://..../...#'";
                    }
                });
        dialog_ns.create();
        dialog_ns.open();
        ListDialog dialog = new ListDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        dialog.setBlockOnOpen(true);
        dialog.setAddCancelButton(true);
        dialog.setContentProvider(new ArrayContentProvider());
        dialog.setLabelProvider(new LabelProvider());
        dialog.setInput(Syntax.list().toArray());
        dialog.setTitle("Select Syntax of Output");
        dialog.open();
        ns = dialog_ns.getValue();
        IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
        String text = doc.get();
        SwidFunctionalities.parseAndPrint(text, ns, (Syntax) dialog.getResult()[0]);
    }

}
