package it.cilea.core.menu.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import it.cilea.core.menu.MenuConstant.TreeNodeDictionaryType;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.util.MessageUtilConstant;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "IDENTIFIER" }) })
public class TreeNode extends IdentifiableObject implements Comparable<TreeNode> {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "IDENTIFIER", nullable = false, unique = true, length = 4000)
	@NotNull
	private String identifier;

	@Column(name = "FK_TREE_NODE_PARENT")
	private Integer treeParentNodeId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_TREE_NODE_PARENT", insertable = false, updatable = false)
	private TreeNode treeParentNode;

	@Column(name = "FK_TREE_NODE_TYPE")
	private Integer treeTypeId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_TREE_NODE_TYPE", insertable = false, updatable = false)
	private TreeNodeType treeType;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "treeParentNode")
	@org.hibernate.annotations.OrderBy(clause = "BROTHER_ORDER asc")
	private Set<TreeNode> treeNodeChildSet = new LinkedHashSet<TreeNode>();

	@Column(name = "VISIBILITY_RULE", insertable = true, updatable = true)
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String visibilityRule;

	@Column(name = "ATTACCHED_RESOURCE", length = 4000)
	private String attacchedResource;

	@Column(name = "VISIBILITY_PATH", insertable = true, updatable = true)
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String visibilityPath;

	@Column(name = "DEFAULT_PARAMETER", insertable = true, updatable = true)
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String defaultParameter;

	@Column(name = "LINK", length = 4000)
	private String link;

	@Column(name = "ONCLICK")
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String onClick;

	@Column(name = "ONMOUSEOVER")
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String onMouseOver;

	@Column(name = "SHOW_PARENT_NAME")
	private Boolean showParentName;

	@Column(name = "CSS_CLASS")
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String cssClass;

	@Column(name = "ICON")
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String icon;

	@Column(name = "BROTHER_ORDER")
	private Integer brotherOrder;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "treeNode", fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
	protected Set<TreeNodeDictionary> treeNodeDictionarySet = new HashSet<TreeNodeDictionary>();

	@Transient
	protected Map<String, String> treeNodeDictionaryMap;

	@Transient
	private String treeParentNodeIdentifier;

	public TreeNode() {
	}

	public void init() {
		if (treeNodeDictionaryMap == null) {
			treeNodeDictionaryMap = new HashMap<String, String>();
			for (TreeNodeDictionary treeNodeDictionary : treeNodeDictionarySet) {
				treeNodeDictionaryMap.put(treeNodeDictionary.getDiscriminator(),
						treeNodeDictionary.getI18nIdentifier());
			}
			if (treeNodeDictionaryMap.get(TreeNodeDictionaryType.LABEL.toString()) == null)
				treeNodeDictionaryMap.put(TreeNodeDictionaryType.LABEL.toString(),
						getI18nIdentifier(TreeNodeDictionaryType.LABEL));
		}
	}

	@Transient
	public String getItemType() {
		return this.getTreeType().getDescription();
	}

	public void setShowParentName(Boolean showParentName) {
		this.showParentName = showParentName;
	}

	public String getOnMouseOver() {
		return onMouseOver;
	}

	public void setOnMouseOver(String onMouseOver) {
		this.onMouseOver = onMouseOver;
	}

	public void setBrotherOrder(Integer brotherOrder) {
		this.brotherOrder = brotherOrder;
	}

	public Integer getBrotherOrder() {
		return brotherOrder;
	}

	@Transient
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	@Override
	public boolean equals(Object object) {
		TreeNode that = (TreeNode) object;
		if (this.id == null && that.getId() == null)
			return true;
		if (this.id == null || that.getId() == null)
			return false;
		return this.id.equals(that.getId());
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return id.hashCode();
	}

	public String getDisplayValue() {
		return getLabel();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setVisibilityPath(String visibilityPath) {
		this.visibilityPath = visibilityPath;
	}

	public void setDefaultParameter(String defaultParameter) {
		this.defaultParameter = defaultParameter;
	}

	public String getDefaultParameter() {
		return defaultParameter;
	}

	public String getVisibilityPath() {
		return visibilityPath;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setVisibilityRule(String visibilityRule) {
		this.visibilityRule = visibilityRule;
	}

	public String getVisibilityRule() {
		return visibilityRule;
	}

	public void setTreeParentNode(TreeNode treeParentNode) {
		this.treeParentNode = treeParentNode;
	}

	public void setTreeParentNodeId(Integer treeParentNodeId) {
		this.treeParentNodeId = treeParentNodeId;
	}

	public TreeNode getTreeParentNode() {
		return treeParentNode;
	}

	public Integer getTreeParentNodeId() {
		return treeParentNodeId;
	}

	public void setTreeNodeChildSet(Set<TreeNode> treeNodeChildSet) {
		this.treeNodeChildSet = treeNodeChildSet;
	}

	public Set<TreeNode> getTreeNodeChildSet() {
		return treeNodeChildSet;
	}

	@Transient
	public List<TreeNode> getChildrenList() {
		return new ArrayList<TreeNode>(treeNodeChildSet);
	}

	@Transient
	public TreeNode getParent() {
		return treeParentNode;
	}

	public String getAttacchedResource() {
		return attacchedResource;
	}

	public void setAttacchedResource(String attacchedResource) {
		this.attacchedResource = attacchedResource;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public Integer getTreeTypeId() {
		return treeTypeId;
	}

	public void setTreeTypeId(Integer treeTypeId) {
		this.treeTypeId = treeTypeId;
	}

	public TreeNodeType getTreeType() {
		return treeType;
	}

	public void setTreeType(TreeNodeType treeType) {
		this.treeType = treeType;
	}

	public Set<TreeNodeDictionary> getTreeNodeDictionarySet() {
		return treeNodeDictionarySet;
	}

	public void setTreeNodeDictionarySet(Set<TreeNodeDictionary> treeNodeDictionarySet) {
		this.treeNodeDictionarySet = treeNodeDictionarySet;
	}

	public Map<String, String> getTreeNodeDictionaryMap() {
		init();
		return treeNodeDictionaryMap;
	}

	public void setTreeNodeDictionaryMap(Map<String, String> treeNodeDictionaryMap) {
		this.treeNodeDictionaryMap = treeNodeDictionaryMap;
	}

	@Override
	public int compareTo(TreeNode o) {
		if (getBrotherOrder() >= o.getBrotherOrder())
			return 1;
		else
			return -1;

	}

	public Collection<TreeNodeType> getDeepTreeNodeType() {
		HashMap<String, TreeNodeType> map = new HashMap<String, TreeNodeType>();
		addTreeNodeType(this, map);
		return map.values();

	}

	private void addTreeNodeType(TreeNode _node, HashMap<String, TreeNodeType> map) {

		map.put(_node.getTreeType().getDescription(), _node.getTreeType());
		for (TreeNode child : _node.getTreeNodeChildSet()) {
			addTreeNodeType(child, map);
		}
	}

	public Boolean getShowParentName() {
		return showParentName;
	}

	@Transient
	public String getState() {
		return "close";
	}

	@Transient
	public Integer getPosition() {
		return brotherOrder;
	}

	@Transient
	public void setPosition(Integer position) {
	}

	public String getLabel() {
		return getGenericTreeNodeDictionaryType(TreeNodeDictionaryType.LABEL);
	}

	public String getDescription() {
		return getGenericTreeNodeDictionaryType(TreeNodeDictionaryType.DESCRIPTION);
	}

	public String getNote() {
		return getGenericTreeNodeDictionaryType(TreeNodeDictionaryType.NOTE);
	}

	protected String getGenericTreeNodeDictionaryType(TreeNodeDictionaryType treeNodeDictionaryType) {
		init();
		String message = MessageUtilConstant.messageUtil
				.findMessage(treeNodeDictionaryMap.get(treeNodeDictionaryType.toString()));
		if (message == null)
			return "";
		return message;
	}

	public Map<String, String> getAdditionalInfo() {
		return new HashMap<String, String>();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTreeParentNodeIdentifier() {
		return treeParentNodeIdentifier;
	}

	public void setTreeParentNodeIdentifier(String treeParentNodeIdentifier) {
		this.treeParentNodeIdentifier = treeParentNodeIdentifier;
	}

	@Transient
	public String getI18nIdentifier(TreeNodeDictionaryType treeNodeDictionaryType) {
		String i18nIdentifier = "menu." + StringUtils.replace(getIdentifier(), "/", ".") + "."
				+ StringUtils.lowerCase(treeNodeDictionaryType.toString());
		i18nIdentifier = StringUtils.replace(i18nIdentifier, "..", ".");
		return i18nIdentifier;
	}
}
