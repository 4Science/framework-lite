package it.cilea.core.i18n.model;

import org.springframework.web.multipart.MultipartFile;

public class I18nImport {

	private MultipartFile multipartFile;
	private Boolean overrideExisting = false;
	private String context;
	private String language;
	private String charset;

	public I18nImport() {
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public Boolean getOverrideExisting() {
		return overrideExisting;
	}

	public void setOverrideExisting(Boolean overrideExisting) {
		this.overrideExisting = overrideExisting;
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

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
