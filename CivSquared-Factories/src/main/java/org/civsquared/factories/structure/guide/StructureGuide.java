package org.civsquared.factories.structure.guide;

import lombok.Data;
import lombok.NonNull;
import org.civsquared.factories.structure.model.StructureModel;

@Data
public class StructureGuide {
    @NonNull
    private StructureModel model;
}
