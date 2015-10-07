INC      = -I. -I../include -Ihp3488 -Ihp54600 -Ie4421b -Ikeithley2015 -Ihp8922
LIBS     =
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC)

OBJS     =  gpib.o gpib_488_2.o gpib_manager.o message_base.o
		   
SRCS     = $(OBJS:.o=.cpp)
TARGET   = libgpib.la
SUBDIRS  = hp3488 hp54600 e4421b keithley2015 hp8922
all      : $(TARGET) subdirectories

$(TARGET) : $(OBJS)
	$(LD) $(LD_FLAGS) -o $@ $(OBJS) $(LIBS)

install :
	$(MAKE)
	install $(TARGET) $(INSTALL)

uninstall:
	cd $(INSTALL); rm $(TARGET)

subdirectories:
	rm -f *.o $(PROGRAM1) core *~
	rm -f *.o $(PROGRAM2) core *~
	@for dir in $(SUBDIRS); \
	do \
		echo ""; \
		echo "------ Building: $$dir"; \
		cd $$dir; \
		make $(MAKEPARMS) MAKEPARMS="$(MAKEPARMS)" all; \
		if [ $$? -eq 0 ] ; \
		then \
			cd ..; \
		else \
			exit 1; \
		fi \
	done

dep :
	@for dir in $(SUBDIRS); \
	do \
		echo ""; \
		echo "------ Depending: $(PROGRAM)/$$dir"; \
		cd $$dir; \
		make $(MAKEPARMS) MAKEPARMS="$(MAKEPARMS)" dep; \
		if [ $$? -eq 0 ] ; \
		then \
			cd ..; \
		else \
			exit 1; \
		fi \
	done; \

clean:
	@for dir in $(SUBDIRS); \
	do \
		echo ""; \
		echo "------ Cleaning: $(PROGRAM)/$$dir"; \
		cd $$dir; \
		make $(MAKEPARMS) MAKEPARMS="$(MAKEPARMS)" clean; \
		if [ $$? -eq 0 ] ; \
		then \
			cd ..; \
		else \
			exit 1; \
		fi \
	done; \
	rm -f *.o $(PROGRAM1) core *~ *.a	
	rm -f ./include/*~

include ./.depend
