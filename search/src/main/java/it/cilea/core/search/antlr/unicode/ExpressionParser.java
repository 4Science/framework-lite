package it.cilea.core.search.antlr.unicode;

// $ANTLR 2.7.5 (20050128): "grammar.g" -> "ExpressionParser.java"$

import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.AST;
import antlr.collections.impl.BitSet;

public class ExpressionParser extends antlr.LLkParser implements ExpressionParserTokenTypes {

	protected ExpressionParser(TokenBuffer tokenBuf, int k) {
		super(tokenBuf, k);
		tokenNames = _tokenNames;
		buildTokenTypeASTClassMap();
		astFactory = new ASTFactory(getTokenTypeToASTClassMap());
	}

	public ExpressionParser(TokenBuffer tokenBuf) {
		this(tokenBuf, 5);
	}

	protected ExpressionParser(TokenStream lexer, int k) {
		super(lexer, k);
		tokenNames = _tokenNames;
		buildTokenTypeASTClassMap();
		astFactory = new ASTFactory(getTokenTypeToASTClassMap());
	}

	public ExpressionParser(TokenStream lexer) {
		this(lexer, 5);
	}

	public ExpressionParser(ParserSharedInputState state) {
		super(state, 5);
		tokenNames = _tokenNames;
		buildTokenTypeASTClassMap();
		astFactory = new ASTFactory(getTokenTypeToASTClassMap());
	}

	public final void expr() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expr_AST = null;

		try { // for error handling
			AST tmp5_AST = null;
			tmp5_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp5_AST);
			match(LPAREN);
			sumExpr();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			expr_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			reportError(ex);
			recover(ex, _tokenSet_0);
		}
		returnAST = expr_AST;
	}

	public final void sumExpr() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sumExpr_AST = null;

		try { // for error handling
			prodExpr();
			astFactory.addASTChild(currentAST, returnAST);
			{
				_loop5: do {
					if ((LA(1) == OR)) {
						{
							AST tmp7_AST = null;
							tmp7_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp7_AST);
							match(OR);
						}
						prodExpr();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						break _loop5;
					}

				} while (true);
			}
			sumExpr_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			reportError(ex);
			recover(ex, _tokenSet_1);
		}
		returnAST = sumExpr_AST;
	}

	public final void prodExpr() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST prodExpr_AST = null;

		try { // for error handling
			powExpr();
			astFactory.addASTChild(currentAST, returnAST);
			{
				_loop9: do {
					if ((LA(1) == AND)) {
						{
							AST tmp8_AST = null;
							tmp8_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp8_AST);
							match(AND);
						}
						powExpr();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						break _loop9;
					}

				} while (true);
			}
			prodExpr_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			reportError(ex);
			recover(ex, _tokenSet_2);
		}
		returnAST = prodExpr_AST;
	}

	public final void powExpr() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST powExpr_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case NOT: {
				{
					AST tmp9_AST = null;
					tmp9_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp9_AST);
					match(NOT);
				}
				atom();
				astFactory.addASTChild(currentAST, returnAST);
				powExpr_AST = (AST) currentAST.root;
				break;
			}
			case LPAREN:
			case ALPHA: {
				atom();
				astFactory.addASTChild(currentAST, returnAST);
				powExpr_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			reportError(ex);
			recover(ex, _tokenSet_0);
		}
		returnAST = powExpr_AST;
	}

	public final void atom() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST atom_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case ALPHA: {
				AST tmp10_AST = null;
				tmp10_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(ALPHA);
				atom_AST = (AST) currentAST.root;
				break;
			}
			case LPAREN: {
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				atom_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			reportError(ex);
			recover(ex, _tokenSet_0);
		}
		returnAST = atom_AST;
	}

	public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "LPAREN", "RPAREN", "OR",
			"AND", "NOT", "ALPHA", "WS" };

	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap = null;
	};

	private static final long[] mk_tokenSet_0() {
		long[] data = { 224L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

	private static final long[] mk_tokenSet_1() {
		long[] data = { 32L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

	private static final long[] mk_tokenSet_2() {
		long[] data = { 96L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

}
