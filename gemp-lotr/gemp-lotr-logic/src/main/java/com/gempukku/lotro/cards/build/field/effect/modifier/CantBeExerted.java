package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import org.json.simple.JSONObject;

public class CantBeExerted implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter", "condition", "by");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");
        final String filter = FieldUtils.getString(object.get("filter"), "filter");
        final String byFilter = FieldUtils.getString(object.get("by"), "by", "any");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final FilterableSource byFilterableSource = environment.getFilterFactory().generateFilter(byFilter, environment);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return (actionContext) -> {
                    return new CantExertWithCardModifier(actionContext.getSource(),
                            filterableSource.getFilterable(actionContext),
                            new RequirementCondition(requirements, actionContext),
                            byFilterableSource.getFilterable(actionContext));
        };
    }
}
