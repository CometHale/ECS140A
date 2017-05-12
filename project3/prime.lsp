(defun flatten (x)
	"flattens an input list. based on http://shrager.org/llisp/15.html"
	(cond

		((null x) nil)
		((atom x) (list x))
		(t (append (flatten (car x)) (flatten (cdr x))))
	)

)

(defun nums (x)
	"find all numbers between 2 and x"
	; to optimize, make this work from 2 to sqrt(x) instead
	; you only need to use that set of numbers to see if a number is prime
	(cond
		((= x 2) x) ;return x if x is 0
		; ((/= x 0) (cons x nums(- x 1))) ;if not cons x with x - 1
		(
			(/= x 2) 
			(flatten (list x (nums (- x 1))))
		)

		(T 0)
	)

)

(defun is-prime(x y)
	"checks whether or not x is a prime by mod x against all numbers within the given list"

	(cond 
		((null y) t)
		((/= (mod x (car y)) 0) (is-prime x (cdr y)))
		((= (mod x (car y)) 0) nil)
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
		((= x 2) 2)

		; check if x is a prime by mod of prime numbers between 2 and sqrt(x)
		(
			(is-prime x (cdr (nums x))) 

			(flatten (reverse (list x (prime (- x 1)))))
		)

		; if is-prime returns nil, then continue with the recursion without adding
		 ; x to the lst 
		(
			(null (is-prime x (cdr (nums x))) )
			(prime (- x 1))
		)

	)

)

