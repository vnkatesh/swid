package org.swid.wikimodel.extensions;

import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.PlainLiteral;
import org.ontoware.rdf2go.model.node.URI;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiReference;

/**
 * @author venkatesh
 * @version alpha
 */

public class PrintPropertyListener extends BlankListener {

	protected class NTriples {
		private String predicate;
		private String subject;
		private Value value;

		public NTriples(String subject, String predicate, Value value) {
			this.subject = subject;
			this.predicate = predicate;
			this.value = value;
		}

		public void addStatement() {
			URI subject = model.createURI(namespace + this.subject);
			URI predicate = model.createURI(namespace + this.predicate);
			if (value.isLiteral) {
				PlainLiteral value = model.createPlainLiteral(this.value
						.getValue());
				model.addStatement(subject, predicate, value);
			} else {
				URI value = model.createURI(namespace + this.value.getValue());
				model.addStatement(subject, predicate, value);
			}
		}

		public String getPredicate() {
			return predicate;
		}

		public String getSubject() {
			return subject;
		}

		public Value getValue() {
			return value;
		}

		public void printStatement(int i) {
			while (i > 0) {
				print("\t");
				i--;
			}
			println(subject + " --> " + predicate + " --> " + value.getValue());
		}
	}

	protected class Value {
		private boolean isLiteral;
		private String value;

		public Value(boolean isLiteral, String value) {
			this.isLiteral = isLiteral;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public boolean isLiteral() {
			return isLiteral;
		}
	}

	public static String getRandObj(int n) {
		char[] s = new char[n];
		int c = 'A';
		int r1 = 0;
		for (int i = 0; i < n; i++) {
			r1 = (int) (Math.random() * 3);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 10);
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 26);
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 26);
				break;
			}
			s[i] = (char) c;
		}
		return new String(s);
	}

	private List<String> arr_pre = new ArrayList<String>();
	private List<String> arr_sub = new ArrayList<String>();
	private List<Value> arr_val = new ArrayList<Value>();
	private String document;
	private final IWikiPrinter fPrinter;
	private int i;
	private org.ontoware.rdf2go.model.Model model;
	private ModelFactory modelFactory;

	private final String namespace = "http://code.google.com/p/wikimodel/resource/commonwikiparsertest#";

	private Reasoning reasoning = Reasoning.rdfs; // can be Reasoning.owl also

	private List<NTriples> triples = new ArrayList<NTriples>();

	public PrintPropertyListener(IWikiPrinter printer) {
		fPrinter = printer;
		i = -1;
		resetDocument();
		modelFactory = RDF2Go.getModelFactory();
		model = modelFactory.createModel(reasoning);
		model.open();
	}

	@Override
	public void beginPropertyBlock(String propertyUri, boolean doc) {
		if (document == null)
			resetDocument();
		i++;
		if (i == 0)
			arr_sub.add(i, getDocument());
		if (i != 0) {
			String temp;
			try {
				temp = arr_val.get(i - 1).getValue();
			} catch (IndexOutOfBoundsException e) {
				temp = getRandObj(7);
				arr_val.add(i - 1, new Value(false, temp));
			}
			arr_sub.add(i, temp);
		}
		arr_pre.add(i, propertyUri);
	}

	@Override
	public void beginPropertyInline(String str) {
		beginPropertyBlock(str, false);
	}

	@Override
	public void endDocument() {
		// TODO Validate..
		// assert model.size()==0 does not fail.. why?
		try {
			model.readFrom(new FileReader(
					"resources/commonwikiparseasdrtestrdf.rdf"));
			// System.out.println(
			// "========================DUMP=================================="
			// );
			// model.dump();
			OutputStreamWriter writer = new OutputStreamWriter(System.out);
			// System.out.println(
			// "========================NTriples=================================="
			// );
			// model.writeTo( writer, Syntax.Ntriples);
			// System.out.println(
			// "========================RDFXML=================================="
			// );
			// model.writeTo( writer, Syntax.RdfXml);
			System.out
					.println("========================Turtle==================================");
			model.writeTo(writer, Syntax.Turtle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endPropertyBlock(String propertyUri, boolean doc) {
		triples
				.add(new NTriples(arr_sub.get(i), arr_pre.get(i), arr_val
						.get(i)));
		// triples.get(triples.size() - 1).printStatement(i);
		triples.get(triples.size() - 1).addStatement();
		arr_sub.remove(i);
		arr_pre.remove(i);
		arr_val.remove(i);
		i--;
	}

	@Override
	public void endPropertyInline(String inlineProperty) {
		endPropertyBlock(inlineProperty, false);
	}

	public String getDocument() {
		return document;
	}

	@Override
	public void onReference(String ref) {
		word(ref, true); // TODO what?
	}

	@Override
	public void onReference(WikiReference ref) {
		word(ref.toString(), false);
	}

	@Override
	public void onSpace(String str) {
		word(" ", true);
	}

	@Override
	public void onWord(String str) {
		word(str, true);
	}

	protected void print(String str) {
		fPrinter.print(str);
	}

	protected void println() {
		fPrinter.println("");
	}

	protected void println(String str) {
		fPrinter.println(str);
	}

	public void resetDocument() {
		// TODO reset document, Ideally, has to return XWiki.vnkatesh , using
		// xmlrpc
		setDocument("Main.test");
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public void word(String str, boolean isLiteral) {
		String temp;
		if (i == -1)
			return; // not inside a property block.
		try {
			temp = arr_val.get(i).getValue();
		} catch (IndexOutOfBoundsException e) {
			temp = "";
		}
		temp += str;
		try {
			arr_val.remove(i);
		} catch (IndexOutOfBoundsException e1) {
			// happens on the first removal
		}
		arr_val.add(i, new Value(isLiteral, temp));
	}
}