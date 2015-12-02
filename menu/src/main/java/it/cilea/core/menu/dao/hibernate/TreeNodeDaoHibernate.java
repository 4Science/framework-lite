package it.cilea.core.menu.dao.hibernate;

import it.cilea.core.menu.dao.TreeNodeDao;
import it.cilea.core.menu.model.TreeNode;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("treeNodeDao")
public class TreeNodeDaoHibernate extends GenericDaoHibernate<TreeNode, Integer> implements TreeNodeDao {
	public TreeNodeDaoHibernate() {
		super(TreeNode.class);
	}

	@Override
	public TreeNode get(String identifier) {
		String queryString = "select treeNode from TreeNode treeNode where treeNode.identifier = :identifier";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setParameter("identifier", identifier);
		return (TreeNode) query.uniqueResult();
	}

	@Override
	public List<TreeNode> getAllEager() {
		String queryString = "select distinct treeNode from  TreeNode treeNode left join fetch treeNode.treeType treeType left join fetch treeNode.treeNodeDictionarySet treeNodeDictionarySet";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		return query.list();
	}

	@Override
	public List<TreeNode> getOrderedChild(Integer treeNodeId) {
		String queryString = "select treeNodeChildSet from TreeNode treeNode left join treeNode.treeNodeChildSet treeNodeChildSet where treeNode.id=:treeNodeId";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setParameter("treeNodeId", treeNodeId);
		return query.list();
	}

}
