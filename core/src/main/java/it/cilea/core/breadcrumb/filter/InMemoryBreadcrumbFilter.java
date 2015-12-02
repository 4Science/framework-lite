package it.cilea.core.breadcrumb.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cilea.core.breadcrumb.model.Breadcrumb;

public class InMemoryBreadcrumbFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(InMemoryBreadcrumbFilter.class);

	private Set<String> excludeUrl = new HashSet<String>();

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String pathServletRelative = request.getServletPath();
		List<Breadcrumb> breadcrumbList = new ArrayList<Breadcrumb>();

		if (request.getSession().getAttribute("breadcrumbList") != null)
			breadcrumbList = (ArrayList<Breadcrumb>) request.getSession().getAttribute("breadcrumbList");

		if (request.getParameter("CLEAR") != null || breadcrumbList.size() == 0) {
			breadcrumbList.clear();
			Breadcrumb home = new Breadcrumb();
			home.setMethod("GET");
			home.setPosition(0);
			home.setTag("index.htm");
			home.setUrl("/");
			breadcrumbList.add(home);
		} else if (request.getParameter("breadcrumbId") != null) {
			Integer start = Integer.valueOf(request.getParameter("breadcrumbId"));
			breadcrumbList = removeByPosition(breadcrumbList, start);
		} else if (request.getParameter("breadcrumbBack") != null) {
			boolean applyBreadcrumbBack = true;
			if (StringUtils.isNotBlank(request.getParameter("breadcrumbBackUrlPattern"))) {
				List<Breadcrumb> breadcrumbListTemp = getClonedBreadcrumbList(breadcrumbList,
						breadcrumbList.size() - 2);
				String backUrl = breadcrumbListTemp.get(breadcrumbListTemp.size() - 1).getUrl();
				applyBreadcrumbBack = Pattern.matches(request.getParameter("breadcrumbBackUrlPattern"), backUrl);
			}
			if (applyBreadcrumbBack) {
				breadcrumbList = removeByPosition(breadcrumbList, breadcrumbList.size() - 2);
				if (breadcrumbList.size() != 0)
					response.sendRedirect(
							request.getContextPath() + breadcrumbList.get(breadcrumbList.size() - 1).getUrl() + "?"
									+ breadcrumbList.get(breadcrumbList.size() - 1).getQueryString());
				else
					response.sendRedirect(request.getContextPath() + "/");
				return;
			}
		} else if (!StringUtils.isEmpty(request.getParameter("loadBreadcrumbBookmark"))) {
			String bookmarkName = request.getParameter("loadBreadcrumbBookmark");
			for (Breadcrumb b : breadcrumbList) {
				if (bookmarkName.equals(b.getBookmark())) {
					response.sendRedirect(request.getContextPath() + b.getUrl() + "?" + b.getQueryString());
					return;
				}

			}
		}

		boolean exclude = false;
		for (String excludeSingleUrl : excludeUrl) {
			if (pathServletRelative.indexOf(excludeSingleUrl) > 0) {
				exclude = true;
				break;
			}
		}

		if (!exclude && request.getParameter("_form") == null) {
			Integer position = 0;

			position = breadcrumbList.size() - 1;

			Breadcrumb breadcrumb = new Breadcrumb();
			breadcrumb.setPosition(position + 1);

			Enumeration<String> params = request.getParameterNames();
			String queryString = "";
			while (params.hasMoreElements()) {
				String paramName = params.nextElement();

				if (!paramName.equals("CLEAR") && !paramName.equals("bookmark")
						&& !paramName.equals("breadcrumbBack")) {
					String[] valori = request.getParameterValues(paramName);
					for (String valore : valori) {
						queryString += "&" + paramName + "=" + valore;
						// palena modifica temporeanea
						// TODO: da togliere!!!!
						request.setAttribute(paramName, valore);
					}
				}
			}
			if (queryString.length() > 0)
				breadcrumb.setQueryString(queryString.substring(1));
			if (!StringUtils.isEmpty(request.getParameter("saveBreadcrumbBookmark")))
				breadcrumb.setBookmark(request.getParameter("saveBreadcrumbBookmark"));

			breadcrumb.setUrl(pathServletRelative);

			breadcrumb.setMethod(request.getMethod());
			String tag = "";
			if (pathServletRelative.indexOf("/index.htm") != -1) {
				tag = pathServletRelative.substring(1).replace("/index.htm", "").replace("/", ".");
			} else {
				tag = pathServletRelative.substring(1).replace(".htm", "").replace("/", ".");
			}

			breadcrumb.setTag(tag);

			int actualPos = findBreadcrumbPosition(breadcrumbList, breadcrumb);

			if (actualPos != -1) {
				breadcrumbList = removeByPosition(breadcrumbList, actualPos);
				Breadcrumb actual = breadcrumbList.get(breadcrumbList.size() - 1);
				actual.setQueryString(breadcrumb.getQueryString());
				actual.setUrl(breadcrumb.getUrl());
				actual.setMethod(breadcrumb.getMethod());
				actual.setBookmark(breadcrumb.getBookmark());
				breadcrumbList.remove(breadcrumbList.size() - 1);
				breadcrumbList.add(actual);
			} else {
				breadcrumbList.add(breadcrumb);

			}
		}

		request.getSession().setAttribute("breadcrumbList", breadcrumbList);

		filterChain.doFilter(req, response);

	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	private int findBreadcrumbPosition(List<Breadcrumb> elenco, Breadcrumb daCercare) {
		for (Breadcrumb breadcrumb : elenco) {
			if (breadcrumb.getTag().equals(daCercare.getTag()))
				return breadcrumb.getPosition();
		}
		return -1;

	}

	private List<Breadcrumb> removeByPosition(List<Breadcrumb> elenco, Integer position) {
		if (position <= elenco.size())
			for (int i = elenco.size() - 1; i > position; i--) {
				elenco.remove(i);
			}
		return elenco;
	}

	private List<Breadcrumb> getClonedBreadcrumbList(List<Breadcrumb> elenco, Integer position) {
		List<Breadcrumb> elencoT = new ArrayList<Breadcrumb>();
		elencoT.addAll(elenco);

		if (position <= elencoT.size())
			for (int i = elencoT.size() - 1; i > position; i--) {
				elencoT.remove(i);
			}
		return elencoT;
	}

	public Set<String> getExcludeUrl() {
		return excludeUrl;
	}

	public void setExcludeUrl(Set<String> excludeUrl) {
		this.excludeUrl = excludeUrl;
	}

}
