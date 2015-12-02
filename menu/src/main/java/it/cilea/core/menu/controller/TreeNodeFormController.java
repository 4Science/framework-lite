package it.cilea.core.menu.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.menu.model.TreeNodeType;
import it.cilea.core.menu.service.TreeNodeService;
import it.cilea.core.menu.utils.TreeUtils;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
@RequestMapping("/fragment/manager/tree/form/index.htm")
public class TreeNodeFormController extends Spring3CoreController {

	@Autowired
	private TreeNodeService treeNodeService;

	public void setTreeNodeService(TreeNodeService treeNodeService) {
		this.treeNodeService = treeNodeService;
	}

	@ModelAttribute("command")
	public TreeNode backingObject(HttpServletRequest request) {
		TreeNode treeNode;
		String paramPortletId = request.getParameter("nodeId");
		if (paramPortletId != null && !"".equals(paramPortletId)) {
			Integer portletId = Integer.valueOf(paramPortletId);
			treeNode = treeNodeService.getTreeNode(portletId);
		} else {
			treeNode = new TreeNode();

		}
		return treeNode;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showGet(@ModelAttribute("command") TreeNode treeNode) {
		return new ModelAndView("manager/tree/form", "command", treeNode);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView showPost(@Valid @ModelAttribute("command") TreeNode treeNode, BindingResult result,
			SessionStatus status, HttpServletRequest request) {

		boolean firstInsert = treeNode.getId() == null;

		treeNodeService.saveOrUpdate(treeNode);
		treeNode.init();
		saveMessage(request, messageUtil.findMessage("action.treeNode." + (firstInsert ? "created" : "updated")));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeId", treeNode.getId());

		return new ModelAndView("redirect:/fragment/manager/tree/form/index.htm", map);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView processGet(@ModelAttribute("command") Object command, HttpServletRequest request) {
		TreeNodeType treeNodeType = TreeUtils.getTreeNodeType(request);
		return new ModelAndView(treeNodeType.getViewName(), "command", command);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView showPost(@Valid @ModelAttribute("command") Object object, BindingResult result,
			SessionStatus status, HttpServletRequest request) throws Exception {

		TreeNodeType treeNodeType = TreeUtils.getTreeNodeType(request);

		String nodeId = null;

		Object service = context.getBean(treeNodeType.getBeanServiceName());
		MethodUtils.invokeMethod(service, "saveOrUpdate", new Object[] { object });

		Object id = (Integer) PropertyUtils.getProperty(object, "id");
		nodeId = id.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeId", nodeId);
		map.put("type", treeNodeType.getDescription());

		return new ModelAndView("redirect:" + request.getServletPath(), map);
	}
}
