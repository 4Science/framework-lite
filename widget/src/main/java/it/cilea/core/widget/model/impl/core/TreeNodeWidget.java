package it.cilea.core.widget.model.impl.core;

import java.util.List;

/**
 * 
 * @author palena
 * 
 */
public class TreeNodeWidget {
	private String id;
	private String text;
	private List<? extends TreeNodeWidget> item;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<? extends TreeNodeWidget> getItem() {
		return item;
	}

	public void setItem(List<? extends TreeNodeWidget> item) {
		this.item = item;
	}

}
