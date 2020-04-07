package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.logic.modifiers.SanctuaryHealModifier;
import org.json.simple.JSONObject;

public class ModifySanctuaryHeal implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "condition", "amount");

        final ValueSource amountSource = ValueResolver.resolveEvaluator(object.get("amount"), environment);
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return (actionContext) -> {
            return new SanctuaryHealModifier(actionContext.getSource(),
                    new RequirementCondition(requirements, actionContext),
                    amountSource.getEvaluator(actionContext));
        };
    }
}
