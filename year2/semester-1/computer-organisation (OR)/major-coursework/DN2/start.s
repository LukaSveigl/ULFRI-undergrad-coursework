.equ PMC_BASE,  0xFFFFFC00  /* (PMC) Base Address */
.equ CKGR_MOR,	0x20        /* (CKGR) Main Oscillator Register */
.equ CKGR_PLLAR,0x28        /* (CKGR) PLL A Register */
.equ PMC_MCKR,  0x30        /* (PMC) Master Clock Register */
.equ PMC_SR,	  0x68        /* (PMC) Status Register */

.text
.code 32

.global _error
_error:
  b _error

.global	_start
_start:

/* select system mode 
  CPSR[4:0]	Mode
  --------------
   10000	  User
   10001	  FIQ
   10010	  IRQ
   10011	  SVC
   10111	  Abort
   11011	  Undef
   11111	  System   
*/

  mrs r0, cpsr
  bic r0, r0, #0x1F   /* clear mode flags */  
  orr r0, r0, #0xDF   /* set supervisor mode + DISABLE IRQ, FIQ*/
  msr cpsr, r0     
  
  /* init stack */
  ldr sp,_Lstack_end
                                   
  /* setup system clocks */
  ldr r1, =PMC_BASE

  ldr r0, = 0x0F01
  str r0, [r1,#CKGR_MOR]

osc_lp:
  ldr r0, [r1,#PMC_SR]
  tst r0, #0x01
  beq osc_lp
  
  mov r0, #0x01
  str r0, [r1,#PMC_MCKR]

  ldr r0, =0x2000bf00 | ( 124 << 16) | 12  /* 18,432 MHz * 125 / 12 */
  str r0, [r1,#CKGR_PLLAR]

pll_lp:
  ldr r0, [r1,#PMC_SR]
  tst r0, #0x02
  beq pll_lp

  /* MCK = PCK/4 */
  ldr r0, =0x0202
  str r0, [r1,#PMC_MCKR]

mck_lp:
  ldr r0, [r1,#PMC_SR]
  tst r0, #0x08
  beq mck_lp

  /* Enable caches */
  mrc p15, 0, r0, c1, c0, 0 
  orr r0, r0, #(0x1 <<12) 
  orr r0, r0, #(0x1 <<2)
  mcr p15, 0, r0, c1, c0, 0 

.global _main
/* main program */
_main:

/* user code here */

bl INIT_IO  @ Init input/output devices
bl INIT_TC0 @ Init timer 0
bl DEBUG_INIT

INF_LOOP:                 @ Local loopback testing loop
  adr r0, STRING          @ Get address of string
  add r0, r0, #0x200000   
  mov r1, #3              @ Set number of characters
  bl RCV_DMA              @ Receive data
     
  ldr r3, =DBGU_BASE
  CHECK_ENDRX:            @ Check if receiving finished
    ldr r2, [r3, #DBGU_SR]
    tst r2, #0b10000 
    beq CHECK_ENDRX

   adr r0, STRING         @ Get address of string
   add r0, r0, #0x200000
   mov r1, #3             @ Set number of characters
   bl SND_DMA             @ Send data
     
   ldr r3, =DBGU_BASE
   CHECK_ENDTX:           @ Check if sending finished
    ldr r2, [r3, #DBGU_SR]
    tst r2, #0b10000 
    beq CHECK_ENDTX
 
   bl XWORD               @ Display morse code on LED 
   b INF_LOOP


/* end user code */

_wait_for_ever:
  b _wait_for_ever

XMCHAR:
  stmfd sp!, {r0, lr}
  
  cmp r0, #'.'    @ Check if R0 is .
  moveq r0, #150  @ If R0 is ., delay for 150 ms
  
  cmp r0, #'-'    @ Check if R0 is -
  moveq r0, #300  @ If R0 is -, delay for 300 ms
  
  bl LED_ON       @ Turn LED on and wait
  bl DELAY_TC0
  
  mov r0, #150    @ Turn LED off and wait for 150 ms
  bl LED_OFF
  bl DELAY_TC0
  
  ldmfd sp!, {r0, pc}
  

XMCODE:
  stmfd sp!, {r0, r1, lr}
  mov r1, r0      @ Copy address of string from R0 to R1
  
  CLOOP:          @ Loop through string
    ldrb r2, [r1] @ Get char of string
    cmp r2, #0    @ Check if end of string - if true, quit subroutine
    beq CBYE
    
    mov r0, r2
    bl XMCHAR     @ Call XMCHAR to turn on LED
    
    add r1, r1, #1@ Move to next char in string
    b CLOOP
  
  CBYE:
  mov r0, #300    @ When done, wait 300 ms
  bl DELAY_TC0
  ldmfd sp!, {r0, r1, pc}
  

GETMCODE:  
  stmfd sp!, {r1, lr} @ Store only value of R1 and link register
  
  mov r2, #6      @ Move value to multiply R1 with
                  @ Value must be 6, as morse codes 
                  @ are stored at address intervals of 6
  
  sub r0, r0, #65 @ Reduce value of char by 65 - get index
  mul r1, r0, r2  @ Multiply value by 6 - get address deviation
  
  adr r0, CHARS   @ Get address of morse codes
  add r0, r0, r1  @ Move to correct offset
  
  ldmfd sp!, {r1, pc} @ Load only value of R1 and program counter


XWORD:
  stmfd sp!, {lr} @ Store only value of link register
  mov r1, r0      @ Copy address from R0 to R1
  
  XLOOP:          @ Loop through all characters in word
    ldrb r0, [r1] @ Load character
    cmp r0, #0    @ Check if end of word
    beq XBYE      @ If end of word, quit subroutine
    bl GETMCODE   @ Get pointer to start of morse code
    bl XMCODE     @ Display morse code
    add r1, r1, #1@ Move to next character in string
    b XLOOP
    
  XBYE:
  ldr r0, =1000   @ Wait for 1 s
  bl DELAY_TC0
  ldmfd sp!, {pc} @ Load only value of program counter


INIT_IO:
  stmfd sp!, {r0, r1, lr}
  .equ PIOA_BASE, 0xFFFFF400 /* Zacetek registrov za vrata A - PIOA */
	.equ PIOB_BASE, 0xFFFFF600 /* Zacetek registrov za vrata B - PIOB */
	.equ PIOC_BASE, 0xFFFFF800 /* Zacetek registrov za vrata C - PIOC */

	.equ PIO_PER, 0x00	       /* Odmiki... */
	.equ PIO_OER, 0x10
	.equ PIO_SODR, 0x30
	.equ PIO_CODR, 0x34
  
  ldr r0, =PIOC_BASE
  
  mov r1, #1 << 1
  str r1, [r0, #PIO_PER]
  str r1, [r0, #PIO_OER]
  
  ldmfd sp!, {r0, r1, pc}
  
INIT_TC0:
  stmfd sp!, {r0-r2, lr}
  .equ PMC_BASE,     0xFFFFFC00    /* Power Management Controller */
                                   /* Base Address */
  .equ PMC_PCER,     0x10          /* Peripheral Clock Enable Register */
    
  .equ TC0_BASE,     0xFFFA0000    /* TC0 Channel Base Address */
  .equ TC_CCR,     0x00            /* TC0 Channel Control Register */
  .equ TC_CMR,     0x04            /* TC0 Channel Mode Register*/
  .equ TC_CV,        0x10          /* TC0 Counter Value */
  .equ TC_RA,        0x14          /* TC0 Register A */
  .equ TC_RB,        0x18          /* TC0 Register B */
  .equ TC_RC,        0x1C          /* TC0 Register C */
  .equ TC_SR,        0x20          /* TC0 Status Register */
  .equ TC_IER,       0x24          /* TC0 Interrupt Enable Register*/
  .equ TC_IDR,       0x28          /* TC0 Interrupt Disable Register */
  .equ TC_IMR,      0x2C           /* TC0 Interrupt Mask Register */

  ldr r0, =PMC_BASE
  ldr r1, [r0, #PMC_PCER]
  ldr r2, =0x20000     /*17. bit se more przgat*/
  orr r1, r1, r2
  str r1, [r0, #PMC_PCER] /*vklop TC0*/
         
  ldr r0, =TC0_BASE
  ldr r1, [r0, #TC_CMR]
  ldr r2, =0xC003      /*15.b = 1, 14.b=1, 13.b=0, 2.b=0, 1.b=1, 0.b=1*/
  orr r1, r1, r2
  str r1, [r0, #TC_CMR] /*konfiguracija*/
     
  ldr r1, =375
  str r1, [r0, #TC_RC] /*zgornja meja stetja*/
     
  ldr r1, [r0, #TC_CCR]
  ldr r2, =0x5
  orr r1, r1, r2
  str r1, [r0, #TC_CCR]  /*start cas*/
     
  ldmfd sp!, {r0-r2, pc}  


DEBUG_INIT:
  stmfd sp!, {r0, r1, lr}
     
  .equ DBGU_BASE, 0xFFFFF200	/* Debug Unit Base Address */
  .equ DBGU_CR, 0x00  		/* DBGU Control Register */
  .equ DBGU_MR, 0x04	  	/* DBGU Mode Register*/
  .equ DBGU_IER, 0x08		/* DBGU Interrupt Enable Register*/
  .equ DBGU_IDR, 0x0C		/* DBGU Interrupt Disable Register */
  .equ DBGU_IMR, 0x10		/* DBGU Interrupt Mask Register */
  .equ DBGU_SR,   0x14		/* DBGU Status Register */
  .equ DBGU_RHR, 0x18		/* DBGU Receive Holding Register */
  .equ DBGU_THR, 0x1C		/* DBGU Transmit Holding Register */
  .equ DBGU_BRGR, 0x20		/* DBGU Baud Rate Generator Register */
     
  .equ DBGU_RPR, 0x100 /* Receive Pointer Register */
  .equ DBGU_RCR, 0x104 /* Receive Counter Register */
  .equ DBGU_TPR, 0x108 /* Transmit Pointer Register */
  .equ DBGU_TCR, 0x10C /* Transmit Counter Register */
  .equ DBGU_RNPR, 0x110 /* Receive Next Pointer Register */
  .equ DBGU_RNCR, 0x114 /* Receive Next Counter Register */
  .equ DBGU_TNPR, 0x118 /* Transmit Next Pointer Register */
  .equ DBGU_TNCR, 0x11C /* Transmit Next Counter Register */
  .equ DBGU_PTCR, 0x120 /* Periph. Transfer Control Register */
  .equ DBGU_PTSR, 0x124 /* Periph. Transfer Status Register */

     
  ldr r0, =DBGU_BASE
     
  ldr r1, =0x400 
  str r1, [r0, #DBGU_MR]      
  ldr r1, =156 
  str r1, [r0, #DBGU_BRGR]      
  ldr r1, =0x50 
  str r1, [r0, #DBGU_CR] 

  ldmfd sp!, {r0, r1, pc}


LED_ON:
  stmfd sp!, {r0, r1, lr}
  ldr r0, =PIOC_BASE
  mov r1, #1 << 1
  str r1, [r0, #PIO_CODR]
  ldmfd sp!, {r0, r1, pc}
      
LED_OFF:
  stmfd sp!, {r0, r1, lr}
  ldr r0, =PIOC_BASE
  mov r1, #1 << 1
  str r1, [r0, #PIO_SODR]
  ldmfd sp!, {r0, r1, pc}


DELAY_TC0:
  stmfd sp!, {r0, r1, r2, lr}
  ldr r1, =TC0_BASE
  TC0_OLOOP:
    TC0_ILOOP:
      ldr r2, [r1, #TC_SR]
        and r2, r2, #0x10
        cmp r2,#0x10 
        bne TC0_ILOOP
         
    subs r0, r0, #1
    bne TC0_OLOOP

  ldmfd sp!, {r0, r1, r2, pc}


RCV_DMA:
  stmfd sp!, {r2-r4, lr}
     
  mov r2, r0 
  cmp r1, #80   
  movhi r1, #80
  cmp r1, #0
  movlo r1, #0
     
  ldr r3, =DBGU_BASE
  str r0, [r3, #DBGU_RPR]    
  str r1, [r3, #DBGU_RCR]    
     
  mov r2, #0b01
  ldr r4, [r3, #DBGU_PTCR]
  orr r2, r2, r4
     
  str r2, [r3, #DBGU_PTCR]     
  ldmfd sp!, {r2-r4, pc} 
     
SND_DMA:
  stmfd sp!, {r2-r4, lr}
     
  mov r2, r0  
  cmp r1, #80   
  movhi r1, #80
  cmp r1, #0
  movlo r1, #0
     
  ldr r3, =DBGU_BASE
  str r0, [r3, #DBGU_TPR]    
  str r1, [r3, #DBGU_TCR]    
     
  mov r2, #0b100000000
  ldr r4, [r3, #DBGU_PTCR]
  orr r2, r2, r4
     
  str r2, [r3, #DBGU_PTCR]       
  ldmfd sp!, {r2-r4, pc}
     
     
/* constants */

CHAR_TEST:  .ascii "--.-"
            .byte 0
            
            .align
            
STRING_TEST:  .ascii "KAJMAK"
              .byte 0

        .align
        
STRING: .ascii "SOS"
        .align
        
        .byte 0 
        
        .align        

CHARS:  .ascii ".-"   @ A 
        .byte 0,0,0,0 

        .ascii "-..." @ B 
        .byte 0,0 

        .ascii "-.-." @ C 
        .byte 0,0 

        .ascii "-.."  @ D 
        .byte 0,0,0 

        .ascii "."    @ E 
        .byte 0,0,0,0,0 

        .ascii "..-." @ F 
        .byte 0,0 

        .ascii "--."  @ G 
        .byte 0,0,0 

        .ascii "...." @ H 
        .byte 0,0 

        .ascii ".."   @ I 
        .byte 0,0,0,0 

        .ascii ".---" @ J 
        .byte 0,0 

        .ascii "-.-"  @ K 
        .byte 0,0,0 

        .ascii ".-.." @ L 
        .byte 0,0 

        .ascii "--"   @ M 
        .byte 0,0,0,0 

        .ascii "-."   @ N 
        .byte 0,0,0,0 

        .ascii "---"  @ O 
        .byte 0,0,0 

        .ascii ".--." @ P 
        .byte 0,0 

        .ascii "--.-" @ Q 
        .byte 0,0 

        .ascii ".-."  @ R 
        .byte 0,0,0 

        .ascii "..."  @ S 
        .byte 0,0,0 

        .ascii "-"    @ T 
        .byte 0,0,0,0,0 

        .ascii "..-"  @ U 
        .byte 0,0,0 

        .ascii "...-" @ V 
        .byte 0,0 

        .ascii ".--"  @ W 
        .byte 0,0,0 

        .ascii "-..-" @ X 
        .byte 0,0 

        .ascii "-.--" @ Y 
        .byte 0,0 

        .ascii "--.." @ Z 
        .byte 0,0 

_Lstack_end:
  .long __STACK_END__

.end

