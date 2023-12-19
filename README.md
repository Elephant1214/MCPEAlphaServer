# MCPE Alpha Server
A server for the MCPE Alpha. 0.14.3 is planned as the first supported version with eventual expansion to multiple versions.\
I currently have a bit of an issue with the client sending a ready packet (0x84) instead of an encapsulated client connect packet (0x09) after it receives a reply to the second open connection request packet (0x08).