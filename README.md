# Communications channel simulation with CRC and repetition coders

A simulated communications channel with CRC error detection and a repetition coder written in Java.

Each class is a Java thread, except the buffer, which the classes are using to communiate between each other. The classes are bound as follows: Source -> CRC Coder -> Repetition Coder -> Channel -> Repetition Decoder -> CRC Decoder -> User.
The point of the simulation is to show, based on the error rate in the channel, the difference in how frequently the real error occurs versus how many errors are detected by the CRC decoder. The user can manipulate the error rate in Test.java file.
