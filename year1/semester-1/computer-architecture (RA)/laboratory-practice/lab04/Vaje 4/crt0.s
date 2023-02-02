          .section .startup

          B __start @ RESET INTERRUPT
          B __start @ UNDEFINED INSTRUCTION INTERRUPT
          B __start @ SOFTWARE INTERRUPT
          B __start @ ABORT (PREFETCH) INTERRUPT
          B __start @ ABORT (DATA) INTERRUPT
          B __start @ RESERVED
          B __start @ IRQ INTERRUPT
          B __start @ FIQ INTERRUPT
  
          .end

