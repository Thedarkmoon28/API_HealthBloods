param([string]$Nombre,[string]$Correo)
$ErrorActionPreference='Stop'
if(-not $Nombre){$Nombre=Read-Host 'Nombre del administrador'}
if(-not $Correo){$Correo=Read-Host 'Correo del administrador'}
if(-not $env:DB_URL -or -not $env:DB_USER -or -not $env:DB_PASSWORD){throw 'Configura DB_URL, DB_USER y DB_PASSWORD antes de continuar.'}
$clave=Read-Host 'Contraseña (12 a 128 caracteres)' -AsSecureString
$ptr=[Runtime.InteropServices.Marshal]::SecureStringToBSTR($clave)
try {
 $env:ADMIN_NAME=$Nombre; $env:ADMIN_EMAIL=$Correo
 $env:ADMIN_PASSWORD=[Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr)
 Push-Location (Split-Path $PSScriptRoot -Parent)
 try {
  if(-not (Test-Path 'target/ROOT/WEB-INF/classes')){throw 'Primero compila con mvn clean verify.'}
  & java -cp 'target/ROOT/WEB-INF/classes;target/ROOT/WEB-INF/lib/*' org.lasalle.controller.AdminController
  if($LASTEXITCODE -ne 0){throw 'No se pudo crear el administrador. Revisa conexión, tablas y correo duplicado.'}
 } finally {Pop-Location}
} finally {
 [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)
 Remove-Item Env:ADMIN_PASSWORD -ErrorAction SilentlyContinue
 Remove-Item Env:ADMIN_NAME -ErrorAction SilentlyContinue
 Remove-Item Env:ADMIN_EMAIL -ErrorAction SilentlyContinue
}
