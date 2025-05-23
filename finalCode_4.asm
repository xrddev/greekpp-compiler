	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, $$$_Main_$$$, _, _
L0:
LMain:
	addi sp, sp, -80
	

# :=, 1, _, i
L1:
	li t1, 1
	sw t1, 12(sp)
	

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
	lw t1, 12(sp)
	li t2, 10
	ble t1, t2, L8
	

# jump, _, _, 13
L5:
	j L13
	

# >=, i, 10, 8
L6:
	lw t1, 12(sp)
	li t2, 10
	bge t1, t2, L8
	

# jump, _, _, 13
L7:
	j L13
	

# out, _, _, i
L8:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, i, 1, $T_1
L9:
	lw t1, 12(sp)
	li t2, 1
	add t1, t1, t2
	sw t1, 44(sp)
	

# :=, $T_1, _, i
L10:
	lw t1, 44(sp)
	sw t1, 12(sp)
	

# >=, 1, 0, 4
L11:
	li t1, 1
	li t2, 0
	bge t1, t2, L4
	

# jump, _, _, 6
L12:
	j L6
	

# :=, 0, _, a
L13:
	li t1, 0
	sw t1, 20(sp)
	

# :=, 2, _, j
L14:
	li t1, 2
	sw t1, 16(sp)
	

# >=, 2, 0, 17
L15:
	li t1, 2
	li t2, 0
	bge t1, t2, L17
	

# jump, _, _, 19
L16:
	j L19
	

# <=, j, 30, 21
L17:
	lw t1, 16(sp)
	li t2, 30
	ble t1, t2, L21
	

# jump, _, _, 27
L18:
	j L27
	

# >=, j, 30, 21
L19:
	lw t1, 16(sp)
	li t2, 30
	bge t1, t2, L21
	

# jump, _, _, 27
L20:
	j L27
	

# :=, j, _, a
L21:
	lw t1, 16(sp)
	sw t1, 20(sp)
	

# out, _, _, a
L22:
	lw t0, 20(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, j, 2, $T_2
L23:
	lw t1, 16(sp)
	li t2, 2
	add t1, t1, t2
	sw t1, 36(sp)
	

# :=, $T_2, _, j
L24:
	lw t1, 36(sp)
	sw t1, 16(sp)
	

# >=, 2, 0, 17
L25:
	li t1, 2
	li t2, 0
	bge t1, t2, L17
	

# jump, _, _, 19
L26:
	j L19
	

# out, _, _, 999
L27:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L28:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L29:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# /, 10, 2, $T_3
L30:
	li t1, 10
	li t2, 2
	div t1, t1, t2
	sw t1, 56(sp)
	

# +, $T_3, 2, $T_4
L31:
	lw t1, 56(sp)
	li t2, 2
	add t1, t1, t2
	sw t1, 52(sp)
	

# *, $T_4, 7, $T_5
L32:
	lw t1, 52(sp)
	li t2, 7
	mul t1, t1, t2
	sw t1, 64(sp)
	

# out, _, _, $T_5
L33:
	lw t0, 64(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# -, 100, 50, $T_6
L34:
	li t1, 100
	li t2, 50
	sub t1, t1, t2
	sw t1, 60(sp)
	

# *, 2, 2, $T_7
L35:
	li t1, 2
	li t2, 2
	mul t1, t1, t2
	sw t1, 72(sp)
	

# +, $T_6, $T_7, $T_8
L36:
	lw t1, 60(sp)
	lw t2, 72(sp)
	add t1, t1, t2
	sw t1, 68(sp)
	

# out, _, _, $T_8
L37:
	lw t0, 68(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# :=, 5, _, j
L38:
	li t1, 5
	sw t1, 16(sp)
	

# =, j, 5, 41
L39:
	lw t1, 16(sp)
	li t2, 5
	beq t1, t2, L41
	

# jump, _, _, 49
L40:
	j L49
	

# out, _, _, 1
L41:
	li t0, 1
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# /, 10, 2, $T_9
L42:
	li t1, 10
	li t2, 2
	div t1, t1, t2
	sw t1, 76(sp)
	

# >, $T_9, 0, 45
L43:
	lw t1, 76(sp)
	li t2, 0
	bgt t1, t2, L45
	

# jump, _, _, 47
L44:
	j L47
	

# out, _, _, 100
L45:
	li t0, 100
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# jump, _, _, 48
L46:
	j L48
	

# out, _, _, 200
L47:
	li t0, 200
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# jump, _, _, 50
L48:
	j L50
	

# out, _, _, 0
L49:
	li t0, 0
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L50:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L51:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L52:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# :=, 100, _, k
L53:
	li t1, 100
	sw t1, 24(sp)
	

# -, 0, 5, $T_10
L54:
	li t1, 0
	li t2, 5
	sub t1, t1, t2
	sw t1, 28(sp)
	

# >=, $T_10, 0, 57
L55:
	lw t1, 28(sp)
	li t2, 0
	bge t1, t2, L57
	

# jump, _, _, 59
L56:
	j L59
	

# <=, k, 20, 61
L57:
	lw t1, 24(sp)
	li t2, 20
	ble t1, t2, L61
	

# jump, _, _, 66
L58:
	j L66
	

# >=, k, 20, 61
L59:
	lw t1, 24(sp)
	li t2, 20
	bge t1, t2, L61
	

# jump, _, _, 66
L60:
	j L66
	

# out, _, _, k
L61:
	lw t0, 24(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, k, $T_10, $T_11
L62:
	lw t1, 24(sp)
	lw t2, 28(sp)
	add t1, t1, t2
	sw t1, 32(sp)
	

# :=, $T_11, _, k
L63:
	lw t1, 32(sp)
	sw t1, 24(sp)
	

# >=, $T_10, 0, 57
L64:
	lw t1, 28(sp)
	li t2, 0
	bge t1, t2, L57
	

# jump, _, _, 59
L65:
	j L59
	

# out, _, _, 1199
L66:
	li t0, 1199
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 1199
L67:
	li t0, 1199
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 1199
L68:
	li t0, 1199
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# :=, 10, _, i
L69:
	li t1, 10
	sw t1, 12(sp)
	

# >=, i, 10, 72
L70:
	lw t1, 12(sp)
	li t2, 10
	bge t1, t2, L72
	

# jump, _, _, 76
L71:
	j L76
	

# -, i, 1, $T_12
L72:
	lw t1, 12(sp)
	li t2, 1
	sub t1, t1, t2
	sw t1, 40(sp)
	

# :=, $T_12, _, i
L73:
	lw t1, 40(sp)
	sw t1, 12(sp)
	

# out, _, _, i
L74:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# jump, _, _, 70
L75:
	j L70
	

# out, _, _, 777
L76:
	li t0, 777
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 777
L77:
	li t0, 777
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 777
L78:
	li t0, 777
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# :=, 0, _, i
L79:
	li t1, 0
	sw t1, 12(sp)
	

# out, _, _, 12345
L80:
	li t0, 12345
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, i, 2, $T_13
L81:
	lw t1, 12(sp)
	li t2, 2
	add t1, t1, t2
	sw t1, 48(sp)
	

# :=, $T_13, _, i
L82:
	lw t1, 48(sp)
	sw t1, 12(sp)
	

# =, i, 20, 87
L83:
	lw t1, 12(sp)
	li t2, 20
	beq t1, t2, L87
	

# jump, _, _, 85
L84:
	j L85
	

# >, i, 40, 87
L85:
	lw t1, 12(sp)
	li t2, 40
	bgt t1, t2, L87
	

# jump, _, _, 80
L86:
	j L80
	

# halt, _, _, _
L87:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L88:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
