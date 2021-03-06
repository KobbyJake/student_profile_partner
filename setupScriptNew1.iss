; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Profiler"
#define MyAppVersion "1.5.0"
#define MyAppPublisher "JCyber Solutions"
#define MyAppURL "http://www.jcybersolutions.com"
#define MyAppExeName "Students' Profile Partner.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{911BDB3E-99A4-4049-9DFA-755E944D088C}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName=C:\{#MyAppName}
DisableDirPage=yes
DisableProgramGroupPage=yes
LicenseFile=D:\Program Files (x86)\ProPartner\license.txt
InfoBeforeFile=D:\Program Files (x86)\ProPartner\Readme.txt
InfoAfterFile=D:\Program Files (x86)\ProPartner\Readme2.txt
OutputDir=H:\output
OutputBaseFilename=Profiler 2016 setup
SetupIconFile=D:\Documents\DOCU\my icons\Old ICO\Graduated.ico
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 0,6.1

[Files]
Source: "D:\Program Files (x86)\ProPartner\dist\bundles\Students' Profile Partner\Students' Profile Partner.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Program Files (x86)\ProPartner\dist\bundles\Students' Profile Partner\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{commonprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

