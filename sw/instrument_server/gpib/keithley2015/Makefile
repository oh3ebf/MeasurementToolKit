INC      = -I../../include -I../../server -I..
LIBS     =
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC)

OBJS    =  keithley2015.o keithley2015_message.o
		   
SRCS     = $(OBJS:.o=.cpp)
TARGET  = libkeithley2015.la
all      : $(TARGET)

$(TARGET) : $(OBJS)
	$(LD) $(LD_FLAGS) -o $@ $(OBJS) $(LIBS)

dep :
	gccmakedep -f .depend $(GLOBAL_CFLAGS) $(INC) $(SRCS)

clean :
	rm -rf *.o $(TARGET) core *~

install :
	$(MAKE)
	install $(TARGET) $(INSTALL)

uninstall:
	cd $(INSTALL); rm $(TARGET)

include ./.depend
