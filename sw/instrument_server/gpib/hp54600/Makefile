INC      = -I../../include -I../../server -I..
LIBS     =
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC)

OBJS    =  hp54600.o hp54600_message.o
		   
SRCS     = $(OBJS:.o=.cpp)
TARGET  = libhp54600.la
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
