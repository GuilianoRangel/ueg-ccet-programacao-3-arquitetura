create table fake_table (
    pk integer not null,
    nome varchar(50) not null,
    telefone varchar(20) null,
    primary key(pk)
);
insert into fake_table(pk, nome, telefone)
values(1,'Nome Fake 01','62-3333-3333');
insert into fake_table(pk, nome, telefone)
values(2,'Nome Fake 02','62-3333-3333');
insert into fake_table(pk, nome)
values(3,'Nome Fake 03');

-- select * from fake_table;