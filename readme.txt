/**
 * readme.txt
 *
 * Login name: yilunq
 *
 * @Yilun Qian
 *
 */

* The results tested on both the remote machine and the local machine are listed below

* Testing on remote machine:
	(1) With no loss: 0 (10 counts)
		
		All packets are sent and received immediately:

			Pinging csil-cpu6.csil.sfu.ca [206.12.16.220]  with 41 bytes of data:

			Reply from 206.12.16.220: bytes = 41 time = 73ms
			Reply from 206.12.16.220: bytes = 41 time = 203ms
			Reply from 206.12.16.220: bytes = 41 time = 196ms
			Reply from 206.12.16.220: bytes = 41 time = 75ms
			Reply from 206.12.16.220: bytes = 41 time = 178ms
			Reply from 206.12.16.220: bytes = 41 time = 175ms
			Reply from 206.12.16.220: bytes = 41 time = 105ms
			Reply from 206.12.16.220: bytes = 41 time = 143ms
			Reply from 206.12.16.220: bytes = 41 time = 215ms
			Reply from 206.12.16.220: bytes = 41 time = 138ms

			Ping statistics from 206.12.16.220:
			Packets: Sent = 10, Received = 10, Lost = 0 (0.0% loss),
			Approximate round trip times in milli-seconds:
			Minimux = 73ms, Maximun = 215ms, Average = 150.10ms


	(2) With moderate loss: 0.5 (10 counts)
		
		The client received about half of packets that are sent to server immediately:

			Pinged csil-cpu6.csil.sfu.ca [206.12.16.220]  with 41 bytes of data:

			Reply from 206.12.16.220: bytes = 41 time = 103ms
			Reply from 206.12.16.220: bytes = 41 time = 164ms
			Timeout!
			Timeout!
			Timeout!
			Reply from 206.12.16.220: bytes = 41 time = 149ms
			Reply from 206.12.16.220: bytes = 41 time = 194ms
			Timeout!
			Reply from 206.12.16.220: bytes = 41 time = 180ms
			Timeout!

			Ping statistics from 206.12.16.220:
			Packets: Sent = 10, Received = 5, Lost = 5 (0.5% loss),
			Approximate round trip times in milli-seconds:
			Minimux = 103ms, Maximun = 194ms, Average = 158.00ms


	(3) With total and high loss: 1.0 (10 counts)

		The client received NO packets and printed out "All packets are lost" warning:

			Pinged csil-cpu6.csil.sfu.ca [206.12.16.220]  with 41 bytes of data:

			Timeout!
			Timeout!
			Timeout!
			Timeout!
			Timeout!
			Timeout!
			Timeout!
			Timeout!
			Timeout!
			Timeout!

			Ping statistics from 206.12.16.220:
			Packets: Sent = 10, Received = 0, Lost = 10 (1.0% loss),

			All packets are lost! Please try again.




* Testing on local machines 
	(1) With no loss: 0 (default counts)

		All packets are sent and received immediately:

			Pinging localhost [127.0.0.1]  with 41 bytes of data:

			Reply from 127.0.0.1: bytes = 41 time = 120ms
			Reply from 127.0.0.1: bytes = 41 time = 18ms
			Reply from 127.0.0.1: bytes = 41 time = 170ms

			Ping statistics from 127.0.0.1:
			Packets: Sent = 3, Received = 3, Lost = 0 (0.0% loss),
			Approximate round trip times in milli-seconds:
			Minimux = 18ms, Maximun = 170ms, Average = 102.67ms


	(2) With moderate loss: 0.5 (default counts)

		The client received about half of packets that are sent to server immediately:

			Pinging localhost [127.0.0.1]  with 41 bytes of data:

			Timeout!
			Reply from 127.0.0.1: bytes = 41 time = 121ms
			Reply from 127.0.0.1: bytes = 41 time = 189ms

			Ping statistics from 127.0.0.1:
			Packets: Sent = 3, Received = 2, Lost = 1 (0.3333333333333333% loss),
			Approximate round trip times in milli-seconds:
			Minimux = 121ms, Maximun = 189ms, Average = 155.00ms


	(3) With total and high loss: 1.0 (default counts)

		The client received NO packets and printed out "All packets are lost" warning:

			Pinged localhost [127.0.0.1]  with 41 bytes of data:
		
			Timeout!
			Timeout!
			Timeout!

			Ping statistics from 127.0.0.1:
			Packets: Sent = 3, Received = 0, Lost = 3 (1.0% loss),

			All packets are lost! Please try again.




 
	


