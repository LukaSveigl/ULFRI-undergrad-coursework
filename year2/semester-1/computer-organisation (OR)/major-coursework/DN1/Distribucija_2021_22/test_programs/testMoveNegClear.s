# This program uses the instructions move, neg and clear defined in the
# basic_microcode file. It moves values into 2 registers, moves value from 
# one register to another, negates it, and clears it.


main:	li	  r0, 50				# Load 50 to r0
        li    r1, 25                # Load 25 to r1
        move  r1, r0                # Move value from r0 to r1
        neg   r1                    # Negate r1
        clr   r1                    # Clear r1