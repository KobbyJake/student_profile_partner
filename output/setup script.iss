; Script generated by the Inno Script Studio Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Profiler 2016 Evaluation Copy"
#define MyAppVersion "1.5.0"
#define MyAppPublisher "JCyber Solutions Group"
#define MyAppExeName "Students' Profile Partner.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{45729333-7BBB-486A-89FF-D32FE1D63316}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName=C:\{#MyAppName}
DisableDirPage=yes
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
LicenseFile=D:\Program Files (x86)\ProPartner\license.txt
InfoBeforeFile=D:\Program Files (x86)\ProPartner\Readme.txt
InfoAfterFile=D:\Program Files (x86)\ProPartner\Readme2.txt
OutputDir=D:\Program Files (x86)\ProPartner\output
OutputBaseFilename=Profiler 2016 Eval_Copy Setup
SetupIconFile=D:\Documents\DOCU\my icons\Old ICO\Graduated.ico
Password=appiahbaahjake@gmail.com
Encryption=yes
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "D:\Program Files (x86)\ProPartner\dist\bundles\Students' Profile Partner\Students' Profile Partner.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Program Files (x86)\ProPartner\dist\bundles\Students' Profile Partner\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent
