# Communications channel simulation with CRC and repetition coders

A simulated communications channel with CRC error detection and a repetition coder written in Java.

Each class is a Java thread, except the buffer, which the classes are using to communiate between each other, and the test class. The classes are bound as follows: Source -> CRC Coder -> Repetition Coder -> Channel -> Repetition Decoder -> CRC Decoder -> User. The buffer is written as a concurrent bounded buffer using the built in Java monitor support (synchronized class methods).

The Source class generates a random sequence of bits 1000 times, which is then passed through the CRC Coder. Afterwards, (3, 1) repetition coding is applied and the data is passed through the Channel, which has a certain error rate per bit. On the recieving end, repetition and the CRC decoding is done. By the time the data reaches the User, we can see how many, if any, errors occured as well as how many of those were detected by the CRC decoder. The procces is repeated 100 times, and then the program calculates the mean of all the numbers of errors (both real and CRC detected ones).

The point of the simulation is to show, based on the error rate in the channel, the difference in how frequently real errors occur versus how many errors are detected by the CRC decoder. The user can manipulate the error rate in the Test.java file by modifying the error rate parameter in Channel constructor.
