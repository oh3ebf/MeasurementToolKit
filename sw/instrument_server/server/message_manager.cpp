/***********************************************************
 * Software: instrument server
 * Module:   yami message manager class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 9.10.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <unistd.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include <config4cpp/Configuration.h>

#include "yami++.h"
#include "message_manager.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

message_manager::message_manager(Configuration *cfg) : fsm_worker_exit(true), log(Category::getInstance("message")),
		server_port(12345), server_name(""), status_timer(100) {

	try {
		// get server parameters
		server_name = cfg->lookupString("server", "name");
		server_port = cfg->lookupInt("server", "port");
	} catch(const ConfigurationException & ex) {
		log.error(ex.c_str());
	}

	/* start message worker */
	if(!this->start_internal_thread()) {
		log.error("failed to start message handling thread");
	}
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

message_manager::~message_manager() {

}

/* Function forces worker thread exit
 *
 * Parameters:
 *
 * Returns:
 *
 */

void message_manager::exit_thread(void) {
	fsm_worker_exit = false;
	this->wait_internal_thread_exit();
}

/* Function adds message handling functions
 *
 * Parameters:
 * handler: map of device handlers
 *
 * Returns:
 *
 */

void message_manager::add_handler(map<string, message_interface *>& handler) {
	msg_handlers.insert(handler.begin(), handler.end());
}

/* Function sends data message to client
 *
 * Parameters:
 * ip: client ip address
 * msg: message name
 * p: message parameters
 *
 * Returns:
 *
 */

void message_manager::send_data(string ip, string msg, ParamSet p) {
	 std::auto_ptr<Message> m;
	// any clients connected
	if(!clients.empty()) {
		// get domain name if exist
		if(clients.find(ip) != clients.end()) {
			// get object by clients ip address
			m = clients[ip]->stream_server.send(clients[ip]->domain, clients[ip]->object, msg, p);
		}
	}
}

/* Function returns reference to client domain
 *
 * Parameters:
 * ip: ip address of client
 *
 * Returns:
 * domain as pointer
 *
 */

string* message_manager::get_domain(string ip) {
	// any clients connected
	if(!clients.empty()) {
		// get domain name if exist
		if(clients.find(ip) == clients.end()) {
			return(&clients[ip]->domain);
		}
	}

	return(NULL);
}

/* Function implements thread worker
 *
 * Parameters:
 *
 * Returns:
 *
 */

void message_manager::internal_thread_run(void) {
	string msg_name, target_device;
	auto_ptr<IncomingMsg> msg_in;
	auto_ptr<ParamSet> params;
	bool msg_handled = false;

	log.info("message handling thread started");

	netInitialize();

	// create server object
	msg_server = new Agent(server_port);
	//msg_client = new Agent();
	msg_server->objectRegister(server_name.c_str(), Agent::ePolling, NULL);

	// message handling loop
	while(fsm_worker_exit) {
		// check if any messages available
		if(msg_server->getIncomingCount(server_name.c_str()) > 0) {
			// not handled yet
			msg_handled = false;

			// read incoming messages
			msg_in = msg_server->getIncoming(server_name.c_str(), false);
			msg_name = msg_in->getMsgName();

			// is message shutdown request
			if((msg_name == "Shutdown") && (msg_handled == false)) {
				log.info("received shutdown request");
				// exit this worker
				fsm_worker_exit = false;
				msg_handled = true;
			}

			// client connect message
			if((msg_name == "Connect") && (msg_handled == false)) {
				/* default response message */
				ParamSet return_params(1);
				return_params.setString(0, SERVER_CONNECT_FAIL_RESP);

				string addr = msg_in->getSourceAddr();
				log.info("connect received from %s", addr.c_str());

				// get parameters
				params = msg_in->getParameters();
				// check for malformatted message
				if(params.get() == NULL) {
					log.error("no parameters in connect message from %s", msg_in->getSourceAddr().c_str());
				} else {
					// check size of available parameters, least target and command
					if(params->getParamCount() == 4) {
						string data_server, data_obj, data_ip;
						int data_port;
						ClientStruct *client = new ClientStruct();

						// get device name
						params->getString(0, data_server);
						params->getString(1, data_obj);
						params->getString(2, data_ip);
						data_port = params->getInt(3);

						// register to client domain
						//msg_server->domainRegister(data_server.c_str(), data_ip.c_str(), data_port, 2);

						// add client to listeners
						client->domain = data_server;
						client->object = data_obj;
						client->ip = data_ip;
						client->port = data_port;
						client->stream_server.domainRegister(data_server.c_str(),
								data_ip.c_str(), data_port, Agent::eLevel2, Agent::eFixedDuplex);
						clients.insert(pair<string, ClientStruct *>(addr, client));

						log.debug("data stream registered to %s domain at %s:%d", data_server.c_str(), data_ip.c_str(), data_port);
						return_params.setString(0, SERVER_CONNECT_OK_RESP);
					}
				}

				// send reply to client
				msg_in->reply(return_params);
				msg_handled = true;
			}

			// client disconnect message
			if((msg_name == "Disconnect") && (msg_handled == false)) {
				// default response string
				ParamSet return_params(1);
				return_params.setString(0, SERVER_DISCONNECT_FAIL_RESP);

				string addr = msg_in->getSourceAddr();
				log.info("disconnect received from %s", addr.c_str());

				// check if clients are registered
				if(!clients.empty()) {
					// get iterator
					map<string, ClientStruct *>::iterator i;
					// loop all items
					for(i = clients.begin(); i != clients.end(); ++i) {
						// find and removed client
						if((*i).first == addr) {
							log.info("client %s disconnected", (*i).first.c_str());
							// any handlers registered
							if(!msg_handlers.empty()) {
								// get iterator
								map<string, message_interface *>::iterator dev_iterator;

								// loop all handlers
								for(dev_iterator = msg_handlers.begin(); dev_iterator != msg_handlers.end(); ++dev_iterator) {
									// delete data stream command for this client
									(*dev_iterator).second->disconnect((*i).first);
								}
							}

							// remove allocated space and remove client from map
							delete((*i).second);
							clients.erase(i);
							return_params.setString(0, SERVER_DISCONNECT_OK_RESP);
							break;
						}
					}
				} else {
					log.warn("disconnect from client not connected: %s", addr.c_str());
				}

				// send reply to client
				msg_in->reply(return_params);
				msg_handled = true;
			}

			// server configuration request message
			if((msg_name == "ServerConfigGet") && (msg_handled == false)) {
				log.info("server configuration request");
				// any handlers registered
				if(!msg_handlers.empty()) {
					// temporary space for config string
					vector<string> *server_config = new vector<string>();

					// get iterator
					map<string, message_interface *>::iterator dev_iterator;

					// loop all handlers
					for(dev_iterator = msg_handlers.begin(); dev_iterator != msg_handlers.end(); ++dev_iterator) {
						// get configuration info
						map<string, string> *config = (*dev_iterator).second->get_config();

						if(!config->empty()) {
							// get iterator
							map<string, string>::iterator dev_data_iterator;

							// loop all properties
							for(dev_data_iterator = config->begin(); dev_data_iterator != config->end(); ++dev_data_iterator) {
								log.debug("Key %s Parameters %s", (*dev_data_iterator).first.c_str(), (*dev_data_iterator).second.c_str());
								// save config string
								server_config->push_back((*dev_data_iterator).second);
							}
						}
						// remove allocated space
						delete(config);
					}

					// allocate response message
					ParamSet return_params(server_config->size());
					// parameter index
					uint32_t index = 0;
					// get iterator
					vector<string>::iterator config_iterator;

					// loop all configuration data
					for(config_iterator = server_config->begin(); config_iterator != server_config->end(); ++config_iterator) {
						// add data to response message
						return_params.setString(index, (*config_iterator));
						index++;
					}

					// send reply message
					msg_in->reply(return_params);
					// remove allocated space
					delete(server_config);
				} else {
					ParamSet return_params(1);
					// send reply to client
					return_params.setString(0, SERVER_HANDLER_MISSING_RESP);
					msg_in->reply(return_params);
				}

				msg_handled = true;
			}

			// is message handled
			if(msg_handled == false) {
				// check if message handler requested is available
				if(msg_handlers.find(msg_name) == msg_handlers.end()) {
					ParamSet return_params(1);

					log.warn("handler for %s message not found", msg_name.c_str());

					// send reply to client
					return_params.setString(0, SERVER_HANDLER_MISSING_RESP);
					msg_in->reply(return_params);
					msg_handled = true;
				} else {
					// get parameters
					params = msg_in->getParameters();
					// check for badly formatted message
					if(params.get() == NULL) {
						log.error("no parameters in gpib message from %s", msg_in->getSourceAddr().c_str());
					} else {
							log.debug("Calling %s message handler", msg_name.c_str());
							// call device handler
							msg_handlers[msg_name]->handle_msg(msg_in);
						//}
					}
					msg_handled = true;
				}
			}
		}

		// send status message on interval
		status_timer--;
		if(status_timer == 0) {
			status_timer = 100;
			server_status();
		}

		// run in 10ms loop
		usleep(10000);
	}

	netCleanup();

	log.debug("exiting message handling thread");
	this->internal_thread_exit();
}

/* Function sends server status message to clients
 *
 * Parameters:
 *
 * Returns:
 *
 */

void message_manager::server_status(void) {
	if(!clients.empty()) {
		// get iterator
		map<string, ClientStruct *>::iterator i;
		// loop all items
		for(i = clients.begin(); i != clients.end(); ++i) {
			//TODO status viestin lähetys
			clients[((*i).first)]->stream_server.send(((*i).second)->domain.c_str(), ((*i).second)->object.c_str(), "server status");
			//msg_server->sendOneWay(((*i).second)->domain.c_str(), ((*i).second)->object.c_str(), "server status");
			//clients[ip]->stream_server.send(clients[ip]->domain, clients[ip]->object, msg, p);
			log.debug("message sent");
		}
	}
}
}
