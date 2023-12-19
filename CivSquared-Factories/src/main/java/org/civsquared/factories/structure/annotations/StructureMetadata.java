package org.civsquared.factories.structure.annotations;

import org.civsquared.factories.structure.StructureConstructionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StructureMetadata {
    String id();

    String model();

    StructureConstructionType constructionType();
}
