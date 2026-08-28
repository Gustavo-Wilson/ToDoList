create table tarefas(
    id integer not null auto_increment,
    titulo varchar(20) not null,
    descricao varchar(100),
    terminada boolean not null default false,
    prioridade enum('baixa','media','alta') not null,
    data_vencimento date,
    id_usuario integer not null,
    id_categoria integer,

    primary key (id),
    foreign key (id_usuario)
        references usuarios(id),
    foreign key (id_categoria)
        references categorias(id)
);