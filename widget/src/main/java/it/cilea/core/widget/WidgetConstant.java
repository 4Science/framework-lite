package it.cilea.core.widget;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;

import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.WidgetDictionary;

public class WidgetConstant {

	public static Map<Integer, Widget> widgetMap = new HashMap<Integer, Widget>();
	public static Map<Integer, WidgetDictionary> widgetDictionaryMap = new HashMap<Integer, WidgetDictionary>();

	public static final String FIRST_FIELD_PREFIX = "1_";
	public static final String SECOND_FIELD_PREFIX = "2_";
	public static final String FUZZINESS_CHECKBOX_SUFFIX_ATTRIBUTE_NAME = "_fuzzinessCheckbox";
	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String DATE_PATTERN_JAVASCRIPT = "%d/%m/%Y";

	public static BeanFactory beanFactory;

	/**
	 * 
	 * @author Davide Palena This enum lists the label types available for a
	 *         widget:
	 *         <ul>
	 *         <li>LABEL: classical widget label</li>
	 *         <li>INFO: info widget label</li>
	 *         <li>ERROR: error widget label</li>
	 *         <li>EMPTY: empty widget label. <br/>
	 *         This type of label is used only for ItemsWidget and precisely for
	 *         SelectWidget and RadioWidget.</li>
	 *         </ul>
	 */
	public static enum WidgetDictionaryType {
		LABEL, INFO, ERROR, EMPTY, option;
	}

	/**
	 * @author Davide Palena This enum lists the parameter types available for a
	 *         widget:
	 *         <ul>
	 *         <li>NAME: this is a parameter name used to retrieve the parameter
	 *         value in request/session/application scopes (in this order)</li>
	 *         <li>VALUE: this is itself the parameter value</li>
	 *         </ul>
	 * 
	 */
	public static enum ParameterType {
		NAME, VALUE, RENDER_AS_HIDDEN_IF_ONE_OPTION, RENDER_EMPTY_OPTION, MULTIPLE_SELECTION, RANGE_SEARCH, RENDER_AS_HIDDEN, STRING_MATCH_TYPE, AJAX_URL, VISIBLE_OPTIONS, POSITIONING, CSS_CLASS, DEFAULT_VALUE, SHOW_COUNTER, MAX_LENGTH, AUTOCOMPLETE_URL, AUTOCOMPLETE_DATA, AUTOCOMPLETE_GET_URL, AUTOCOMPLETE_GET_DATA, AUTOCOMPLETE_INPUT_NAME, AUTOCOMPLETE_INPUT_VALUE, REQUIRED, DECORATOR_CLASS, STYLE, SCRIPT, SORTABLE, SORT_PROPERTY, MEDIA, IS_FRAGMENT_TABLE, SHOW_I18N, SUFFIX_ID_ONLY, CK_EDITOR, LAYOUT_MODE, MODE_TYPE, IS_MONEY, TEST, ALLOW_MULTIPLE, MULTIPLE_INDEX_TO_SHOW, FRAGMENT_DIV_ID, UNIQUE_IDENTIFIER, FILENAME_FIELD, EXPRESSION, PAGE, AUTO_DISPLAY;
	}

	/**
	 * 
	 * @author Davide Palena This enum lists the available ways to populate
	 *         OptionsWidget. The method
	 *         {@link it.cilea.core.widget.model.OptionsWidget#getPopulationType()
	 *         OptionsWidget.getPopulationType()} returns only strings equals to
	 *         the lower case of the value of this enum The value returned by
	 *         {@link it.cilea.core.widget.model.OptionsWidget#getPopulationValue()
	 *         OptionsWidget.getPopulationValue()} will be evaluated accordingly
	 *         to the population type chosen.
	 *         <ul>
	 *         <li>DICTIONARY: it means that the population value is a
	 *         discriminator (String) to use to retrieve options from
	 *         {@link it.cilea.core.widget.model.WidgetDictionary
	 *         WidgetDictionary}, accordingly to the user current roles</li>
	 *         <li>JAVA_METHOD: it means that the population value has this
	 *         format: SPRING_REF_TO_SERVICE|JAVA_METHOD_NAME. The first piece
	 *         of the string is the name of a spring bean containing a public
	 *         method of name equals to the second piece (without brackets) This
	 *         method is invoked after having resolved all the references to the
	 *         parameters (named or static) of the widget The invoked method
	 *         must return list of it.cilea.core.Selectable
	 *         <li>HQL: it means that the population value is a HQL query
	 *         returning list of it.cilea.core.Selectable</li>
	 *         <li>JAVASCRIPT_HQL: it means that the population value is a
	 *         JAVASCRIPT that generates a HQL query returning list of
	 *         it.cilea.core.Selectable</li>
	 *         <li>SOLR: it means that the population value is a SOLR query
	 *         returning list of it.cilea.core.Selectable</li>
	 *         <li>JAVASCRIPT_SOLR: it means that the population value is a
	 *         JAVASCRIPT that generates a SOLR query returning list of
	 *         it.cilea.core.Selectable</li>
	 *         </ul>
	 * 
	 * 
	 */
	public static enum OptionsWidgetPopulationType {
		DICTIONARY_MINE, JAVA_METHOD, HQL, JAVASCRIPT_HQL, SOLR, JAVASCRIPT_SOLR, WIDGET_OBJECT, PARAMETERS;
	}

}
