package it.cilea.core.widget.model;

import it.cilea.core.widget.util.WidgetUtil;

import java.util.List;

public class ParameterDTO {

	private String name;
	private Boolean rangeSearch;
	private Boolean multipleValuesDisjuncted;
	private String disjunctiveClauseId;
	private String joinAlias;
	private List<String> joinWithClause;
	private String stringMatchType;	
	private Boolean negate;
	private String notClauseGroupId;
	private Boolean useFuzziness;
	
	//questa property DEVE essere settata quando nella query che sto costruendo
	//ho due ParameterDTO con stesso nome
	//questo serve per permettere al metodo equals di distinguere gli elementi
	//altrimenti in fase di put dell'item nella mappa verrebbe sovrascritto
	private String aliasName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		ParameterDTO parameterDTO = (ParameterDTO) obj;
		//TODO:da aggiungere joinWithClause
		if (WidgetUtil.areObjectEquals(name, parameterDTO.getName())
				&& WidgetUtil.areObjectEquals(rangeSearch, parameterDTO.getRangeSearch())
				&& WidgetUtil.areObjectEquals(multipleValuesDisjuncted, parameterDTO.getMultipleValuesDisjuncted())
				&& WidgetUtil.areObjectEquals(disjunctiveClauseId, parameterDTO.getDisjunctiveClauseId())
				&& WidgetUtil.areObjectEquals(joinAlias, parameterDTO.getJoinAlias())
				&& WidgetUtil.areObjectEquals(stringMatchType, parameterDTO.getStringMatchType())
				&& WidgetUtil.areObjectEquals(negate, parameterDTO.getNegate())
				&& WidgetUtil.areObjectEquals(notClauseGroupId, parameterDTO.getNotClauseGroupId())						
				&& WidgetUtil.areObjectEquals(aliasName, parameterDTO.getAliasName())
				&& WidgetUtil.areObjectEquals(useFuzziness, parameterDTO.getUseFuzziness())
				)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		//TODO:da aggiungere joinWithClause
		return ((name == null) ? 0 : name.hashCode()) +
				((rangeSearch == null) ? 0 : rangeSearch.hashCode())+
				((multipleValuesDisjuncted == null) ? 0 : multipleValuesDisjuncted.hashCode())+
				((joinAlias == null) ? 0 : joinAlias.hashCode())+
				((stringMatchType == null) ? 0 : stringMatchType.hashCode()+
				((negate == null) ? 0 : negate.hashCode())+
				((notClauseGroupId == null) ? 0 : notClauseGroupId.hashCode())+
				((aliasName == null) ? 0 : aliasName.hashCode())+				
				((useFuzziness == null) ? 0 : useFuzziness.hashCode())
				);
	}

	public Boolean getRangeSearch() {
		return rangeSearch;
	}

	public void setRangeSearch(Boolean rangeSearch) {
		this.rangeSearch = rangeSearch;
	}

	public ParameterDTO(String name, Boolean rangeSearch) {
		super();
		boolean tempRangeSearch=false;
		this.name = name;
		if (rangeSearch!=null)
			tempRangeSearch=rangeSearch;
		this.rangeSearch = tempRangeSearch;
	}

	public ParameterDTO() {
		super();
		this.rangeSearch = false;
	}

	public String getJoinAlias() {
		return joinAlias;
	}

	public void setJoinAlias(String joinAlias) {
		this.joinAlias = joinAlias;
	}

	public List<String> getJoinWithClause() {
		return joinWithClause;
	}

	public void setJoinWithClause(List<String> joinWithClause) {
		this.joinWithClause = joinWithClause;
	}

	public String getStringMatchType() {
		return stringMatchType;
	}

	public void setStringMatchType(String stringMatchType) {
		this.stringMatchType = stringMatchType;
	}

	public Boolean getMultipleValuesDisjuncted() {
		return multipleValuesDisjuncted;
	}

	public void setMultipleValuesDisjuncted(Boolean multipleValuesDisjuncted) {
		this.multipleValuesDisjuncted = multipleValuesDisjuncted;
	}

	public String getDisjunctiveClauseId() {
		return disjunctiveClauseId;
	}

	public void setDisjunctiveClauseId(String disjunctiveClauseId) {
		this.disjunctiveClauseId = disjunctiveClauseId;
	}

	public Boolean getNegate() {
		return negate;
	}

	public void setNegate(Boolean negate) {
		this.negate = negate;
	}

	public String getNotClauseGroupId() {
		return notClauseGroupId;
	}

	public void setNotClauseGroupId(String notClauseGroupId) {
		this.notClauseGroupId = notClauseGroupId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}	

	public Boolean getUseFuzziness() {
		return useFuzziness;
	}

	public void setUseFuzziness(Boolean useFuzziness) {
		this.useFuzziness = useFuzziness;
	}

}
