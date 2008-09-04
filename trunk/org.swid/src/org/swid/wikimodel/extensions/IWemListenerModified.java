/**
 * 
 */
package org.swid.wikimodel.extensions;

import org.ontoware.rdf2go.model.Model;
import org.wikimodel.wem.IWemListener;

/**
 * @author gen
 *
 */
public interface IWemListenerModified extends IWemListener {
	
	Model getModel();
		
}
