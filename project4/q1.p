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
*/

is_pq(X, Prereqs, _) :- member(X, Prereqs).
is_pq(X, Prereqs, [PQH | PQTail]) :- course(PQH, PQ, _), is_pq(X, PQ, PQ). 
is_pq(X, Prereqs, [PQH | PQTail]) :- is_pq(X, PQTail, PQTail).

allprereq(Prereqs, Course) :- findall(X, (course(X, _, _), course(Course, PQ, _), is_pq(X, PQ, PQ)), Prereqs), sort(Prereqs).

/* (f)
student_teach: finds all teachers of a course (input: teachers and a course)

*/

get_teachers(CourseList, [CH | CTail]) :- member(CH, CourseList).
get_teachers(CourseList, [CH | CTail]) :- get_teachers(CourseList, CTail).

student_teach(Course, Teachers) :- findall(X, (student(Student , CurrC, _), member(Course, CurrC), instructor(X, CourseList), get_teachers(CourseList, CurrC)), Teachers), sort(Teachers).