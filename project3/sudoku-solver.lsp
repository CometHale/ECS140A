; backtrack example:
; (1 2 0 0; 0 0 0 0; 0 0 0 0; 0 0 0 4)

(defun checkAll(x index)
  (if
    (and (check (getSquare x (findSquare (getRowI index) (getColI index))))
         (check (getRow x index))
         (check (getCol x index))
    )
    t
    nil
  )
)

(defun check(x)
  (if (or (> (count 1 x) 1)
          (> (count 2 x) 1)
          (> (count 3 x) 1)
          (> (count 4 x) 1)
      )
    nil
    t
  )
) ;checks if list has same number

(defun getValue(x index)
  (nth (getColI index) (getRow x index))
)

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

(defun getRow(x index)
  "gets a specified row of the sudoku puzzle"
  (nth (getRowI index) x); ;returns nth item in list
)

(defun getCol(x index) 
  "gets a specified column of the sudoku puzzle"
  (if (null x)
    nil
    (cons (nth (getColI index) (car x))
          (getCol (cdr x) (getColI index))
    )
  )
) ; used Reference #1 on ReadMe.txt (4 Appending Lists)

(defun getColI(index)
  (mod index 4)
)

(defun getRowI(index)
  (floor index 4)
) ;gets single index from row and col 

(defun updateRow (row val index)
  (cond 
    ((and (= (list-length row) 1)
          (not (= index 0))
      )
      row
    ) ; end of row
    ((and (= (list-length row) 1)
          (= index 0)
      )
      (list val)
    )
    ((= index 0)
      (cons val (updateRow (cdr row) val (- index 1))) ;update vale
    )
    ((not (= index 0))
      (cons (car row) (updateRow (cdr row) val (- index 1)))
    )
  )
) ; updates a row

(defun updateBoard(x val index)
  (cond
    (T 
      (if (= (list-length x) 1)
            (list (updateRow (car x) val index))
            (cons  (updateRow (car x) val index)
                   (updateBoard (cdr x) val (- index 4))
            )
      )
    )
  )
)

(defun ifcomp(a x)
  (if (not a)
    (list NIL x)
    (list T a)
  )
)

(defun ifcmps(a)
  (if a
    a
    (sudoku x index (+ val 1))
  )
)

(defun sudoku (x index val)
  (cond
    ( (= index 16) 
      x
    ) ;end of board
    ( (not (= (getValue x index) 0))
      (sudoku x (+ index 1) 1)
    ) ;move on
    
    ( (= (getValue x index) 0)
      (if (> val 4)
        nil
        (if (checkAll (updateBoard x val index) index)
          (ifcmps 
            (sudoku (updateBoard x val index) (+ index 1) 1)
          )
          (sudoku x index (+ val 1))
        )
      ) ; if checked all possible insertions
    )
  )
) ;use index to keep track of indices

(defun sudoku-solver (x) 
  "solves a mini 4x4 sudoku puzzle"
  (ifcomp 
    (sudoku x 0 1)
    x
  ) ;-1 mmeans unassigned 
)