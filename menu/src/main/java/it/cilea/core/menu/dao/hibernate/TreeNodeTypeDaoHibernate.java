package it.cilea.core.menu.dao.hibernate;

import it.cilea.core.menu.dao.TreeNodeTypeDao;
import it.cilea.core.menu.model.TreeNodeType;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

@Repository("treeNodeTypeDao")
public class TreeNodeTypeDaoHibernate extends GenericDaoHibernate<TreeNodeType, Integer> implements TreeNodeTypeDao {
	public TreeNodeTypeDaoHibernate() {
		super(TreeNodeType.class);
	}

}
