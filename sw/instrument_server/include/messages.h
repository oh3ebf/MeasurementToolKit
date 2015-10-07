/***********************************************************
 * Software: instrument server
 * Module:   yami message definitions includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.10.2012
 *
 ***********************************************************/
 
#ifndef MESSAGES_H_
#define MESSAGES_H_

#define SERVER_EXIT_MESSAGE "shutdown"

#define SERVER_CONNECT_OK_RESP "OKconnect"
#define SERVER_CONNECT_FAIL_RESP "FAILconnect"
#define SERVER_DISCONNECT_OK_RESP "OKdisconnect"
#define SERVER_DISCONNECT_FAIL_RESP "FAILdisconnect"
#define SERVER_HANDLER_MISSING_RESP "FAILmissing"

#define SERVER_COMMAND_OK_RESP "CommandOK"
#define SERVER_COMMAND_FAIL_RESP "CommandFail"

#define SERVER_DATA_OK_STATUS "DataOk"
#define SERVER_DATA_FAIL_STATUS "DataFail"

#endif
