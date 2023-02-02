package pins.data.ast;

import pins.common.logger.*;
import pins.common.report.*;
import pins.data.ast.visitor.AstVisitor;

/**
 * An abstract syntax tree.
 */
public abstract class AST implements Loggable {
	
	private static int count = 0;

	public final int id;
	
	public final Location location;
	
	public AST(Location location) {
		this.location = location;
		id = count++;
	}
	
	public abstract <Result, Arg> Result accept(AstVisitor<Result, Arg> visitor, Arg arg);
	
}
