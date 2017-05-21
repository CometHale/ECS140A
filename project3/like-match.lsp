(defun wild-match (x y index len string_list)
	; len is the number of chars after the %
	(write-line "entered wild-match")


	(cond

		;exit if % or _ are found

		;
	)

	; ;check if the substr after % in x is the same as the substr
	; ;after index(%) in y. If it isn't, return (nil nil)
	; ;if it is do wild-match

	; (cond

	; 	; if len is greater than the length(x) - index, return
	; 	(
	; 		(>  (+ len 1) (- (length x)  index))
	; 		nil
	; 	)

	; 	;check if the char in x is a _ -- stop recursing for the %
	; 	( ;may need to add something more to this
	; 		(string= (char x index) "_")
	; 		nil
	; 	)

	; 	;check if char is %, then concat a nil and keep recursing
	; 	(
	; 		(string= (char x index) "%")

	; 		(append
	; 			nil
	; 			(wild-match x y index (+ len 1))
	; 		)
	; 	)

	; 	;check if the subtring from index(%) to index + len in y fits in x at index(%)
	; 	;if it does then concat the current character in y to the 
	; 	;return string, else exit

	; 	;to check whether or not a substring fits in x, compare the substrings
	; 	;before the position of % in x and y and the substrings after. If both of those are equal
	; 	;then the substring fits.

	; 	(
	; 		(and

	; 			(string= 
	; 				; (substring x 0 (- index 1)) 

	; 			)

	; 			()

	; 		)

	; 	)
	; )

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

		;if char at index in x is not equal to char at index in y
		;and char at index in x isn't % or _, return nil nil
		(
			(and
				(and
					(string\= (char x index) "%")
					(string\= (char x index) "_")
				)
				(string\= (char x index) (char y index))
			)

			(nil nil)
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
			(wild-match x y index 0 (list nil)) ; start at the index to match
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
			(> (length x) (length y))  ; it's okay for x to be shorter than y : "h%" "hello"
			
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