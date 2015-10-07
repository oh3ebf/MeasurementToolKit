/***********************************************************
 * Software: instrument server
 * Module:   string tokenizer class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 16.1.2013
 *
 ***********************************************************/

#include <string>

#include "tokenizer.h"

namespace instrument_server {

using namespace std;

const string tokenizer::DELIMITERS(" \t\n\r");

/* Function implements constructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

tokenizer::tokenizer(const string& s) : offset(0), str(s), delimiters(DELIMITERS) {

}

/* Function implements constructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

tokenizer::tokenizer(const string& s, const string& delimiters) : offset(0), str(s), delimiters(delimiters) {

}

/* Function
 *
 * Parameters:
 *
 * Returns:
 *
 */

bool tokenizer::next_token() {
	return next_token(delimiters);
}

/* Function
 *
 * Parameters:
 *
 * Returns:
 *
 */

bool tokenizer::next_token(const string& delimiters) {
	size_t i = str.find_first_not_of(delimiters, offset);

	if (string::npos == i) {
		offset = str.length();
		return false;
	}

	size_t j = str.find_first_of(delimiters, i);
	if (string::npos == j) {
		token = str.substr(i);
		offset = str.length();
		return true;
	}

	token = str.substr(i, j - i);
	offset = j;
	return true;
}

/* Function
 *
 * Parameters:
 *
 * Returns:
 *
 */

const string tokenizer::get_token() const {
	return(token);
}

/* Function
 *
 * Parameters:
 *
 * Returns:
 *
 */

void tokenizer::reset() {
	offset = 0;
}

}
