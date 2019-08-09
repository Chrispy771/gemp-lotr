package com.gempukku.lotro.cards.set12.shire;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 2
 * Vitality: 3
 * Resistance: 8
 * Game Text: Sam cannot be overwhelmed unless his strength is tripled. Each time the Free Peoples player assigns Rosie
 * Cotton to a skirmish, add 2 burdens.
 */
public class Card12_129 extends AbstractCompanion {
    public Card12_129() {
        super(1, 2, 3, 8, Culture.SHIRE, Race.HOBBIT, null, "Rosie Cotton", "Barmaid", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new OverwhelmedByMultiplierModifier(self, Filters.sam, 3));
}

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedToSkirmish(game, effectResult, Side.FREE_PEOPLE, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
