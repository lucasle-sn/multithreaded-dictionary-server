# multithreaded-dictionary-server
The main objective of the project is to overcome the problem of multi-threaded dictionary server


==================================================
# Usage

1. Input of "MultiThreadsDictionaryServer.jar" is:

	> MultiThreadsDictionaryServer.jar <port> <dictionary-file>
	
	Where: 	<port>: the port number where the server will listen for incoming client connections
			<dictionary-file>: the path to the file containing the initial dictionary data
			

2. Input of "DictionaryClient" is:

	> DictionaryClient.jar <server-address> <server-port>
	
	Where	<server-address>: IP address of the server
			<server-port>: the port number where the server will listen for incoming client connections
		