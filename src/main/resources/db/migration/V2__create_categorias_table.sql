create table categorias (
    id integer not null auto_increment,
    nome varchar(20) not null,
    id_usuario integer not null,

    primary key (id),
    unique (id_usuario, nome),
    foreign key (id_usuario)
        references usuarios(id)
);