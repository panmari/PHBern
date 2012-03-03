20090219:
- neu wird der LegoNXT-Treiber im NXJTools.zip heruntergelden, und nicht mehr einzeln
- LibUSB.exe und NXJFlash.exe werden nicht mehr verwendet

20090512
- Win XP:
	- NXJTools enthält Treiber und NextTool (dürfen nicht als Ordner gezippt werden -> siehe readme in NXJTools)
	- lejosNXJ enthält alle benötigten Dateien welche zum User heruntergeladen werden und für die Firmwareinstallation
	  nötig sind

- Win Vista
	- Vista enthält die nötigen Treiber welche heruntegeldaen werden und vom User selbständig installiert werden
	- lejosNXJ (siehe XP)

- Mac OS X:
	- NXTDriver_Mac enthält die beiden Treiberpackete welche für Mac heruntergeladen und installiert werden (dieser
	  Ordner muss nicht verpackt werden, da die Packete direkt aus dem Ordner geholt werden)
	- lejosNXJ_Mac enthält alle benötigten Dateien welche zum User heruntergeladen werden und für die 
	  Firmwareinstallation nötig sind. Dieser Ordner sollte immer vom Mac geholt werden und auch auf einem Mac zu einem
	  .tar verpackt werden (tar können auf windows nicht erzeugt werden)
	- unpackNXJForMac.sh ist das Bash-Script welches benötigt wird um lejosNXJ_Mac.tar beim user lokal zu entpacken
	  (kann auch in einem normalen Editor von Windows bearbeitet werden)

- Linux
	- lejosNXJ_Linux enthält alle benötigten Dateien welche zum User heruntergeladen werden und für die 
	  Firmwareinstallation nötig sind. Dieser Ordner sollte immer vom Linux geholt werden und auch auf einem Linux zu einem
	  .tar verpackt werden (tar können auf windows nicht erzeugt werden)
	- unpackNXJForLinux.sh ist das Bash-Script welches benötigt wird um lejosNXJ_Linux.tar beim user lokal zu entpacken
	  (kann auch in einem normalen Editor von Windows bearbeitet werden => mit Vorteil aber in Linux)
	- setNxtUdev.sh ist wie eine Art Treiberinstallation bei Mac und Windows. Hier werden einfach die Regeln für den USB gesetzt.