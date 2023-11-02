package com.itson.dominio;

import com.itson.dominio.Persona;
import com.itson.dominio.Placa;
import com.itson.recursos.clases.DuracionLicencia;
import com.itson.recursos.clases.TipoLicencia;
import java.util.Calendar;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-02T16:16:39", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Licencia.class)
public class Licencia_ extends Tramite_ {

    public static volatile SingularAttribute<Licencia, DuracionLicencia> vigencia;
    public static volatile SingularAttribute<Licencia, Calendar> fechaExpedicion;
    public static volatile SingularAttribute<Licencia, Persona> persona;
    public static volatile SingularAttribute<Licencia, Calendar> fechaMax;
    public static volatile SingularAttribute<Licencia, TipoLicencia> tipoLicencia;
    public static volatile ListAttribute<Licencia, Placa> placas;

}