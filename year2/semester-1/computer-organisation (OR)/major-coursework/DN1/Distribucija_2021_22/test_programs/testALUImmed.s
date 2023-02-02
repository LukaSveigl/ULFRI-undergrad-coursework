# This program uses the instructions of alu with immediate operands
# defined in the basic_microcode file. It loads value 50 to register r0,
# adds 23 as immediate operant to r0, adds -1 to r0, subtracts 15 from r0,
# subtracts -1 from r0, multiplies r0 by 6, multiplies r0 by -1, negates
# r0, divides r0 by 2 and stores the remainder of another division by 2 to r0

main:	li	   r0, 50				# Load value 50 into register r0
        addi   r0, r0, 23           # Add 23 (immediate operand) to r0
        addi   r0, r0, -1           # Add -1 to r0
        subi   r0, r0, 15           # Subtract 15 from r0
        subi   r0, r0, -1           # Subtract -1 from r0
        muli   r0, r0, 6            # Multiply r0 by 6
        muli   r0, r0, -1           # Multiply r0 by -1
        neg    r0                   # Negate r0
        divi   r0, r0, 2            # Divide r0 by 2
        remi   r0, r0, 2            # Store remainder of division of r0 and 2 in r0