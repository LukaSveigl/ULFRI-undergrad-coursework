          .text
          
STEV1:    .word 0xFFEEDDCC
STEV2:    .space 4

          .align
          .global __start
__start:
          adr r0, STEV1
          adr r1, STEV2
          
          ldrb r2, [r0]
          strb r2, [r1, #3]
          
          ldrb r2, [r0, #1]
          strb r2, [r1, #2]
          
          ldrb r2, [r0, #2]
          strb r2, [r1, #1]
          
          ldrb r2, [r0, #3]
          strb r2, [r1]
__end:    b __end   




