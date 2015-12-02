package it.cilea.core.i18n.model;

public class I18nExport {

	private String context;
	private String language;
	private Boolean sortExport = true;
	private String charset = "UTF-8";

	/**
	 * SoloDB, SoloFile, Entrambi
	 */
	private Integer tipoExport = 0;

	public I18nExport() {
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getTipoExport() {
		return tipoExport;
	}

	public void setTipoExport(Integer tipoExport) {
		this.tipoExport = tipoExport;
	}

	public Boolean getSortExport() {
		return sortExport;
	}

	public void setSortExport(Boolean sortExport) {
		this.sortExport = sortExport;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
