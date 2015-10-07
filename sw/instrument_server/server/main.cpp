/***********************************************************
 * Software: instrument server
 * Module:   Main function code
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 21.9.2012
 *
 ***********************************************************/

#include <string>
#include <iostream>

#include <locale.h>
#include <signal.h>
#include <stdlib.h>
#include <stdint.h>
#include <argtable2.h>
#include <log4cpp/Category.hh>
#include "log4cpp/PropertyConfigurator.hh"
#include <config4cpp/Configuration.h>

#include "version.h"
#include "gpib_manager.h"
#include "message_manager.h"
#include "canbus_manager.h"

using namespace std;
using namespace log4cpp;
using namespace config4cpp;
using namespace instrument_server;

static void signal_handler(int signal);

static volatile bool exit_flag = true;
//static  config instrument_config;

static const char *progname = "instrument_server";
static string sw_date = "18.07.2013";
static uint32_t ver = 0x00010000; // 0000.00.00 major.minor.fix

Category *logger;
gpib_manager *gpib_handler;
message_manager *msg_handler;
canbus_manager *can_handler;

int main(int argc, char **argv) {
  int ret = 0, n = 0, exit_val = 0, max_fd = 0;
  //int i = 0, ch = 0;
  Configuration *cfg;


  struct arg_lit  *version = arg_lit0(NULL, "version", "print version information and exit\n");
  struct arg_lit  *help    = arg_lit0(NULL, "help", "print this help and exit\n");
  struct arg_end  *end = arg_end(20);
  void* argtable[] = {help, version, end};

  const char *file_log4cpp_init = "log4cpp_properties";

  try	{
    // parse logger properties
    log4cpp::PropertyConfigurator::configure(file_log4cpp_init);
  } catch( log4cpp::ConfigureFailure &e ) {
    std::cout
    << e.what()
    << " [log4cpp::ConfigureFailure catched] while reading "
    << file_log4cpp_init
    << std::endl;

    exit(1);
  }

  // get logger instance
  logger = &(Category::getInstance("server"));
  logger->debug("logger started at main");
  logger->info("Instrument server stated at : sw version: %d.%d.%d build date: %s",
		  ((0xffff0000 & ver) >> 16),
		  ((0x0000ff00 & ver) >> 8),
		  (0x000000ff & ver),
		  sw_date.c_str());

  /* verify the argtable[] entries were allocated sucessfully */
  if(arg_nullcheck(argtable) != 0) {
    /* NULL entries were detected, some allocations must have failed */
    logger->error("%s: insufficient memory", progname);

    /* deallocate each non-null entry in argtable[] */
    arg_freetable(argtable,sizeof(argtable) / sizeof(argtable[0]));
    exit(1);
  }

  /* Parse the command line as defined by argtable[] */
  ret = arg_parse(argc, argv, argtable);

  /* If the parser returned any errors then display them and exit */
  if(ret > 0) {
    /* Display the error details contained in the arg_end struct.*/
    arg_print_errors(stdout, end, progname);
    logger->error("try '%s --help' for more information.", progname);

    /* deallocate each non-null entry in argtable[] */
    arg_freetable(argtable, sizeof(argtable) / sizeof(argtable[0]));
    exit(1);
  }

  /* special case: '--help' takes precedence over error reporting */
  if(help->count > 0) {
    cout << "Usage: " << progname;
    arg_print_syntax(stdout, argtable,"\n");
    arg_print_glossary(stdout, argtable,"  %-25s %s");

    /* deallocate each non-null entry in argtable[] */
    arg_freetable(argtable, sizeof(argtable) / sizeof(argtable[0]));
    exit(1);
  }

  /* special case: '--version' takes precedence error reporting */
  if(version->count > 0) {
	  cout << TXT_ABOUT_L1 << endl;
	  cout << TXT_ABOUT_L2 << endl;
	  cout << TXT_ABOUT_L3 << endl;
	  cout << TXT_ABOUT_L4 << endl;
    //cout << "Version: " << ver << " Date: " << sw_date << ", Kim Kristo" << endl;

    /* deallocate each non-null entry in argtable[] */
    arg_freetable(argtable, sizeof(argtable) / sizeof(argtable[0]));;
    exit(1);
  }

  // set locale for config file parsing
  setlocale(LC_ALL, "");
  cfg = Configuration::create();

  try {
    cfg->parse("server.cfg");

    // initialize gpib
    if(cfg->lookupBoolean("gpib", "active")) {
      gpib_handler = new gpib_manager(cfg);

      // create devices
      logger->info("gpib configured: ");
    }

    // initialize can
    if(cfg->lookupBoolean("can","active")) {
    	can_handler = new canbus_manager(cfg);
    }

    // initialize message server
    if(cfg->lookupBoolean("server","active")) {
    	msg_handler = new message_manager(cfg);

    	// add gpib device message handlers
    	msg_handler->add_handler(gpib_handler->get_msg_list());
    	// start gpib fsm
    	gpib_handler->start_worker(msg_handler);

    	// add can device message handlers
    	msg_handler->add_handler(can_handler->get_msg_list());
    	// start can fsm
    	can_handler->start_worker(msg_handler);
    }

  }catch(const ConfigurationException & ex) {
    logger->error(ex.c_str());
  }

  /* register some signals */
    signal(SIGINT, signal_handler);
    signal(SIGHUP, signal_handler);
    signal(SIGTERM, signal_handler);
    signal(SIGQUIT, signal_handler);

    /* Main loop */
    while(exit_flag) {
    	sleep(1);
    	//log.debug("run");

    }

  // cleanup handlers
  gpib_handler->exit_thread();
  msg_handler->exit_thread();
  can_handler->exit_thread();

  // clean up configuration
  cfg->destroy();

  /* deallocate each non-null entry in argtable[] */
  arg_freetable(argtable, sizeof(argtable) / sizeof(argtable[0]));

#if 0
    /* does lock file exist? if not exit */
    log->info("remove lock file");
    if(access(LOCK_FILE, F_OK) != -1) {
      /* try to remove lock file */
      if(unlink(LOCK_FILE) == -1) {
        perror("Removing lock file");
      }
    }
#endif

  return(exit_val);
}

/* Function implements signal handler
 *
 * Parameters:
 * signal: signal from operating system
 *
 * Returns:
 *
 */

static void signal_handler(int signal) {
  /* exit only once */
  if(exit_flag == true) {
    logger->info("exiting application");
    // make main loop exit
    exit_flag = false;
  }
}



