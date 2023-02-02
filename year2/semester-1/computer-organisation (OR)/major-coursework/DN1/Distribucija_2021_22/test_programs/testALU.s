# This program uses the instructions of alu with defined in the basic_microcode file. 
# It loads value 50 to register r0, loads value 10 to r1, adds r1 as to r0, 
# adds r1 to r0,subtracts r1 from r0, subtracts r1 from r0, multiplies r0 by r1, 
# multiplies r0 by r1, negates r0, divides r0 by 2 and stores the remainder of 
# another division by 2 to r0, with negations of r1 in between.

main:	li    r0, 50			   # Load value 50 into register r0
        li    r1, 10
        add   r0, r0, r1           # Add r1 to r0
        neg   r1
        add   r0, r0, r1           # Add r1 to r0
        neg   r1
        sub   r0, r0, r1           # Subtract r1 from r0
        neg   r1
        sub   r0, r0, r1           # Subtract r1 from r0
        neg   r1
        mul   r0, r0, r1           # Multiply r0 by r1
        neg   r1
        mul   r0, r0, r1           # Multiply r0 by r1
        neg   r0                   # Negate r0
        neg   r1
        div   r0, r0, r1           # Divide r0 by r1
        rem   r0, r0, r1           # Store remainder of division of r0 and r1 in r0