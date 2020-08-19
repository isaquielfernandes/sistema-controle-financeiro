package com.blue.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contacto.class)
public abstract class Contacto_ {

	public static volatile SingularAttribute<Contacto, Long> codigo;
	public static volatile SingularAttribute<Contacto, String> nome;
	public static volatile SingularAttribute<Contacto, String> email;
	public static volatile SingularAttribute<Contacto, String> telefone;
	public static volatile SingularAttribute<Contacto, Pessoa> pessoa;
}
