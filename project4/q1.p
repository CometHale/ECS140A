fc_course(X) :- course(X, _, Units), Units > 2.

imprereq(X, Course) :- course(X, PQ, _), member(Course, PQ).

students(X, Course) :- course(X, CurrC, _), member(Course, CurrC).