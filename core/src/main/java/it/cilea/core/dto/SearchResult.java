package it.cilea.core.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SearchResult {

	private Long count;		
	private Map<String, Collection<String>> requestParameters;
	private Integer pageSize;
	private Integer page;
	private Collection<String> sort;
	private String sortDir;
	
	@XmlElementWrapper(name = "resultList")
	@XmlElement(name = "item")
	private List<?> resultList;
	

	public SearchResult() {
	}

	public SearchResult(List<?> resultList) {
		this.resultList = resultList;
	}	
	
	public SearchResult(Long count, List<?> resultList, Map<String, Collection<String>> requestParameters, Integer pageSize,
			Integer page, Collection<String> sort, String sortDir) {
		this.count = count;
		this.resultList = resultList;
		this.requestParameters = requestParameters;
		this.pageSize = pageSize;
		this.page = page;
		this.sort = sort;
		this.sortDir=sortDir;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<?> getResultList() {
		return resultList;
	}

	public void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}
	
	
	public Map<String, Collection<String>> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(Map<String, Collection<String>> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	public Collection<String> getSort() {
		return sort;
	}

	public void setSort(Collection<String> sort) {
		this.sort = sort;
	}
}