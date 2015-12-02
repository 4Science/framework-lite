package it.cilea.core.model;

/**
 * This interface defines an object that can be selected such as with a drop
 * down list.
 * 
 * @author Ted Bergeron
 * @version $Id: Selectable.java,v 1.1 2007-11-20 16:03:23 palenacvs Exp $
 */
public interface Selectable {
	String getIdentifyingValue();

	String getDisplayValue();
}
