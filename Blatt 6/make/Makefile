PKGROOT=de
SRCDIR=src
BINDIR=bin
PKGPATH=de/uos/inf/swe
FILES=$(PKGPATH)/a/A.java $(PKGPATH)/b/B.java $(PKGPATH)/Main.java
JAR=MyProject.jar
SRC=$(addprefix $(SRCDIR)/,$(FILES))
CLS=$(addprefix $(BINDIR)/,$(FILES:java=class))
MANIFEST=MANIFEST
MAIN_CLASS=Main
DOC=doc

run: $(JAR)
	java -jar $<

doc: $(SRC)
	javadoc -d $(DOC) $(SRC)

clean:
	$(RM) -r $(JAR) $(CLS) $(BINDIR) $(MANIFEST) $(DOC)

jar: $(JAR)

$(JAR): $(CLS) $(MANIFEST)
	jar -cvmf $(MANIFEST) $(JAR) -C $(BINDIR) $(PKGROOT)

$(BINDIR)/%.class: $(SRCDIR)/%.java | $(BINDIR)
	javac -implicit:none -cp $(BINDIR) -d $(BINDIR) $<

$(MANIFEST):
	echo "Main-Class: $(PKGPATH)/$(MAIN_CLASS)" > $(MANIFEST)

$(BINDIR):
	mkdir $(BINDIR)
