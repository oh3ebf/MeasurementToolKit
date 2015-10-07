/***********************************************************
 * Software: instrument server
 * Module:   thread interface includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 26.9.2012
 *
 ***********************************************************/

#ifndef THREAD_INTEFACE_CLASS_H_
#define THREAD_INTEFACE_CLASS_H_

namespace instrument_server {

class thread_interface {
public:
	thread_interface() : _running(false) {/* empty */}
	virtual ~thread_interface() {/* empty */}

	/** Returns true if the thread was successfully started, false if there was an error starting the thread */
	bool start_internal_thread() {
		// try to start thread
		if(pthread_create(&_thread, NULL, internal_thread_entry_func, this) == 0) {
			_running = true;
		} else {
			_running = false;
		}

		// return result
		return (_running);
	}

	/** Will not return until the internal thread has exited. */
	bool wait_internal_thread_exit(void) {
		// join only when thread is ruunning
		if(_running == true) {
			(void) pthread_join(_thread, NULL);
			return(true);
		} else {
			return(false);
		}
	}

	/** return current status of thread */
	bool isRunning(void) {
		return(_running);
	}

protected:
	/** Implement this method in your subclass with the code you want your thread to run. */
	virtual void internal_thread_run() = 0;

	/** Executed from thread worker when exiting */
	void internal_thread_exit() {
		_running = false;
		pthread_exit(this);
	}
private:
	static void *internal_thread_entry_func(void *This) {((thread_interface *)This)->internal_thread_run(); return NULL;}

	pthread_t _thread;
	volatile bool _running;
};

}
#endif /* THREAD_INTEFACE_CLASS_H_ */
