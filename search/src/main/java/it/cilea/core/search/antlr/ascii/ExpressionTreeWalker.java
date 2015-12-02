// $ANTLR 2.7.5 (20050128): "grammar.g" -> "ExpressionTreeWalker.java"$
package it.cilea.core.search.antlr.ascii;

import antlr.NoViableAltException;
import antlr.RecognitionException;
import antlr.collections.AST;

public class ExpressionTreeWalker extends antlr.TreeParser implements ExpressionParserTokenTypes {
	public ExpressionTreeWalker() {
		tokenNames = _tokenNames;
	}

	public final java.lang.String expr(AST _t) throws RecognitionException {
		java.lang.String r;

		AST expr_AST_in = (_t == ASTNULL) ? null : (AST) _t;
		AST i = null;
		java.lang.String a, b;
		r = "";

		try { // for error handling
			if (_t == null)
				_t = ASTNULL;
			switch (_t.getType()) {
			case OR: {
				AST __t24 = _t;
				AST tmp1_AST_in = (AST) _t;
				match(_t, OR);
				_t = _t.getFirstChild();
				a = expr(_t);
				_t = _retTree;
				b = expr(_t);
				_t = _retTree;
				_t = __t24;
				_t = _t.getNextSibling();
				r = CustomEvaluator.evaluateBinaryOperation(a, b, "OR");
				break;
			}
			case AND: {
				AST __t25 = _t;
				AST tmp2_AST_in = (AST) _t;
				match(_t, AND);
				_t = _t.getFirstChild();
				a = expr(_t);
				_t = _retTree;
				b = expr(_t);
				_t = _retTree;
				_t = __t25;
				_t = _t.getNextSibling();
				r = CustomEvaluator.evaluateBinaryOperation(a, b, "AND");
				break;
			}
			case NOT: {
				AST __t26 = _t;
				AST tmp3_AST_in = (AST) _t;
				match(_t, NOT);
				_t = _t.getFirstChild();
				a = expr(_t);
				_t = _retTree;
				_t = __t26;
				_t = _t.getNextSibling();
				r = CustomEvaluator.evaluateNot(a);
				break;
			}
			case LPAREN: {
				AST __t27 = _t;
				AST tmp4_AST_in = (AST) _t;
				match(_t, LPAREN);
				_t = _t.getFirstChild();
				a = expr(_t);
				_t = _retTree;
				_t = __t27;
				_t = _t.getNextSibling();
				r = CustomEvaluator.evaluateBracket(a);
				break;
			}
			case ALPHA: {
				i = (AST) _t;
				match(_t, ALPHA);
				_t = _t.getNextSibling();
				r = i.getText();
				break;
			}
			default: {
				throw new NoViableAltException(_t);
			}
			}
		} catch (RecognitionException ex) {
			reportError(ex);
			if (_t != null) {
				_t = _t.getNextSibling();
			}
		}
		_retTree = _t;
		return r;
	}

	public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "LPAREN", "RPAREN", "OR",
			"AND", "NOT", "ALPHA", "WS" };

}
