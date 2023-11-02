package com.itson.dominio;

import com.itson.dominio.Persona;
import com.itson.dominio.Placa;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-02T16:16:39", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Automovil.class)
public class Automovil_ { 

    public static volatile SingularAttribute<Automovil, String> marca;
    public static volatile SingularAttribute<Automovil, Persona> persona;
    public static volatile SingularAttribute<Automovil, String> color;
    public static volatile SingularAttribute<Automovil, Long> id;
    public static volatile SingularAttribute<Automovil, String> numSerie;
    public static volatile SingularAttribute<Automovil, String> modelo;
    public static volatile SingularAttribute<Automovil, String> linea;
    public static volatile ListAttribute<Automovil, Placa> placas;

}