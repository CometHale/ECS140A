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

(defun findSquare(row col) 
  (cond
    ((and (<= row 1)
          (<= col 1))
        1
    )
    ((and (<= row 1)
          (> col 1))
        2
    )
    ((and (> row 1)
          (<= col 1))
      `3
    )
    ((and (> row 1)
          (> col 1))
      `4
    )

  )
);find square that index belongs to

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

(defun getIndex(row col)
  (cond
    (T (+ (* row 4) col))
  )
) ;gets single index from row and col 

(defun updateRow (x val index)
  (if (= index 0)
    (cons val (updateRow (cdr x) val (- index 1))) ;update vale
    (cons (car x) (updateRow (cdr x) val (- index 1)))
  )
) ; updates a row

(defun updateBoard(x val index)
  (cond
    (T 
      (list
        (cons (updateRow (car x) val index) (updateBoard (cdr x) val index))
      )
    )
  )
)

(defun sudoku (x row col)

) ;use row and col to keep track of indices

(defun sudoku-solver (x) 
  "solves a mini 4x4 sudoku puzzle"
  (cond
    (T (updateBoard x 2 3))
  )
  
)