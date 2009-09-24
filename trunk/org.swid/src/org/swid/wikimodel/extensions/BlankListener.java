package org.swid.wikimodel.extensions;

import org.ontoware.rdf2go.model.Model;
import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;

public class BlankListener implements IWemListenerModified
{

    public void beginDefinitionDescription()
    {
    }

    public void beginDefinitionList(WikiParameters params)
    {
    }

    public void beginDefinitionTerm()
    {
    }

    public void beginDocument()
    {
    }

    public void beginFormat(WikiFormat format)
    {
    }

    public void beginHeader(int level, WikiParameters params)
    {
    }

    public void beginInfoBlock(char infoType, WikiParameters params)
    {
    }

    public void beginList(WikiParameters params, boolean ordered)
    {
    }

    public void beginListItem()
    {
    }

    public void beginParagraph(WikiParameters params)
    {
    }

    public void beginPropertyBlock(String propertyUri, boolean doc)
    {
    }

    public void beginPropertyInline(String str)
    {
    }

    public void beginQuotation(WikiParameters params)
    {
    }

    public void beginQuotationLine()
    {
    }

    public void beginTable(WikiParameters params)
    {
    }

    public void beginTableCell(boolean tableHead, WikiParameters params)
    {
    }

    public void beginTableRow(WikiParameters params)
    {
    }

    public void endDefinitionDescription()
    {
    }

    public void endDefinitionList(WikiParameters params)
    {
    }

    public void endDefinitionTerm()
    {
    }

    public void endDocument()
    {
    }

    public void endFormat(WikiFormat format)
    {
    }

    public void endHeader(int level, WikiParameters params)
    {
    }

    public void endInfoBlock(char infoType, WikiParameters params)
    {
    }

    public void endList(WikiParameters params, boolean ordered)
    {
    }

    public void endListItem()
    {
    }

    public void endParagraph(WikiParameters params)
    {
    }

    public void endPropertyBlock(String propertyUri, boolean doc)
    {
    }

    public void endPropertyInline(String inlineProperty)
    {
    }

    public void endQuotation(WikiParameters params)
    {
    }

    public void endQuotationLine()
    {
    }

    public void endTable(WikiParameters params)
    {
    }

    public void endTableCell(boolean tableHead, WikiParameters params)
    {
    }

    public void endTableRow(WikiParameters params)
    {
    }

    public void onEmptyLines(int count)
    {
    }

    public void onEscape(String str)
    {
    }

    public void onExtensionBlock(String extensionName, WikiParameters params)
    {
    }

    public void onExtensionInline(String extensionName, WikiParameters params)
    {
    }

    public void onHorizontalLine()
    {
    }

    public void onLineBreak()
    {
    }

    public void onMacroBlock(String macroName, WikiParameters params, String content)
    {
    }

    public void onMacroInline(String macroName, WikiParameters params, String content)
    {
    }

    public void onNewLine()
    {
    }

    public void onReference(String ref)
    {
    }

    public void onReference(WikiReference ref)
    {
    }

    public void onSpace(String str)
    {
    }

    public void onSpecialSymbol(String str)
    {
    }

    public void onTableCaption(String str)
    {
    }

    public void onVerbatimBlock(String str)
    {
    }

    public void onVerbatimInline(String str)
    {
    }

    public void onWord(String str)
    {
    }

    @Override
    public Model getModel()
    {
        return null;
    }

}
