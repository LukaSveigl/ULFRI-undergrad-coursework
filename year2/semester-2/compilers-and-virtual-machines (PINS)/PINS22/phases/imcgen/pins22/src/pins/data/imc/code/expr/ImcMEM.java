package pins.data.imc.code.expr;

import pins.data.imc.code.stmt.*;
import pins.data.imc.visitor.*;

/**
 * Memory access.
 * 
 * Evaluates the address, reads the value from this address in the memory and
 * returns the value read (but see {@link ImcMOVE} as well.)
 */
public class ImcMEM extends ImcExpr {

	/** The memory address. */
	public final ImcExpr addr;

	/**
	 * Constucts a memory access.
	 * 
	 * @param addr The memory address.
	 */
	public ImcMEM(ImcExpr addr) {
		this.addr = addr;
	}

	@Override
	public <Result, Arg> Result accept(ImcVisitor<Result, Arg> visitor, Arg accArg) {
		return visitor.visit(this, accArg);
	}

	@Override
	public void log(String pfx) {
		System.out.println(pfx + "MEM");
		addr.log(pfx + "  ");
	}

	@Override
	public String toString() {
		return "MEM(" + addr.toString() + ")";
	}

}
