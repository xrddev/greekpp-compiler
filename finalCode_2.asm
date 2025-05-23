	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, sub, _, _
L0:
	sw ra, 0(sp)
	

# out, _, _, 300
L1:
	li t0, 300
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, n1
L2:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, n2
L3:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, result
L4:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# -, n1, n2, $T_1
L5:
	lw t0, 12(sp)
	lw t1, 0(t0)
	lw t0, 16(sp)
	lw t2, 0(t0)
	sub t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_1, _, result
L6:
	lw t1, 24(sp)
	lw t0, 20(sp)
	sw t1, 0(t0)
	

# :=, 11, _, n1
L7:
	li t1, 11
	lw t0, 12(sp)
	sw t1, 0(t0)
	

# :=, 12, _, n2
L8:
	li t1, 12
	lw t0, 16(sp)
	sw t1, 0(t0)
	

# end_block, sub, _, _
L9:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, sub_2, _, _
L10:
	sw ra, 0(sp)
	

# out, _, _, 300
L11:
	li t0, 300
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, s1
L12:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, s2
L13:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, s3
L14:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# -, s1, s2, $T_2
L15:
	lw t0, 12(sp)
	lw t1, 0(t0)
	lw t0, 16(sp)
	lw t2, 0(t0)
	sub t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_2, _, s3
L16:
	lw t1, 24(sp)
	lw t0, 20(sp)
	sw t1, 0(t0)
	

# out, _, _, s1
L17:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, s2
L18:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, s3
L19:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 300
L20:
	li t0, 300
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# end_block, sub_2, _, _
L21:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 2)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, add, _, _
L22:
	sw ra, 0(sp)
	

# out, _, _, 200
L23:
	li t0, 200
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, n1
L24:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, n2
L25:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, result
L26:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# -, 0, n2, $T_3
L27:
	li t1, 0
	lw t0, 16(sp)
	lw t2, 0(t0)
	sub t1, t1, t2
	sw t1, 28(sp)
	

# :=, $T_3, _, n2
L28:
	lw t1, 28(sp)
	lw t0, 16(sp)
	sw t1, 0(t0)
	

# par, n1, ref, _
L29:
	# Ignored. Call quad will handle it.
	

# par, n2, ref, _
L30:
	# Ignored. Call quad will handle it.
	

# par, result, ref, _
L31:
	# Ignored. Call quad will handle it.
	

# call, _, _, sub_2
L32:
	mv t3, sp
	addi sp, sp, -28
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter n1 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter n2 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 20
	lw t0, 0(t0)
	sw t0, 20(sp)
	# parameter result ↑↑↑

	sw ra, 0(sp)
	jal L10
	# call ↑↑↑

	addi sp, sp, 28
	# Free callee stack ↑↑↑
	

# out, _, _, n1
L33:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, n2
L34:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, result
L35:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 200
L36:
	li t0, 200
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# end_block, add, _, _
L37:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 2)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, add_and_mul, _, _
L38:
	sw ra, 0(sp)
	

# out, _, _, 100
L39:
	li t0, 100
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, m1
L40:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, m2
L41:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, m3
L42:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# par, m1, ref, _
L43:
	# Ignored. Call quad will handle it.
	

# par, m2, ref, _
L44:
	# Ignored. Call quad will handle it.
	

# par, m1, ref, _
L45:
	# Ignored. Call quad will handle it.
	

# call, _, _, add
L46:
	mv t3, sp
	addi sp, sp, -32
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter m1 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter m2 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t0, 0(t0)
	sw t0, 20(sp)
	# parameter m1 ↑↑↑

	sw ra, 0(sp)
	jal L22
	# call ↑↑↑

	addi sp, sp, 32
	# Free callee stack ↑↑↑
	

# out, _, _, m1
L47:
	lw t0, 12(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, m2
L48:
	lw t0, 16(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, m3
L49:
	lw t0, 20(sp)
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# *, m1, 2, $T_4
L50:
	lw t0, 12(sp)
	lw t1, 0(t0)
	li t2, 2
	mul t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_4, _, m2
L51:
	lw t1, 24(sp)
	lw t0, 16(sp)
	sw t1, 0(t0)
	

# *, m1, m2, $T_5
L52:
	lw t0, 12(sp)
	lw t1, 0(t0)
	lw t0, 16(sp)
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 28(sp)
	

# :=, $T_5, _, m3
L53:
	lw t1, 28(sp)
	lw t0, 20(sp)
	sw t1, 0(t0)
	

# out, _, _, 100
L54:
	li t0, 100
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# end_block, add_and_mul, _, _
L55:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, $$$_Main_$$$, _, _
L56:
LMain:
	addi sp, sp, -24
	

# :=, 3, _, a
L57:
	li t1, 3
	sw t1, 12(sp)
	

# :=, 7, _, b
L58:
	li t1, 7
	sw t1, 16(sp)
	

# :=, 98, _, c
L59:
	li t1, 98
	sw t1, 20(sp)
	

# out, _, _, a
L60:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, b
L61:
	lw t0, 16(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, c
L62:
	lw t0, 20(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# par, a, ref, _
L63:
	# Ignored. Call quad will handle it.
	

# par, b, ref, _
L64:
	# Ignored. Call quad will handle it.
	

# par, c, ref, _
L65:
	# Ignored. Call quad will handle it.
	

# call, _, _, sub
L66:
	mv t3, sp
	addi sp, sp, -28
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t0, 12(sp)
	# parameter a ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	sw t0, 16(sp)
	# parameter b ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 20
	sw t0, 20(sp)
	# parameter c ↑↑↑

	sw ra, 0(sp)
	jal L0
	# call ↑↑↑

	addi sp, sp, 28
	# Free callee stack ↑↑↑
	

# out, _, _, a
L67:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, b
L68:
	lw t0, 16(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, c
L69:
	lw t0, 20(sp)
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
	

# out, _, _, 999
L71:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, 999
L72:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# par, a, ref, _
L73:
	# Ignored. Call quad will handle it.
	

# par, b, ref, _
L74:
	# Ignored. Call quad will handle it.
	

# par, c, ref, _
L75:
	# Ignored. Call quad will handle it.
	

# call, _, _, add_and_mul
L76:
	mv t3, sp
	addi sp, sp, -32
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t0, 12(sp)
	# parameter a ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	sw t0, 16(sp)
	# parameter b ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 20
	sw t0, 20(sp)
	# parameter c ↑↑↑

	sw ra, 0(sp)
	jal L38
	# call ↑↑↑

	addi sp, sp, 32
	# Free callee stack ↑↑↑
	

# out, _, _, 999
L77:
	li t0, 999
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, a
L78:
	lw t0, 12(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, b
L79:
	lw t0, 16(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# out, _, _, c
L80:
	lw t0, 20(sp)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# halt, _, _, _
L81:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L82:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
