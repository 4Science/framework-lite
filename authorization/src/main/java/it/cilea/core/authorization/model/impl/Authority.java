package it.cilea.core.authorization.model.impl;

import it.cilea.core.authorization.model.CoreAuthority;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;
import it.cilea.core.util.MessageUtilConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({
		@NamedQuery(name = "Authority.findAllEager", query = "select authority from Authority authority order by authority.priority asc"),
		@NamedQuery(name = "Authority.findAll", query = "select authority from Authority authority order by authority.priority asc") })
public class Authority extends IdentifiableObject implements CoreAuthority<String>, Selectable {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "USE_INFOS")
	private Boolean useInfos;

	@Column(name = "INFO_LIST_URL", length = 4000)
	private String infoListUrl;
	@Column(name = "INFO_GET_URL", length = 4000)
	private String infoGetUrl;

	@Column(name = "PRIORITY")
	private Integer priority;

	public Authority() {
	}

	@Override
	public String getIdentifier() {
		return description;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Boolean useInfos() {
		return useInfos();
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	@Transient
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	public String getDisplayValue() {
		return MessageUtilConstant.messageUtil.findMessage("authority." + description);
	}

	public Boolean getUseInfos() {
		return useInfos;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUseInfos(Boolean useInfos) {
		this.useInfos = useInfos;
	}

	public String getInfoGetUrl() {
		return infoGetUrl;
	}

	public void setInfoGetUrl(String infoGetUrl) {
		this.infoGetUrl = infoGetUrl;
	}

	public String getInfoListUrl() {
		return infoListUrl;
	}

	public void setInfoListUrl(String infoListUrl) {
		this.infoListUrl = infoListUrl;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
