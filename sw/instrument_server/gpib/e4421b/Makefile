INC      = -I../../include -I../../server -I..
LIBS     =
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC)

OBJS    =  e4421b.o e4421b_message.o
		   
SRCS     = $(OBJS:.o=.cpp)
TARGET  = libe4421b.la
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
