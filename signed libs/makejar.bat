rem @echo off

set archivepath=%1archive\
set number=%2
set name=%3
set jarpath=%archivepath%%number%\%name%.jar

jar cfm %archivepath%%number%\%name%.jar %archivepath%%number%\manifest.mf -C %1 java.gif
jar uf %archivepath%%number%\%name%.jar -C %archivepath%%number%\classes .
 


jarsigner -keystore %1legoKeystore -storepass lego000 %jarpath% lego

echo unlocked > %archivepath%%number%\lockjar.txt