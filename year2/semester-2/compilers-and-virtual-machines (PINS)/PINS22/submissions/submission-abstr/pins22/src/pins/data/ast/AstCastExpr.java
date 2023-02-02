package pins.data.ast;

import pins.common.report.*;

/**
 * A cast expression.
 */
public class AstCastExpr extends AstExpr {
	
	public final AstExpr subExpr;
	
	public final AstType type;
	
	public AstCastExpr(Location location, AstExpr subExpr, AstType type) {
		super(location);
		this.subExpr = subExpr;
		this.type = type;
	}

	@Override
	public void log(String pfx) {
		System.out.println(pfx + "\033[1mAstCastExpr\033[0m @(" + location + ")");
		subExpr.log(pfx + "  ");
		type.log(pfx + "  ");
	}

}
