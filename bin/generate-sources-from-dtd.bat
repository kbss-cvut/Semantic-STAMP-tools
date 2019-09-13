:: set up the xjc
set xjc="%JAVA_HOME%\bin\xjc.exe"
echo %xjc%


:: set up the input file
set inputDTD="c:\Users\user\Documents\skola\projects\10-2017-ZETA\code\lkpr-process-model-extraction\lkpr-process-models\adoxml31.dtd"
:: set up output directory
set outputDir=src\main\generated-sources

:: generate sources
::"%xjc%" --help
%xjc% -dtd -p cz.cvut.kbss.adoxml -d %outputDir% %inputDTD% 
