package org.swid.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.swid.actions.SwidAddLocalSchemas;
import org.swid.actions.SwidAddRemoteSchemas;
import org.swid.actions.SwidParsePrintToConsole;

/**
 * @author venkatesh
 */
public class SwidActionContributor extends TextEditorActionContributor
{

    private SwidParsePrintToConsole fSwidParsePrintToConsole;

    private SwidAddLocalSchemas fSwidAddLocalSchemas;

    private SwidAddRemoteSchemas fSwidAddRemoteSchemas;

    public SwidActionContributor()
    {
        super();
        try {
            fSwidParsePrintToConsole = new SwidParsePrintToConsole();
            fSwidAddLocalSchemas = new SwidAddLocalSchemas();
            fSwidAddRemoteSchemas = new SwidAddRemoteSchemas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * @see IEditorActionBarContributor#init(IActionBars)
     */
    @Override
    public void init(IActionBars bars)
    {
        super.init(bars);
        IMenuManager menuManager = bars.getMenuManager();
        // IMenuManager editMenu =
        // menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
        IMenuManager swidMenu = new MenuManager("Swid", "Semantic Tools");
        if (swidMenu != null) {
            swidMenu.add(fSwidParsePrintToConsole);
            swidMenu.add(new Separator());
            swidMenu.add(fSwidAddLocalSchemas);
            swidMenu.add(fSwidAddRemoteSchemas);
            menuManager.add(swidMenu);
        }

    }

    private void doSetActiveEditor(IEditorPart part)
    {
        super.setActiveEditor(part);
        ITextEditor editor = null;
        if (part instanceof ITextEditor)
            editor = (ITextEditor) part;
        fSwidParsePrintToConsole.setEditor(editor);
        fSwidAddLocalSchemas.setEditor(editor);
        fSwidAddRemoteSchemas.setEditor(editor);
    }

    /*
     * @see IEditorActionBarContributor#setActiveEditor(IEditorPart)
     */
    @Override
    public void setActiveEditor(IEditorPart part)
    {
        super.setActiveEditor(part);
        doSetActiveEditor(part);
    }

    /*
     * @see IEditorActionBarContributor#dispose()
     */
    @Override
    public void dispose()
    {
        doSetActiveEditor(null);
        super.dispose();
    }
}
