package it.cilea.core.search.antlr;
// $ANTLR 2.7.5 (20050128): "grammar.g" -> "ExpressionLexer.java"$

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;

public class ExpressionLexer extends antlr.CharScanner implements ExpressionParserTokenTypes, TokenStream
 {
public ExpressionLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public ExpressionLexer(Reader in) {
	this(new CharBuffer(in));
}
public ExpressionLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public ExpressionLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				if ((LA(1)==' '||LA(1)=='(') && (true) && (true) && (true) && (true)) {
					mLPAREN(true);
					theRetToken=_returnToken;
				}
				else if ((LA(1)==' '||LA(1)==')') && (true) && (true) && (true) && (true)) {
					mRPAREN(true);
					theRetToken=_returnToken;
				}
				else if ((LA(1)==' '||LA(1)=='|') && (true) && (true) && (true) && (true)) {
					mOR(true);
					theRetToken=_returnToken;
				}
				else if ((LA(1)==' '||LA(1)=='&') && (true) && (true) && (true) && (true)) {
					mAND(true);
					theRetToken=_returnToken;
				}
				else if ((LA(1)==' '||LA(1)=='^') && (true) && (true) && (true) && (true)) {
					mNOT(true);
					theRetToken=_returnToken;
				}
				else if ((LA(1)=='\t'||LA(1)=='\n'||LA(1)=='\r')) {
					mWS(true);
					theRetToken=_returnToken;
				}
				else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true) && (true)) {
					mALPHA(true);
					theRetToken=_returnToken;
				}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		{
		_loop15:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop15;
			}
			
		} while (true);
		}
		match('(');
		{
		_loop17:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop17;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		{
		_loop20:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop20;
			}
			
		} while (true);
		}
		match(')');
		{
		_loop22:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop22;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OR;
		int _saveIndex;
		
		{
		_loop25:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop25;
			}
			
		} while (true);
		}
		match('|');
		{
		_loop27:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop27;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = AND;
		int _saveIndex;
		
		{
		_loop30:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop30;
			}
			
		} while (true);
		}
		match('&');
		{
		_loop32:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop32;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT;
		int _saveIndex;
		
		{
		_loop35:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop35;
			}
			
		} while (true);
		}
		match('^');
		{
		_loop37:
		do {
			if ((LA(1)==' ')) {
				match(' ');
			}
			else {
				break _loop37;
			}
			
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case '\t':
		{
			match('\t');
			break;
		}
		case '\r':
		{
			match('\r');
			match('\n');
			newline();
			break;
		}
		case '\n':
		{
			match('\n');
			newline();
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		_ttype = Token.SKIP;
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mALPHA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ALPHA;
		int _saveIndex;
		
		{
		int _cnt43=0;
		_loop43:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				{
				match(_tokenSet_0);
				}
			}
			else {
				if ( _cnt43>=1 ) { break _loop43; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			
			_cnt43++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[2048];
		data[0]=-3573412800001L;
		data[1]=-1152921505680588801L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	
	}
