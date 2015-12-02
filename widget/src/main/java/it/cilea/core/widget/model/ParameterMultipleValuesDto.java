package it.cilea.core.widget.model;

public class ParameterMultipleValuesDto {
	
	private String [] parameterValues;	
	private boolean rangeSearch;
	private boolean isLowerBound;	
	private boolean useFuzziness;
	
	public ParameterMultipleValuesDto(String[] parameterValues, boolean rangeSearch, boolean isLowerBound, boolean useFuzziness) {
		this(parameterValues, rangeSearch, isLowerBound);
		this.useFuzziness=useFuzziness;
	}
	
	public ParameterMultipleValuesDto(String[] parameterValues, boolean rangeSearch, boolean isLowerBound) {
		this.parameterValues=parameterValues;
		this.rangeSearch=rangeSearch;
		this.isLowerBound=isLowerBound;
	}
	
	public boolean isLowerBound() {
		return isLowerBound;
	}
	public void setLowerBound(boolean isLowerBound) {
		this.isLowerBound = isLowerBound;
	}
	public String[] getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(String[] parameterValues) {
		this.parameterValues = parameterValues;
	}
	public boolean isRangeSearch() {
		return rangeSearch;
	}
	public void setRangeSearch(boolean rangeSearch) {
		this.rangeSearch = rangeSearch;
	}

	public boolean isUseFuzziness() {
		return useFuzziness;
	}

	public void setUseFuzziness(boolean useFuzziness) {
		this.useFuzziness = useFuzziness;
	}
}
