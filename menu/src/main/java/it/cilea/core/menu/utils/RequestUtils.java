package it.cilea.core.menu.utils;

import it.cilea.core.menu.model.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class RequestUtils {

	public static void main(String[] args) {

		TreeNode tn = new TreeNode();
		TreeNode parent = new TreeNode();
		TreeNode grand = new TreeNode();
		grand.setOnClick("ciaop");
		parent.setTreeParentNode(grand);
		tn.setTreeParentNode(parent);

		ExpressionParser parser = new SpelExpressionParser();
		Object value = parser.parseExpression("treeParentNode.treeParentNode.onClick").getValue(tn);
	}

	public static List<String> getParamList(String stringa) {
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");

		Matcher matcher = pattern.matcher(stringa);
		List<String> paramList = new ArrayList<String>();
		while (matcher.find()) {
			paramList.add(StringUtils.remove(StringUtils.remove(matcher.group(), "${"), "}"));

		}
		return paramList;

	}

	public static String getParamValue(String stringa, HttpServletRequest request) {

		String values = "";
		if (stringa.contains("param.")) {
			String[] paramValues = null;

			paramValues = request.getParameterValues(StringUtils.remove(stringa, "param."));
			if (paramValues != null) {
				for (String value : paramValues) {
					values += "," + value;
				}
				values = values.substring(1);
			}

		}
		if (stringa.contains("session.")) {
			if (StringUtils.countMatches(stringa, ".") == 1)
				values = "" + request.getSession().getAttribute(StringUtils.remove(stringa, "session."));
			else {
				stringa = StringUtils.remove(stringa, "session.");
				String key = StringUtils.substringBefore(stringa, ".");
				String path = StringUtils.substringAfter(stringa, ".");
				ExpressionParser parser = new SpelExpressionParser();
				if (request.getSession().getAttribute(key) != null)
					values = "" + parser.parseExpression(path).getValue(request.getSession().getAttribute(key));
			}
		}

		if (stringa.contains("request.")) {
			if (StringUtils.countMatches(stringa, ".") == 1)
				values = "" + request.getAttribute(StringUtils.remove(stringa, "request."));
			else {
				stringa = StringUtils.remove(stringa, "request.");
				String key = StringUtils.substringBefore(stringa, ".");
				String path = StringUtils.substringAfter(stringa, ".");
				ExpressionParser parser = new SpelExpressionParser();
				if (request.getAttribute(key) != null)
					values = "" + parser.parseExpression(path).getValue(request.getAttribute(key));
			}
		}

		if (stringa.contains("application.")) {
			values = ""
					+ request.getSession().getServletContext()
							.getAttribute(StringUtils.remove(stringa, "application."));
		}

		return values;

	}

	@Deprecated
	public static String formatValues(List<String> stringaList, boolean isString) {

		String values = "";
		if (stringaList != null) {
			for (String value : stringaList) {
				if (isString) {
					values += ",'" + value + "'";
				} else {
					values += "," + value;
				}
				values = values.substring(1);
			}
		}

		return values;

	}

	@Deprecated
	public static String getPageUrl(HttpServletRequest request, boolean withContextPath) {
		String path = "";
		if (request.getAttribute("javax.servlet.forward.request_uri") == null) {
			path = request.getServletPath();
			if (withContextPath)
				path = request.getContextPath() + path;

		}

		else {
			path = request.getAttribute("javax.servlet.forward.request_uri").toString();
			if (!withContextPath)
				path = StringUtils.remove(path, request.getContextPath());
		}

		return path;
	}
}
