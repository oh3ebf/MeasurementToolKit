//
// yami++.cpp
//
// definition of all the YAMI C++ wrappers
//
// Copyright (C) 2001-2008 Maciej Sobczak
// http://www.msobczak.com/prog/yami/
//
// Permission to copy, use, modify, sell and distribute this software
// is granted provided this copyright notice appears in all copies.
// This software is provided "as is" without express or implied
// warranty, and with no claim as to its suitability for any purpose.
//

#include "yami++.h"

#include "yamic.h"
#include "yamiparams.h"
#include "yamierrors.h"
#include "yamisynchro.h"

#include <assert.h>

using namespace YAMI;


//
// exceptions
//

LogicException::LogicException(int cc)
     : std::logic_error(std::string(yamiErrorString(cc))),
     cc_(cc)
{
}

int LogicException::getErrorCode() const
{
     return cc_;
}

RunTimeException::RunTimeException(int cc)
     : std::runtime_error(std::string(yamiErrorString(cc))),
     cc_(cc)
{
}

int RunTimeException::getErrorCode() const
{
     return cc_;
}

BadAddress::BadAddress(int cc)
     : RunTimeException(cc)
{
}

Overflow::Overflow()
     : RunTimeException(YAMIOVERFLOW)
{
}

OSError::OSError(int cc)
     : RunTimeException(cc)
{
}

InternalException::InternalException()
     : RunTimeException(YAMIINTERNAL)
{
}

// helper for translating YAMI C errors into C++ exceptions

namespace
{
     void _yamiThrow(int cc)
     {
          switch (cc)
          {
          case YAMIBADHANDLE:
          case YAMIOUTOFRANGE:
          case YAMICANNOTINITIALIZETWICE:
          case YAMIBADTYPE:
          case YAMINOTINITIALIZED:
          case YAMIALREADYREGISTERED:
          case YAMINOTFOUND:
          case YAMIQUEUEEMPTY:
          case YAMIPARSEERROR:
          case YAMICANNOTPROCESSTWICE:
          case YAMIBADLENGTH:
          case YAMINOSUCHSERVICE:
               throw LogicException(cc);
          case YAMINOMEMORY:
               throw std::bad_alloc();
          case YAMICANNOTRESOLVEADDRESS:
          case YAMIBADADDRTYPE:
               throw BadAddress(cc);
          case YAMIOVERFLOW:
               throw Overflow();
          case YAMITHREADERROR:
          case YAMISYNCHROERROR:
          case YAMINETERROR:
               throw OSError(cc);
          case YAMIINTERNAL:
               throw InternalException();
          default:
               // unknown error code or error that should
               // not appear in the wrappers
               // - serious bug, please report
               assert(0);
          }
     }
}

//
// parameter set
//

// constructor/destructor

ParamSet::ParamSet(int parcount)
{
     int cc;
     
     cc = yamiCreateParamSet(&h_, parcount);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     owner_ = true;
}

ParamSet::ParamSet(void *handle, bool owner)
     : h_(handle), owner_(owner)
{
}

ParamSet::~ParamSet()
{
     // ignore the errors

     if (h_ != NULL && owner_ == true)     
          yamiDestroyParamSet(h_);
}

// copy

ParamSet::ParamSet(const ParamSet &ps)
{
     int cc;
     
     cc = yamiCopyParamSet(&h_, ps.h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
          
     owner_ = true;
}

ParamSet & ParamSet::operator=(const ParamSet &ps)
{
     ParamSet tmp(ps);
     swap(tmp);

     return *this;
}

void ParamSet::copyParameter(int positiondst,
          const ParamSet &ps, int positionsrc)
{
     int cc;
     
     cc = yamiCopyParameter(h_, positiondst, ps.h_, positionsrc);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::clear(int position)
{
     int cc;

     cc = yamiClearParameter(h_, position);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

// accessors

// setters

void ParamSet::setInt(int position, int value)
{
     int cc;
     
     cc = yamiSetInt(h_, position, value);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setByte(int position, char value)
{
     int cc;
     
     cc = yamiSetByte(h_, position, value);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setDouble(int position, double value)
{
     int cc;
     
     cc = yamiSetDouble(h_, position, value);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setBinary(int position, const char *buf, int bufsize)
{
     int cc;
     
     cc = yamiSetBinary(h_, position, buf, bufsize);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setBinaryShallow(int position, const char *buf, int bufsize)
{
     int cc;
     
     cc = yamiSetBinaryShallow(h_, position, buf, bufsize);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setString(int position, const char *str)
{
     int cc;

     cc = yamiSetString(h_, position, str);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setString(int position, const std::string &str)
{
     setString(position, str.c_str());
}

void ParamSet::setStringShallow(int position, const char *str)
{
     int cc;

     cc = yamiSetStringShallow(h_, position, str);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void ParamSet::setWString(int position, const wchar_t *wstr)
{
     int cc;
     
     cc = yamiSetWString(h_, position, wstr);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

#ifndef YAMI_NO_WSTRING
void ParamSet::setWString(int position, const std::wstring &wstr)
{
     setWString(position, wstr.c_str());
}
#endif

void ParamSet::setWStringShallow(int position, const wchar_t *wstr)
{
     int cc;
     
     cc = yamiSetWStringShallow(h_, position, wstr);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

// getters

int ParamSet::getParamCount() const
{
     int cc, count;
     
     cc = yamiGetParamCount(h_, &count);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return count;
}

ParamSet::eType ParamSet::getType(int position) const
{
     enum paramType type;
     int cc;
     
     cc = yamiGetType(h_, position, &type);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return static_cast<enum eType>(static_cast<int>(type));
}

bool ParamSet::isASCII(int position) const
{
     int isascii, cc;

     cc = yamiIsASCIIString(h_, position, &isascii);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return (isascii != 0);
}

int ParamSet::getInt(int position) const
{
     int value;
     int cc;
     
     cc = yamiGetInt(h_, position, &value);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return value;
}

char ParamSet::getByte(int position) const
{
     char value;
     int cc;
     
     cc = yamiGetByte(h_, position, &value);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return value;
}

double ParamSet::getDouble(int position) const
{
     double value;
     int cc;
     
     cc = yamiGetDouble(h_, position, &value);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return value;
}

int ParamSet::getBinarySize(int position) const
{
     int size;
     int cc;
     
     cc = yamiGetBinarySize(h_, position, &size);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return size;
}

void ParamSet::getBinaryValue(int position, char *buf) const
{
     int cc;
     
     cc = yamiGetBinaryValue(h_, position, buf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

const char * ParamSet::getBinaryBuffer(int position) const
{
     const char *buf;
     int cc;
     
     cc = yamiGetBinaryBuffer(h_, position, &buf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return buf;
}

int ParamSet::getStringLength(int position) const
{
     int length;
     int cc;
     
     cc = yamiGetStringLength(h_, position, &length);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return length;
}

void ParamSet::getStringValue(int position, char *buf) const
{
     int cc;
     
     cc = yamiGetStringValue(h_, position, buf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

const char * ParamSet::getStringBuffer(int position) const
{
     const char *buf;
     int cc;
     
     cc = yamiGetStringBuffer(h_, position, &buf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return buf;
}

void ParamSet::getString(int position, std::string &str) const
{
     str = getStringBuffer(position);
}

int ParamSet::getWStringLength(int position) const
{
     int length;
     int cc;
     
     cc = yamiGetWStringLength(h_, position, &length);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return length;
}

void ParamSet::getWStringValue(int position, wchar_t *buf) const
{
     int cc;
     
     cc = yamiGetWStringValue(h_, position, buf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

const wchar_t * ParamSet::getWStringBuffer(int position) const
{
     const wchar_t *buf;
     int cc;
     
     cc = yamiGetWStringBuffer(h_, position, &buf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return buf;
}

#ifndef YAMI_NO_WSTRING
void ParamSet::getWString(int position, std::wstring &wstr) const
{
     wstr = getWStringBuffer(position);
}
#endif

// handle operations

void * ParamSet::getHandle() const
{
     return h_;
}

void * ParamSet::resetHandle(void *handle, bool owner)
{
     void *tmp = h_;
     h_ = handle;
     owner_ = owner;
     
     return tmp;
}

void ParamSet::swap(ParamSet &ps)
{
     bool thatowner = ps.owner_;
     void *thathandle = ps.h_;
     
     ps.resetHandle(h_, owner_);
     resetHandle(thathandle, thatowner);
}


//
// agent
//

//
// incoming message wrapper
//

// constructor/destructor

IncomingMsg::IncomingMsg(void *handle, bool owner)
     : h_(handle), owner_(owner)
{
}

IncomingMsg::~IncomingMsg()
{
     // ignore the errors
     
     if (h_ != NULL && owner_ == true)
          yamiAgentDestroyIncomingMsg(h_);
}

std::string IncomingMsg::getMsgName() const
{
     const char *str;
     int cc;
     
     cc = yamiAgentIncomingMsgGetMsgName(h_, &str);
     if (cc != YAMIOK)
          _yamiThrow(cc);
          
     return std::string(str);
}

std::string IncomingMsg::getObjectName() const
{
     const char *str;
     int cc;
     
     cc = yamiAgentIncomingMsgGetObjectName(h_, &str);
     if (cc != YAMIOK)
          _yamiThrow(cc);
          
     return std::string(str);
}

std::auto_ptr<ParamSet> IncomingMsg::getParameters() const
{
     HPARAMSET hparset;
     
     int cc = yamiAgentIncomingMsgGetParameters(h_, &hparset);
     if (cc == YAMINOTAVAILABLE)
          return std::auto_ptr<ParamSet>((ParamSet*)NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return std::auto_ptr<ParamSet>(new ParamSet(hparset, true));
}

int IncomingMsg::getLevel() const
{
     int levelno;
     int cc;
     
     cc = yamiAgentIncomingMsgGetLevel(h_, &levelno);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return levelno;
}

std::string IncomingMsg::getSourceAddr() const
{
     char addrbuf[15];
     int cc;
     
     cc = yamiAgentIncomingMsgGetSourceAddr(h_, addrbuf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return std::string(addrbuf);
}

int IncomingMsg::getSourcePort() const
{
     int port;
     int cc;

     cc = yamiAgentIncomingMsgGetSourcePort(h_, &port);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return port;
}

std::string IncomingMsg::getEphemericAddr() const
{
     char addrbuf[15];
     int cc;
     
     cc = yamiAgentIncomingMsgGetEphemAddr(h_, addrbuf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return std::string(addrbuf);
}

int IncomingMsg::getEphemericPort() const
{
     int port;
     int cc;

     cc = yamiAgentIncomingMsgGetEphemPort(h_, &port);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return port;
}

void IncomingMsg::eat()
{
     int cc;
     
     cc = yamiAgentIncomingMsgEat(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::reject()
{
     int cc;
     
     cc = yamiAgentIncomingMsgReject(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::reply()
{
     int cc;
     
     cc = yamiAgentIncomingMsgReply(h_, NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::reply(const ParamSet &paramset)
{
     int cc;
     
     cc = yamiAgentIncomingMsgReply(h_, paramset.getHandle());
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::forward(const char *domainname, const char *objectname,
     const char *messagename)
{
     int cc;
     
     cc = yamiAgentIncomingMsgForward(h_, domainname, objectname,
          messagename, NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::forward(const std::string &domainname,
     const std::string &objectname, const std::string &messagename)
{
     forward(domainname.c_str(), objectname.c_str(), messagename.c_str());
}

void IncomingMsg::forward(const char *domainname, const char *objectname,
     const char *messagename, const ParamSet &paramset)
{
     int cc;
     
     cc = yamiAgentIncomingMsgForward(h_, domainname, objectname,
          messagename, paramset.getHandle());
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::forward(const std::string &domainname,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &paramset)
{
     forward(domainname.c_str(), objectname.c_str(), messagename.c_str(),
          paramset);
}

void IncomingMsg::forwardAddr(const char *addr, int port, int level,
     const char *objectname, const char *messagename)
{
     int cc;
     
     cc = yamiAgentIncomingMsgForwardAddr(h_, addr, port, level,
          objectname, messagename, NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::forwardAddr(const std::string &addr, int port, int level,
     const std::string &objectname, const std::string &messagename)
{
     forwardAddr(addr.c_str(), port, level,
          objectname.c_str(), messagename.c_str());
}

void IncomingMsg::forwardAddr(const char *addr, int port, int level,
     const char *objectname, const char *messagename,
     const ParamSet &paramset)
{
     int cc;
     
     cc = yamiAgentIncomingMsgForwardAddr(h_, addr, port, level,
          objectname, messagename, paramset.getHandle());
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void IncomingMsg::forwardAddr(const std::string &addr, int port, int level,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &paramset)
{
     forwardAddr(addr.c_str(), port, level, objectname.c_str(),
          messagename.c_str(), paramset);
}

// handle operations

void * IncomingMsg::getHandle() const
{
     return h_;
}

void * IncomingMsg::resetHandle(void *handle, bool owner)
{
     void *tmp = h_;
     h_ = handle;
     owner_ = owner;
     
     return tmp;
}

void IncomingMsg::swap(IncomingMsg &inc)
{
     bool thatowner = inc.owner_;
     void *thathandle = inc.h_;
     
     inc.resetHandle(h_, owner_);
     resetHandle(thathandle, thatowner);
}

const char *YAMI::AnyObject = YAMI_ANY_OBJECT;

const char *YAMI::ThisDomain = YAMI_THIS_DOMAIN;

//
// message sent wrapper
//

// constructor/destructor

Message::Message(void *handle, bool owner)
     : h_(handle), owner_(owner)
{
}

Message::~Message()
{
     // ignore the errors
     
     if (h_ != NULL && owner_ == true)
          yamiAgentMsgDestroy(h_);
}

void Message::setTimeOut(int timeout)
{
     int cc;
     
     cc = yamiAgentSetUpTimeOut(h_, timeout);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

Message::eStatus Message::getStatus() const
{
     enum msgStatus status;
     int cc;
     
     cc = yamiAgentMsgGetStatus(h_, &status);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return static_cast<eStatus>(static_cast<int>(status));
}

void Message::wait() const
{
     int cc;
     
     cc = yamiAgentMsgWait(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

std::auto_ptr<ParamSet> Message::getResponse() const
{
     HPARAMSET hparset;
     
     int cc;
     
     cc = yamiAgentMsgGetResponse(h_, &hparset);
     if (cc == YAMINOTAVAILABLE)
          return std::auto_ptr<ParamSet>((ParamSet*)NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return std::auto_ptr<ParamSet>(new ParamSet(hparset, true));
}

// handle operations

void * Message::getHandle() const
{
     return h_;
}

void * Message::resetHandle(void *handle, bool owner)
{
     void *tmp = h_;
     h_ = handle;
     owner_ = owner;
     
     return tmp;
}

void Message::swap(Message &msg)
{
     bool thatowner = msg.owner_;
     void *thathandle = msg.h_;
     
     msg.resetHandle(h_, owner_);
     resetHandle(thathandle, thatowner);
}

//
// policies wrapper
//

class YAMI::PoliciesImpl : public yamipolicies
{
};

Policies::Policies()
     : pimpl_(new PoliciesImpl())
{
     yamiGetDefaultPolicies(pimpl_);
}

Policies::~Policies()
{
     delete pimpl_;
}

void Policies::setAgentLevel(int value)
{
     pimpl_->agentlevel = value;
}

void Policies::setObjQMaxLength(int value)
{
     pimpl_->objqmaxlength = value;
}

void Policies::setObjQMaxSize(int value)
{
     pimpl_->objqmaxsize = value;
}

void Policies::setDispatchers(int value)
{
     pimpl_->dispatchers = value;
}

void Policies::setSenders(int value)
{
     pimpl_->senders = value;
}

void Policies::setSQMaxLength(int value)
{
     pimpl_->sqmaxlength = value;
}

void Policies::setSQMaxSize(int value)
{
     pimpl_->sqmaxsize = value;
}

void Policies::setConnPoolSize(int value)
{
     pimpl_->connpoolsize = value;
}

void Policies::setSendTries(int value)
{
     pimpl_->sendtries = value;
}

void Policies::setHasSocket(bool value)
{
     pimpl_->hassocket = (int)value;
}

void Policies::setReuseAddr(bool value)
{
     pimpl_->reuseaddr = (int)value;
}

void Policies::setHasReceiver(bool value)
{
     pimpl_->hasreceiver = (int)value;
}

void Policies::setHasWaker(bool value)
{
     pimpl_->haswaker = (int)value;
}

void Policies::setMTUser(bool value)
{
     pimpl_->mtuser = (int)value;
}

void Policies::setAllowDuplex(bool value)
{
     pimpl_->allowduplex = (int)value;
}

void Policies::setMaxDplxConns(int value)
{
     pimpl_->maxdplxconns = value;
}

void Policies::setReceiverIdle(int value)
{
     pimpl_->receiveridle = value;
}

//
// agent wrapper
//

// constructor/destructor

Agent::Agent(int port)
{
     int cc;
     
     cc = yamiCreateAgent(&h_, port, NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     owner_ = true;
}

Agent::Agent(int port, const Policies &policies)
{
     int cc;
     
     cc = yamiCreateAgent(&h_, port, policies.pimpl_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
          
     owner_ = true;
}

Agent::Agent(void *handle, bool owner)
     : h_(handle), owner_(owner)
{
}

Agent::~Agent()
{
     // ignore the errors
     
     if (h_ != NULL && owner_ == true)
          yamiDestroyAgent(h_);
}

// domain set operations

void Agent::domainRegister(const char *name, const char *addr,
     int port, int level, int options)
{
     int cc;
     
     cc = yamiAgentDomainRegisterEx(h_, name, addr, port, level, options);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::domainRegister(const std::string &name, const std::string &addr,
     int port, int level, int options)
{
     domainRegister(name.c_str(), addr.c_str(), port, level, options);
}

void Agent::domainUnregister(const char *name)
{
     int cc;
     
     cc = yamiAgentDomainUnregister(h_, name);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::domainUnregister(const std::string &name)
{
     domainUnregister(name.c_str());
}

// object registration and unregistration

// dispatcher function wrapper
// (this also creates the instances of IncomingMsg)

namespace {

extern "C" void dispatcher_helper(HINCMSG msg)
{
     void *hint;
     int cc;
     
     cc = yamiAgentIncomingMsgGetHint(msg, &hint);
     if (cc != YAMIOK)
          return;     // serious problem, but what to do?
     
     assert(hint != NULL);
     
     PassiveObject *servant = static_cast<PassiveObject*>(hint);
     
     try
     {
          std::auto_ptr<IncomingMsg> incoming(new IncomingMsg(msg, false));
          
          servant->call(*incoming);
     }
     catch (...)
     {
          // we just catch all the exceptions so that they
          // don't propagate to the agent
          // - it would really make a mess
          
          // YAMI does not allow exceptions to propagate to the
          // caller (like CORBA), so any exception thrown by the
          // passive object is eaten here and the agent will
          // automatically reject the message, if it wasn't already
          // processed by explicit reject, reply or eaten by the servant
     }
}

}

void Agent::objectRegister(const char *name, enum eObjectType type,
     PassiveObject *object)
{
     int cc;
     
     cc = yamiAgentObjectRegister(h_, name,
          static_cast<objectType>(static_cast<int>(type)),
          dispatcher_helper,
          static_cast<void*>(object));
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::objectRegister(const std::string &name, enum eObjectType type,
     PassiveObject *object)
{
     objectRegister(name.c_str(), type, object);
}

void Agent::objectUnregister(const char *name)
{
     int cc;
     
     cc = yamiAgentObjectUnregister(h_, name);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::objectUnregister(const std::string &name)
{
     objectUnregister(name.c_str());
}

// agent creates instances of incoming messages

std::auto_ptr<IncomingMsg> Agent::getIncoming(
     const char *objectname, bool wait)
{
     HINCMSG hinc;
     int cc;
     
     cc = yamiAgentIncomingMsgGetNext(h_, objectname, &hinc, (int)wait);
     if (cc == YAMINOTAVAILABLE)
          return std::auto_ptr<IncomingMsg>((IncomingMsg*)NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return std::auto_ptr<IncomingMsg>(new IncomingMsg(hinc, true));
}

std::auto_ptr<IncomingMsg> Agent::getIncoming(const std::string &objectname,
     bool wait)
{
     return getIncoming(objectname.c_str(), wait);
}

int Agent::getIncomingCount(const char *objectname)
{
     int len;
     int cc;
     
     cc = yamiAgentIncomingMsgGetCount(h_, objectname, &len);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return len;
}

int Agent::getIncomingCount(const std::string &objectname)
{
     return getIncomingCount(objectname.c_str());
}

// message invocations

std::auto_ptr<Message> Agent::send(const char *domainname,
     const char *objectname, const char *messagename)
{
     HMESSAGE hmsg;
     
     int cc = yamiAgentMsgSend(h_, domainname,
          objectname, messagename, NULL, &hmsg);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return std::auto_ptr<Message>(new Message(hmsg, true));
}

std::auto_ptr<Message> Agent::send(const std::string &domainname,
     const std::string &objectname, const std::string &messagename)
{
     return send(domainname.c_str(), objectname.c_str(),
          messagename.c_str());
}

std::auto_ptr<Message> Agent::send(const char *domainname,
     const char *objectname, const char *messagename,
     const ParamSet &paramset)
{
     HMESSAGE hmsg;
     
     int cc = yamiAgentMsgSend(h_, domainname,
          objectname, messagename, paramset.getHandle(), &hmsg);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return std::auto_ptr<Message>(new Message(hmsg, true));
}

std::auto_ptr<Message> Agent::send(const std::string &domainname,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &paramset)
{
     return send(domainname.c_str(), objectname.c_str(),
          messagename.c_str(), paramset);
}

std::auto_ptr<Message> Agent::sendAddr(const char *addr, int port, int level,
     const char *objectname, const char *messagename, int options)
{
     HMESSAGE hmsg;
     
     int cc = yamiAgentMsgSendAddrEx(h_, addr, port, level,
          objectname, messagename, NULL, &hmsg, options);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return std::auto_ptr<Message>(new Message(hmsg, true));
}

std::auto_ptr<Message> Agent::sendAddr(
     const std::string &addr, int port, int level,
     const std::string &objectname, const std::string &messagename,
     int options)
{
     return sendAddr(addr.c_str(), port, level,
          objectname.c_str(), messagename.c_str(), options);
}

std::auto_ptr<Message> Agent::sendAddr(const char *addr, int port, int level,
     const char *objectname, const char *messagename,
     const ParamSet &paramset, int options)
{
     HMESSAGE hmsg;
     
     int cc = yamiAgentMsgSendAddrEx(h_, addr, port, level,
          objectname, messagename, paramset.getHandle(), &hmsg, options);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return std::auto_ptr<Message>(new Message(hmsg, true));
}

std::auto_ptr<Message> Agent::sendAddr(
     const std::string &addr, int port, int level,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &paramset, int options)
{
     return sendAddr(addr.c_str(), port, level, objectname.c_str(),
          messagename.c_str(), paramset, options);
}

void Agent::sendOneWay(const char *domainname,
     const char *objectname, const char *messagename)
{
     int cc;
     
     cc = yamiAgentMsgSend(h_, domainname,
          objectname, messagename, NULL, NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::sendOneWay(const std::string &domainname,
     const std::string &objectname, const std::string &messagename)
{
     sendOneWay(domainname.c_str(), objectname.c_str(),
          messagename.c_str());
}

void Agent::sendOneWay(const char *domainname,
     const char *objectname, const char *messagename,
     const ParamSet &paramset)
{
     int cc;
     
     cc = yamiAgentMsgSend(h_, domainname,
          objectname, messagename, paramset.getHandle(), NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::sendOneWay(const std::string &domainname,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &paramset)
{
     send(domainname.c_str(), objectname.c_str(),
          messagename.c_str(), paramset);
}

void Agent::sendOneWayAddr(const char *addr, int port, int level,
     const char *objectname, const char *messagename, int options)
{
     int cc;
     
     cc = yamiAgentMsgSendAddrEx(h_, addr, port, level,
          objectname, messagename, NULL, NULL, options);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::sendOneWayAddr(const std::string &addr, int port, int level,
     const std::string &objectname, const std::string &messagename,
     int options)
{
     sendOneWayAddr(addr.c_str(), port, level,
          objectname.c_str(), messagename.c_str(), options);
}

void Agent::sendOneWayAddr(const char *addr, int port, int level,
     const char *objectname, const char *messagename,
     const ParamSet &paramset, int options)
{
     int cc;
     
     cc = yamiAgentMsgSendAddrEx(h_, addr, port, level,
          objectname, messagename, paramset.getHandle(), NULL, options);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Agent::sendOneWayAddr(const std::string &addr, int port, int level,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &paramset, int options)
{
     sendAddr(addr.c_str(), port, level, objectname.c_str(),
          messagename.c_str(), paramset, options);
}

// handle operations

void * Agent::getHandle() const
{
     return h_;
}

void * Agent::resetHandle(void *handle, bool owner)
{
     void *tmp = h_;
     h_ = handle;
     owner_ = owner;
     
     return tmp;
}

void Agent::swap(Agent &a)
{
     bool thatowner = a.owner_;
     void *thathandle = a.h_;
     
     a.resetHandle(h_, owner_);
     resetHandle(thathandle, thatowner);
}

std::string Agent::getLocalAddress() const
{
     char addrbuf[15];
     int cc;
     
     cc = yamiAgentGetLocalAddress(h_, addrbuf);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return std::string(addrbuf);
}

int Agent::getLocalPort() const
{
     int port;
     int cc;

     cc = yamiAgentGetLocalPort(h_, &port);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return port;
}

Agent::eProcessEventResult Agent::processEvent(int timeout)
{
     int cc;
     
     cc = yamiAgentProcessEvent(h_, timeout);
     if (cc == YAMINOTFOUND)
          return eNothingToWaitFor;
     if (cc == YAMINOTAVAILABLE)
          return eNoEvent;
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     return eEventProcessed;
}

//
// additional functions
//

void YAMI::easySend(const char *address, int port, int level,
     const char *objectname, const char *messagename)
{
     int cc;
     
     cc = yamiEasySend(address, port, level, objectname, messagename, NULL);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void YAMI::easySend(const std::string &address, int port, int level,
     const std::string &objectname, const std::string &messagename)
{
     easySend(address.c_str(), port, level, objectname.c_str(),
               messagename.c_str());
}

void YAMI::easySend(const char *address, int port, int level,
     const char *objectname, const char *messagename, const ParamSet &ps)
{
     int cc;
     
     cc = yamiEasySend(address, port, level, objectname, messagename,
          ps.getHandle());
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void YAMI::easySend(const std::string &address, int port, int level,
     const std::string &objectname, const std::string &messagename,
     const ParamSet &ps)
{
     easySend(address.c_str(), port, level, objectname.c_str(),
               messagename.c_str(), ps);
}

//
// synchronization helpers
//

void YAMI::sleep(int timeout)
{
     yamiSleep(timeout);
}

Mutex::Mutex()
{
     int cc;
     
     cc = yamiCreateMutex(&h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
     
     owner_ = true;
}

Mutex::Mutex(void *handle, bool owner)
     : h_(handle), owner_(owner)
{
}

Mutex::~Mutex()
{
     // ignore the errors
     
     if (h_ != NULL && owner_ == true)
          yamiDestroyMutex(h_);
}

void Mutex::lock()
{
     int cc;
     
     cc = yamiMutexLock(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void Mutex::unlock()
{
     int cc;
     
     cc = yamiMutexUnlock(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void * Mutex::getHandle() const
{
     return h_;
}

void * Mutex::resetHandle(void *handle, bool owner)
{
     void *tmp = h_;
     h_ = handle;
     owner_ = owner;
     
     return tmp;
}

void Mutex::swap(Mutex &m)
{
     bool thatowner = m.owner_;
     void *thathandle = m.h_;
     
     m.resetHandle(h_, owner_);
     resetHandle(thathandle, thatowner);
}

Semaphore::Semaphore(int initval)
{
     int cc;
     
     cc = yamiCreateSemaphore(&h_, initval);
     if (cc != YAMIOK)
          _yamiThrow(cc);

     owner_ = true;     
}

Semaphore::Semaphore(void *handle, bool owner)
     : h_(handle), owner_(owner)
{
}

Semaphore::~Semaphore()
{
     // ignore the errors
     
     if (h_ != NULL && owner_ == true)
          yamiDestroySemaphore(h_);
}

void Semaphore::acquire()
{
     int cc;
     
     cc = yamiSemaphoreAcquire(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

bool Semaphore::tryAcquire()
{
     int cc;
     
     cc = yamiSemaphoreTryAcquire(h_);
     if (cc == YAMINOTAVAILABLE)
          return false;
     if (cc != YAMIOK)
          _yamiThrow(cc);

     return true;     
}

void Semaphore::release()
{
     int cc;
     
     cc = yamiSemaphoreRelease(h_);
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void * Semaphore::getHandle() const
{
     return h_;
}

void * Semaphore::resetHandle(void *handle, bool owner)
{
     void *tmp = h_;
     h_ = handle;
     owner_ = owner;
     
     return tmp;
}

void Semaphore::swap(Semaphore &s)
{
     bool thatowner = s.owner_;
     void *thathandle = s.h_;
     
     s.resetHandle(h_, owner_);
     resetHandle(thathandle, thatowner);
}

void YAMI::netInitialize(void)
{
     int cc;
     
     cc = yamiNetInitialize();
     if (cc != YAMIOK)
          _yamiThrow(cc);
}

void YAMI::netCleanup(void)
{
     int cc;
     
     cc = yamiNetCleanup();
     if (cc != YAMIOK)
          _yamiThrow(cc);
}
