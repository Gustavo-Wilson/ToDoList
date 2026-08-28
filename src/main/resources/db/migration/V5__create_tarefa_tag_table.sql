create table tarefa_tag(
    id integer not null auto_increment,
    id_tarefa integer not null,
    id_tag integer not null,

    primary key (id),
    unique (id_tarefa, id_tag),
    foreign key (id_tarefa)
        references tarefas(id),
    foreign key (id_tag)
        references tags(id)
);