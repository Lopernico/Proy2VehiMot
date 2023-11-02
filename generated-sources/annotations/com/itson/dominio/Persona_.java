package com.itson.dominio;

import com.itson.dominio.Automovil;
import com.itson.dominio.Licencia;
import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-02T16:16:39", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Persona.class)
public class Persona_ { 

    public static volatile SingularAttribute<Persona, Calendar> fechaNacimiento;
    public static volatile SingularAttribute<Persona, String> apellidoP;
    public static volatile ListAttribute<Persona, Automovil> automoviles;
    public static volatile SingularAttribute<Persona, String> apellidoM;
    public static volatile SingularAttribute<Persona, Long> id;
    public static volatile SingularAttribute<Persona, String> telefono;
    public static volatile SingularAttribute<Persona, String> RFC;
    public static volatile ListAttribute<Persona, Licencia> licencias;
    public static volatile SingularAttribute<Persona, String> nombres;

}