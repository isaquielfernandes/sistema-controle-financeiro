CREATE TABLE categoria(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria(nome) values('Lazer');
INSERT INTO categoria(nome) values('Alimentação');
INSERT INTO categoria(nome) values('Supermercado');
INSERT INTO categoria(nome) values('Farmácia');
INSERT INTO categoria(nome) values('Outros');
INSERT INTO categoria(nome) values('Financiamento');
INSERT INTO categoria(nome) values('Extraordinárias');
INSERT INTO categoria(nome) values('Impostos');
INSERT INTO categoria(nome) values('Despesas bancárias');
INSERT INTO categoria(nome) values('Restaurantes');
INSERT INTO categoria(nome) values('Informática');
INSERT INTO categoria(nome) values('Transire');