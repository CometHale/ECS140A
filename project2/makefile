JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        CScanner.java \
        CToken.java \
	Tree.java \
	CParser.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
