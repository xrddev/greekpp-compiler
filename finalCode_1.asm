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

	sw ra, 0(sp)
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
	

# begin_block, factor, _, _
L45:
	sw ra, 0(sp)
	

# out, _, _, n
L46:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# =, n, 0, 49
L47:
	lw t1, 12(sp)
	li t2, 0
	beq t1, t2, L49
	

# jump, _, _, 51
L48:
	j L51
	

# :=, 1, _, f
L49:
	li t1, 1
	lw t0, 4(sp)
	addi t0 , t0, 24
	sw t1, 0(t0)
	

# jump, _, _, 57
L50:
	j L57
	

# -, n, 1, $T_9
L51:
	lw t1, 12(sp)
	li t2, 1
	sub t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_9, _, temp
L52:
	lw t1, 20(sp)
	sw t1, 16(sp)
	

# par, temp, cv, _
L53:
	# Ignored. Call quad will handle it.
	

# call, _, _, factor
L54:
	mv t3, sp
	addi sp, sp, -28
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter temp ↑↑↑

	sw ra, 0(sp)
	jal L45
	# call ↑↑↑

	addi sp, sp, 28
	# Free callee stack ↑↑↑
	

# *, f, n, $T_10
L55:
	lw t0, 4(sp)
	addi t0 , t0, 24
	lw t1, 0(t0)
	lw t2, 12(sp)
	mul t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_10, _, f
L56:
	lw t1, 24(sp)
	lw t0, 4(sp)
	addi t0 , t0, 24
	sw t1, 0(t0)
	

# end_block, factor, _, _
L57:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, $$$_Main_$$$, _, _
L58:
LMain:
	addi sp, sp, -60
	

# :=, 5, _, a
L59:
	li t1, 5
	sw t1, 12(sp)
	

# :=, 13, _, b
L60:
	li t1, 13
	sw t1, 16(sp)
	

# :=, 2, _, c
L61:
	li t1, 2
	sw t1, 20(sp)
	

# :=, 30, _, f
L62:
	li t1, 30
	sw t1, 24(sp)
	

# par, a, cv, _
L63:
	# Ignored. Call quad will handle it.
	

# par, b, cv, _
L64:
	# Ignored. Call quad will handle it.
	

# par, $T_11, ret, _
L65:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L66:
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

	addi t0,t3, 56
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L16
	# call ↑↑↑

	addi sp, sp, 52
	# Free callee stack ↑↑↑
	

# out, _, _, $T_11
L67:
	lw t0, 56(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L68:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L69:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L70:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# /, 100, 10, $T_12
L71:
	li t1, 100
	li t2, 10
	div t1, t1, t2
	sw t1, 36(sp)
	

# +, 4, $T_12, $T_13
L72:
	li t1, 4
	lw t2, 36(sp)
	add t1, t1, t2
	sw t1, 40(sp)
	

# /, $T_13, 2, $T_14
L73:
	lw t1, 40(sp)
	li t2, 2
	div t1, t1, t2
	sw t1, 28(sp)
	

# par, $T_14, cv, _
L74:
	# Ignored. Call quad will handle it.
	

# par, 20, cv, _
L75:
	# Ignored. Call quad will handle it.
	

# par, $T_15, ret, _
L76:
	# Ignored. Call quad will handle it.
	

# call, _, _, min
L77:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_14 ↑↑↑

	li t0, 20
	sw t0, 16(sp)
	# parameter 20 ↑↑↑

	addi t0,t3, 32
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L37
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# par, 4, cv, _
L78:
	# Ignored. Call quad will handle it.
	

# par, 76, cv, _
L79:
	# Ignored. Call quad will handle it.
	

# par, $T_16, ret, _
L80:
	# Ignored. Call quad will handle it.
	

# call, _, _, min
L81:
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

	addi t0,t3, 48
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L37
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# par, $T_15, cv, _
L82:
	# Ignored. Call quad will handle it.
	

# par, $T_16, cv, _
L83:
	# Ignored. Call quad will handle it.
	

# par, $T_17, ret, _
L84:
	# Ignored. Call quad will handle it.
	

# call, _, _, min
L85:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_15 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 48
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter $T_16 ↑↑↑

	addi t0,t3, 52
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L37
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# /, $T_17, 1, $T_18
L86:
	lw t1, 52(sp)
	li t2, 1
	div t1, t1, t2
	sw t1, 44(sp)
	

# out, _, _, $T_18
L87:
	lw t0, 44(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L88:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L89:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L90:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, f
L91:
	lw t0, 24(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# halt, _, _, _
L92:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L93:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
