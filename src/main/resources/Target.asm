 section .data
	fmtin:	db "%d",  0x0
 	fmtout:	db "%d", 0xA, 0x0
	str_1: db "Digite um numero", 10,0
	str_2: db "soma", 10,0
	str_3: db "inteiro e menor que 3", 10,0
	str_4: db "inteiro e maior que 3", 10,0
	str_5: db "ainda nao chegou no 10", 10,0

 section .bss
	inteiro: resd 1
	soma: resd 1

 section .text
	global _main
	extern _printf
	extern _scanf

_main:
 	push ebp
 	mov ebp,esp
	push dword str_1
	call _printf
	add esp, 4
	push inteiro
	push dword fmtin
	call _scanf
	add esp, 8
	mov eax,2
	mov [soma], eax
	mov eax, [inteiro]
	mov ebx, 3
	xor edx, edx
	div ebx
	mov [soma], eax
	push dword str_2
	call _printf
	add esp, 4
	mov eax, [inteiro]
	cmp eax, 3
	jge _L1
	push dword str_3
	call _printf
	add esp, 4
	jmp _L2
_L1:
	push dword str_4
	call _printf
	add esp, 4
_L2:
_L3:
	mov eax, [inteiro]
	cmp eax, 10
	jge _L4
	push dword str_5
	call _printf
	add esp, 4
	mov eax, [inteiro]
	mov ebx, 1
	add eax, ebx
	mov [inteiro], eax
	jmp _L3
_L4:

	mov esp,ebp
 	pop ebp
 	ret