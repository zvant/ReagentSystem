# ReagentSystem
A simple Client/Server program to manage reagemts in a laboratory.

When I was working at a chemistry laboratory, I noticed that the reagents management is quite messy. There is not a updated database for all the reagents we have, so anyone who wanted a reagent had to look for it everywhere without knowing if it even existed. So I write this program to maintain a database that records every reagent in the laboratory, including their name, basic information and quantity in stock.

This program would work well among computers in an area network, which is the most common network configuration in most laboratories. The database would be maintained by a server program running on specific computer. Then everyone could access the database specifying the server IP address and a password using the client program. They can search a reagent to check its existance, add a new reagent to the database after purchasing it, or remove one reagent from the database when run out of it. They could also get a list of all the reagents recorded in the database in PDF file.

The PDF file would be generated by <img src="http://chart.googleapis.com/chart?cht=tx&chl=$\latex $" style="border:none;"> and send to client computer via HTTP. So LaTeX environment and HTTP server are required on the server computer. I tested with TeXLive and apache2 on Raspbian on my Raspberry Pi B board and it worked well. If you want to run the server on other kind of OS, you might have to modify the *Server/pdf/compile.sh* first. The client could work well on any system with JRE 7 and higher.
