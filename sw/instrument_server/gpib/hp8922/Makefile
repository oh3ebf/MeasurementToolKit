INC      = -I../../include -I../../server -I..
LIBS     =
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC)

OBJS    =  hp8922.o hp8922_message.o
		   
SRCS     = $(OBJS:.o=.cpp)
TARGET  = libhp8922.la
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
