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
import java.util.Iterator;

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
import org.ontoware.rdf2go.model.Statement;
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

    private Model currentModel;

    public static void parseAndPrint(final String text, final String dir, final String[] names, final String ns)
    {
        Model model = returnParsedModel(text, dir, names, ns);
        System.out.println("\n========================DUMP==================================\n");
        model.dump();
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        System.out.println("\n========================NTriples==================================\n");
        try {
            // model.writeTo(writer, Syntax.Ntriples);
            // System.out.println("========================RDFXML==================================");
            // model.writeTo(writer, Syntax.RdfXml);
            System.out.println("\n========================Turtle==================================\n");
            model.writeTo(writer, Syntax.Turtle);
        } catch (ModelRuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("##########################################here###################################3");
        Iterator<Statement> iterator = model.iterator();
        for (Statement stmt = null; iterator.hasNext(); stmt = iterator.next()) {
            try {
                System.out.println(stmt.getSubject().toString() + " --> ");
            } catch (NullPointerException e) {
                System.out.println("  --> ");
            }
            try {
                System.out.print(stmt.getPredicate().toString() + " --> ");
            } catch (NullPointerException e) {
                System.out.print("  --> ");
            }
            try {
                System.out.print(stmt.getObject().toString());
            } catch (NullPointerException e) {
                System.out.println(" ");
            }
        }
        model.close();
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
                            new PrintPropertyListener(newPrinter(buf), namespace, Reasoning.rdfs, dir, names);
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
        boolean quick)
    {
        if (quick)
            currentModel = quickReturnParsedModel(text, dir, names, ns);
        else
            currentModel = returnParsedModel(text, dir, names, ns);
    }

    public void saveCurrentModel(Model model)
    {
        currentModel = model;
    }

    public Model getGetCurrentModel()
    {
        return currentModel;
    }

    public SwidFunctionalities()
    {
        currentModel = null;
    }

    public static Model quickReturnParsedModel(final String text, final String dir, final String[] names,
        final String ns)
    {
        IWemListenerModified listener =
            new PrintPropertyListener(newPrinter(new StringBuffer()), ns, Reasoning.rdfs, dir, names);
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
            model = readFrom(model, file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static Model readFrom(Model model, File file)
    {
        try {
            model.readFrom(new FileReader(file));
            return model;
        } catch (ModelRuntimeException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
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

    public void createModel()
    {
        currentModel = RDF2Go.getModelFactory().createModel();
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
            PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
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
                };
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
