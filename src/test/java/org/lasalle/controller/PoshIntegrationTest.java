package org.lasalle.controller;
import org.lasalle.model.*;
import org.lasalle.rest.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.jackson.JacksonFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.*;
import java.net.http.*;
import java.time.*;
import java.util.*;

@EnabledIfEnvironmentVariable(named="RUN_DB_TESTS",matches="true")
class PoshIntegrationTest{
 static HttpServer server;static final HttpClient HTTP=HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();static final ObjectMapper JSON=new ObjectMapper();
 static final String BASE="http://127.0.0.1:13308/api/";
 record Reply(int status, Object body){}
 static Reply send(String method,String path,String token,Object body)throws Exception{
  var b=HttpRequest.newBuilder(URI.create(BASE+path)).header("Content-Type","application/json");if(token!=null)b.header("Authorization","Bearer "+token);
  var r=HTTP.send(b.method(method,body==null?HttpRequest.BodyPublishers.noBody():HttpRequest.BodyPublishers.ofString(JSON.writeValueAsString(body))).build(),HttpResponse.BodyHandlers.ofString());
  return new Reply(r.statusCode(),r.body().isBlank()?null:JSON.readValue(r.body(),Object.class));
 }
 @SuppressWarnings("unchecked") static Map<String,Object> map(Reply r){return (Map<String,Object>)r.body;}
 static String login(String email)throws Exception{var r=send("POST","posh/auth/login",null,Map.of("correo",email,"password","Prueba-segura-12345"));assertEquals(200,r.status,r.body.toString());return map(r).get("token").toString();}
 @BeforeAll static void setup()throws Exception{
  try(var c=DatosController.open()){
   // Solo usar en una base temporal. Requiere confirmación explícita para borrar datos de pruebas.
   if(!"POSH_TEMPORAL".equals(System.getenv("TEST_DATABASE_CONFIRM")))throw new IllegalStateException("Se requiere base temporal de pruebas");
   for(String t:List.of("revisiones_caducidad","favoritos","registros_etiquetado","productos","usuarios"))DatosController.update(c,"DELETE FROM "+t);
   for(String role:List.of("ADMINISTRADOR","EMPLEADO"))DatosController.update(c,"INSERT INTO usuarios(id,nombre,correo,password_hash,rol) VALUES(?,?,?,?,?)",UUID.randomUUID().toString(),role,role.toLowerCase()+"@example.test",SeguridadController.hash("Prueba-segura-12345"),role);
  }
  var config=new ResourceConfig().packages("org.lasalle.rest").register(JacksonFeature.class);
  server=GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE),config,false);
  for(var l:server.getListeners()){l.getTransport().setSelectorRunnersCount(1);l.getTransport().setWorkerThreadPoolConfig(org.glassfish.grizzly.threadpool.ThreadPoolConfig.defaultConfig().setCorePoolSize(2).setMaxPoolSize(4));}
  server.start();
 }
 @AfterAll static void stop(){if(server!=null)server.shutdownNow();}
 @Test void flujoCompletoYPermisos()throws Exception{
  assertEquals(200,send("GET","posh/salud",null,null).status);
  assertEquals(401,send("GET","posh/productos",null,null).status);
  assertEquals(401,send("POST","posh/auth/login",null,Map.of("correo","empleado@example.test","password","Clave-incorrecta-123")).status);
  String admin=login("administrador@example.test"),employee=login("empleado@example.test");
  assertEquals(403,send("GET","posh/usuarios",employee,null).status);
  var p=new HashMap<String,Object>(Map.of("item","852583","nombre","Mini muffins","marca","Café Valley Bakery","diasTrasDescongelar",28,"articulosPorCaja",8,"articulosPorNivel",64,"activo",true));
  assertEquals(403,send("POST","posh/productos",employee,p).status);
  var created=send("POST","posh/productos",admin,p);assertEquals(201,created.status,created.body.toString());String pid=map(created).get("id").toString();
  assertEquals(409,send("POST","posh/productos",admin,p).status);
  assertEquals(1,((List<?>)send("GET","posh/productos?q=852583",employee,null).body).size());
  var b=new HashMap<String,Object>(Map.of("id",UUID.randomUUID().toString(),"productoID",pid,"tipoCaptura","CAJAS","cantidad",3,"fechaDescongelacion","2026-11-12"));
  var bad=new HashMap<>(b);bad.put("fechaDescongelacion","2026-02-30");assertEquals(400,send("POST","posh/calculos",employee,bad).status);
  var calc=send("POST","posh/calculos",employee,b);assertEquals(200,calc.status);assertEquals(24,((Number)map(calc).get("totalEtiquetas")).intValue());assertEquals("2026-12-10",map(calc).get("fechaCaducidad"));
  var saved=send("POST","posh/registros",employee,b);assertEquals(201,saved.status,saved.body.toString());String rid=map(saved).get("id").toString();
  assertEquals(200,send("POST","posh/registros",employee,b).status);assertEquals(1,((List<?>)send("GET","posh/registros",employee,null).body).size());
  b.put("cantidad",4);assertEquals(409,send("POST","posh/registros",employee,b).status);
  assertEquals(200,send("PUT","posh/favoritos/"+pid,employee,null).status);assertEquals(200,send("PUT","posh/favoritos/"+pid,employee,null).status);assertEquals(1,((List<?>)send("GET","posh/favoritos",employee,null).body).size());assertEquals(204,send("DELETE","posh/favoritos/"+pid,employee,null).status);
  p.put("diasTrasDescongelar",10);p.put("articulosPorCaja",4);assertEquals(200,send("PUT","posh/productos/"+pid,admin,p).status);
  var historical=map(send("GET","posh/registros/"+rid,employee,null));assertEquals(28,historical.get("diasAplicados"));assertEquals(24,((Number)historical.get("totalEtiquetas")).intValue());
  b.put("cantidad",3);var retry=send("POST","posh/registros",employee,b);assertEquals(200,retry.status);assertEquals(24,((Number)map(retry).get("totalEtiquetas")).intValue());
  var upcoming=map(send("GET","posh/caducidades?estado=PROXIMOS&fecha=2026-12-08",employee,null));assertEquals(1,((List<?>)upcoming.get("registros")).size());
  assertEquals(201,send("POST","posh/registros/"+rid+"/revisiones",employee,Map.of("resultado","RETIRADO","observaciones","Revisión de prueba")).status);
  assertEquals(409,send("POST","posh/registros/"+rid+"/revisiones",employee,Map.of("resultado","VIGENTE")).status);
  assertEquals(0,((List<?>)map(send("GET","posh/caducidades?estado=ACTIVOS",employee,null)).get("registros")).size());
  b.put("id",UUID.randomUUID().toString());b.put("fechaDescongelacion","2000-01-01");var old=send("POST","posh/registros",employee,b);assertEquals(201,old.status);String oid=map(old).get("id").toString();assertEquals(400,send("POST","posh/registros/"+oid+"/revisiones",employee,Map.of("resultado","VIGENTE")).status);
  assertEquals(200,send("POST","posh/auth/logout",employee,null).status);assertEquals(401,send("GET","posh/productos",employee,null).status);
  assertEquals(404,send("GET","glucosa/getAllUsuarios",null,null).status);
 }
}
