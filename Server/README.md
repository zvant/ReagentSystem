## Server Program
This program is a server that maintain a database of all reagents in a laboratory. The main program would use *port:8189* to accept requests from clients. After generate PDF file of reagents list, the PDF would be moved to directory *${WWW_DIR}/PDF/* and could be visited and downloaded via HTTP. Password is required to visit this server. The defualt password is 123456. Modify class ServerEcho in *Server.java* and re-compile to change it.

### Build
`$ javac -Xlint:unchecked Reagent.java GeneratePDF.java StructureImage.java ReagentSystem.java Server.java`

### Run
# java Server

Requirements
Java Developer's Kit(JDK) and Java Runtime Environment(JRE) are required to build and run this program.
This program use several local commands:
convert - convert images. Install imagemagick to enable.
pdflatex - generate PDF file of reagent list. Install TeXLive to enable.

If this server has problem with reading or writing files, try removing all files in ./list/ and ./image/ directory (note that this would clear the database). PDF compilation process could differ from one system to another, see compilation script ./pdf/compile.sh for more details. You should try to execute it as root before you run the server. You might need to modify the content and permission of the script file.
