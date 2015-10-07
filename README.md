# MeasurementToolKit
Java/C++ based measurement framework

This is software framework to control all kind of measurement instruments. 
Current implementation is 3rd. generation of this software. 
On end of 90' I started to develop logic analyser hardware. 
At work I used HP logic analysers and prototype analysers.
Those devices inspired me to develop own measurement frame. 
It consist hardware installed in sub-rack and software running in Linux computer.
First software version was implemented in Ansi-C and used XForms ui library in X windowing environment.
At 2002 I started porting software to Java Swing ui. Software was divided in server and Graphical client. 
Server code was still Ansi-C based.

3rd generation development started at 2012 when I bought GPIB based instruments and Linux compatible GPIB adapter.
Code was totally rewritten only small parts of low level C code is recycled. 
New server side was written in C++ using Yami messaging library to communicate with client ui.
In client code almost all code started from scratch. 
New architecture was made to give possibility to add new communication buses and measurement instruments easily to code.