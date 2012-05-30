Set oShell = CreateObject ("Wscript.Shell") 
Dim strArgs
strArgs = "cmd /c Run.bat"
oShell.Run strArgs, 0, false
strArgs2 = "cmd /c Run2.bat"
oShell.Run strArgs2, 0, false