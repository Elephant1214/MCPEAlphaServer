# MCPE Alpha Server
A server for the MCPE Alpha. 0.14.3 is planned as the first supported version with eventual expansion to multiple versions.

### Resources
Along with looking at (very old) source from various servers that already exist:
- [Protocol Specification](https://wiki.vg/Pocket_Minecraft_Protocol)
- [Login Procedure](https://wiki.vg/Pocket_Edition_Login)

### Current State
No yet working, I currently have a bit of an issue with the client sending a ready packet (0x84) instead of an encapsulated client connect packet (0x09) after it receives the open connection reply reply two packet (0x08).