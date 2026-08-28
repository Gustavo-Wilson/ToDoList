create table usuarios(
    id integer not null auto_increment,
    nome varchar(50) not null,
    email varchar(50) not null,
    senha varchar(50) not null,

    primary key(id),
    unique(email)
    );