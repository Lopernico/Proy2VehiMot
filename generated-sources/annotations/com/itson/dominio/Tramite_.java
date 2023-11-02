package com.itson.dominio;

import com.itson.dominio.Pago;
import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-02T16:16:39", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Tramite.class)
public abstract class Tramite_ { 

    public static volatile SingularAttribute<Tramite, Double> costo;
    public static volatile SingularAttribute<Tramite, Calendar> fechaEmision;
    public static volatile SingularAttribute<Tramite, Long> id;
    public static volatile SingularAttribute<Tramite, Pago> pago;

}