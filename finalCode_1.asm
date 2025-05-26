	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, $$$_Main_$$$, _, _
L0:
LMain:
	addi sp, sp, -32
	

# in, _, _, a
L1:
	li a7, 5
	ecall
	sw a0, 12(sp)
	

# +, a, 2, $T_1
L2:
	lw t1, 12(sp)
	li t2, 2
	add t1, t1, t2
	sw t1, 28(sp)
	

# out, _, _, $T_1
L3:
	lw t0, 28(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# halt, _, _, _
L4:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L5:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
