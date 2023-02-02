package pins.data.ast;

import pins.common.report.*;

/**
 * An expression containing a list of declarations.
 */
public class AstWhereExpr extends AstExpr {

	public final ASTs<AstDecl> decls;
	
	public final AstExpr subExpr;
	
	public AstWhereExpr(Location location, ASTs<AstDecl> decls, AstExpr subExpr) {
		super(location);
		this.decls = decls;
		this.subExpr = subExpr;
	}
	
	@Override
	public void log(String pfx) {
		System.out.println(pfx + "\033[1mAstWhereExpr\033[0m @(" + location + ")");
		System.out.println(pfx + "  {Decls}");
		decls.log(pfx + "    ");
		subExpr.log(pfx + "  ");
	}
	
}
