
.equ PMC_BASE,  0xFFFFFC00  /* (PMC) Base Address */
.equ CKGR_MOR,	0x20        /* (CKGR) Main Oscillator Register */
.equ CKGR_PLLAR,0x28        /* (CKGR) PLL A Register */
.equ PMC_MCKR,  0x30        /* (PMC) Master Clock Register */
.equ PMC_SR,	  0x68        /* (PMC) Status Register */

.text
.code 32

NIZ: .space 80
.align

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
      
     @ Initialize registers
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

bl DEBUG_INIT @ Initialize debug

MAIN_LOOP: @ Execute program     
     adr r0, NIZ
     add r0, r0, #0x200000
     mov r1, #3
     bl RCV_DMA
     
     ldr r3, =DBGU_BASE
     CHECK_ENDRX: 
        ldr r2, [r3, #DBGU_SR]
        tst r2, #0b1000 
        beq CHECK_ENDRX
               
     adr r0, NIZ
     add r0, r0, #0x200000
     mov r2, #3
     bl CHANGE
        
     adr r0, NIZ
     add r0, r0, #0x200000
     mov r1, #3
     bl SND_DMA
     
     ldr r3, =DBGU_BASE
     CHECK_ENDTX:
        ldr r2, [r3, #DBGU_SR]
        tst r2, #0b10000 
        beq CHECK_ENDTX
                                               
     b MAIN_LOOP


DEBUG_INIT:
     stmfd r13!, {r0-r1, lr}
     ldr r0, =DBGU_BASE
     
     ldr r1, =0x400 
     str r1, [r0, #DBGU_MR]      
     ldr r1, =156 
     str r1, [r0, #DBGU_BRGR]      
     ldr r1, =0x50 
     str r1, [r0, #DBGU_CR] 

     ldmfd r13!, {r0-r1, pc}
   
RCV_DMA:
     stmfd r13!, {r2-r4, lr}
     
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
     ldmfd r13!, {r2-r4, pc} 
     
SND_DMA:
     stmfd r13!, {r2-r4, lr}
     
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
     ldmfd r13!, {r2-r4, pc}
     
     
CHANGE:
     stmfd r13!, {r3-r4, lr}
     
     mov r3, #0
     CHANGE_LOOP:
        ldrb r4, [r0, r3]
        @sub r4, r4, #32
        eor r4, r4, #0b100000
        strb r4, [r0, r3]   
        add r3, r3, #1  
        subs r2, r2, #1
        BNE CHANGE_LOOP
     
     ldmfd r13!, {r3-r4, pc}


         
/* end user code */

_wait_for_ever:
  b _wait_for_ever

/* constants */

_Lstack_end:
  .long __STACK_END__

.end





