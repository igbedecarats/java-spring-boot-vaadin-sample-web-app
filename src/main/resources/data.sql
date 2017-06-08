-- Service Categories
INSERT INTO service_category (id, name) VALUES (1, 'Oficios');
INSERT INTO service_category (id, name) VALUES (2, 'Ropa y Moda');
INSERT INTO service_category (id, name) VALUES (3, 'Servicio Técnico');

-- Service SubCategories
INSERT INTO service_sub_category (name, category_id) VALUES ('Albañilería', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Carpintería', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Cerrajería', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Herrería', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Pintor', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Plomero', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Gasista', 1);
INSERT INTO service_sub_category (name, category_id) VALUES ('Arreglos en General', 2);
INSERT INTO service_sub_category (name, category_id) VALUES ('Diseño', 2);
INSERT INTO service_sub_category (name, category_id) VALUES ('Lavandería y Tintorería', 2);
INSERT INTO service_sub_category (name, category_id) VALUES ('Hogar y Oficina', 3);
INSERT INTO service_sub_category (name, category_id) VALUES ('Tecnología', 3);

-- Locations
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.7206336', '-58.2546051', 'Quilmes');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.7108688', '-58.2800827', 'Bernal');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.748497', '-58.238205', 'Ezpeleta');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.6610756', '-58.3669739', 'Avellaneda');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.7678337', '-58.3792534', 'Temperley');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.7030869', '-58.2974319', 'Don Bosco');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.695385', '-58.3320999', 'Villa Dominico');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.6539459', '-58.3489914', 'Dock Sud');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_NORTE', '-34.425087', '-58.5796585', 'Tigre');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_NORTE', '-35.0241698', '-58.4237574', 'San Vicente');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_NORTE', '-34.470829', '-58.5286102', 'San Isidro');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.7611823', '-58.4302476', 'Lomas de Zamora');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.6994795', '-58.3920795', 'Lanus');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.4938049', '-58.6272715', 'Don Torcuato');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_OESTE', '-34.6685004', '-58.7282483', 'Merlo');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_OESTE', '-34.6340099', '-58.791382', 'Moreno');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_NORTE', '-34.5281205', '-58.473816', 'Vicente Lopez');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_NORTE', '-34.47509', '-58.753746', 'Tortuguitas');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.7900852', '-58.1548511', 'Hudson');
INSERT INTO location
(AREA, LATITUDE, LONGITUDE, NAME)
VALUES('GBA_SUR', '-34.762001', '-58.2112961', 'Berazategui');

-- Users
INSERT INTO user (username, password, email, first_name, last_name, preferred_location_id, role)
VALUES ('johnsmith', 'x28', 'john.smith@gmail.com', 'John', 'Smith', 1, 'ADMIN');

INSERT INTO user (username, password, email, first_name, last_name, preferred_location_id, role)
VALUES ('johnsnow', 'js', 'john.snow@gmail.com', 'John', 'Snow', 2, 'PROVIDER');


