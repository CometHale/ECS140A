(defun wildcard(str num index newstr)
	(if (and
				(= num 0)
				(string= newstr "")
			) ; if we are taking in 0 characters
		'(NIL)
		(if (> (+ index num) (length str))
			nil
			(if (= num 0)
				(list newstr)
				(wildcard str (- num 1) (+ index 1) (concatenate 'string newstr (string (char str index))))
			)
		) ; too many characters
	)
) ;take in num amount of characters starting from index

(defun ifcmp(a)
	(if a
		(list T a)
		(list NIL NIL)
	)
)

(defun appendList(li app)
	(if li
		(append li app)
		app
	) ;if list exists
)

(defun ifBackTrack(exists x y xindex yindex strings num)
	(if exists
		exists
		(like-match-extended x y xindex yindex strings (+ num 1)) ;try again
	) ;if exists
) ;if wildcard fails try again

(defun like-match-extended (x y xindex yindex strings num)
	; index is the current index being accessed in x
	(cond
		; if index is greater than the length of second string (y), return
		(
			(and 
				(= yindex (length y))
				(= xindex (length x))
			)
			strings
			 ;if no wildcard string in the making
		)

		(
			(and
				(not (= yindex (length y)))
				(= xindex (length x))
			)
			nil
		) 
		; if the current char is _, append the char
		; at index in y to the list and continue recursion
		(
			(string= (char x xindex) #\_) 
			(like-match-extended x y (+ xindex 1) (+ yindex 1) (appendList strings (list (string (char y yindex)))) 0) 
		)

		; if current char is % do some shit that is unknown atm
		; probably have a helper function for this
		(
			(string= (char x xindex) #\%) 
			(if (wildcard y num yindex "")
				(ifBackTrack 
					(like-match-extended x y (+ xindex 1) (+ yindex num) (appendList strings (wildcard y num yindex "")) 0) ; start with wildcard taking 0 characters
					x y xindex yindex strings num
				)
				nil
			) ; if we can get characters from y
		)

		; if index is still less than or equal to the length of x, continue recursion (just a regular letter)
		(
			(<= (+ yindex 1) (length y)) 
			(if (string= (char x xindex) (char y yindex))
				(like-match-extended x y (+ xindex 1) (+ yindex 1) strings 0)
				nil
			) ; if they do not match, return nil
		)

	)
)

(defun like-match (x y)
	(cond

		; if either of the strings are empty, end
		((null x) nil)
		((null y) nil)

		; if either of the strings are longer than the other, end
		( (and (> (length x) (length y))  ; it's okay for x to be shorter than y : "h%" "hello"
					 (null (position #\% x))
			)
			(list nil nil)
		)

		( (and (not (null (position #\_ x)))
					 (null (position #\% x))
					 (not (= (length x) (length y)))) ; contains underscore and no percents
			(list nil nil)
		)

		; if the strings match exactly, return (t nil)
		((string= x y) (list t nil))

		(; check if there's a % or _
			(or (not (null (position #\_ x))) (not (null (position #\% x)) ))
			(ifcmp
				(like-match-extended x y 0 0 '() 0)
			)
			
		)

		(T (list nil nil))
	)
)