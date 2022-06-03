create database db_biblioteca;

use db_biblioteca;

create table autores(
	id_autor varchar(300) primary key,
	nom_autor varchar(300) not null
);

create table editoriales(
	id_editorial varchar(300) primary key,
	nom_editorial varchar(500) not null
);

create table libros(
	isbn varchar(15) primary key,
	portada varchar(500),
	nom_libro varchar(500) not null,
	autor text not null,
	descripcion text,
	editorial varchar(500) not null,
	anio_publicacion varchar(4),
	edicion varchar(100),
	existencias Integer not null,
	categoria text not null
);

create table usuarios(
	id_usuario varchar(5) primary key,
	nom_usuario varchar(500) not null,
	estado_usuario varchar(100),
	contrasena varchar(10) not null
);

create table adminusuarios(
	id_usuario varchar(10) primary key,
	nom_usuario varchar(500) not null,
	contrasena varchar(10) not null
);

create table categorias(
	id_categoria varchar(100) primary key,
	nom_categoria text not null
);

create table prestamos(
	isbn varchar(15) not null,
	id_usuario varchar(5) not null,
	fecha_prestamo date not null,
	fecha_devolucion date
);

insert into categorias values("c0001", "Fantasia");
insert into categorias values("c0002", "Ingenieria");
insert into categorias values("c0003", "Medicina");
insert into categorias values("c0004", "Thriller");
insert into categorias values("c0005", "Superacion Personal");
insert into categorias values("c0006", "Infantil");

insert into usuarios values("u0001", "Sergio Peralta", null, "sergiop");
insert into usuarios values("u0002", "Jose Luis Perez", "Deudor", "josep");
insert into usuarios values("u0003", "Laura Morales", "Deudor", "lauram");
insert into usuarios values("u0004", "Luis Medina", null, "luism");

insert into adminusuarios values("adm0001", "Sergio Peralta", "sergiop");

insert into autores values("a0001", "Walter Riso");
insert into autores values("a0002", "Harvey M. Deitel");
insert into autores values("a0003", "Paul J. Deitel");
insert into autores values("a0004", "Paulo Coelho");
insert into autores values("a0005", "Nilo Ney Coutinho Menezes");
insert into autores values("a0006", "Stephen King");

insert into editoriales values("e0001", "AlfaOmega");
insert into editoriales values("e0002", "Planeta");
insert into editoriales values("e0003", "Debolsillo");
insert into editoriales values("e0004", "Pearson Prentice Hall");

insert into libros values("9786070768828","9786070768828.png","Atrevete a ser quien eres","Walter Riso", "","Planeta","2019", "1ra edicion", 10, "Superacion Personal");


SELECT 
libros.isbn, 
libros.nom_libro, 
libros.autor, 
libros.editorial, 
libros.anio_publicacion, 
libros.edicion,
usuarios.id_usuario,
usuarios.nom_usuario,
usuarios.estado_usuario, 
prestamos.fecha_prestamo,
prestamos.fecha_devolucion 
from libros, usuarios, prestamos 
where 
libros.isbn = prestamos.isbn and
usuarios.id_usuario = prestamos.id_usuario;