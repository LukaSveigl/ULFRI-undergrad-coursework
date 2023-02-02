package pins.data.ast;

import pins.common.report.*;

/**
 * A constant expression
 */
public class AstConstExpr extends AstExpr {

	public enum Kind {
		VOID, CHAR, INT, PTR
	};

	public final Kind kind;

	public final String name;

	public AstConstExpr(Location location, AstConstExpr.Kind kind, String name) {
		super(location);
		this.kind = kind;
		this.name = name;
	}

	@Override
	public void log(String pfx) {
		System.out.println(pfx + "\033[1mAstConstExpr(" + kind + "," + name + ")\033[0m @(" + location + ")");
	}

}
