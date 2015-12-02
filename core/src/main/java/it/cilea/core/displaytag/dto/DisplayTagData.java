package it.cilea.core.displaytag.dto;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

public class DisplayTagData implements PaginatedList {

	private int totalCount;

	private List pageItems;

	private int pageSize;

	private int page;

	private String sort;

	private String dir;

	public DisplayTagData() {
		this(0, Collections.EMPTY_LIST, "id", "asc", 1, 10);
	}

	public DisplayTagData(int count, List pageItems, String sort, String dir, int page, int pageSize) {
		this.totalCount = count;
		this.pageItems = pageItems;
		this.sort = sort;
		this.dir = dir;
		this.page = page;
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setPageItems(List pageItems) {
		this.pageItems = pageItems;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// PaginatedList

	public int getFullListSize() {
		return totalCount;
	}

	public List getList() {
		return pageItems;
	}

	public int getObjectsPerPage() {
		return pageSize;
	}

	public int getPageNumber() {
		return page;
	}

	public String getSearchId() {
		return null;
	}

	public String getSortCriterion() {
		return sort;
	}

	public SortOrderEnum getSortDirection() {
		return "asc".equals(StringUtils.trim(dir)) ? SortOrderEnum.ASCENDING
				: ("desc".equals(StringUtils.trim(dir)) ? SortOrderEnum.DESCENDING : null);
	}

	public int getPageSize() {
		return pageSize;
	}

}
