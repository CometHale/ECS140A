JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        CScanner.java \
        CToken.java \
	CParser.java \
	ObservableParser.java \
	CTranslator.java \
	CSymbol.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
