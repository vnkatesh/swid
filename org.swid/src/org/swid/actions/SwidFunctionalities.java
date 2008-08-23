package org.swid.actions;

import java.io.StringReader;

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
	
	public static void parseAndPrint(String text) {
		StringReader reader = new StringReader(text);
		IWikiParser parser = new CommonWikiParser();
		final StringBuffer buf = new StringBuffer();
		IWemListener listener = new PrintPropertyListener(newPrinter(buf));
		try {
			parser.parse(reader, listener);
		} catch (WikiParserException e) {
			e.printStackTrace();
		}
		String test = buf.toString();
		System.out.println(test);
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
