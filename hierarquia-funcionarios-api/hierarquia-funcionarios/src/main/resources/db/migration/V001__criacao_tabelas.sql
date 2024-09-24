
create table funcionario(
    id int not null auto_increment,
    nome varchar(100),
    id_supervisor int references funcionario(id),
    is_supervisor boolean,
    senha varchar (80) not null ,
    senha_score smallint not null,
    version int,
    primary key (id)
);