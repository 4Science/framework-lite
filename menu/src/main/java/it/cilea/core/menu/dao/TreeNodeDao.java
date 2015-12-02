package it.cilea.core.menu.dao;

import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.spring.dao.GenericDao;

import java.util.List;

public interface TreeNodeDao extends GenericDao<TreeNode, Integer> {
	TreeNode get(String identifier);

	List<TreeNode> getAllEager();

	List<TreeNode> getOrderedChild(Integer treeNodeId);

}
