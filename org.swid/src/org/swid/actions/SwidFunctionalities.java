package org.swid.actions;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
 * 
 */
public class SwidFunctionalities {

	public static void parseAndPrint(final String text, final String dir,
			final String[] names, final String ns) {
		Model model = returnParsedModel(text, dir, names, ns);
		System.out
				.println("\n========================DUMP==================================\n");
		model.dump();
		OutputStreamWriter writer = new OutputStreamWriter(System.out);
		System.out
				.println("\n========================NTriples==================================\n");
		try {
			model.writeTo(writer, Syntax.Ntriples);
			// System.out.println(
			// "========================RDFXML=================================="
			// );
			// model.writeTo( writer, Syntax.RdfXml);
			System.out
					.println("\n========================Turtle==================================\n");
			model.writeTo(writer, Syntax.Turtle);
		} catch (ModelRuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(
		// "##########################################here###################################3"
		// );
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
	}

	public static Model returnParsedModel(final String text, final String dir,
			final String[] names, final String ns) {
		try {
			final Model model = RDF2Go.getModelFactory().createModel();
			model.open();
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(
					new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
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
								IWemListenerModified listener = new PrintPropertyListener(
										newPrinter(buf), namespace,
										Reasoning.rdfs, dir, names);
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

	public static Model quickReturnParsedModel(final String text,
			final String dir, final String[] names, final String ns) {
		IWemListenerModified listener = new PrintPropertyListener(
				newPrinter(new StringBuffer()), ns, Reasoning.rdfs, dir, names);
		try {
			(new CommonWikiParser()).parse(new StringReader(text), listener);
		} catch (WikiParserException e) {
			e.printStackTrace();
		}
		return listener.getModel();
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