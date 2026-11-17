package org.lasalle.controller;
import org.lasalle.model.*;
import org.lasalle.rest.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class SecurityTest{
 @Test void hashConSal(){String a=SeguridadController.hash("Una-clave-larga-123"),b=SeguridadController.hash("Una-clave-larga-123");assertNotEquals(a,b);assertTrue(SeguridadController.verify("Una-clave-larga-123",a));assertFalse(SeguridadController.verify("otra",a));assertFalse(SeguridadController.verify("otra","hash roto"));}
 @Test void tokensVencidosYAlterados(){String secret="12345678901234567890123456789012",id=UUID.randomUUID().toString();String t=SeguridadController.token(id,3,secret,1000);assertEquals(id,SeguridadController.claims(t,secret,1001)[0]);assertThrows(ErrorAplicacion.class,()->SeguridadController.claims(t+"X",secret,1001));assertThrows(ErrorAplicacion.class,()->SeguridadController.claims(t,secret,1000+SeguridadController.SESSION_SECONDS));assertThrows(ErrorAplicacion.class,()->SeguridadController.claims(t,"otro-secreto-de-longitud-32-caracteres",1001));}
 @Test void secretoDebil(){assertThrows(IllegalStateException.class,()->SeguridadController.token(UUID.randomUUID().toString(),0,"corto",1000));}
}
