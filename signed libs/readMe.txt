20090219:
- neu wird der LegoNXT-Treiber im NXJTools.zip heruntergelden, und nicht mehr einzeln
- LibUSB.exe und NXJFlash.exe werden nicht mehr verwendet

20090512
- Win XP:
	- NXJTools enth�lt Treiber und NextTool (d�rfen nicht als Ordner gezippt werden -> siehe readme in NXJTools)
	- lejosNXJ enth�lt alle ben�tigten Dateien welche zum User heruntergeladen werden und f�r die Firmwareinstallation
	  n�tig sind

- Win Vista
	- Vista enth�lt die n�tigen Treiber welche heruntegeldaen werden und vom User selbst�ndig installiert werden
	- lejosNXJ (siehe XP)

- Mac OS X:
	- NXTDriver_Mac enth�lt die beiden Treiberpackete welche f�r Mac heruntergeladen und installiert werden (dieser
	  Ordner muss nicht verpackt werden, da die Packete direkt aus dem Ordner geholt werden)
	- lejosNXJ_Mac enth�lt alle ben�tigten Dateien welche zum User heruntergeladen werden und f�r die 
	  Firmwareinstallation n�tig sind. Dieser Ordner sollte immer vom Mac geholt werden und auch auf einem Mac zu einem
	  .tar verpackt werden (tar k�nnen auf windows nicht erzeugt werden)
	- unpackNXJForMac.sh ist das Bash-Script welches ben�tigt wird um lejosNXJ_Mac.tar beim user lokal zu entpacken
	  (kann auch in einem normalen Editor von Windows bearbeitet werden)

- Linux
	- lejosNXJ_Linux enth�lt alle ben�tigten Dateien welche zum User heruntergeladen werden und f�r die 
	  Firmwareinstallation n�tig sind. Dieser Ordner sollte immer vom Linux geholt werden und auch auf einem Linux zu einem
	  .tar verpackt werden (tar k�nnen auf windows nicht erzeugt werden)
	- unpackNXJForLinux.sh ist das Bash-Script welches ben�tigt wird um lejosNXJ_Linux.tar beim user lokal zu entpacken
	  (kann auch in einem normalen Editor von Windows bearbeitet werden => mit Vorteil aber in Linux)
	- setNxtUdev.sh ist wie eine Art Treiberinstallation bei Mac und Windows. Hier werden einfach die Regeln f�r den USB gesetzt.