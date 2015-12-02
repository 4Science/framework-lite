package it.cilea.core.search.antlr;

public class CustomEvaluator {
	
	private static boolean isBlank(String value){
		if ( value == null 	|| value.length() == 0	|| value.contains("${"))
			return true;
		else 
			return false;
	}
	
	public static String evaluateBinaryOperation(String firstTerm, String secondTerm, String operator){
		if (!isBlank(firstTerm) && isBlank(secondTerm))
			return firstTerm;		
		else if (isBlank(firstTerm) && !isBlank(secondTerm))
			return secondTerm;
		else 
			return firstTerm + " " + operator +" " +secondTerm;
		
	}
	
	public static String evaluateBracket(String term){		
		if ( term == null	|| term.length() == 0	|| term.contains("${")				
			){
			return "";
		} else {			
			return "("+term+")";
		}
	}
	
	public static String evaluateNot(String term){		
		if ( term == null	|| term.length() == 0	|| term.contains("${")				
			){
			return "";
		} else {			
			return " NOT "+term;
		}
	}
}
