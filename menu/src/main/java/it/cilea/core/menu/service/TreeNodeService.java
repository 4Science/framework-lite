package it.cilea.core.menu.service;

import it.cilea.core.menu.dao.TreeNodeDao;
import it.cilea.core.menu.dao.TreeNodeDictionaryDao;
import it.cilea.core.menu.dao.TreeNodeTypeDao;
import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.menu.model.TreeNodeDictionary;
import it.cilea.core.menu.model.TreeNodeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreeNodeService {
	@Autowired
	private TreeNodeDao treeNodeDao;
	@Autowired
	private TreeNodeTypeDao treeNodeTypeDao;

	@Autowired
	private TreeNodeDictionaryDao treeNodeDictionaryDao;

	public void setTreeNodeDictionaryDao(TreeNodeDictionaryDao treeNodeDictionaryDao) {
		this.treeNodeDictionaryDao = treeNodeDictionaryDao;
	}

	public void setTreeNodeTypeDao(TreeNodeTypeDao treeNodeTypeDao) {
		this.treeNodeTypeDao = treeNodeTypeDao;
	}

	public void setTreeNodeDao(TreeNodeDao treeNodeDao) {
		this.treeNodeDao = treeNodeDao;
	}

	// TreeNode
	public TreeNode getTreeNode(Integer id) {
		return treeNodeDao.get(id);
	}

	public TreeNode getTreeNode(String identifier) {
		return treeNodeDao.get(identifier);
	}

	public Integer saveOrUpdate(TreeNode treeNode) {
		return treeNodeDao.save(treeNode).getId();
	}

	public void deleteTreeNode(Integer id) {
		treeNodeDao.remove(id);
	}

	public Map<String, TreeNode> getTreeNodeMap() {
		List<TreeNode> treeNodeList = treeNodeDao.getAllEager();
		Map<Integer, TreeNode> idTreeMap = new HashMap<Integer, TreeNode>();
		for (TreeNode treeNode : treeNodeList) {
			treeNode.init();
			treeNode.setTreeNodeChildSet(new TreeSet<TreeNode>());
			idTreeMap.put(treeNode.getId(), treeNode);
		}
		for (TreeNode treeNode : treeNodeList) {
			if (treeNode.getTreeParentNodeId() != null)
				idTreeMap.get(treeNode.getTreeParentNodeId()).getTreeNodeChildSet().add(treeNode);
		}
		Map<String, TreeNode> identifierTreeMap = new HashMap<String, TreeNode>();
		for (Integer treeNodeId : idTreeMap.keySet()) {
			if (idTreeMap.get(treeNodeId).getIdentifier() != null)
				identifierTreeMap.put(idTreeMap.get(treeNodeId).getIdentifier(), idTreeMap.get(treeNodeId));
		}
		return identifierTreeMap;
	}

	public List<TreeNode> getOrderedChildSet(Integer treeNodeId) {
		return treeNodeDao.getOrderedChild(treeNodeId);
	}

	// TreeNodeType
	public TreeNodeType getTreeNodeType(Integer id) {
		return treeNodeTypeDao.get(id);
	}

	public List<TreeNodeType> getTreeNodeTypeList() {
		List<TreeNodeType> treeNodeTypeList = treeNodeTypeDao.findByNamedQuery("TreeNodeType.findAll");
		return treeNodeTypeList;
	}

	// TreeNodeDictionary
	public Integer saveOrUpdate(TreeNodeDictionary treeNodeDictionary) {
		return treeNodeDictionaryDao.save(treeNodeDictionary).getId();

	}

	public void deleteTreeNodeDictionary(Integer id) {
		treeNodeDictionaryDao.remove(id);
	}

}
