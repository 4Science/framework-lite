package it.cilea.core.search.antlr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import antlr.CommonAST;

public class Tester {
	public static void main(String args[]) {
		try {
			// DataInputStream input = new DataInputStream(System.in);
			// System.out.println(input.readUTF());
			// String text="("+ "\u00E1\u00E7"+"ncora)";
			// String text="(A & B & (C & D))";

			// String
			// text="(((a=pippo & b=pluto ) | (b=minnie                        |                     c=${nonloso}) )&          d=allora                come stai)";
			String text = "(\"ciao come stai\"[TI] AND \"ciao come stai\"[TI] )";

			System.out.println(text);
			InputStream input = new ByteArrayInputStream(text.getBytes("ISO-8859-1"));

			ExpressionLexer lexer = new ExpressionLexer(input);
			// Token token=null;
			// do{
			// token=lexer.nextToken();
			//
			// System.out.println(token.getText());
			// }while (token.getText()!=null);
			//

			ExpressionParser parser = new ExpressionParser(lexer);
			parser.expr();
			CommonAST parseTree = (CommonAST) parser.getAST();
			// System.out.println(parseTree.toStringList());
			// ASTFrame frame = new ASTFrame("The tree", parseTree);
			// frame.setVisible(true);
			ExpressionTreeWalker walker = new ExpressionTreeWalker();
			String r = walker.expr(parseTree);

			System.out.println("Value: " + r);
		} catch (Exception e) {
			System.out.println("Beccato");
			e.printStackTrace();
		}
	}
}
