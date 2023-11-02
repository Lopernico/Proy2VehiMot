package com.itson.dominio;

import com.itson.dominio.Automovil;
import com.itson.dominio.Licencia;
import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-02T16:16:39", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Placa.class)
public class Placa_ extends Tramite_ {

    public static volatile SingularAttribute<Placa, Automovil> automovil;
    public static volatile SingularAttribute<Placa, Licencia> licencia;
    public static volatile SingularAttribute<Placa, String> numPlaca;
    public static volatile SingularAttribute<Placa, Calendar> fechaRecepcion;

}