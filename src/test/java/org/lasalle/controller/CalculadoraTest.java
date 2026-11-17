package org.lasalle.controller;
import org.lasalle.model.*;
import org.lasalle.rest.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class CalculadoraTest{
 private Map<String,Object> p(){return Map.of("id",UUID.randomUUID().toString(),"item","852583","nombre","Muffins","activo",true,"diasTrasDescongelar",28,"articulosPorCaja",8,"articulosPorNivel",64);}
 private Map<String,Object> b(String tipo,Object qty,String date){return Map.of("tipoCaptura",tipo,"cantidad",qty,"fechaDescongelacion",date);}
 @Test void cajasYCalendario(){var r=CalculadoraController.calcular(p(),b("CAJAS",3,"2026-11-12"));assertEquals(24L,r.get("totalEtiquetas"));assertEquals("2026-12-10",r.get("fechaCaducidad"));}
 @Test void nivelesNoMultiplicanPorCaja(){assertEquals(128L,CalculadoraController.calcular(p(),b("NIVELES",2,"2026-11-12")).get("totalEtiquetas"));}
 @Test void fechaBisiesta(){assertEquals("2028-03-28",CalculadoraController.calcular(p(),b("CAJAS",1,"2028-02-29")).get("fechaCaducidad"));}
 @Test void rechazaCantidadesYTipo(){for(Object q:List.of(0,-1,1.2,"3",1000001))assertThrows(ErrorAplicacion.class,()->CalculadoraController.calcular(p(),b("CAJAS",q,"2026-11-12")));assertThrows(ErrorAplicacion.class,()->CalculadoraController.calcular(p(),b("PALLETS",1,"2026-11-12")));}
 @Test void rechazaInactivoYFecha(){var p=new HashMap<>(p());p.put("activo",false);assertThrows(ErrorAplicacion.class,()->CalculadoraController.calcular(p,b("CAJAS",1,"2026-11-12")));assertThrows(java.time.DateTimeException.class,()->CalculadoraController.calcular(p(),b("CAJAS",1,"2026-02-30")));}
 @Test void evitaDesbordamiento(){var p=new HashMap<>(p());p.put("articulosPorNivel",1000000);assertEquals(1000000000000L,CalculadoraController.calcular(p,b("NIVELES",1000000,"2026-11-12")).get("totalEtiquetas"));}
}
