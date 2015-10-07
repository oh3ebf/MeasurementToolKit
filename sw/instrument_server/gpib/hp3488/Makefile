INC      = -I../../include -I..
LIBS     =
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC)

OBJS    =  hp3488.o hp3488_message.o hp3488_option_base.o hp44470.o\
		   hp44471.o hp44472.o hp44473.o hp44474.o hp44475.o hp44476.o hp44477.o\
		   hp44478.o
		   
SRCS     = $(OBJS:.o=.cpp)
TARGET  = libhp3488.la
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
