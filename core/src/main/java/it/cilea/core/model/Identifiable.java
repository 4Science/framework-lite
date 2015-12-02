package it.cilea.core.model;

/**
 * An object that can be identified by its ID.
 * 
 * @author Ted Bergeron
 * @version $Id: Identifiable.java,v 1.1 2007-11-20 16:03:23 palenacvs Exp $
 */
public interface Identifiable<PK> {
	PK getId();
	void setId(PK id);
}
