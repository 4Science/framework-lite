package it.cilea.core.menu.dao.hibernate;

import it.cilea.core.menu.dao.TreeNodeDictionaryDao;
import it.cilea.core.menu.model.TreeNodeDictionary;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

@Repository("treeNodeDictionaryDao")
public class TreeNodeDictionaryDaoHibernate extends GenericDaoHibernate<TreeNodeDictionary, Integer> implements
		TreeNodeDictionaryDao {
	public TreeNodeDictionaryDaoHibernate() {
		super(TreeNodeDictionary.class);
	}

}
