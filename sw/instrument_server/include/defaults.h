/* Logic analyser software 
 * General defaults        
 * 10.3.1999               
 * Copyright Kim Kristo    /
 *
 * updated 15.9.1999 
 *  all items from logic_ui.h moved in here 
 */

#define TRUE 1
#define FALSE 0

/* pod status definitions */

#define NOT_PRESENT 0
#define INACTIVE    1
#define ACTIVE      2

#define MAXDATASIZE 100                  /* max number of bytes we can get at once */
#define MAXDATAPACKET 1024               /* max number of bytes we can send at once */
//#define LA40_READER_SEMA 0               /* logic analyser memory reader semaphore */ 

//#define FIFO_LA40 "/tmp/LA40"
//#define FIFO_DSO40 "/tmp/DSO40"

#define SERVER_PORT 1234 /* the port users will be connecting to */
#define BACKLOG 10 /* how many pending connections queue will hold */

#define SERVER_NAME "instrument"

#define SERVER_EXIT_MESSAGE "shutdown"
#define SERVER_EXIT_RESP "OKexit"

#define LA40_INIT_MESSAGE "OKpwrinitLA40"
#define LA40_HWINIT_MESSAGE "OKcnthwinitLA40"
#define LA40_CLKSEL_MESSAGE "OKClkLA40"
#define LA40_TRIG_MESSAGE "OKTriggerLA40"
#define LA40_TEST_MESSAGE "OKTestLA40"
#define LA40_TEST_ERROR_MESSAGE "FAILTestLA40"

//#define LA40_STATUSOK_MESSAGE "OKstatusLA40"


#define LA40_STATUS_ARMED_MESSAGE "OKArmedLA40"
#define LA40_STATUS_TRIGGERED_MESSAGE "OKTriggeredLA40"
#define LA40_STATUS_STOPPED_MESSAGE "OKStoppedLA40"

#define SERVER_PARMETER_ERROR_MESSAGE "FAILParameterError"
#define LA40__ERROR_MESSAGE "FAILStatusLA40"

#define LA40_STOP_MESSAGE "OKstopLA40"
#define LA40_RAM_SW_INIT_MESSAGE "OKramswinitLA40"
#define LA40_RAM_TRIG_PATTERN_MESSAGE "OKramtrigpatLA40"


#define DSO40_INIT_MESSAGE "OKinitDSO40"
#define DSO40_HWINIT_MESSAGE "OKhwinitDSO40"
#define DSO40_ARMED_MESSAGE "OKarmedDSO40"
#define DSO40_STATUS_MESSAGE "OKstatusDSO40"
#define DSO40_READ_MESSAGE "OKreadDSO40"
