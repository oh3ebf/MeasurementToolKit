CC 		= gcc
LD 		= ld
AR 		= ar
RANLIB 	= ranlib
MKDIR 	= mkdir

INC 	= -I/usr/local/include -I/usr/local/gpib/include -I/mnt/disks/projects/lib/yami-3.3.1/include
LIBS 	= -lm -lpthread -lgpib -llog4cpp -lconfig4cpp -largtable2 -lyamic\
		  -L/usr/local/lib -L/usr/local/gpib/lib -L/mnt/disks/projects/lib/yami-3.3.1/lib -luuid
SUBDIRS = server gpib can #epp device
INSTALL =

# -D__DEMO__ option is used to compile version witch don't need any special hardware
# -D__DEBUG__ option is used to gerate extra information of program execution 

GLOBAL_CFLAGS = -D__LINUX__ $(INC) # -D__DEMO__
LD_FLAGS = -r 
AR_CREATE_FLAGS = rc

OBJS1    = ./server/libserver.la ./gpib/libgpib.la ./gpib/hp3488/libhp3488.la\
			./gpib/hp54600/libhp54600.la ./gpib/e4421b/libe4421b.la\
			./gpib/keithley2015/libkeithley2015.la ./gpib/hp8922/libhp8922.la\
			./can/libcan.la
			#./device/liblogic.a ./epp/libepp.a 
OBJS2	 = 
OBJS	 = $(OBJS1) $(OBJS2)
SRCS     = $(OBJS:.o=.c)
PROGRAM1 = instrument_server
PROGRAM2 = 

MAKEPARMS = CC='$(CC)' GLOBAL_CFLAGS='$(GLOBAL_CFLAGS)' GLOBAL_INC='$(INC)'\
			LD='$(LD)' LD_FLAGS='$(LD_FLAGS)' \
            AR='$(AR)' AR_CREATE_FLAGS='$(AR_CREATE_FLAGS)' \
	        RANLIB='$(RANLIB)' \
	        INSTALL='$(INSTALL)' MKDIR='$(MKDIR)'

all : subdirectories $(PROGRAM1) $(PROGRAM2)

$(PROGRAM1) : $(OBJS1)
	$(CC) -o $(PROGRAM1) $(OBJS1) $(LIBS)

#$(PROGRAM2) : $(OBJS2)
#	$(CC) -o $(PROGRAM2) $(OBJS2) $(LIBS)

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

install :
	$(MAKE)
	install $(TARGET) $(INSTALL)

uninstall:
	$(INSTALL) rm $(TARGET)

release:
	$(MAKE)
	#cp -f logic ../bin
	#cp -f hwconfig.cfg ../bin
