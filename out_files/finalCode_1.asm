	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, absDiff, _, _
L0:
	sw ra, 0(sp)
	

# out, _, _, p
L1:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, q
L2:
	lw t0, 16(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 666
L3:
	li t0, 666
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, f
L4:
	lw t0, 4(sp)
	lw t0, 4(t0)
	addi t0 , t0, 24
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 666
L5:
	li t0, 666
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# -, p, q, $T_1
L6:
	lw t1, 12(sp)
	lw t2, 16(sp)
	sub t1, t1, t2
	sw t1, 28(sp)
	

# >=, $T_1, 0, 9
L7:
	lw t1, 28(sp)
	li t2, 0
	bge t1, t2, L9
	

# jump, _, _, 12
L8:
	j L12
	

# -, p, q, $T_2
L9:
	lw t1, 12(sp)
	lw t2, 16(sp)
	sub t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_2, _, absDiff
L10:
	lw t1, 24(sp)
	sw t1, 20(sp)
	

# jump, _, _, 14
L11:
	j L14
	

# -, q, p, $T_3
L12:
	lw t1, 16(sp)
	lw t2, 12(sp)
	sub t1, t1, t2
	sw t1, 32(sp)
	

# :=, $T_3, _, absDiff
L13:
	lw t1, 32(sp)
	sw t1, 20(sp)
	

# retv, _, _, absDiff
L14:
	lw t0, 8(sp)
	lw t1, 20(sp)
	sw t1, 0(t0)
	

# end_block, absDiff, _, _
L15:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 2)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, max, _, _
L16:
	sw ra, 0(sp)
	

# +, x, y, $T_4
L17:
	lw t1, 12(sp)
	lw t2, 16(sp)
	add t1, t1, t2
	sw t1, 32(sp)
	

# :=, $T_4, _, x1
L18:
	lw t1, 32(sp)
	sw t1, 24(sp)
	

# par, x, cv, _
L19:
	# Ignored. Call quad will handle it.
	

# par, y, cv, _
L20:
	# Ignored. Call quad will handle it.
	

# par, $T_5, ret, _
L21:
	# Ignored. Call quad will handle it.
	

# call, _, _, absDiff
L22:
	mv t3, sp
	addi sp, sp, -36
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter x ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter y ↑↑↑

	addi t0,t3, 40
	sw t0, 8(sp)
	# ret par ↑↑↑

	jal L0
	# call ↑↑↑

	addi sp, sp, 36
	# Free callee stack ↑↑↑
	

# :=, $T_5, _, x2
L23:
	lw t1, 40(sp)
	sw t1, 28(sp)
	

# out, _, _, x1
L24:
	lw t0, 24(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 555
L25:
	li t0, 555
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, a
L26:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 555
L27:
	li t0, 555
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, x2
L28:
	lw t0, 28(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, c
L29:
	lw t0, 4(sp)
	addi t0 , t0, 20
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, x1, x2, $T_6
L30:
	lw t1, 24(sp)
	lw t2, 28(sp)
	add t1, t1, t2
	sw t1, 36(sp)
	

# out, _, _, $T_6
L31:
	lw t0, 36(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, x1, x2, $T_7
L32:
	lw t1, 24(sp)
	lw t2, 28(sp)
	add t1, t1, t2
	sw t1, 48(sp)
	

# /, $T_7, c, $T_8
L33:
	lw t1, 48(sp)
	lw t0, 4(sp)
	addi t0 , t0, 20
	lw t2, 0(t0)
	div t1, t1, t2
	sw t1, 44(sp)
	

# :=, $T_8, _, max
L34:
	lw t1, 44(sp)
	sw t1, 20(sp)
	

# retv, _, _, max
L35:
	lw t0, 8(sp)
	lw t1, 20(sp)
	sw t1, 0(t0)
	

# end_block, max, _, _
L36:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, min, _, _
L37:
	sw ra, 0(sp)
	

# <, i, j, 40
L38:
	lw t1, 12(sp)
	lw t2, 16(sp)
	blt t1, t2, L40
	

# jump, _, _, 42
L39:
	j L42
	

# :=, i, _, min
L40:
	lw t1, 12(sp)
	sw t1, 20(sp)
	

# jump, _, _, 43
L41:
	j L43
	

# :=, j, _, min
L42:
	lw t1, 16(sp)
	sw t1, 20(sp)
	

# retv, _, _, min
L43:
	lw t0, 8(sp)
	lw t1, 20(sp)
	sw t1, 0(t0)
	

# end_block, min, _, _
L44:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, $$$_Main_$$$, _, _
L45:
LMain:
	addi sp, sp, -60
	

# :=, 5, _, a
L46:
	li t1, 5
	sw t1, 12(sp)
	

# :=, 13, _, b
L47:
	li t1, 13
	sw t1, 16(sp)
	

# :=, 2, _, c
L48:
	li t1, 2
	sw t1, 20(sp)
	

# :=, 30, _, f
L49:
	li t1, 30
	sw t1, 24(sp)
	

# par, a, cv, _
L50:
	# Ignored. Call quad will handle it.
	

# par, b, cv, _
L51:
	# Ignored. Call quad will handle it.
	

# par, $T_9, ret, _
L52:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L53:
	mv t3, sp
	addi sp, sp, -52
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter a ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter b ↑↑↑

	addi t0,t3, 48
	sw t0, 8(sp)
	# ret par ↑↑↑

	jal L16
	# call ↑↑↑

	addi sp, sp, 52
	# Free callee stack ↑↑↑
	

# out, _, _, $T_9
L54:
	lw t0, 48(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L55:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L56:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L57:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# /, 100, 10, $T_10
L58:
	li t1, 100
	li t2, 10
	div t1, t1, t2
	sw t1, 52(sp)
	

# +, 4, $T_10, $T_11
L59:
	li t1, 4
	lw t2, 52(sp)
	add t1, t1, t2
	sw t1, 56(sp)
	

# /, $T_11, 2, $T_12
L60:
	lw t1, 56(sp)
	li t2, 2
	div t1, t1, t2
	sw t1, 36(sp)
	

# par, $T_12, cv, _
L61:
	# Ignored. Call quad will handle it.
	

# par, 20, cv, _
L62:
	# Ignored. Call quad will handle it.
	

# par, $T_13, ret, _
L63:
	# Ignored. Call quad will handle it.
	

# call, _, _, min
L64:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_12 ↑↑↑

	li t0, 20
	sw t0, 16(sp)
	# parameter 20 ↑↑↑

	addi t0,t3, 40
	sw t0, 8(sp)
	# ret par ↑↑↑

	jal L37
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# par, 4, cv, _
L65:
	# Ignored. Call quad will handle it.
	

# par, 76, cv, _
L66:
	# Ignored. Call quad will handle it.
	

# par, $T_14, ret, _
L67:
	# Ignored. Call quad will handle it.
	

# call, _, _, min
L68:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	li t0, 4
	sw t0, 12(sp)
	# parameter 4 ↑↑↑

	li t0, 76
	sw t0, 16(sp)
	# parameter 76 ↑↑↑

	addi t0,t3, 28
	sw t0, 8(sp)
	# ret par ↑↑↑

	jal L37
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# par, $T_13, cv, _
L69:
	# Ignored. Call quad will handle it.
	

# par, $T_14, cv, _
L70:
	# Ignored. Call quad will handle it.
	

# par, $T_15, ret, _
L71:
	# Ignored. Call quad will handle it.
	

# call, _, _, min
L72:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_13 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter $T_14 ↑↑↑

	addi t0,t3, 32
	sw t0, 8(sp)
	# ret par ↑↑↑

	jal L37
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# /, $T_15, 1, $T_16
L73:
	lw t1, 32(sp)
	li t2, 1
	div t1, t1, t2
	sw t1, 44(sp)
	

# out, _, _, $T_16
L74:
	lw t0, 44(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L75:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L76:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L77:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, f
L78:
	lw t0, 24(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# halt, _, _, _
L79:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L80:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
