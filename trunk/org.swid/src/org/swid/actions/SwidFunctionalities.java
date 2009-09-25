package org.swid.actions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.swid.wikimodel.extensions.IWemListenerModified;
import org.swid.wikimodel.extensions.PrintPropertyListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.common.CommonWikiParser;

/**
 * @author venkatesh
 */
public class SwidFunctionalities
{

    private static Model currentModel;

    public static void parseAndPrint(final String text, final String dir, final String[] names, final String ns)
    {
        if (currentModel == null)
            createModel();
        currentModel.addModel(returnParsedModel(text, dir, names, ns));
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        try {
            System.out.println("\n========================Turtle==================================\n");
            currentModel.writeTo(writer, Syntax.Turtle);
        } catch (ModelRuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Model returnParsedModel(final String text, final String dir, final String[] names, final String ns)
    {
        try {
            final Model model = RDF2Go.getModelFactory().createModel();
            model.open();
            PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    if (monitor == null)
                        monitor = new NullProgressMonitor();
                    try {
                        monitor.beginTask("Working..", 100);
                        monitor.subTask("Reading..");
                        StringReader reader = new StringReader(text);
                        IWikiParser parser = new CommonWikiParser();
                        final StringBuffer buf = new StringBuffer();
                        String namespace = ns;
                        monitor.worked(10);
                        IWemListenerModified listener =
                            new PrintPropertyListener(newPrinter(buf), namespace, Reasoning.rdfs, dir, names, monitor);
                        try {
                            monitor.subTask("Parsing Input..");
                            parser.parse(reader, listener);
                            monitor.worked(80);
                            model.addModel(listener.getModel());
                        } catch (WikiParserException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        monitor.done();
                    }
                }
            });
            return model;
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public void saveCurrentModel(final String text, final String dir, final String[] names, final String ns,
        IProgressMonitor monitor, boolean quick)
    {
        if (quick)
            currentModel = quickReturnParsedModel(text, dir, names, ns, monitor);
        else
            currentModel = returnParsedModel(text, dir, names, ns);
    }

    public void saveCurrentModel(Model model)
    {
        currentModel = model;
    }

    public static Model getGetCurrentModel()
    {
        if (currentModel == null)
            createModel();
        return currentModel;
    }

    public SwidFunctionalities()
    {
        createModel();
    }

    public static Model quickReturnParsedModel(final String text, final String dir, final String[] names,
        final String ns, IProgressMonitor monitor)
    {
        IWemListenerModified listener =
            new PrintPropertyListener(newPrinter(new StringBuffer()), ns, Reasoning.rdfs, dir, names, monitor);
        try {
            (new CommonWikiParser()).parse(new StringReader(text), listener);
        } catch (WikiParserException e) {
            e.printStackTrace();
        }
        return listener.getModel();
    }

    protected static IWikiPrinter newPrinter(final StringBuffer buf)
    {
        IWikiPrinter printer = new IWikiPrinter()
        {

            public void print(String str)
            {
                // buf.append(str);
                System.out.print(str);
            }

            public void println(String str)
            {
                // buf.append(str);
                // buf.append("\n");
                System.out.println(str);
            }

        };
        return printer;
    }

    public static Model returnRemoteModel(final String url)
    {
        String tmpDir;
        try {
            tmpDir = File.createTempFile("swid", "").getParent();
            String[] split_url = url.split("/");
            final File file = new File(tmpDir + "/" + split_url[split_url.length - 1]);
            Model model = RDF2Go.getModelFactory().createModel();
            model.open();
            if (!file.exists()) {
                downloadRemoteModel(url, file);
            }
            model = readFrom(model, new FileReader(file.getAbsolutePath()));
            return model;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static Model readFrom(Model model, FileReader fileReader)
    {

        try {
            model.readFrom(fileReader);
        } catch (ModelRuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static void createModel()
    {
        currentModel = RDF2Go.getModelFactory().createModel();
        currentModel.open();
    }

    public void loadRemoteModel(String url)
    {

        if (currentModel == null) {
            createModel();
        }
        currentModel.addModel(returnRemoteModel(url));
    }

    public static void downloadRemoteModel(final String url, final File file)
    {
        try {
            URL attachmentURL = new URL(url);
            URLConnection attachmentConnection = attachmentURL.openConnection();
            BufferedInputStream bis = new BufferedInputStream(attachmentConnection.getInputStream());
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[1024];
            int count = 0;
            while ((count = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, count);
            }
            bis.close();
            bos.close();
        } catch (FileNotFoundException e) {
            showMessageDialog(Display.getDefault().getActiveShell(), SWT.ICON_ERROR, "File Error.",
                "There was an error with Opening/Creating a temporary file on your local system.");
        } catch (MalformedURLException e) {
            showMessageDialog(Display.getDefault().getActiveShell(), SWT.ICON_ERROR, "URL Error.",
                "There was error with URL/Locating URL of the Attachment file.");
        } catch (IOException e) {
            showMessageDialog(Display.getDefault().getActiveShell(), SWT.ICON_ERROR, "File Error.",
                "There was an error with Opening/Creating a temporary file on your local system.");
        }
    }

    public static void showMessageDialog(Shell shell, String title, String message)
    {
        showMessageDialog(shell, SWT.ICON_INFORMATION, title, message);
    }

    public static void showMessageDialog(Shell shell, int style, String title, String message)
    {
        MessageBox mb = new MessageBox(shell, style | SWT.APPLICATION_MODAL);
        mb.setText(title);
        mb.setMessage(message);
        mb.open();
    }
}
