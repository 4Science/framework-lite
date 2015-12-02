package it.cilea.core.search.strategy.impl.sql;

import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.authorization.model.impl.UserDetail;
import it.cilea.core.displaytag.dto.DisplayTagData;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.dao.SqlStrategyDao;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.regex.RegexParameterResolver;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.RegexParameterProviderImpl;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;
import it.cilea.core.search.util.GsonUtil;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.WidgetDictionary;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SqlSearchStrategy implements SearchStrategy {
	@Autowired
	private SqlStrategyDao sqlStrategyDao;

	public SqlSearchStrategy() {
	}

	protected ApplicationContext context;

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public List<?> getResult(SearchStrategyData d) throws Exception {
		SqlSearchStrategyData data = (SqlSearchStrategyData) d;

		List result = new ArrayList();
		if (data.getFilterClause() == null)
			throw new IllegalStateException("The filterClause must not be NULL");
		result = sqlStrategyDao.getResultList(data);
		return result;
	}

	public String getSql(SearchStrategyData d) throws Exception {
		SqlSearchStrategyData data = (SqlSearchStrategyData) d;

		if (data.getFilterClause() == null)
			throw new IllegalStateException("The filterClause must not be NULL");
		return sqlStrategyDao.getSqlList(data);
	}

	@Override
	public Long getCount(SearchStrategyData d) throws Exception {
		SqlSearchStrategyData data = (SqlSearchStrategyData) d;
		return sqlStrategyDao.getResultCount(data);
	}

	public void setSqlStrategyDao(SqlStrategyDao sqlStrategyDao) {
		this.sqlStrategyDao = sqlStrategyDao;
	}

	public Map getModel(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			SearchService searchService) throws Exception {
		Map model = new HashMap();
		SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		model.put("searchBuilderName", searchBuilder.getName());
		if (data.getPost()) {
			if (StringUtils.isBlank(request.getParameter("_mediaType"))
					|| request.getParameter("_mediaType").equals("html")) {

				List<?> resultList;

				resultList = getResult(data);
				Long resultCount = getCount(data);
				String sortField = "";
				if (!ArrayUtils.isEmpty(data.getSortFieldList())) {
					sortField = data.getSortFieldList()[0];
					if (StringUtils.isBlank(sortField))
						sortField = "";
				}
				DisplayTagData displayTagData = new DisplayTagData(resultCount.intValue(), resultList, sortField,
						data.getSortDirection(), data.getPage(), data.getPageSize());
				model.put(SearchConstant.DISPLAY_TAG_DATA, displayTagData);
				List<WidgetDictionary> selectedList = new ArrayList<WidgetDictionary>();
				UserDetail userDetail = AuthorizationUserHolder.getUser();
				if (searchBuilder.getSearchBuilderWidgetLinkSetBySectionMap().get(SearchConstant.SECTION_GROUP).size() != 0) {

					String ids = RegexParameterResolver.getParsedMetaQueryParameterValue(data.getGroupClauseOriginal(),
							request, new RegexParameterProviderImpl());
					ids = StringUtils.remove(ids, "(");
					ids = StringUtils.remove(ids, ")");
					if (StringUtils.isNotBlank(ids))
						for (String id : StringUtils.split(ids, ",")) {
							WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer
									.valueOf(id));
							if (userDetail == null
									|| widgetDictionary.getAuthorizationResource() == null
									|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource()
											.getIdentifier()))
								selectedList.add(widgetDictionary);
						}

				}
				if (searchBuilder.getSearchBuilderWidgetLinkSetBySectionMap().get(SearchConstant.SECTION_SELECT).size() != 0) {

					String ids = RegexParameterResolver.getParsedMetaQueryParameterValue(
							data.getSelectClauseOriginal(), request, new RegexParameterProviderImpl());
					ids = StringUtils.remove(ids, "(");
					ids = StringUtils.remove(ids, ")");
					if (StringUtils.isNotBlank(ids))
						for (String id : StringUtils.split(ids, ",")) {
							WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer
									.valueOf(id));
							if (userDetail == null
									|| widgetDictionary.getAuthorizationResource() == null
									|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource()
											.getIdentifier()))
								selectedList.add(widgetDictionary);
						}

				}
				model.put("selectedList", selectedList);
			} else {
				handleExport(searchBuilder, request, response, getSql(data), searchService);
				return null;
			}
		}
		return model;
	}

	private void handleExport(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			String sqlQuery, SearchService searchService) throws IOException {

		Map<String, Object> model = new HashMap<String, Object>();
		String fileFormat = request.getParameter("_mediaType");
		String type;

		String outputFormat = "";

		// DocumentConfiguration docConfig =
		// statistica.getDocumentConfiguration();

		// DocumentBuilder documentBuilder = docConfig.getDocumentBuilder();
		// if (fileFormat == null) {
		// fileFormat = docConfig.getFormat();
		// }
		if (fileFormat.equals("xls"))
			type = "application/vnd.ms-excel";
		else if (fileFormat.equals("xlsx"))
			type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		else if (fileFormat.equals("rtf"))
			type = "application/rtf";
		else
			type = "application/" + fileFormat;
		if (fileFormat.equals("csv")) {
			type = "text/csv";
		}
		outputFormat = fileFormat;

		// if (docConfig.isZip()) {
		// outputFormat = "zip";
		// type = "application/zip";
		// }

		String intestazione = "";
		String contenuto = "";
		Connection conn = null;
		boolean empty = true;
		Workbook wb = null;
		try {
			conn = sqlStrategyDao.getConnection();
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs;

			StringBuffer intestazioneBuf = new StringBuffer();
			StringBuffer contenutoBuf = new StringBuffer();

			Statement st = conn.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (fileFormat.equals("xls")) {
				wb = new HSSFWorkbook();
				HSSFSheet sheet = ((HSSFWorkbook) wb).createSheet("-");
				int rowNum = 0;
				int colNum = 0;
				HSSFRow xlsRow = sheet.createRow(rowNum++);
				HSSFCellStyle headerStyle = ((HSSFWorkbook) wb).createCellStyle();
				headerStyle.setFillPattern((short) 2);
				headerStyle.setFillBackgroundColor((short) 54);
				HSSFFont bold = ((HSSFWorkbook) wb).createFont();
				bold.setBoldweight((short) 700);
				bold.setColor((short) 0);
				headerStyle.setFont(bold);
				HSSFCell cell;

				List<WidgetDictionary> selectedList = new ArrayList<WidgetDictionary>();
				UserDetail userDetail = AuthorizationUserHolder.getUser();
				SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
				if (searchBuilder.getSearchBuilderWidgetLinkSetBySectionMap().get(SearchConstant.SECTION_GROUP).size() != 0) {

					String ids = RegexParameterResolver.getParsedMetaQueryParameterValue(data.getGroupClauseOriginal(),
							request, new RegexParameterProviderImpl());
					ids = StringUtils.remove(ids, "(");
					ids = StringUtils.remove(ids, ")");
					if (StringUtils.isNotBlank(ids))
						for (String id : StringUtils.split(ids, ",")) {
							WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer
									.valueOf(id));
							if (userDetail == null
									|| widgetDictionary.getAuthorizationResource() == null
									|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource()
											.getIdentifier()))
								selectedList.add(widgetDictionary);
						}

				}
				if (searchBuilder.getSearchBuilderWidgetLinkSetBySectionMap().get(SearchConstant.SECTION_SELECT).size() != 0) {

					String ids = RegexParameterResolver.getParsedMetaQueryParameterValue(
							data.getSelectClauseOriginal(), request, new RegexParameterProviderImpl());
					ids = StringUtils.remove(ids, "(");
					ids = StringUtils.remove(ids, ")");
					if (StringUtils.isNotBlank(ids))
						for (String id : StringUtils.split(ids, ",")) {
							WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer
									.valueOf(id));
							if (userDetail == null
									|| widgetDictionary.getAuthorizationResource() == null
									|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource()
											.getIdentifier()))
								selectedList.add(widgetDictionary);
						}

				}
				for (WidgetDictionary wd : selectedList) {
					cell = xlsRow.createCell((short) colNum++);
					cell.setCellStyle(headerStyle);
					cell.setCellValue(new HSSFRichTextString(wd.getDisplayValue()));
				}

				int conta = 0;
				while (rs.next()) {
					conta++;
					empty = false;
					xlsRow = sheet.createRow(rowNum++);
					colNum = 0;
					for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {

						if (rs.getMetaData().getColumnType(i) == 2) {
							cell = xlsRow.createCell((short) colNum++, HSSFCell.CELL_TYPE_NUMERIC);
						} else
							cell = xlsRow.createCell((short) colNum++, HSSFCell.CELL_TYPE_STRING);
						Object value = null;
						if (rs.getObject(i) != null)
							value = rs.getObject(i);
						writeCell(value, cell);
					}
				}
				// if (docConfig.getAutoSizeColumn())
				// for (int colCount = 0; colCount <= colNum;)
				// sheet.autoSizeColumn((short) (colCount++));

			} else if (fileFormat.equals("xlsx")) {
				wb = new SXSSFWorkbook();
				Sheet sheet = ((SXSSFWorkbook) wb).createSheet("-");
				int rowNum = 0;
				int colNum = 0;
				Row xlsRow = sheet.createRow(rowNum++);
				CellStyle headerStyle = ((SXSSFWorkbook) wb).createCellStyle();
				headerStyle.setFillPattern((short) 2);
				headerStyle.setFillBackgroundColor((short) 54);
				Font bold = ((SXSSFWorkbook) wb).createFont();
				bold.setBoldweight((short) 700);
				bold.setColor((short) 0);
				headerStyle.setFont(bold);
				Cell cell;

				List<WidgetDictionary> selectedList = new ArrayList<WidgetDictionary>();
				UserDetail userDetail = AuthorizationUserHolder.getUser();
				SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
				if (searchBuilder.getSearchBuilderWidgetLinkSetBySectionMap().get(SearchConstant.SECTION_GROUP).size() != 0) {

					String ids = RegexParameterResolver.getParsedMetaQueryParameterValue(data.getGroupClauseOriginal(),
							request, new RegexParameterProviderImpl());
					ids = StringUtils.remove(ids, "(");
					ids = StringUtils.remove(ids, ")");
					if (StringUtils.isNotBlank(ids))
						for (String id : StringUtils.split(ids, ",")) {
							WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer
									.valueOf(id));
							if (userDetail == null
									|| widgetDictionary.getAuthorizationResource() == null
									|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource()
											.getIdentifier()))
								selectedList.add(widgetDictionary);
						}

				}
				if (searchBuilder.getSearchBuilderWidgetLinkSetBySectionMap().get(SearchConstant.SECTION_SELECT).size() != 0) {

					String ids = RegexParameterResolver.getParsedMetaQueryParameterValue(
							data.getSelectClauseOriginal(), request, new RegexParameterProviderImpl());
					ids = StringUtils.remove(ids, "(");
					ids = StringUtils.remove(ids, ")");
					if (StringUtils.isNotBlank(ids))
						for (String id : StringUtils.split(ids, ",")) {
							WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer
									.valueOf(id));
							if (userDetail == null
									|| widgetDictionary.getAuthorizationResource() == null
									|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource()
											.getIdentifier()))
								selectedList.add(widgetDictionary);
						}

				}
				for (WidgetDictionary wd : selectedList) {
					cell = xlsRow.createCell((short) colNum++);
					cell.setCellStyle(headerStyle);
					cell.setCellValue(new XSSFRichTextString(wd.getDisplayValue()));
				}

				int conta = 0;
				while (rs.next()) {
					conta++;
					empty = false;
					xlsRow = sheet.createRow(rowNum++);
					colNum = 0;
					for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {

						if (rs.getMetaData().getColumnType(i) == 2) {
							cell = xlsRow.createCell((short) colNum++, Cell.CELL_TYPE_NUMERIC);
						} else
							cell = xlsRow.createCell((short) colNum++, Cell.CELL_TYPE_STRING);
						Object value = null;
						if (rs.getObject(i) != null)
							value = rs.getObject(i);
						writeCell(value, cell);
					}
				}
				// if (docConfig.getAutoSizeColumn())
				// for (int colCount = 0; colCount <= colNum;)
				// sheet.autoSizeColumn((short) (colCount++));

			}
			if (fileFormat.equals("csv")) {
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++)
					intestazioneBuf.append("\"" + StringUtils.replace(rs.getMetaData().getColumnName(i), "\"", "\"\"")
							+ "\";");
				intestazioneBuf.append("\r\n");
				String value = "";
				while (rs.next()) {
					for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
						if (rs.getString(i) == null)
							value = "";
						else
							value = rs.getString(i);
						contenutoBuf.append("\"" + StringUtils.replace(value, "\"", "\"\"") + "\";");
					}
					contenutoBuf.append("\r\n");
				}

			}

			contenuto = contenutoBuf.toString();
			intestazione = intestazioneBuf.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (empty) {
			return;
		} else {
			response.setHeader("Content-Type", type);
			OutputStream output = response.getOutputStream();
			ZipOutputStream out;
			response.setCharacterEncoding("UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"list." + outputFormat + "\"");
			if (fileFormat.equals("xls") || fileFormat.equals("xlsx")) {
				wb.write(output);
			} else {
				output.write(intestazione.getBytes());
				output.write(contenuto.getBytes());
			}
			output.flush();
			output.close();
			return;
		}

	}

	protected void writeCell(Object value, HSSFCell cell) {
		if (value instanceof Number) {
			Number num = (Number) value;
			cell.setCellValue(num.doubleValue());
		} else if (value instanceof Date)
			cell.setCellValue((Date) value);
		else if (value instanceof Calendar)
			cell.setCellValue((Calendar) value);
		else
			cell.setCellValue(new HSSFRichTextString(escapeColumnValue(value)));
	}

	protected void writeCell(Object value, Cell cell) {
		if (value instanceof Number) {
			Number num = (Number) value;
			cell.setCellValue(num.doubleValue());
		} else if (value instanceof Date)
			cell.setCellValue((Date) value);
		else if (value instanceof Calendar)
			cell.setCellValue((Calendar) value);
		else
			cell.setCellValue(new XSSFRichTextString(escapeColumnValue(value)));
	}

	protected String escapeColumnValue(Object rawValue) {
		if (rawValue == null) {
			return null;
		} else {
			String returnString = ObjectUtils.toString(rawValue);
			returnString = StringEscapeUtils.escapeJava(StringUtils.trimToEmpty(returnString));
			returnString = StringUtils.replace(StringUtils.trim(returnString), "\\t", " ");
			returnString = StringUtils.replace(StringUtils.trim(returnString), "\\r", " ");
			returnString = StringEscapeUtils.unescapeJava(returnString);
			return returnString;
		}
	}

	@Override
	// TODO: Add xml serialization
	public Writer getRestMarkup(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			String outputType, SearchService searchService) throws Exception {

		if (GsonUtil.isGsonEnabled(searchBuilder))
			return GsonUtil.getJson(searchBuilder, this, request, Object[].class, searchService);
		else {
			SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
			String jsonProperty = searchBuilder.getSearchBuilderParameterMap().get(
					SearchBuilderParameterName.JSON_PROPERTY.toString());
			List<String> columnNames = new ArrayList<String>();
			if (StringUtils.isNotEmpty(jsonProperty))
				columnNames = Arrays.asList(StringUtils.split(jsonProperty, ","));
			else
				columnNames = getColumnName(data.getSelectClause());
			List<?> resultList;
			resultList = getResult(data);
			StringBuffer resultsForJson = new StringBuffer("[");
			java.util.Iterator<?> k = resultList.iterator();
			while (k.hasNext()) {
				Object[] row = null;
				Object obj = k.next();

				if (obj instanceof Object[])
					row = (Object[]) obj;
				else
					row = new Object[] { obj };
				resultsForJson.append("{");
				for (int i = 0; i < row.length; i++) {
					try {
						if (i == row.length - 1)
							resultsForJson.append("\"" + columnNames.get(i) + "\":"
									+ (row[i] == null ? "null" : "\"" + escape(row[i].toString()) + "\""));
						else
							resultsForJson.append("\"" + columnNames.get(i) + "\":"
									+ (row[i] == null ? "null" : "\"" + escape(row[i].toString()) + "\"") + ",");
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
				if (k.hasNext())
					resultsForJson.append("},");
				else
					resultsForJson.append("}");
			}
			resultsForJson.append("]");
			StringReader reader = new StringReader(resultsForJson.toString());
			StringWriter out = new StringWriter();
			IOUtils.copy(reader, out);
			return out;
		}
	}

	private List<String> getColumnName(String split) {
		String[] schemaDotColumn = StringUtils.split(split, ",");
		List<String> splittedColumn = new ArrayList<String>();
		for (String i : schemaDotColumn) {
			String columnName = StringUtils.substringAfterLast(i, ".");
			String columnCleaned = columnName.replaceAll("[|'{}]+", "");
			splittedColumn.add(columnCleaned);
		}
		return splittedColumn;
	}

	private StringBuffer escape(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb;
	}

}
