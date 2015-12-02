package it.cilea.core.hibernate.dialect;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class OracleDialect extends Oracle10gDialect {

	public OracleDialect() {
		super();
		registerFunction("tochar", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
		registerFunction("dbms_lob.substr", new StandardSQLFunction("dbms_lob.substr", StandardBasicTypes.STRING));
		registerFunction("coalesce", new StandardSQLFunction("coalesce", StandardBasicTypes.STRING));
		registerFunction("score", new StandardSQLFunction("score", StandardBasicTypes.STRING));
		registerFunction("ROWIDTOCHAR", new StandardSQLFunction("ROWIDTOCHAR", StandardBasicTypes.STRING));
		registerColumnType(-101, "timestamp");

	}
}
