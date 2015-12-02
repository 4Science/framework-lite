package it.cilea.core.displaytag.dto;

import it.cilea.core.displaytag.dto.DisplayTagData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class DisplayTagCheckData extends DisplayTagData {

	// Hidden field storing all checked rows
	private String checkHidden;

	// Checked rows in the current page
	private String[] checkPage = {};

	public DisplayTagCheckData() {
		super();
	}

	public DisplayTagCheckData(int count, List pageItems, String sort,
			String dir, int page, int pageSize) {
		super(count, pageItems, sort, dir, page, pageSize);
	}

	public String getCheckHidden() {
		return checkHidden;
	}

	public void setCheckHidden(String checkHidden) {
		this.checkHidden = checkHidden;
	}

	public String[] getCheckPage() {
		return checkPage;
	}

	public void setCheckPage(String[] checkPage) {
		this.checkPage = checkPage;
	}

	public Set getCheckOutPage() {
		Set set = new HashSet();
		if (checkHidden != null) {
			for (String s : StringUtils.split(checkHidden, ' ')) {
				set.add(s);
			}
		}
		for (String s : checkPage) {
			set.add(s);
		}
		return set;
	}

	public void setCheckOutPage(Set set) {
		checkHidden = StringUtils.join(set.iterator(), ' ');
	}

	public Set getCheckInPage() {
		Set set = new HashSet(checkPage.length);
		for (String s : checkPage) {
			set.add(s);
		}
		return set;
	}

	public void setCheckInPageIds(Set set) {
		checkPage = (String[]) set.toArray(new String[0]);
	}

	public Set getCheck() {
		Set set = new HashSet();
		set.addAll(getCheckInPage());
		set.addAll(getCheckOutPage());
		return set;
	}

	public void updateCheckInOutPage(Set check, String[] idsInPage) {
		// moves the checked rows in the current page from the 'check' set to
		// the 'set' set
		Set set = new HashSet();
		for (String id : idsInPage) {
			if (check.contains(id)) {
				check.remove(id);
				set.add(id);
			}
		}
		setCheckInPageIds(set);
		setCheckOutPage(check);
	}

}
