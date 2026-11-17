# Posh Control API

Adaptación de API_HealthBloods para el proyecto SwiftUI Posh Control.
Java 21, Jersey 4, Tomcat 11, Maven, MySQL 8 / TiDB.
La carpeta conserva su nombre por compatibilidad con NetBeans. Las rutas activas son `/api/posh`.
El código activo se organiza en las carpetas originales:

- model: Usuario, Producto, RegistroEtiquetado, Favorito, RevisionCaducidad y CalculoEtiquetas, con atributos, constructores, getters y setters.
- controller: consultas preparadas, conversiones a modelos, reglas de negocio y transacciones.
- rest: rutas HTTP, autenticación de solicitudes y traducción de errores; delegan en los controladores.
- connection: se usa ConeccionMysql sin modificar su código.

Se sustituyó el código de glucosa, conservando el respaldo fuera del proyecto en work/before_mvc.
Las rutas, tablas y datos de Posh no cambian. No se necesita volver a crear la base por esta reorganización.
No contiene impresión, ventas ni conexión al sistema de Costco.

## 1. Base existente en TiDB (tu caso)

1. Abre el editor SQL de TiDB o tu conexión de MySQL Workbench.
2. Selecciona la base que ya creaste. Puedes escribir `USE nombre_real_de_tu_base;`.
3. Ejecuta `database/03_tidb_base_existente.sql`.
4. En la MISMA base, ejecuta `database/04_catalogo_base_existente.sql`.
5. Confirma con `SHOW TABLES;` y `SELECT item,nombre,activo FROM productos;`.

Estos scripts no crean otra base, no hacen DROP ni borran los datos HealthBloods.
Si ya existen tablas llamadas usuarios, productos, registros_etiquetado, favoritos o revisiones_caducidad,
revisa su estructura con SHOW CREATE TABLE: CREATE TABLE IF NOT EXISTS no migra tablas incompatibles.
Las tablas anteriores de HealthBloods (`users`, `lectura_glucosa`) no se convierten en cuentas Posh.

Para MySQL LOCAL y una base nueva, usa 01_schema.sql y después 02_catalogo.sql en lugar de los anteriores.
El esquema local utiliza CHECK; el de TiDB no depende de esa característica, que puede estar deshabilitada.
La API valida los mismos límites antes de escribir. Claves foráneas según soporte/configuración del clúster.

### Cinco tablas

| Tabla | Relación |
|---|---|
| usuarios | 1:N registros, favoritos y revisiones |
| productos | 1:N registros y favoritos |
| registros_etiquetado | N:1 usuario y producto; 1:N revisiones |
| favoritos | N:1 usuario y producto; combinación única usuario/producto |
| revisiones_caducidad | N:1 registro y usuario revisor |

Los UUID se almacenan en CHAR(36). El ítem se guarda como texto único.
Los registros guardan copias de días, cantidad por empaque, ítem y nombre para conservar el historial.
No se eliminan usuarios/productos con historial: se desactivan.

### Catálogo inicial

Incluye los 14 productos proporcionados. Solo 5 tienen completas ambas cantidades de empaque:
672832, 845499, 852583, 1583892 y 1767386. Están activos.
Los otros 9 permanecen inactivos con NULL en los valores aún no proporcionados, sin inventar cantidades.
Dos marcas se indican como `Por confirmar` porque no se proporcionaron.
El administrador puede consultar `GET /productos?incluirInactivos=true`, completar los datos y activarlos.
No existe una tabla de reportes de datos faltantes.

Ejemplo para completar un producto (sustituye las cantidades por las reales):

```sql
-- UPDATE productos SET articulos_por_caja=CANTIDAD_REAL,
-- articulos_por_nivel=CANTIDAD_REAL, activo=TRUE WHERE item='7070';
```

## 2. Variables de conexión

Configura en el proceso Java o en Render → Environment:

| Variable | Valor |
|---|---|
| DB_URL | jdbc:mysql://HOST_TIDB:4000/NOMBRE_BASE?sslMode=VERIFY_IDENTITY&connectionTimeZone=UTC&forceConnectionTimeZoneToSession=true |
| DB_USER | Usuario completo que muestra TiDB, incluido el prefijo |
| DB_PASSWORD | Contraseña de TiDB |
| AUTH_SECRET | Secreto aleatorio privado de al menos 32 caracteres |
| APP_TIMEZONE | America/Mexico_City |
| PORT | 10000 en Render |

Usa el host y puerto exactos de TiDB → Connect; no copies literalmente HOST_TIDB.
No conectes Render a localhost de tu computadora. Un .env no se carga automáticamente en Java.
No subas contraseñas a GitHub. La plantilla .env.example no contiene credenciales reales.
Si Java no confía en el certificado del clúster, instala la CA indicada por TiDB en el truststore Java;
no desactives la verificación TLS como solución de despliegue.

Puedes generar AUTH_SECRET en PowerShell con:

```powershell
[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(48))
```

## 3. Compilar y crear el primer administrador

En NetBeans abre el proyecto Maven desde pom.xml para usar las dependencias reproducibles.
Se conserva el proyecto Ant original, pero la compilación Maven es la referencia para Render.
Con Java 21 y Maven:

```powershell
mvn clean verify
```

Si Maven no está en PATH, en esta computadora está disponible en:

```powershell
& 'C:/Program Files/NetBeans-25/netbeans/java/maven/bin/mvn.cmd' clean verify
```

Se genera `target/ROOT.war` y su versión expandida `target/ROOT`.
Antes de crear el administrador, configura DB_URL, DB_USER y DB_PASSWORD en PowerShell.
Puedes capturar la contraseña sin escribirla en el historial:

```powershell
$env:DB_URL='jdbc:mysql://HOST_TIDB:4000/NOMBRE_BASE?sslMode=VERIFY_IDENTITY&connectionTimeZone=UTC&forceConnectionTimeZoneToSession=true'
$env:DB_USER='USUARIO_COMPLETO_TIDB'
$dbClave=Read-Host 'Contraseña de TiDB' -AsSecureString
$dbCred=New-Object System.Management.Automation.PSCredential('db',$dbClave)
$env:DB_PASSWORD=$dbCred.GetNetworkCredential().Password
./scripts/crear-admin.ps1
Remove-Item Env:DB_PASSWORD
```

El script solicita nombre, correo y contraseña de la cuenta de la APP (12 a 128 caracteres).
La contraseña de la app es diferente de la contraseña de TiDB.
Guarda un hash PBKDF2-HMAC-SHA256 con sal aleatoria y 600000 iteraciones; no hay contraseña por defecto.
Crear administrador no requiere iniciar el servidor ni AUTH_SECRET; usa directamente la conexión configurada.
No hay endpoint público de registro o bootstrap. Los siguientes usuarios se crean con sesión de administrador.

## 4. Ejecutar y desplegar

Despliega target/ROOT.war en Tomcat 11 con las variables anteriores.
La base de las rutas será `http://localhost:8080/api/posh` si el contexto es ROOT.
Si NetBeans publica con otro contexto, será `http://localhost:8080/CONTEXTO/api/posh`.
El contexto solo afecta la URL, no los datos.

Docker: `docker build -t posh-control-api .`.
Render: usa un servicio NUEVO para Posh Control si HealthBloods debe seguir funcionando.
El render.yaml está preparado para Docker y salud `/api/posh/salud`.
Configura las variables de TiDB y AUTH_SECRET. El Dockerfile ejecuta pruebas unitarias al construir.
Publicar estos cambios en el mismo repositorio/servicio anterior puede sustituir la API HealthBloods.
Este trabajo modifica archivos locales; no incluye un despliegue ni cambios en TiDB real.

## 5. Probar desde Bruno o SwiftUI

Importa la carpeta `bruno`. Selecciona el entorno Local o Render y cambia baseUrl.
No hay credenciales precargadas. En Login ingresa la cuenta creada en el paso 3.
Copia el token de la respuesta a la variable de entorno `token`.
Todos los cuerpos son JSON y las rutas protegidas requieren `Authorization: Bearer TOKEN`.
SwiftUI debe conservar el token en Keychain y enviar fechas locales como yyyy-MM-dd.

Ejemplo de login:

```json
{"correo":"TU_CORREO","password":"TU_CONTRASEÑA"}
```

Respuesta: token, tokenType, expiresIn (28800 segundos) y usuario, sin password_hash.
Los tokens son opacos firmados HMAC; no son JWT. No deben interpretarse en la app.
Cerrar sesión invalida todas las sesiones del usuario. Cambiar contraseña, rol o estado también las invalida.
La ausencia o expiración de sesión responde 401; permisos insuficientes, 403.

### Rutas (relativas a /api/posh)

| Método | Ruta | Acceso / función |
|---|---|---|
| GET | /salud | Público, proceso activo |
| GET | /salud/db | Público, verifica conexión y cinco tablas sin mostrar datos |
| POST | /auth/login | Público, autenticar |
| GET | /auth/me | Mi perfil |
| POST | /auth/logout | Cerrar todas mis sesiones |
| PUT | /auth/password | passwordActual y password nuevo |
| GET | /usuarios | Administrador |
| POST | /usuarios | Administrador; nombre, correo, password, rol |
| PUT | /usuarios/{id} | Administrador; nombre, correo, rol, activo; password opcional |
| GET | /productos?q=852583 | Buscar activos por nombre, ítem o marca |
| GET | /productos?incluirInactivos=true | Administrador, incluir inactivos |
| GET | /productos/{id} | Detalle |
| POST | /productos | Administrador, crear |
| PUT | /productos/{id} | Administrador, actualizar todos los datos |
| DELETE | /productos/{id} | Administrador, desactivar sin borrar |
| POST | /calculos | Calcular sin guardar |
| POST | /registros | Guardar cálculo recalculado por servidor |
| GET | /registros?q=&desde=2026-11-01&hasta=2026-11-30 | Historial propio; administrador ve todos |
| GET | /registros/{id} | Detalle y revisiones; compartido para seguimiento del equipo |
| GET | /favoritos | Favoritos propios |
| PUT | /favoritos/{productoID} | Agregar, sin duplicar |
| DELETE | /favoritos/{productoID} | Quitar |
| GET | /caducidades?estado=PROXIMOS | Seguimiento compartido del equipo |
| POST | /registros/{id}/revisiones | Registrar revisión |

Datos de producto al crear/editar:

```json
{"item":"852583","nombre":"Mini muffins con chispas de chocolate","marca":"Café Valley Bakery","diasTrasDescongelar":28,"articulosPorCaja":8,"articulosPorNivel":64,"activo":true}
```

ProductoID es el UUID obtenido del catálogo; no es el número de ítem.
Calcular: enviar productoID, tipoCaptura (CAJAS o NIVELES), cantidad y fechaDescongelacion.
La app debe tomar inicialmente la fecha del dispositivo y enviarla explícitamente; el servidor no adivina esa fecha.

```json
{"productoID":"UUID_DEL_PRODUCTO","tipoCaptura":"CAJAS","cantidad":3,"fechaDescongelacion":"2026-11-12"}
```

Guardar: mismo cuerpo más `id`, un UUID generado por la app UNA SOLA VEZ por operación.
Si la app reintenta por un fallo de conexión, debe reutilizar ese UUID para no duplicar etiquetas.
Una operación diferente requiere otro UUID. Reutilizar un UUID con otros datos devuelve 409.
El servidor calcula total y caducidad; ignora resultados enviados por el cliente.
Si el catálogo cambió entre consultar y guardar, la respuesta guardada contiene el resultado definitivo que debe mostrar la app.

Revisión:

```json
{"resultado":"RETIRADO","observaciones":"Producto retirado del área"}
```

No puede marcarse VIGENTE un registro vencido. RETIRADO o AGOTADO cierran el registro.
Los cierres no se reabren en esta versión; un usuario puede revisar registros creados por otro.
Caducidades admite ACTIVOS, PROXIMOS, VENCIDOS y CERRADOS.
PROXIMOS incluye entre 0 y 3 días; VENCIDOS tiene fecha anterior a hoy.
`fecha=2026-12-08` permite consultar respecto a una fecha de referencia sin modificar datos.
La API usa APP_TIMEZONE para el día actual; las marcas de tiempo se entregan en UTC ISO-8601.
Los filtros desde/hasta del historial corresponden a días UTC de creación, no a descongelación.

## 6. Límites y alcance de sincronización

Cantidades enteras entre 1 y 1000000; duración entre 1 y 3650 días.
Fechas de descongelación entre 2000 y 2100. Total de etiquetas BIGINT para evitar desbordamientos.
Las búsquedas tienen máximo 200 caracteres. Correos e ítems son únicos.
La API no implementa SwiftData: la app iOS debe descargar y guardar modelos localmente y reintentar pendientes.
El backend proporciona persistencia y UUID de operaciones; no incluye resolución automática de conflictos
de edición, sincronización incremental, ni una app SwiftUI en este repositorio.
No se habilita acceso anónimo en la API real: el acceso libre solicitado corresponde al mockup de Figma.

## 7. Pruebas

`mvn clean verify` ejecuta las pruebas unitarias de cálculo y seguridad.
La prueba HTTP/MySQL se activa únicamente con RUN_DB_TESTS=true y TEST_DATABASE_CONFIRM=POSH_TEMPORAL.
SOLO usar en una base temporal: elimina filas de las cinco tablas durante su preparación.
No ejecutar esa prueba contra tu TiDB real. La integración escucha en 127.0.0.1:13308.

## Referencias

- MySQL: https://dev.mysql.com/doc/refman/8.4/en/create-table-foreign-keys.html
- TiDB y restricciones: https://docs.pingcap.com/tidb/stable/constraints/
- Render y Docker: https://render.com/docs/docker
