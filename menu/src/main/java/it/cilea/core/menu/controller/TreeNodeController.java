package it.cilea.core.menu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.cilea.core.menu.MenuConstant;
import it.cilea.core.menu.json.serializer.TreeNodeSerializer;
import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.menu.model.TreeNodeType;
import it.cilea.core.menu.rhino.iterfaces.AddJsInfoInterface;
import it.cilea.core.menu.service.TreeNodeService;
import it.cilea.core.menu.utils.MenuUtil;
import it.cilea.core.menu.utils.TreeUtils;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
public class TreeNodeController extends Spring3CoreController {

	@Autowired
	private TreeNodeService treeNodeService;

	public void setTreeNodeService(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

	private Logger log = LoggerFactory.getLogger(TreeNodeController.class);

	@RequestMapping(value = { "/menu/reload" })
	public ModelAndView reload(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		MenuUtil.reload(request.getServletContext(), treeNodeService);
		saveMessage(request, messageUtil.findMessage("action.menu.reload"));
		return new ModelAndView("redirect:/");
	}

	@RequestMapping("/menu/reload.fragment")
	public void handleReloadPersistenteTree(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("Refresh Tree context");
		MenuUtil.reload(request.getServletContext(), treeNodeService);
		response.getWriter().println("<html>ok</html>");
	}

	@RequestMapping("/menu/get.json")
	public void handleJsonTree(HttpServletRequest request, HttpServletResponse response) {
		TreeNode treeNode = getTree(request);
		String json = getJson(treeNode);
		try {
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/menu/update.json")
	public void handleUpdateNode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer nodeId = null, newJoinId = null, index = 0, parentId = null, copy = 0;
		String todo = "";
		String returnCode = null;
		TreeNode ret = null;

		String displayValue = request.getParameter("title");

		if (StringUtils.trimToNull(request.getParameter("newJoinId")) != null)
			newJoinId = Integer.valueOf(request.getParameter("newJoinId"));
		if (StringUtils.trimToNull(request.getParameter("nodeId")) != null)
			nodeId = Integer.valueOf(request.getParameter("nodeId"));
		if (StringUtils.trimToNull(request.getParameter("index")) != null)
			index = Integer.valueOf(request.getParameter("index"));
		if (StringUtils.trimToNull(request.getParameter("todo")) != null)
			todo = request.getParameter("todo");
		if (StringUtils.trimToNull(request.getParameter("parentId")) != null)
			parentId = Integer.valueOf(request.getParameter("parentId"));
		if (StringUtils.trimToNull(request.getParameter("copy")) != null)
			copy = Integer.valueOf(request.getParameter("copy"));

		if (todo.equals("delete") && nodeId == null) {
			returnCode = "101 - id nodo mancante";
		} else if (todo.equals("rename") && nodeId == null) {
			returnCode = "101 - id nodo mancante";
		} else if (todo.equals("create") && parentId == null) {
			returnCode = "102 - id nodo padre mancante";
		} else if (todo.equals("move")) {
			if (nodeId == null)
				returnCode = "101 - id nodo mancante";
			if (newJoinId == null)
				returnCode = "102 - id nodo padre mancante";
			if (index == null)
				returnCode = "103 - indice mancante";
		}

		if (returnCode == null) {
			TreeNodeType treeNodeType = TreeUtils.getTreeNodeType(request);
			if ("delete".equals(todo)) {
				deleteNode(nodeId, treeNodeType, false, request);
			} else if ("rename".equals(todo)) {
				renameNode(nodeId, treeNodeType, displayValue, request);
			} else if ("create".equals(todo)) {

				TreeNode treeNode = createNode(displayValue, treeNodeType, parentId, index, request);
				try {
					response.getWriter().println(getJson(treeNode));
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if ("move".equals(todo) && copy == 0) {
				moveNode(nodeId, treeNodeType, parentId, newJoinId, index, request);
			} else {
				copyNode(nodeId, treeNodeType, parentId, newJoinId, index, request);
			}
		} else {
			response.getWriter().println(returnCode);
		}
	}

	@RequestMapping({ "/menu/list.htm", "/frame/menu/list.htm" })
	public ModelAndView handleJspTree(HttpServletRequest request, HttpServletResponse response) {
		TreeNode treeNode = getTree(request);
		return new ModelAndView("menu/tree/list", "tree", treeNode);
	}

	@RequestMapping({ "/menu/nodeIdentifier/**/list.htm" })
	public ModelAndView handleJspTreeNew(HttpServletRequest request, HttpServletResponse response) {
		String nodeIdentifier = StringUtils.removeStart(request.getServletPath(), "/menu/nodeIdentifier/");
		nodeIdentifier = StringUtils.substring(nodeIdentifier, 0, StringUtils.lastIndexOf(nodeIdentifier, "/list.htm"));
		TreeNode menu = getMenu(nodeIdentifier, request);
		if (menu == null)
			menu = getMenu("/" + nodeIdentifier, request);
		return new ModelAndView("menu/list", "menu", menu);
	}

	@RequestMapping({ "/menu/get.htm", "/menu/get.fragment", "/frame/menu/get.htm" })
	public ModelAndView handleTree(HttpServletRequest request, HttpServletResponse response) {
		TreeNode treeNode = getTree(request);
		if (StringUtils.isNotBlank(request.getParameter("viewName")))
			return new ModelAndView(request.getParameter("viewName"), "tree", treeNode);
		return new ModelAndView("menu/tree/get", "tree", treeNode);
	}

	protected String getJson(TreeNode treeNode) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(TreeNode.class, new TreeNodeSerializer());
		Gson gson = gsonBuilder.create();
		return gson.toJson(treeNode);
	}

	public TreeNode getMenu(String nodeIdentifier, HttpServletRequest request) {

		boolean showRoot = false;
		boolean isAdmin = false;
		String jsRules = MenuConstant.DEFAULT_JS_RULES_CLASS;
		TreeNode returnValue = null;

		if (StringUtils.trimToNull(nodeIdentifier) != null) {

			if (StringUtils.trimToNull(request.getParameter("showRoot")) != null)
				showRoot = request.getParameter("showRoot").equals("true");
			if (StringUtils.trimToNull(request.getParameter("isAdmin")) != null)
				isAdmin = request.getParameter("isAdmin").equals("true");
			if (StringUtils.trimToNull(request.getParameter("jsRules")) != null)
				jsRules = request.getParameter("jsRules");

			TreeNode treeNode = null;

			if (((HashMap<String, TreeNode>) request.getSession().getServletContext().getAttribute("identifierTreeMap"))
					.containsKey(nodeIdentifier))
				treeNode = ((HashMap<String, TreeNode>) request.getSession().getServletContext()
						.getAttribute("identifierTreeMap")).get(nodeIdentifier);
			else
				treeNode = treeNodeService.getTreeNode(nodeIdentifier);

			if (treeNode != null) {

				AddJsInfoInterface addInfo = null;

				try {

					Object obj = Class.forName(jsRules).newInstance();
					addInfo = (AddJsInfoInterface) obj;
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Class not found:" + jsRules);
				}

				returnValue = TreeUtils.deepClone(treeNode, request, addInfo, isAdmin);

			}
		}

		return returnValue;
	}

	public TreeNode getTree(HttpServletRequest request) {

		boolean showRoot = false;
		boolean isAdmin = false;
		String jsRules = MenuConstant.DEFAULT_JS_RULES_CLASS;
		TreeNode returnValue = null;

		if (StringUtils.trimToNull(request.getParameter("nodeIdentifier")) != null) {

			if (StringUtils.trimToNull(request.getParameter("showRoot")) != null)
				showRoot = request.getParameter("showRoot").equals("true");
			if (StringUtils.trimToNull(request.getParameter("isAdmin")) != null)
				isAdmin = request.getParameter("isAdmin").equals("true");
			if (StringUtils.trimToNull(request.getParameter("jsRules")) != null)
				jsRules = request.getParameter("jsRules");

			TreeNode treeNode = null;

			String nodeIdentifier = request.getParameter("nodeIdentifier");
			if (((HashMap<String, TreeNode>) request.getSession().getServletContext().getAttribute("identifierTreeMap"))
					.containsKey(nodeIdentifier))
				treeNode = ((HashMap<String, TreeNode>) request.getSession().getServletContext()
						.getAttribute("identifierTreeMap")).get(nodeIdentifier);
			else
				treeNode = treeNodeService.getTreeNode(nodeIdentifier);

			if (treeNode != null) {

				AddJsInfoInterface addInfo = null;

				try {

					Object obj = Class.forName(jsRules).newInstance();
					addInfo = (AddJsInfoInterface) obj;
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Class not found:" + jsRules);
				}

				returnValue = TreeUtils.deepClone(treeNode, request, addInfo, isAdmin);

			}
		}

		return returnValue;
	}

	public void deleteNode(Integer nodeId, TreeNodeType treeNodeType, boolean isRecursive, HttpServletRequest request) {

		TreeNode delNode = treeNodeService.getTreeNode(nodeId);
		TreeNode treeNodeParent = treeNodeService.getTreeNode(delNode.getTreeParentNodeId());
		treeNodeService.deleteTreeNode(nodeId);
		int i = 0;
		for (TreeNode treeChildNode : treeNodeService.getOrderedChildSet(treeNodeParent.getId())) {
			treeChildNode.setBrotherOrder(i++);
			treeNodeService.saveOrUpdate(treeChildNode);
		}

	}

	public void renameNode(Integer nodeId, TreeNodeType treeNodeType, String newValue, HttpServletRequest request) {
		TreeNode renameNode = treeNodeService.getTreeNode(nodeId);
		treeNodeService.saveOrUpdate(renameNode);

	}

	public TreeNode createNode(String name, TreeNodeType treeNodeType, Integer parentId, Integer position,
			HttpServletRequest request) throws Exception {
		TreeNode newNode = new TreeNode();
		newNode.setBrotherOrder(position);
		newNode.setTreeParentNodeId(parentId);
		newNode.setShowParentName(false);
		newNode.setTreeTypeId(treeNodeType.getId());
		newNode.setTreeType(treeNodeType);
		Integer newNodeId = treeNodeService.saveOrUpdate(newNode);
		TreeNode parentNode = treeNodeService.getTreeNode(parentId);
		int i = 0;
		for (TreeNode treeChildNode : treeNodeService.getOrderedChildSet(parentNode.getId())) {
			treeChildNode.setBrotherOrder(i++);
			treeNodeService.saveOrUpdate(treeChildNode);
		}

		// non va salvato: serve solo per il dato di ritorno

		// TODO editNode non ha pi� questa firma
		newNode.setOnClick("editNode(" + newNode.getId() + ")");
		return newNode;
	}

	public void copyNode(Integer sourceNodeId, TreeNodeType treeNodeType, Integer sourceParentNodeId,
			Integer targetParentNodeId, Integer position, HttpServletRequest request) {

	}

	public void moveNode(Integer sourceNodeId, TreeNodeType treeNodeType, Integer sourceParentNodeId,
			Integer targetParentNodeId, Integer position, HttpServletRequest request) {

	}

	@RequestMapping(value = { "/menu/init" })
	public ModelAndView init(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Map<String, TreeNode> map = treeNodeService.getTreeNodeMap();
		if (!map.containsKey("/module.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/module.menu");
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/top.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/top.menu");
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/my.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/my.menu");
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/module/browse.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/module/browse.menu");
			treeNode.setBrotherOrder(1);
			treeNode.setTreeParentNodeId(map.get("/module.menu").getId());
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/module/browse/community.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/module/browse/community.menu");
			treeNode.setBrotherOrder(1);
			treeNode.setTreeParentNodeId(map.get("/module/browse.menu").getId());
			treeNode.setLink("/${DSPACE_MODULE_NAME}/community/browse.htm?CLEAR");
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/module/browse/collection.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/module/browse/collection.menu");
			treeNode.setBrotherOrder(2);
			treeNode.setTreeParentNodeId(map.get("/module/browse.menu").getId());
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/module/item.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/module/item.menu");
			treeNode.setBrotherOrder(2);
			treeNode.setTreeParentNodeId(map.get("/module.menu").getId());
			treeNode.setLink("/${DSPACE_MODULE_NAME}/item/list.htm?CLEAR");
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}
		if (!map.containsKey("/top/item/new.menu")) {
			TreeNode treeNode = new TreeNode();
			treeNode.setIdentifier("/top/item/new.menu");
			treeNode.setBrotherOrder(1);
			treeNode.setTreeParentNodeId(map.get("/top.menu").getId());
			treeNode.setLink("/${DSPACE_MODULE_NAME}/item/new.htm");
			treeNode.setVisibilityPath(".*/item/.*");
			treeNodeService.saveOrUpdate(treeNode);
			map.put(treeNode.getIdentifier(), treeNode);
		}

		MenuUtil.reload(request.getServletContext(), treeNodeService);
		saveMessage(request, messageUtil.findMessage("action.menu.init"));
		return new ModelAndView("redirect:/");
	}

}
