# This program uses the instructions defined in the
# basic_microcode file. It adds the numbers from 100
# down to 1 and stores the result in memory location 256.
# (c) GPL3 Warren Toomey, 2012
#
main:	li	r0, 16			# r0 is the loop counter
		li	r1, 64			# r1 is current value (start ASCII char 64 @,A,B,C,D,...)
		li	r2, -1			# Used to decrement r0
		li	r3, 1			# Used to increment r1 
		li	r4, 32769		# Bit Pattern to be shown on FB
        li  r6, 384

        li  r5, 1           # Store 1 to register r5, this will be displayed on screen

loop:	sw	r1, 32768		# Save current value to TTY...

		sw	r4, 16384		# Save current value to FB (0th line)...
        sw	r6, 16385		# Save current value to FB (1th line)...	
        sw	r4, 16386		# Save current value to FB (2th line)...	
        sw	r4, 16387		# Save current value to FB (3th line)...	
        sw	r4, 16388		# Save current value to FB (4th line)...	
        sw	r6, 16389		# Save current value to FB (5th line)...	
        sw	r4, 16390		# Save current value to FB (6th line)...	
        sw	r4, 16391		# Save current value to FB (7th line)...			
		sw	r6, 16392		# Save current value to FB (9th line)...
        sw	r4, 16393		# Save current value to FB (10th line)...	
        sw	r4, 16394		# Save current value to FB (11th line)...	
        sw	r6, 16395		# Save current value to FB (12th line)...	
        sw	r4, 16396		# Save current value to FB (13th line)...	
        sw	r4, 16397		# Save current value to FB (14th line)...	
        sw	r6, 16398		# Save current value to FB (15th line)...		
		sw	r4, 16399		# Save current value to FB (16th line)...

        # addi r4, r4, 1
        # neg r4

        divi r4, r4, 3

        # addi r6, r6, 64

        # subi r4, r4, 16
        # addi r4, r4, 3
        # neg r4

        sw  r5, 49152       # Display value of r5 on every second FB
        sw  r5, 49154       # line
        sw  r5, 49156
        sw  r5, 49158
        sw  r5, 49160

		add	r1, r1, r3		# r1 ... increment value
		add	r0, r0, r2		# r0 ... decrement counter
		add	r4, r4, r4		# r4 ... move 1 bit left (add is multiply by 2)
        add r5, r5, r3
		jnez	r0, loop	# loop if r0 != 0
		jnez	r1, main	# loop if r1 != 0 -> loop forever
		
