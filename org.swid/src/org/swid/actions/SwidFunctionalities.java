package org.swid.actions;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.ontoware.rdf2go.Reasoning;
import org.swid.wikimodel.extensions.PrintPropertyListener;
import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.common.CommonWikiParser;

/**
 * @author venkatesh
 * 
 */
public class SwidFunctionalities {

	public static void parseAndPrint(final String text,final String dir,final String[] names,final String ns) {
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
					if(monitor==null)
						monitor = new NullProgressMonitor();
					try
					{
						monitor.beginTask("Working..", 100);
						monitor.subTask("Reading..");
						StringReader reader = new StringReader(text);
						IWikiParser parser = new CommonWikiParser();
						final StringBuffer buf = new StringBuffer();
						String namespace = ns;
						monitor.worked(10);
						IWemListener listener = new PrintPropertyListener(newPrinter(buf), namespace, Reasoning.rdfs,dir,names);
						try {
							monitor.subTask("Parsing Input..");
							parser.parse(reader, listener);
							monitor.worked(70);
						} catch (WikiParserException e) {
							e.printStackTrace();
						}
						monitor.subTask("Printing...");
						String test = buf.toString();
						monitor.worked(10);
						System.out.println(test);
						monitor.worked(10);
					}
					finally
					{
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	protected static IWikiPrinter newPrinter(final StringBuffer buf) {
		IWikiPrinter printer = new IWikiPrinter() {

			public void print(String str) {
				buf.append(str);
			}

			public void println(String str) {
				buf.append(str);
				buf.append("\n");
			}
		};
		return printer;
	}
}