-- No inventa cantidades. Completar productos inactivos antes de activarlos.
-- Usa la misma base seleccionada en el paso anterior.
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'7070','Salmón ahumado en rebanadas','Por confirmar',26,NULL,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='7070');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'7859','Salmón noruego ahumado','Foppen',28,NULL,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='7859');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'512447','Pan orgánico de 21 granos','Dave’s Killer Bread',10,4,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='512447');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'672832','Mini baguettes francesas','Menissez',30,4,20,TRUE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='672832');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'845499','Magdalenas','Sugar Bowl Bakery',42,8,80,TRUE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='845499');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'852583','Mini muffins con chispas de chocolate','Café Valley Bakery',28,8,64,TRUE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='852583');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'1583892','Pastelitos de brownie','Sugar Bowl Bakery',40,8,64,TRUE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='1583892');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'1644309','Salmón rojo salvaje ahumado','Kirkland Signature',30,NULL,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='1644309');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'1748643','Pastel de chocolate','Posh Pantry',16,6,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='1748643');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'1763550','Pastel de limón','Posh Pantry',16,6,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='1763550');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'1767386','Pan tipo croissant','Schwartz Brothers',21,6,48,TRUE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='1767386');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'1838774','Macarons surtidos','Le Chic Pâtissier',30,9,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='1838774');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'2069264','Galletas glaseadas','Kimberleys',21,10,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='2069264');
INSERT INTO productos(id,item,nombre,marca,dias_tras_descongelar,articulos_por_caja,articulos_por_nivel,activo)
SELECT UUID(),'2091056','Rosca de calabaza','Por confirmar',10,NULL,NULL,FALSE WHERE NOT EXISTS(SELECT 1 FROM productos WHERE item='2091056');
