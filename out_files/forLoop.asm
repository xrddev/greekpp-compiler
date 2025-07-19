	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, for_loop_default_step, _, _
L0:
	sw ra, 0(sp)
	

# :=, 1, _, i
L1:
	li t1, 1
	lw t0, 4(sp)
	addi t0 , t0, 28
	sw t1, 0(t0)
	

# >=, 1, 0, 4
L2:
	li t1, 1
	li t2, 0
	bge t1, t2, L4
	

# jump, _, _, 6
L3:
	j L6
	

# <=, i, 10, 8
L4:
	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t1, 0(t0)
	li t2, 10
	ble t1, t2, L8
	

# jump, _, _, 15
L5:
	j L15
	

# >=, i, 10, 8
L6:
	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t1, 0(t0)
	li t2, 10
	bge t1, t2, L8
	

# jump, _, _, 15
L7:
	j L15
	

# *, a, b, $T_1
L8:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 16(sp)
	

# +, a, $T_1, $T_2
L9:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t2, 16(sp)
	add t1, t1, t2
	sw t1, 12(sp)
	

# :=, $T_2, _, a
L10:
	lw t1, 12(sp)
	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# +, i, 1, $T_3
L11:
	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t1, 0(t0)
	li t2, 1
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_3, _, i
L12:
	lw t1, 20(sp)
	lw t0, 4(sp)
	addi t0 , t0, 28
	sw t1, 0(t0)
	

# >=, 1, 0, 4
L13:
	li t1, 1
	li t2, 0
	bge t1, t2, L4
	

# jump, _, _, 6
L14:
	j L6
	

# end_block, for_loop_default_step, _, _
L15:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, for_loop_positive_step, _, _
L16:
	sw ra, 0(sp)
	

# :=, 2, _, j
L17:
	li t1, 2
	lw t0, 4(sp)
	addi t0 , t0, 32
	sw t1, 0(t0)
	

# >=, 4, 0, 20
L18:
	li t1, 4
	li t2, 0
	bge t1, t2, L20
	

# jump, _, _, 22
L19:
	j L22
	

# <=, j, 30, 24
L20:
	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t1, 0(t0)
	li t2, 30
	ble t1, t2, L24
	

# jump, _, _, 31
L21:
	j L31
	

# >=, j, 30, 24
L22:
	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t1, 0(t0)
	li t2, 30
	bge t1, t2, L24
	

# jump, _, _, 31
L23:
	j L31
	

# *, a, b, $T_4
L24:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 12(sp)
	

# +, a, $T_4, $T_5
L25:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t2, 12(sp)
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_5, _, a
L26:
	lw t1, 20(sp)
	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# +, j, 4, $T_6
L27:
	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t1, 0(t0)
	li t2, 4
	add t1, t1, t2
	sw t1, 16(sp)
	

# :=, $T_6, _, j
L28:
	lw t1, 16(sp)
	lw t0, 4(sp)
	addi t0 , t0, 32
	sw t1, 0(t0)
	

# >=, 4, 0, 20
L29:
	li t1, 4
	li t2, 0
	bge t1, t2, L20
	

# jump, _, _, 22
L30:
	j L22
	

# end_block, for_loop_positive_step, _, _
L31:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, for_lοοp_negative_step, _, _
L32:
	sw ra, 0(sp)
	

# :=, 100, _, k
L33:
	li t1, 100
	lw t0, 4(sp)
	addi t0 , t0, 36
	sw t1, 0(t0)
	

# -, 0, 5, $T_7
L34:
	li t1, 0
	li t2, 5
	sub t1, t1, t2
	sw t1, 16(sp)
	

# >=, $T_7, 0, 37
L35:
	lw t1, 16(sp)
	li t2, 0
	bge t1, t2, L37
	

# jump, _, _, 39
L36:
	j L39
	

# <=, k, 20, 41
L37:
	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t1, 0(t0)
	li t2, 20
	ble t1, t2, L41
	

# jump, _, _, 48
L38:
	j L48
	

# >=, k, 20, 41
L39:
	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t1, 0(t0)
	li t2, 20
	bge t1, t2, L41
	

# jump, _, _, 48
L40:
	j L48
	

# *, a, b, $T_8
L41:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 12(sp)
	

# +, a, $T_8, $T_9
L42:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t2, 12(sp)
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_9, _, a
L43:
	lw t1, 20(sp)
	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# +, k, $T_7, $T_10
L44:
	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t1, 0(t0)
	lw t2, 16(sp)
	add t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_10, _, k
L45:
	lw t1, 24(sp)
	lw t0, 4(sp)
	addi t0 , t0, 36
	sw t1, 0(t0)
	

# >=, $T_7, 0, 37
L46:
	lw t1, 16(sp)
	li t2, 0
	bge t1, t2, L37
	

# jump, _, _, 39
L47:
	j L39
	

# end_block, for_lοοp_negative_step, _, _
L48:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, max, _, _
L49:
	sw ra, 0(sp)
	

# >, x, y, 52
L50:
	lw t1, 12(sp)
	lw t2, 16(sp)
	bgt t1, t2, L52
	

# jump, _, _, 54
L51:
	j L54
	

# :=, x, _, max
L52:
	lw t1, 12(sp)
	sw t1, 20(sp)
	

# jump, _, _, 55
L53:
	j L55
	

# :=, y, _, max
L54:
	lw t1, 16(sp)
	sw t1, 20(sp)
	

# retv, _, _, max
L55:
	lw t0, 8(sp)
	lw t1, 20(sp)
	sw t1, 0(t0)
	

# end_block, max, _, _
L56:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, for_lοοp_with_expressions, _, _
L57:
	sw ra, 0(sp)
	

# /, 12, 4, $T_11
L58:
	li t1, 12
	li t2, 4
	div t1, t1, t2
	sw t1, 16(sp)
	

# /, 30, 5, $T_12
L59:
	li t1, 30
	li t2, 5
	div t1, t1, t2
	sw t1, 36(sp)
	

# par, $T_11, cv, _
L60:
	# Ignored. Call quad will handle it.
	

# par, $T_12, cv, _
L61:
	# Ignored. Call quad will handle it.
	

# par, $T_13, ret, _
L62:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L63:
	mv t3, sp
	addi sp, sp, -24
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_11 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter $T_12 ↑↑↑

	addi t0,t3, 44
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L49
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# +, 2, 1, $T_14
L64:
	li t1, 2
	li t2, 1
	add t1, t1, t2
	sw t1, 28(sp)
	

# par, $T_14, cv, _
L65:
	# Ignored. Call quad will handle it.
	

# par, 4, cv, _
L66:
	# Ignored. Call quad will handle it.
	

# par, $T_15, ret, _
L67:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L68:
	mv t3, sp
	addi sp, sp, -24
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_14 ↑↑↑

	li t0, 4
	sw t0, 16(sp)
	# parameter 4 ↑↑↑

	addi t0,t3, 32
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L49
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# par, $T_13, cv, _
L69:
	# Ignored. Call quad will handle it.
	

# par, $T_15, cv, _
L70:
	# Ignored. Call quad will handle it.
	

# par, $T_16, ret, _
L71:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L72:
	mv t3, sp
	addi sp, sp, -24
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 44
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_13 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter $T_15 ↑↑↑

	addi t0,t3, 56
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L49
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# /, $T_16, 2, $T_17
L73:
	lw t1, 56(sp)
	li t2, 2
	div t1, t1, t2
	sw t1, 60(sp)
	

# /, $T_17, 7, $T_18
L74:
	lw t1, 60(sp)
	li t2, 7
	div t1, t1, t2
	sw t1, 48(sp)
	

# :=, $T_18, _, p
L75:
	lw t1, 48(sp)
	lw t0, 4(sp)
	addi t0 , t0, 40
	sw t1, 0(t0)
	

# *, 2, 4, $T_19
L76:
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	sw t1, 52(sp)
	

# +, 1, $T_19, $T_20
L77:
	li t1, 1
	lw t2, 52(sp)
	add t1, t1, t2
	sw t1, 24(sp)
	

# +, $T_20, 1, $T_21
L78:
	lw t1, 24(sp)
	li t2, 1
	add t1, t1, t2
	sw t1, 12(sp)
	

# /, 10, 5, $T_22
L79:
	li t1, 10
	li t2, 5
	div t1, t1, t2
	sw t1, 20(sp)
	

# >=, $T_22, 0, 82
L80:
	lw t1, 20(sp)
	li t2, 0
	bge t1, t2, L82
	

# jump, _, _, 84
L81:
	j L84
	

# <=, p, $T_21, 86
L82:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t1, 0(t0)
	lw t2, 12(sp)
	ble t1, t2, L86
	

# jump, _, _, 91
L83:
	j L91
	

# >=, p, $T_21, 86
L84:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t1, 0(t0)
	lw t2, 12(sp)
	bge t1, t2, L86
	

# jump, _, _, 91
L85:
	j L91
	

# out, _, _, p
L86:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, p, $T_22, $T_23
L87:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t1, 0(t0)
	lw t2, 20(sp)
	add t1, t1, t2
	sw t1, 40(sp)
	

# :=, $T_23, _, p
L88:
	lw t1, 40(sp)
	lw t0, 4(sp)
	addi t0 , t0, 40
	sw t1, 0(t0)
	

# >=, $T_22, 0, 82
L89:
	lw t1, 20(sp)
	li t2, 0
	bge t1, t2, L82
	

# jump, _, _, 84
L90:
	j L84
	

# end_block, for_lοοp_with_expressions, _, _
L91:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, $$$_Main_$$$, _, _
L92:
LMain:
	addi sp, sp, -44
	

# :=, 10, _, a
L93:
	li t1, 10
	sw t1, 12(sp)
	

# call, _, _, for_loop_default_step
L94:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L0
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# call, _, _, for_loop_positive_step
L95:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L16
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# call, _, _, for_lοοp_negative_step
L96:
	mv t3, sp
	addi sp, sp, -28
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L32
	# call ↑↑↑

	addi sp, sp, 28
	# Free callee stack ↑↑↑
	

# call, _, _, for_lοοp_with_expressions
L97:
	mv t3, sp
	addi sp, sp, -64
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L57
	# call ↑↑↑

	addi sp, sp, 64
	# Free callee stack ↑↑↑
	

# halt, _, _, _
L98:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L99:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
