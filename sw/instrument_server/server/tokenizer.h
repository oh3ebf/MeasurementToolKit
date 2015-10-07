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

#ifndef TOKENIZER_H_
#define TOKENIZER_H_

namespace instrument_server {

using namespace std;

class tokenizer {
public:
	static const string DELIMITERS;
	tokenizer(const string& str);
	tokenizer(const string& str, const string& delimiters);
	bool next_token();
	bool next_token(const string& delimiters);
	const string get_token() const;
	void reset();
protected:
	size_t offset;
	const string str;
	string token;
	string delimiters;
private:
};
}
#endif /* TOKENIZER_H_ */
