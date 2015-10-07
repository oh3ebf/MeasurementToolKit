INC      = -I../include -I../device -I../epp -I../gpib -I../can
LIBS     = 
CPPFLAGS   = -O -Wall $(GLOBAL_CFLAGS) $(INC) $(GLOBAL_INC)

OBJS    = canbus_manager.o can_message.o can_buffer.o can_base.o lincan.o
SRCS     = $(OBJS:.o=.cpp)
TARGET  = libcan.la
all      : $(TARGET)

$(TARGET) : $(OBJS)
	$(LD) $(LD_FLAGS) -o $(TARGET) $(OBJS)

dep :
	gccmakedep -f .depend $(GLOBAL_CFLAGS) $(INC) $(GLOBAL_INC) $(SRCS)

clean :
	rm -rf *.o $(TARGET) core *~

include ./.depend

