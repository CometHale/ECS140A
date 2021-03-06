(defun is-prime(x y)
	"checks whether or not y is a factor of x"

	(cond 
		((= x y) t)
		((/= (mod x y) 0) (is-prime x (+ y 1)))
		((= (mod x y) 0) nil)
		(T 0)
	)

)

(defun prime (x)
	"finds all of primes less than or equal to input number"

	; (is-prime x (cdr (nums x)))
	(cond 
		;check if x is less than 2, returh nil if true
		((< x 2) nil)

		 ; check if x is 2 since 2 is the least prime
		((= x 2) (list 2))

		; check if x is a prime by mod of prime numbers between 2 and sqrt(x)
		(
			(is-prime x '2) 

			(append (car (list (prime (- x 1)))) (list x))

		)

		; if is-prime returns nil, then continue with the recursion without adding
		 ; x to the lst 
		(
			(null (is-prime x '2))
			(prime (- x 1))
		)


	)

)
