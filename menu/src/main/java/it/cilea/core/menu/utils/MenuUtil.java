package it.cilea.core.menu.utils;

import java.util.Map;

import javax.servlet.ServletContext;

import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.menu.service.TreeNodeService;

public class MenuUtil {

	public static void reload(ServletContext servletContext, TreeNodeService treeNodeService) {
		servletContext.setAttribute("identifierTreeMap", treeNodeService.getTreeNodeMap());
	}


}
