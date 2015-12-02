package it.cilea.core.menu.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.authorization.model.impl.UserDetail;
import it.cilea.core.menu.MenuConstant;
import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.menu.model.TreeNodeType;
import it.cilea.core.menu.rhino.iterfaces.AddJsInfoInterface;

public class TreeUtils {
	private static Logger log = LoggerFactory.getLogger(Logger.class);

	public static boolean isVisibleByRules(TreeNode _tree, String jsRulesClass, HttpServletRequest request) {
		if (_tree.getVisibilityRule() == null)
			return true;
		ContextFactory.getGlobal().enterContext();
		Context cx = Context.getCurrentContext();
		String resultString = "";
		String javascriptCodeIntegration = "";
		AddJsInfoInterface addInfo = null;
		try {
			Object obj = Class.forName(jsRulesClass);
			addInfo = (AddJsInfoInterface) obj;

		} catch (Exception e) {
			log.error("Class not found for Visibility by role:" + jsRulesClass);
		}
		if (addInfo != null)
			javascriptCodeIntegration = addInfo.getJavascriptCode(request);
		// Initialize the standard objects (Object, Function, etc.)
		// This must be done before scripts can be executed. Returns
		// a scope object that we use in later calls.
		try {
			Scriptable scope = cx.initStandardObjects();

			// Now evaluate the string we've colected.
			String script = "function f(){\n" + javascriptCodeIntegration + "\nvar booleanResult='';\n"
					+ _tree.getVisibilityRule() + "\nreturn booleanResult;} f();";

			Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);

			// Convert the result to a string and print it.
			resultString = Context.toString(result);

		} finally {
			// Exit from the context.
			Context.exit();
		}
		return resultString.equals("true");
	}

	// inizia
	// Pattern pattern = Pattern.compile("^/progetti/.*$");

	// contiene
	// Pattern pattern = Pattern.compile("^.*/progetti/.*$");

	public static boolean isVisibleByPath(TreeNode _tree, String path) {
		if (StringUtils.trimToNull(_tree.getVisibilityPath()) == null)
			return true;
		String visibilityPath = _tree.getVisibilityPath();
		for (String key : MenuConstant.MENU_REPLACE_RULES.keySet()) {
			visibilityPath = StringUtils.replace(visibilityPath, key, MenuConstant.MENU_REPLACE_RULES.get(key));
		}

		Pattern pattern = Pattern.compile(visibilityPath);
		Matcher matcher = pattern.matcher(path);
		return matcher.matches();
	}

	public static boolean isVisibleByRole(TreeNode _tree) {
		if (StringUtils.trimToNull(_tree.getAttacchedResource()) == null)
			return true;
		try {
			UserDetail userDetail = AuthorizationUserHolder.getUser();
			return userDetail.hasAuthorities(_tree.getAttacchedResource());
		} catch (NullPointerException e) {
			return false;
		}

	}

	public static String buildUrl(TreeNode _tree, HttpServletRequest request) {

		String url = _tree.getLink();
		for (String key : MenuConstant.MENU_REPLACE_RULES.keySet())
			url = StringUtils.replace(url, key, MenuConstant.MENU_REPLACE_RULES.get(key));
		if (StringUtils.trimToNull(url) != null) {
			List<String> paramList = RequestUtils.getParamList(url);

			HashMap<String, List<String>> parametriMap = new HashMap<String, List<String>>();
			if (_tree.getDefaultParameter() != null) {
				String[] parametriArray = _tree.getDefaultParameter().split("&");

				for (String singolo : parametriArray) {
					String[] param = singolo.split("=");

					if (parametriMap.containsKey(param[0])) {
						List<String> valoreList = (List<String>) parametriMap.get(param[0]);
						valoreList.add(param[1]);
					} else {
						List<String> valoreList = new ArrayList<String>();
						valoreList.add(param[1]);
						parametriMap.put(param[0], valoreList);
					}

				}
			}

			// ciclo sui paraametri presenti nella query
			for (String parametro : paramList) {

				// se c'� in request o in session
				if (!RequestUtils.getParamValue(parametro, request).equals(""))
					url = StringUtils.replace(url, "${" + parametro + "}",
							RequestUtils.getParamValue(parametro, request));
				else
					// altrimenti DEVE essere tra i parametri di default
					url = StringUtils.replace(url, "${" + parametro + "}", StringUtils.remove(parametro, "param."));

			}

		} else {
			url = "";
		}

		String contextPath = request.getContextPath();

		if (!url.equals("#")) {
			if (url.startsWith("http://") || url.startsWith("https://")) {
			} else if (url.startsWith("/")) {
				url = getUrlBase(request) + url;
			} else {
				url = contextPath + "/" + url;
			}
		}

		return url;

	}

	public static TreeNode deepClone(TreeNode _node, HttpServletRequest request, AddJsInfoInterface addInfo,
			boolean isAdmin) {

		TreeNode treeNode = null;
		String path = request.getServletPath();
		String removePath = request.getContextPath();
		boolean visibilityByRole = true;
		boolean visibilityByRules = true;
		boolean visibilityByPath = true;
		if (!isAdmin) {
			visibilityByRole = TreeUtils.isVisibleByRole(_node);
			visibilityByRules = TreeUtils.isVisibleByRules(_node, addInfo.getClass().toString(), request);
			visibilityByPath = TreeUtils.isVisibleByPath(_node, StringUtils.remove(path, removePath));
		}

		if (visibilityByRole && visibilityByRules && visibilityByPath) {
			treeNode = new TreeNode();
			treeNode.setBrotherOrder(_node.getBrotherOrder());
			treeNode.setCssClass(_node.getCssClass());
			treeNode.setDefaultParameter(_node.getDefaultParameter());
			treeNode.setTreeNodeDictionarySet(_node.getTreeNodeDictionarySet());
			treeNode.setId(_node.getId());
			treeNode.setLink(buildUrl(_node, request));
			treeNode.setOnClick(_node.getOnClick());
			treeNode.setOnMouseOver(_node.getOnMouseOver());
			treeNode.setTreeParentNodeId(_node.getTreeParentNodeId());
			if (_node.getTreeParentNode() != null)
				treeNode.setTreeParentNodeIdentifier(_node.getTreeParentNode().getIdentifier());
			treeNode.setVisibilityPath(_node.getVisibilityPath());
			treeNode.setAttacchedResource(_node.getAttacchedResource());
			treeNode.setVisibilityRule(_node.getVisibilityRule());
			treeNode.setTreeType(_node.getTreeType());
			treeNode.setTreeTypeId(_node.getTreeTypeId());
			Set<TreeNode> children = new TreeSet<TreeNode>();
			treeNode.setTreeNodeDictionarySet(_node.getTreeNodeDictionarySet());
			treeNode.setTreeNodeDictionaryMap(_node.getTreeNodeDictionaryMap());
			treeNode.setTreeParentNodeId(_node.getTreeParentNodeId());
			if (_node.getTreeParentNode() != null)
				treeNode.setTreeParentNode(deepCloneUpOnly(_node.getTreeParentNode(), request, addInfo, isAdmin));
			treeNode.setIdentifier(_node.getIdentifier());
			for (TreeNode child : _node.getTreeNodeChildSet()) {
				TreeNode evaluated = deepClone(child, request, addInfo, isAdmin);
				if (evaluated != null)
					children.add(evaluated);
			}
			treeNode.setTreeNodeChildSet(children);
		} else {
		}
		return treeNode;
	}

	public static TreeNode deepCloneUpOnly(TreeNode _node, HttpServletRequest request, AddJsInfoInterface addInfo,
			boolean isAdmin) {

		TreeNode treeNode = null;
		String path = request.getServletPath();
		String removePath = request.getContextPath();
		boolean visibilityByRole = true;
		boolean visibilityByRules = true;
		boolean visibilityByPath = true;
		if (!isAdmin) {
			visibilityByRole = TreeUtils.isVisibleByRole(_node);
			visibilityByRules = TreeUtils.isVisibleByRules(_node, addInfo.getClass().toString(), request);
			visibilityByPath = TreeUtils.isVisibleByPath(_node, StringUtils.remove(path, removePath));
		}

		if (visibilityByRole && visibilityByRules && visibilityByPath) {
			treeNode = new TreeNode();
			treeNode.setBrotherOrder(_node.getBrotherOrder());
			treeNode.setCssClass(_node.getCssClass());
			treeNode.setDefaultParameter(_node.getDefaultParameter());
			treeNode.setTreeNodeDictionarySet(_node.getTreeNodeDictionarySet());
			treeNode.setId(_node.getId());
			treeNode.setLink(buildUrl(_node, request));
			treeNode.setOnClick(_node.getOnClick());
			treeNode.setOnMouseOver(_node.getOnMouseOver());
			treeNode.setTreeParentNodeId(_node.getTreeParentNodeId());
			if (_node.getTreeParentNode() != null)
				treeNode.setTreeParentNodeIdentifier(_node.getTreeParentNode().getIdentifier());
			treeNode.setVisibilityPath(_node.getVisibilityPath());
			treeNode.setAttacchedResource(_node.getAttacchedResource());
			treeNode.setVisibilityRule(_node.getVisibilityRule());
			treeNode.setTreeType(_node.getTreeType());
			treeNode.setTreeTypeId(_node.getTreeTypeId());
			treeNode.setTreeNodeDictionarySet(_node.getTreeNodeDictionarySet());
			treeNode.setTreeNodeDictionaryMap(_node.getTreeNodeDictionaryMap());
			if (_node.getTreeParentNode() != null)
				treeNode.setTreeParentNode(deepCloneUpOnly(_node.getTreeParentNode(), request, addInfo, isAdmin));
			treeNode.setIdentifier(_node.getIdentifier());
		} else {
		}
		return treeNode;
	}

	public static TreeNodeType getTreeNodeType(HttpServletRequest request) {
		String nodeType = request.getParameter("type");
		Map<String, TreeNodeType> map = (Map<String, TreeNodeType>) request.getSession().getServletContext()
				.getAttribute("treeNodeTypeMap");
		if (map.get(nodeType) == null)
			throw new RuntimeException("Cannot find definition for nodeType: " + nodeType);
		else
			return map.get(nodeType);
	}

	public static boolean notEmptyLiMenu(TreeNode _tree, String path, String jsRulesClass, HttpServletRequest request) {
		if (_tree != null)
			for (Object o : _tree.getChildrenList()) {
				TreeNode treeNode = (TreeNode) o;
				if (isVisibleByPath(treeNode, path) && isVisibleByRole(treeNode)
						&& isVisibleByRules(_tree, jsRulesClass, request))
					return true;
			}
		return false;
	}

	private static String getUrlBase(HttpServletRequest request) {
		URL requestUrl = null;
		try {
			requestUrl = new URL(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
		return requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;
	}
}
