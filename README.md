# API HealthBloods

API Java 21, Jersey 4 y Tomcat 11. El proyecto de NetBeans se conserva; Maven permite compilar fuera de tu computadora.

## Variables de entorno

Configura estas variables tanto en Render como en la ejecución local de NetBeans:

- DB_URL: URL JDBC de una base MySQL accesible, por ejemplo jdbc:mysql://servidor:3306/healthBlood?sslMode=VERIFY_IDENTITY
- DB_USER: usuario de MySQL.
- DB_PASSWORD: contraseña de MySQL.

No subas credenciales a GitHub. Un archivo .env no se carga automáticamente en Java ni en NetBeans.
La base de datos y sus tablas deben existir. El proyecto no incluye un script de creación o migración. Render no puede conectarse a MySQL en 127.0.0.1 de tu computadora.

## Compilación

Con Maven y Java 21: `mvn clean package`.
Con Docker: `docker build -t api-healthbloods .`.
El archivo generado es target/ROOT.war.

## Render

Sube el proyecto a GitHub y crea un Blueprint de Render usando render.yaml, o un Web Service con lenguaje Docker. Configura DB_URL, DB_USER y DB_PASSWORD. El servidor usa PORT (10000 por defecto).

Prueba GET https://TU-SERVICIO.onrender.com/api/glucosa/saludar . Esta ruta verifica la API sin consultar MySQL. Las demás rutas conservan el prefijo /api/glucosa; el contexto /API_HealthBloods se elimina al desplegar como ROOT.

El servicio web de este archivo solicita el plan free; confirma su disponibilidad y condiciones en tu cuenta. La base MySQL se contrata/configura por separado.
