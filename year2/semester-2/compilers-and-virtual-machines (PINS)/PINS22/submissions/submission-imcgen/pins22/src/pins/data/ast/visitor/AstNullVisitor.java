package pins.data.ast.visitor;

import pins.data.ast.*;

/**
 * Abstract syntax tree visitor that does nothing.
 * 
 * @param <Result> The result type.
 * @param <Arg>    The argument type.
 */
public class AstNullVisitor<Result, Arg> implements AstVisitor<Result, Arg> {

	// GENERAL PURPOSE

	@Override
	public Result visit(ASTs<? extends AST> trees, Arg arg) {
		return null;
	}

	// DECLARATIONS

	@Override
	public Result visit(AstFunDecl funDecl, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstParDecl parDecl, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstTypDecl typDecl, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstVarDecl varDecl, Arg arg) {
		return null;
	}

	// EXPRESSIONS

	@Override
	public Result visit(AstBinExpr binExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstCallExpr callExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstCastExpr castExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstConstExpr constExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstNameExpr nameExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstPreExpr preExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstPstExpr pstExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstStmtExpr stmtExpr, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstWhereExpr whereExpr, Arg arg) {
		return null;
	}

	// STATEMENTS

	@Override
	public Result visit(AstAssignStmt assignStmt, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstExprStmt exprStmt, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstIfStmt ifStmt, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstWhileStmt whileStmt, Arg arg) {
		return null;
	}

	// TYPES

	@Override
	public Result visit(AstArrType arrType, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstAtomType atomType, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstPtrType ptrType, Arg arg) {
		return null;
	}

	@Override
	public Result visit(AstTypeName typeName, Arg arg) {
		return null;
	}

}
