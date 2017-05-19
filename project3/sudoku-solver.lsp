; backtrack example:
; (1 2 0 0; 0 0 0 0; 0 0 0 0; 0 0 0 4)

(defun check(x)
  (if (= (list-length (remove-duplicates x)) 4)
    t
    nil
  )
) ;checks if list has same number

(defun getSquare(x num)
  "gets a 2x2 square of the sudoku puzzle"
  (cond
    ((= num 1) 
      (list 
        (nth 0 (car x))
        (nth 1 (car x))
        (nth 0 (nth 1 x))
        (nth 1 (nth 1 x))
      )
    ) ; top left square
    ((= num 2)
      (list
        (nth 2 (car x))
        (nth 3 (car x))
        (nth 2 (nth 1 x))
        (nth 3 (nth 1 x))
      )
    ) ; top right square
    ((= num 3)
      (list
        (nth 0 (nth 2 x))
        (nth 1 (nth 2 x))
        (nth 0 (nth 3 x))
        (nth 1 (nth 3 x))
      )
    ) ; bottom left square
    ((= num 4)
      (list 
        (nth 2 (nth 2 x))
        (nth 3 (nth 2 x))
        (nth 2 (nth 3 x))
        (nth 3 (nth 3 x))
      )
    ) ; bottom right square
  )
)


(defun getRow(x row)
  "gets a specified row of the sudoku puzzle"
  (cond
    (T (nth row x)); ;returns nth item in list
  )
)

(defun getCol(x col) 
  "gets a specified column of the sudoku puzzle"
  (if (null x)
    nil
    (cons (nth col (car x))
          (getCol (cdr x) col)
    )
  )
) ; used Reference #1 on ReadMe.txt (4 Appending Lists)

(defun sudoku-solver (x) 
  "solves a mini 4x4 sudoku puzzle"
  (cond 
   (T (getCol x 1))
  )
  




)