class ExpressionParser extends Parser;
options {		 
	k=5; 
	buildAST=true;	
}

expr     : LPAREN^ sumExpr RPAREN! ;
sumExpr  : prodExpr ((OR^) prodExpr)* ;
prodExpr : powExpr ((AND^) powExpr)* ;
powExpr  : (NOT^) atom | atom ;
atom     : ALPHA | expr ;

class ExpressionLexer extends Lexer;
options {
k=5;
charVocabulary='\u0000'..'\uFFFE';
}
LPAREN: '(' ;
RPAREN: ')' ;
OR  : "|" ;
AND   : "&" ;
NOT   : "^" ;
WS    :
    ('\t'
    | '\r' '\n' { newline(); }
    | '\n'      { newline(); }
    )
    { $setType(Token.SKIP); }
  ;
ALPHA : (~('('|')'|'|'|'&'|'^'|'\t'|'\r'|'\n'))+;




{import java.lang.Math;}
class ExpressionTreeWalker extends TreeParser;


expr returns [java.lang.String r]
  { java.lang.String a,b; r=""; }

  : #(OR a=expr b=expr)  { r=CustomEvaluator.evaluateBinaryOperation(a,b,"OR"); }  
  | #(AND  a=expr b=expr)  { r=CustomEvaluator.evaluateBinaryOperation(a,b,"AND"); }
  | #(NOT  a=expr)  { r=CustomEvaluator.evaluateNot(a); }
  | #(LPAREN a=expr) { r=CustomEvaluator.evaluateBracket(a);}
  | i:ALPHA { r=i.getText(); }
  ;
