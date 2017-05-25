/* (a) 
fc_course: Finds all courses that are 3 or 4 units.
*/
fc_course(X) :- findall(X, (course(X, _, Units), Units > 2), X).

/* (b) 
imprereq: Finds all immediate prerequisites for a given Course
*/
imprereq(X, Course) :- findall(X, (course(X, PQ, _), member(Course, PQ)), X).

/* (c) 
students: Finds all students currently taking a Course
*/
students(X, Course) :- findall(X, (student(X, CurrC, _), member(Course, CurrC)), X).

/* (d) 
students_prereq: finds all students who have NOT satisfied all prereqs for their current courses
check_taken: Gets current courses prereqs and sents to check_prereq (recursive)
check_prereq: Checks if the prereq is taken by the student. (True or False) (recursive)
*/
check_prereq([], _).
check_prereq([PQH | PQTail], Taken) :- member(PQH, Taken), check_prereq(PQTail, Taken).

check_taken([], _).
check_taken([CurrHead | CurrTail], Taken) :- course(CurrHead, PQ, _), check_prereq(PQ, Taken), check_taken(CurrTail, Taken).

students_prereq(Student) :- findall(Student, (student(Student, CurrC, Taken), \+check_taken(CurrC, Taken)), Student).

/* (e) 
allprereq: finds all prerequisites for a given Course (immediate and non-immediate)
is_pq: Checks if a course is a prerequisite (recursive)

is_pq(_, []).
is_pq(Course, [PQH | PQTail]) :- course(PQH, Prereqs, _), is_pq(Prereqs), is_pq(PQTail).

allprereq(Prereqs, Course) :- findall(Prereqs, (course(Course, Prereqs, _), is_pq(Course, Prereqs), Prereqs).
*/

