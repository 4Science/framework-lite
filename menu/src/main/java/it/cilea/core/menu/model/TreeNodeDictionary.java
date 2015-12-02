package it.cilea.core.menu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import it.cilea.core.menu.MenuConstant.TreeNodeDictionaryType;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;
import it.cilea.core.util.MessageUtilConstant;

@Entity
public class TreeNodeDictionary extends IdentifiableObject implements Selectable {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DISCRIMINATOR")
	private String discriminator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_TREE_NODE", insertable = false, updatable = false, nullable = true)
	private TreeNode treeNode;

	@Column(name = "FK_TREE_NODE", insertable = true, updatable = true, nullable = false)
	private Integer treeNodeId;

	@Column(name = "I18N_CUSTOM_IDENTIFIER", length = 4000)
	private String i18nCustomIdentifier;

	public TreeNodeDictionary() {
	}

	public TreeNodeDictionary(String discriminator) {
		this.discriminator = discriminator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setTreeNode(TreeNode treeNode) {
		this.treeNode = treeNode;
	}

	public TreeNode getTreeNode() {
		return treeNode;
	}

	public void setTreeNodeId(Integer treeNodeId) {
		this.treeNodeId = treeNodeId;
	}

	public Integer getTreeNodeId() {
		return treeNodeId;
	}

	@Override
	public String getDisplayValue() {
		return MessageUtilConstant.messageUtil.findMessage(getI18nIdentifier());
	}

	@Override
	public String getIdentifyingValue() {
		return id.toString();
	}

	@Override
	public boolean equals(Object obj) {
		TreeNodeDictionary treeNodeDictionary = (TreeNodeDictionary) obj;
		if (treeNodeDictionary == null)
			return false;

		if (id != null)
			return id.equals(treeNodeDictionary.getId());
		return false;
	}

	public String getI18nCustomIdentifier() {
		return i18nCustomIdentifier;
	}

	public void setI18nCustomIdentifier(String i18nCustomIdentifier) {
		this.i18nCustomIdentifier = i18nCustomIdentifier;
	}

	@Transient
	public String getI18nIdentifier() {
		if (StringUtils.isNotBlank(i18nCustomIdentifier))
			return i18nCustomIdentifier;
		return getTreeNode().getI18nIdentifier(TreeNodeDictionaryType.valueOf(discriminator));
	}

}
