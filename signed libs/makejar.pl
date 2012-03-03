@echo off
cd %1archive\%2
del *.jar
del *.jnlp
jar cf %3.jar *.class
jar uf %3.jar -C %1 java.gif
jar uf %3.jar -C d:\classes ch
jarsigner -keystore %1legoKeystore -storepass lego000 -keypass lego000 %3.jar lego
cd..
cd..