	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, func, _, _
L0:
	sw ra, 0(sp)
	

# :=, 4, _, d
L1:
	li t1, 4
	sw t1, 16(sp)
	

# :=, B, _, A
L2:
	lw t0, 4(sp)
	lw t0, 4(t0)
	addi t0 , t0, 16
	lw t1, 0(t0)
	lw t0, 4(sp)
	lw t0, 4(t0)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# :=, a, _, b
L3:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t1, 0(t0)
	

# +, c, d, $T_1
L4:
	lw t0, 4(sp)
	addi t0 , t0, 20
	lw t1, 0(t0)
	lw t2, 16(sp)
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_1, _, func
L5:
	lw t1, 20(sp)
	sw t1, 12(sp)
	

# retv, _, _, func
L6:
	lw t0, 8(sp)
	lw t1, 12(sp)
	sw t1, 0(t0)
	

# end_block, func, _, _
L7:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 2)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, proc, _, _
L8:
	sw ra, 0(sp)
	

# :=, 3, _, c
L9:
	li t1, 3
	sw t1, 20(sp)
	

# out, _, _, A
L10:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, B
L11:
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# par, $T_2, ret, _
L12:
	# Ignored. Call quad will handle it.
	

# call, _, _, func
L13:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	addi t0,t3, 24
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L0
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# :=, $T_2, _, B
L14:
	lw t1, 24(sp)
	lw t0, 4(sp)
	addi t0 , t0, 16
	sw t1, 0(t0)
	

# out, _, _, A
L15:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, B
L16:
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# end_block, proc, _, _
L17:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, $$$_Main_$$$, _, _
L18:
LMain:
	addi sp, sp, -20
	

# :=, 1, _, A
L19:
	li t1, 1
	sw t1, 12(sp)
	

# :=, 2, _, B
L20:
	li t1, 2
	sw t1, 16(sp)
	

# par, A, cv, _
L21:
	# Ignored. Call quad will handle it.
	

# par, B, ref, _
L22:
	# Ignored. Call quad will handle it.
	

# call, _, _, proc
L23:
	mv t3, sp
	addi sp, sp, -28
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter A ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	sw t0, 16(sp)
	# parameter B ↑↑↑

	sw ra, 0(sp)
	jal L8
	# call ↑↑↑

	addi sp, sp, 28
	# Free callee stack ↑↑↑
	

# out, _, _, A
L24:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, B
L25:
	lw t0, 16(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# halt, _, _, _
L26:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L27:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
