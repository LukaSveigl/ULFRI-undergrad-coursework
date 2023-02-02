# This program uses the instructions jeq and lw defined in the
# basic_microcode file. It moves values into register r0, stores
# the value of register r0 to address 200, loads value from address
# 200 to register r1, and loops as long as values of register r0 and r1
# are equal, thus creating an infinite loop.

main:	li	  r0, 50				# Load 50 to r0				
        sw    r0, 200               # Store value of r0 to address 200
        lw    r1, 200               # Read value from address 200 into r1
loop:   jeq   r1, r0, loop          # Compare values of r1 and r0, if they are equal jump to address of loop