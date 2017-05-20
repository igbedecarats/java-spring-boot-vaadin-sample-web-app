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