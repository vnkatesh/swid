package org.swid.editors;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author venkatesh
 */
public class SwidEditorMessages
{

    public SwidEditorMessages()
    {
    }

    private static final String RESOURCE_BUNDLE = "org.swid.editors.SwidMessages";//$NON-NLS-1$

    private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    public static String getString(String key)
    {
        try {
            return fgResourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
        }
    }

    public static ResourceBundle getResourceBundle()
    {
        return fgResourceBundle;
    }

}
