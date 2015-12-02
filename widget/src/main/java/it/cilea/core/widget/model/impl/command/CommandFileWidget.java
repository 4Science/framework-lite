package it.cilea.core.widget.model.impl.command;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.Widget;

@Entity
@DiscriminatorValue("command-file")
public class CommandFileWidget extends Widget {

	@Transient
	private String fragmentDivId;
	@Transient
	private String uniqueIdentifier;
	@Transient
	private String fileNameField;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.containsKey(ParameterType.FRAGMENT_DIV_ID.name()))
			fragmentDivId = parameterMap.get(ParameterType.FRAGMENT_DIV_ID.name()).iterator().next();
		if (parameterMap.containsKey(ParameterType.UNIQUE_IDENTIFIER.name()))
			uniqueIdentifier = parameterMap.get(ParameterType.UNIQUE_IDENTIFIER.name()).iterator().next();
		if (parameterMap.containsKey(ParameterType.FILENAME_FIELD.name()))
			fileNameField = parameterMap.get(ParameterType.FILENAME_FIELD.name()).iterator().next();
	}

	public String getFragmentDivId() {
		return fragmentDivId;
	}

	public void setFragmentDivId(String fragmentDivId) {
		this.fragmentDivId = fragmentDivId;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getFileNameField() {
		return fileNameField;
	}

	public void setFileNameField(String fileNameField) {
		this.fileNameField = fileNameField;
	}

}
