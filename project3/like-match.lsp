(defun wild-match (x y index)

)

(defun like-match-extended (x y index)
	; index is the current index being accessed in x
	(write-line "entered like-match-extended")


	(cond

		; if index is greater than the length of x, return
		(
			(>  (+ index 1) (length x) )
			nil
		)

		; if the current char is _, append the char
		; at index in y to the list and continue recursion
		(
			(string= (char x index) #\_) 

			(append
				(list (string (char y index)))
				(like-match-extended x y (+ index 1)) 
				
			)
		)

		; if current char is % do some shit that is unknown atm
		; probably have a helper function for this
		(
			(string= (char x index) "%") 
			(write-line "found a %")
			(like-match-extended x y (+ index 1))
		)

		; if index is still less than or equal to the length of x, continue recursion
		(
			(<= (+ index 1) (length x)) 
			(like-match-extended x y (+ index 1))
		)

	)
)


(defun like-match (x y)
	
	(write-line "entered like-match")

	(cond

		; if either of the strings are empty, end
		((null x) nil)
		((null y) nil)

		; if either of the strings are longer than the other, end
		(
			(or (> (length x) (length y)) (> (length y) (length x)))
			
			(list nil nil)
		)

		; if the strings match exactly, return (t nil)
		((string= x y) (list t nil))

		(; check if there's a % or _
			(or (not (null (position #\_ x))) (not (null (position #\% x)) ))

			(list t (like-match-extended x y 0))
			
		)

		(T (list nil nil))
	)
)