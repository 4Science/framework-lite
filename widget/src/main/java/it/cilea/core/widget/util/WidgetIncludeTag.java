package it.cilea.core.widget.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


public class WidgetIncludeTag extends TagSupport {
	/** Path of default JSP version */
	private String page;

	/**
	 * Get the JSP to display (default version)
	 * 
	 * @return the page to display
	 */
	public String getPage() {
		return page;
	}

	/**
	 * Set the JSP to display (default version)
	 * 
	 * @param s
	 *            the page to display
	 */
	public void setPage(String s) {
		page = s;
	}

	public int doStartTag() throws JspException {
		try {
			pageContext.include(page);
		} catch (IOException ie) {
			throw new JspException(ie);
		} catch (ServletException se) {
			throw new JspException(se);
		}

		return SKIP_BODY;
	}
}
