package it.cilea.core.search.antlr;

import java.io.Reader;
import java.io.StringReader;

import antlr.CommonAST;

public class QueryNormalizer {
	public enum MissingParameterStrategy{		
		STRIP_MISSING_PARAMETER, STRIP_MISSING_PARAMETER_CLAUSE;
	}
	
	public static String getNormalizedQuery(String query) {
		String result="";
		try {
			//Modified from InputStream to Reader.
			//In this way can be avoided the use of encoding (ISO, UTF-8)
			Reader input = new StringReader(query);
			ExpressionLexer lexer = new ExpressionLexer(input);
			ExpressionParser parser = new ExpressionParser(lexer);
			parser.expr();
			CommonAST parseTree = (CommonAST) parser.getAST();			
			ExpressionTreeWalker walker = new ExpressionTreeWalker();
			result = walker.expr(parseTree);
			
		} catch (Exception e) {	
			
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		//String query="(NOT ((((a=${a} AND b=pluto) OR (b=pluto OR c=${c})) AND d=${d}) AND NOT z=pluto))";
		//String query="(valoreVisualizzato:${param.valoreVisualizzato} AND personaId:6901)";
		//String query="({scheda.annoRiferimento}=${param.annoRiferimento} AND {scheda.membroSet.lev2AreaId}=${param.membroSet.lev2AreaId} AND {scheda.membroSet.lev2StrutturaId}=618 AND ( {scheda.membroSet_orderResponsabile.discriminator} in ['responsabile'] AND {scheda.membroSet_orderResponsabile.ruoloMembroId} in [1100,550,1,71,3,140,1023,142,7,9,11,132,74,552,17,1000,703,84,700,19,1002,86,800,80,802,82,25,144,92,27,29,88,31,91,90,35,33,580,40,106,107,590,1201,902,723,900,605]))";
		
		
		
		//String query="({scheda.annoRiferimento}=${param.annoRiferimento} & {scheda.membroSet.lev2AreaId}=${param.membroSet.lev2AreaId} & {scheda.membroSet.lev2StrutturaId}=618 & ({scheda.membroSet_orderResponsabile.discriminator} in ['responsabile'] & {scheda.membroSet_orderResponsabile.ruoloMembroId} in [1100,550,1,71,3,140,1023,142,7,9,11,132,74,552,17,1000,703,84,700,19,1002,86,800,80,802,82,25,144,92,27,29,88,31,91,90,35,33,580,40,106,107,590,1201,902,723,900,605]))";
		String query="(A & B &(C & D)    )";
		//& ( {scheda.membroSet_orderResponsabile.discriminator} in ['responsabile'] & {scheda.membroSet_orderResponsabile.ruoloMembroId} in [1100,550,1,71,3,140,1023,142,7,9,11,132,74,552,17,1000,703,84,700,19,1002,86,800,80,802,82,25,144,92,27,29,88,31,91,90,35,33,580,40,106,107,590,1201,902,723,900,605])
			
//		
		
	}
}
