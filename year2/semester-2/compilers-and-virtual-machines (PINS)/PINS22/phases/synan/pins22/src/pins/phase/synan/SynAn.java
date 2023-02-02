package pins.phase.synan;

import pins.common.report.Report;
import pins.data.symbol.Symbol;
import pins.data.symbol.Token;
import pins.phase.lexan.*;

/**
 * Syntax analyzer, using LL(1) parsing with recursive descent.
 */
public class SynAn implements AutoCloseable {

	/**
	 * lexan holds the lexical analyser ({@link LexAn}), which returns symbols of source file one by one.
	 * currSymbol is the lookahead buffer of length 1, which holds the current symbol returned by lexan.
	 */
	private final LexAn lexan;
	private Symbol currSymbol;
	

	public SynAn(LexAn lexan) {		
		this.lexan = lexan;
		this.currSymbol = null;
	}

	public void close() {
		lexan.close();
	}

	/**
	 * Initializes parsing of source file, by grabbing first symbol and calling recursive parsing.
	 */
	public void parser() {
		currSymbol = lexan.lexer();
		parsePrg();
	}


	/**
	 * Checks if current symbol matches the expected symbol of production. If so, {@link LexAn#lexer()
	 * move to next symbol}, otherwise throw error.
	 *
	 * @param t Token to which the current symbol will be compared to.
	 * @throws Report.Error when current symbol doesn't match expected token.
	 */
	private void checkAndMove(Token t) {
		// Check if current symbol matches expected token.
		if (currSymbol.token != t) {
			throw new Report.Error(currSymbol.location,
					"Syntax error! Expected: " + getLexeme(t) + ", got: " + currSymbol.lexeme);
		}
		// Move to next symbol in source file.
		currSymbol = lexan.lexer();
	}

	/**
	 * Returns lexeme representation of token.
	 *
	 * @param token Token of which to get lexeme.
	 * @return Lexeme of token.
	 */
	private String getLexeme(Token token) {
		return switch (token) {
			case LPARENTHESIS -> "(";
			case RPARENTHESIS -> ")";
			case LBRACKET -> "[";
			case RBRACKET -> "]";
			case LBRACE -> "{";
			case RBRACE -> "}";
			case COMMA -> ",";
			case COLON -> ":";
			case SEMIC -> ";";
			case AND -> "&";
			case OR -> "|";
			case NOT -> "!";
			case EQU -> "==";
			case NEQ -> "!=";
			case LTH -> "<";
			case GTH -> ">";
			case LEQ -> "<=";
			case GEQ -> ">=";
			case MUL -> "*";
			case DIV -> "/";
			case MOD -> "%";
			case ADD -> "+";
			case SUB -> "-";
			case PTR -> "^";
			case ASSIGN -> "=";
			case CHAR -> "char";
			case DEL -> "del";
			case DO -> "do";
			case ELSE -> "else";
			case END -> "end";
			case FUN -> "fun";
			case IF -> "if";
			case INT -> "int";
			case NEW -> "new";
			case THEN -> "then";
			case TYP -> "typ";
			case VAR -> "var";
			case VOID -> "void";
			case WHERE -> "where";
			case WHILE -> "while";
			case CONSTVOID -> "none";
			case CONSTPTR -> "nil";
			case CONSTCHAR -> "character constant";
			case IDENTIFIER -> "identifier";
			case CONSTINT -> "int constant";
			default -> null;
		};
	}


	/**
	 * Parse start symbol of program.
	 */
	private void parsePrg() {
		Report.info("prg -> decls");
		parseDecls();
	}

	/**
	 * Parse declarations of program.
	 */
	private void parseDecls() {
		Report.info("decls -> decl declsRst");

		parseDecl();
		parseDeclsRst();
	}

	/**
	 * Parse rest of declarations in program.
	 *
	 * @throws Report.Error when current symbol not one of: TYP, VAR, FUN, EOF, RPARENTHESIS.
	 */
	private void parseDeclsRst() {
		switch (currSymbol.token) {
			case TYP, VAR, FUN -> {
				// If token a start of declaration, parse declaration.
				
				Report.info("declsRst -> decls");
				parseDecls();
			}
			case EOF, RPARENTHESIS -> {
				// If token end-of-file or right parenthesis, there are no more declarations.
				Report.info("declsRst -> ");
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing declarations!");
			}
		}
	}


	/**
	 * Parse declaration, which can start with keywords TYP, VAR or FUN. Otherwise throw error.
	 *
	 * @throws Report.Error when current symbol not one of: TYP, VAR, FUN.
	 */
	private void parseDecl() {
		switch (currSymbol.token) {
			case TYP -> {
				// Parse type declaration, which is: typ identifier = type;

				checkAndMove(Token.TYP);
				checkAndMove(Token.IDENTIFIER);
				checkAndMove(Token.ASSIGN);

				Report.info("decl -> typ identifier = type ;");

				parseType();

				checkAndMove(Token.SEMIC);
			}
			case VAR -> {
				// Parse variable declaration, which is: var identifier : type;

				checkAndMove(Token.VAR);
				checkAndMove(Token.IDENTIFIER);
				checkAndMove(Token.COLON);

				Report.info("decl -> var identifier : type ;");

				parseType();
				
				checkAndMove(Token.SEMIC);
			}
			case FUN -> {
				// Parse function declaration, which is: fun identifier ( funParams ) : type = expr;

				checkAndMove(Token.FUN);
				checkAndMove(Token.IDENTIFIER);
				checkAndMove(Token.LPARENTHESIS);

				Report.info("decl -> fun identifier ( funParams ) : type = expr ;");

				parseFunParams();

				checkAndMove(Token.RPARENTHESIS);
				checkAndMove(Token.COLON);

				parseType();

				checkAndMove(Token.ASSIGN);

				parseExpr();
				
				checkAndMove(Token.SEMIC);
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing declaration!");
			}
		}
	}

	/**
	 * Parse function parameters.
	 *
	 * @throws Report.Error when current symbol not one of: IDENTIFIER, RPARENTHESIS.
	 */
	private void parseFunParams() {
		switch (currSymbol.token) {
			case IDENTIFIER -> {
				// Parse function parameter, which is: identifier : type funParamsRst

				checkAndMove(Token.IDENTIFIER);
				checkAndMove(Token.COLON);

				Report.info("funParams -> identifier : type funParamsRst");

				parseType();
				parseFunParamsRst();
			}
			case RPARENTHESIS -> {
				// If right parenthesis encountered, function has no parameters.
				Report.info("funParams -> ");
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing function parameters!");
			}
		}
	}

	/**
	 * Parse rest of function parameters.
	 *
	 * @throws Report.Error when current symbol not one of: COMMA, RPARENTHESIS.
	 */
	private void parseFunParamsRst() {
		switch (currSymbol.token) {
			case COMMA -> {
				// Parse the rest of function parameter, which is: , identifier : type funParamsRst

				checkAndMove(Token.COMMA);
				checkAndMove(Token.IDENTIFIER);
				checkAndMove(Token.COLON);

				Report.info("funParamsRst -> , identifier : type funParamsRst");

				parseType();
				parseFunParamsRst();
			}
			case RPARENTHESIS -> {
				// If right parenthesis encountered, there are no more parameters.
				Report.info("funParamsRst -> ");
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing function parameters!");
			}
		}
	}


	/**
	 * Parse data types, which can be void, char, int, id, [ expr ] type, ^ type or ( type ).
	 *
	 * @throws Report.Error when current symbol not one of: VOID, CHAR, INT, IDENTIFIER, PTR, LBRACKET, LPARENTHESIS.
	 */
	private void parseType() {
		switch (currSymbol.token) {
			// Parse primitive types.
			case VOID -> {
				checkAndMove(Token.VOID);
				Report.info("type -> void");
			}
			case CHAR -> {
				checkAndMove(Token.CHAR);
				Report.info("type -> char");
			}
			case INT -> {
				checkAndMove(Token.INT);
				Report.info("type -> int");
			}
			// Parse named type.
			case IDENTIFIER -> {
				checkAndMove(Token.IDENTIFIER);
				Report.info("type -> identifier");
			}
			// Parse array type of form [ expr ] type
			case LBRACKET -> {
				checkAndMove(Token.LBRACKET);
				Report.info("type -> [ expr ] type");

				parseExpr();

				checkAndMove(Token.RBRACKET);

				parseType();
			}
			// Parse pointer type of form ^ type
			case PTR -> {
				checkAndMove(Token.PTR);
				Report.info("type -> ^ type");

				parseType();
			}
			// Parse enclosed type of form ( type ).
			case LPARENTHESIS -> {
				checkAndMove(Token.LPARENTHESIS);
				Report.info("type -> ( type )");

				parseType();

				checkAndMove(Token.RPARENTHESIS);
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing type!");
			}
		}
	}


	/**
	 * Parse statement. This could be an IF statement, a WHILE statement, an assignment or expression statement.
	 */
	private void parseStmt() {
		switch (currSymbol.token) {
			case IF -> {
				// Parse if statement, which is: if expr then stmts end; or if expr then stmts else stmts end;

				checkAndMove(Token.IF);
				Report.info("stmt -> if expr then stmts stmtElse end ");

				parseExpr();

				checkAndMove(Token.THEN);

				parseStmts();
				parseStmtElse();

				checkAndMove(Token.END);
			}
			case WHILE -> {
				// Parse where statement, which is: where expr do stmts end;

				checkAndMove(Token.WHILE);
				Report.info("stmt -> while expr do stmts end ");

				parseExpr();

				checkAndMove(Token.DO);

				parseStmts();

				checkAndMove(Token.END);
			}
			default -> {
				// Parse expression or assignment statement.
				Report.info("stmt -> expr exprAsgn");

				parseExpr();
				parseExprAsgn();
			}
		}
	}

	/**
	 * Parse multiple statements.
	 */
	private void parseStmts() {
		Report.info("stmts -> stmt stmtsRst");

		parseStmt();
		parseStmtsRst();
	}

	/**
	 * Parse rest of statements.
	 *
	 * @throws Report.Error when current symbol not one of: SEMIC.
	 */
	private void parseStmtsRst() {
		switch (currSymbol.token) {
			case SEMIC -> {
				// Parse rest of statements, which is: ; stmts'

				checkAndMove(Token.SEMIC);
				Report.info("stmtsRst -> ; stmts' ");

				parseStmts_();
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing statements!");
			}
		}
	}

	/**
	 * Parse assignment expression.
	 */
	private void parseExprAsgn() {
		switch (currSymbol.token) {
			case ASSIGN -> {
				// Parse assignment expression, which is: = expr ;

				checkAndMove(Token.ASSIGN);
				Report.info("exprAsgn -> = expr");

				parseExpr();
			}
			default -> {
				Report.info("exprAsgn -> ");
			}
		}
	}

	/**
	 * Parse else statement.
	 */
	private void parseStmtElse() {
		switch (currSymbol.token) {
			case ELSE -> {
				// Parse else statement, which is: else stmts

				checkAndMove(Token.ELSE);
				Report.info("stmtElse -> else stmts");

				parseStmts();
			}
			default -> {
				Report.info("stmtElse -> ");
			}
		}
	}

	/**
	 * Parse stmts' nonterminal. This is needed so we can have multiple statements follow each other. If it encounters
	 * a semicolon, a right brace, an END symbol or ELSE symbol, that signifies that statements are over. Otherwise it
	 * loops back to stmts nonterminal.
	 */
	private void parseStmts_() {
		switch (currSymbol.token) {
			case SEMIC, RBRACE, END, ELSE -> {
				Report.info("stmts' -> ");
			}
			default -> {
				Report.info("stmts' -> stmts");

				parseStmts();
			}
		}
	}


	/**
	 * Parse expression.
	 */
	private void parseExpr() {
		switch (currSymbol.token) {
			case NEW, DEL -> {
				// Initialize parsing of allocation expression.

				Report.info("expr -> exprAlloc");

				parseExprAlloc();
			}
			case LBRACE -> {
				// Initialize parsing of compound expression.

				checkAndMove(Token.LBRACE);
				Report.info("expr -> { stmts }");

				parseStmts();

				checkAndMove(Token.RBRACE);
			}
			default -> {
				// Initialize parsing of disjunction expression.

				Report.info("expr -> exprDis");

				parseExprDis();
			}
		}
	}

	/**
	 * Parse allocation expression.
	 *
	 * @throws Report.Error when current symbol not one of: NEW, DEL.
	 */
	private void parseExprAlloc() {
		switch (currSymbol.token) {
			case NEW -> {
				// Parse allocation expression, which is: new expr

				checkAndMove(Token.NEW);
				Report.info("exprAlloc -> new expr");

				parseExpr();
			}
			case DEL -> {
				// Parse allocation expression, which is: del expr

				checkAndMove(Token.DEL);
				Report.info("exprAlloc -> del expr");

				parseExpr();
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing allocation expression!");
			}
		}
	}

	/**
	 * Parse disjunction ( | ) expression.
	 */
	private void parseExprDis() {
		Report.info("exprDis -> exprCon exprDisRst");

		parseExprCon();
		parseExprDisRst();
	}

	/**
	 * Parse rest of disjunction expression.
	 */
	private void parseExprDisRst() {
		switch (currSymbol.token) {
			case OR -> {
				// Parse rest of disjunction expression, which is: | exprCon exprDisRst

				checkAndMove(Token.OR);
				Report.info("exprDisRst -> | exprCon exprDisRst");

				parseExprCon();
				parseExprDisRst();
			}
			default -> {
				Report.info("exprDisRst -> ");
			}
		}
	}

	/**
	 * Parse conjunction ( & ) expression.
	 */
	private void parseExprCon() {
		Report.info("exprCon -> exprRel exprConRst");

		parseExprRel();
		parseExprConRst();
	}

	/**
	 * Parse rest of conjunction expression.
	 */
	private void parseExprConRst() {
		switch (currSymbol.token) {
			case AND -> {
				// Parse rest of disjunction expression, which is: & exprRel exprConRst

				checkAndMove(Token.AND);
				Report.info("exprConRst -> & exprRel exprConRst");

				parseExprRel();
				parseExprConRst();
			}
			default -> {
				Report.info("exprConRst -> ");
			}
		}
	}

	/**
	 * Start parsing relational expression. This includes parsing additive expression and rest of relational expression,
	 * which starts with a relational operator ( ==, !=, <, >, <=, >= ).
	 */
	private void parseExprRel() {
		Report.info("exprRel -> exprAdd exprRelRst");

		parseExprAdd();
		parseExprRelRst();
	}

	/**
	 * Parse rest of relational expression.
	 */
	private void parseExprRelRst() {
		switch (currSymbol.token) {
			case EQU -> {
				// Parse rest of relational expression, which is: == exprAdd exprRelRst

				checkAndMove(Token.EQU);
				Report.info("exprRelRst -> == exprAdd exprRelRst");

				parseExprAdd();
				parseExprRelRst();
			}
			case NEQ -> {
				// Parse rest of relational expression, which is: != exprAdd exprRelRst

				checkAndMove(Token.NEQ);
				Report.info("exprRelRst -> != exprAdd exprRelRst");

				parseExprAdd();
				parseExprRelRst();
			}
			case GEQ -> {
				// Parse rest of relational expression, which is: >= exprAdd exprRelRst

				checkAndMove(Token.GEQ);
				Report.info("exprRelRst -> >= exprAdd exprRelRst");

				parseExprAdd();
				parseExprRelRst();
			}
			case LEQ -> {
				// Parse rest of relational expression, which is: <= exprAdd exprRelRst

				checkAndMove(Token.LEQ);
				Report.info("exprRelRst -> <= exprAdd exprRelRst");

				parseExprAdd();
				parseExprRelRst();
			}
			case LTH -> {
				// Parse rest of relational expression, which is: > exprAdd exprRelRst

				checkAndMove(Token.LTH);
				Report.info("exprRelRst -> < exprAdd exprRelRst");

				parseExprAdd();
				parseExprRelRst();
			}
			case GTH -> {
				// Parse rest of relational expression, which is: < exprAdd exprRelRst

				checkAndMove(Token.GTH);
				Report.info("exprRelRst -> > exprAdd exprRelRst");

				parseExprAdd();
				parseExprRelRst();
			}
			default -> {
				Report.info("exprRelRst -> ");
			}
		}
	}

	/**
	 * Parse additive expression. This includes parsing of multiplicative expression and rest of additive expression,
	 * which starts with an additive operator ( +, - ).
	 */
	private void parseExprAdd() {
		Report.info("exprAdd -> exprMul exprAddRst");

		parseExprMul();
		parseExprAddRst();
	}

	/**
	 * Parse rest of additive expression.
	 */
	private void parseExprAddRst() {
		switch (currSymbol.token) {
			case ADD -> {
				// Parse rest of additive expression, which is: + exprMul exprAddRst

				checkAndMove(Token.ADD);
				Report.info("exprAddRst -> + exprMul exprAddRst");

				parseExprMul();
				parseExprAddRst();
			}
			case SUB -> {
				// Parse rest of additive expression, which is: - exprMul exprAddRst

				checkAndMove(Token.SUB);
				Report.info("exprAddRst -> - exprMul exprAddRst");

				parseExprMul();
				parseExprAddRst();
			}
			default -> {
				Report.info("exprAddRst -> ");
			}
		}
	}

	/**
	 * Parse multiplicative expression. This includes parsing of prefix expression and rest of multiplicative expression,
	 * which starts with a multiplicative operator ( *, /, % ).
	 */
	private void parseExprMul() {
		Report.info("exprMul -> exprPref exprMulRst");

		parseExprPref();
		parseExprMulRst();
	}

	/**
	 * Parse rest of multiplicative expression.
	 */
	private void parseExprMulRst() {
		switch (currSymbol.token) {
			case MUL -> {
				// Parse rest of multiplicative expression, which is: * exprPref exprMulRst

				checkAndMove(Token.MUL);
				Report.info("exprMulRst -> * exprPref exprMulRst");

				parseExprPref();
				parseExprMulRst();
			}
			case DIV -> {
				// Parse rest of multiplicative expression, which is: / exprPref exprMulRst

				checkAndMove(Token.DIV);
				Report.info("exprMulRst -> / exprPref exprMulRst");

				parseExprPref();
				parseExprMulRst();
			}
			case MOD -> {
				// Parse rest of multiplicative expression, which is: % exprPref exprMulRst

				checkAndMove(Token.MOD);
				Report.info("exprMulRst -> % exprPref exprMulRst");

				parseExprPref();
				parseExprMulRst();
			}
			default -> {
				Report.info("exprMulRst -> ");
			}
		}
	}

	/**
	 * Parse prefix expression, which is an expression that starts with a prefix ( !, +, -, ^ ). It can also parse into
	 * a postfix expression.
	 */
	private void parseExprPref() {
		switch (currSymbol.token) {
			case NOT -> {
				// Parse rest of prefix expression, which is: ! exprPref

				checkAndMove(Token.NOT);
				Report.info("exprPref -> ! exprPref");

				parseExprPref();
			}
			case ADD -> {
				// Parse rest of prefix expression, which is: + exprPref

				checkAndMove(Token.ADD);
				Report.info("exprPref -> + exprPref");

				parseExprPref();
			}
			case SUB -> {
				// Parse rest of prefix expression, which is: - exprPref

				checkAndMove(Token.SUB);
				Report.info("exprPref -> - exprPref");

				parseExprPref();
			}
			case PTR -> {
				// Parse rest of prefix expression, which is: ^ exprPref

				checkAndMove(Token.PTR);
				Report.info("exprPref -> ^ exprPref");

				parseExprPref();
			}
			default -> {
				// Initialize parsing of postfix expression.

				Report.info("exprPref -> exprPost");

				parseExprPost();
			}
		}
	}

	/**
	 * Parse postfix expression, which includes parsing an atomic expression and the rest of postfix expression, which
	 * is a postfix ( [ expr ], ^ ).
	 */
	private void parseExprPost() {
		Report.info("exprPost -> exprAtom exprPostRst");

		parseExprAtom();
		parseExprPostRst();
	}

	/**
	 * Parse rest of postfix expression.
	 */
	private void parseExprPostRst() {
		switch (currSymbol.token) {
			case LBRACKET -> {
				// Parse rest of postfix expression, which is: [ expr ]

				checkAndMove(Token.LBRACKET);
				Report.info("exprPostRst -> [ expr ]");

				parseExpr();

				checkAndMove(Token.RBRACKET);
			}
			case PTR -> {
				// Parse rest of postfix expression, which is: ^

				checkAndMove(Token.PTR);
				Report.info("exprPostRst -> ^");
			}
			default -> {
				Report.info("exprPostRst -> ");
			}
		}
	}

	/**
	 * Parse atomic expression, which is an expression that cannot be simplified further. This includes constants,
	 * identifiers, enclosed, typecast and where expressions.
	 *
	 * @throws Report.Error when current symbol not one of: CONSTVOID, CONSTCHAR, CONSTINT, CONSTPTR, IDENTIFIER,
	 * 														LPARENTHESIS.
	 */
	private void parseExprAtom() {
		switch (currSymbol.token) {
			case CONSTVOID -> {
				checkAndMove(Token.CONSTVOID);
				Report.info("exprAtom -> constvoid");
			}
			case CONSTCHAR -> {
				checkAndMove(Token.CONSTCHAR);
				Report.info("exprAtom -> constchar");
			}
			case CONSTINT -> {
				checkAndMove(Token.CONSTINT);
				Report.info("exprAtom -> constint");
			}
			case CONSTPTR -> {
				checkAndMove(Token.CONSTPTR);
				Report.info("exprAtom -> constptr");
			}
			case IDENTIFIER -> {
				// Parse atomic expression, which is: identifier or identifier(identParams)

				checkAndMove(Token.IDENTIFIER);
				Report.info("exprAtom -> identifier identifierRst");

				parseIdentifierRst();
			}
			case LPARENTHESIS -> {
				// Parse atomic expression, which is: (expr exprRst)

				checkAndMove(Token.LPARENTHESIS);
				Report.info("exprAtom -> ( expr exprRst )");

				parseExpr();
				parseExprRst();

				checkAndMove(Token.RPARENTHESIS);
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing atomic expression!");
			}
		}
	}

	/**
	 * Parse rest of identifier. This is used in case of a function call.
	 */
	private void parseIdentifierRst() {
		switch (currSymbol.token) {
			case LPARENTHESIS -> {
				checkAndMove(Token.LPARENTHESIS);
				Report.info("identifierRst -> ( identParams )");

				parseIdentParams();

				checkAndMove(Token.RPARENTHESIS);
			}
			default -> {
				Report.info("identifierRst -> ");
			}
		}
	}

	/**
	 * Parse parameters of rest of identifier (function call).
	 */
	private void parseIdentParams() {
		Report.info("identParams -> expr identParamsRst");

		parseExpr();
		parseIdentParamsRst();
	}

	/**
	 * Parse rest of identifier parameters.
	 */
	private void parseIdentParamsRst() {
		switch (currSymbol.token) {
			case COMMA -> {
				checkAndMove(Token.COMMA);
				Report.info("identParamsRst -> , expr identParamsRst");

				parseExpr();
				parseIdentParamsRst();
			}
			default -> {
				Report.info("identParamsRst -> ");
			}
		}
	}

	/**
	 * Parse rest of expression. This could be a typecast, a where expression or nothing.
	 */
	private void parseExprRst() {
		switch (currSymbol.token) {
			case COLON -> {
				// Parse rest of expression, which is: : typecast

				Report.info("exprRst -> typecast");

				parseTypecast();
			}
			case WHERE -> {
				// Parse rest of expression, which is: where decls

				Report.info("exprRst -> exprWhere");

				parseExprWhere();
			}
			default -> {
				Report.info("exprRst -> ");
			}
		}
	}

	/**
	 * Parse typecast part of expression.
	 *
	 * @throws Report.Error when current symbol not one of: COLON.
	 */
	private void parseTypecast() {
		switch (currSymbol.token) {
			case COLON -> {
				// Parse rest of expression, which is: : typecast

				checkAndMove(Token.COLON);
				Report.info("typecast -> : type");

				parseType();
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing type cast!");
			}
		}
	}

	/**
	 * Parse where part of expression.
	 *
	 * @throws Report.Error when current symbol not one of: WHERE.
	 */
	private void parseExprWhere() {
		switch (currSymbol.token) {
			case WHERE -> {
				// Parse rest of expression, which is: where decls

				checkAndMove(Token.WHERE);
				Report.info("exprWhere -> where decls");

				parseDecls();
			}
			default -> {
				throw new Report.Error(currSymbol.location, "Syntax error when parsing where expression!");
			}
		}
	}
}
