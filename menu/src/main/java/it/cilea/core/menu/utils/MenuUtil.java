package it.cilea.core.menu.utils;

import javax.servlet.ServletContext;

import it.cilea.core.menu.service.TreeNodeService;

public class MenuUtil {

	public static void reload(ServletContext servletContext, TreeNodeService treeNodeService) {
		servletContext.setAttribute("identifierTreeMap", treeNodeService.getTreeNodeMap());
	}

}
