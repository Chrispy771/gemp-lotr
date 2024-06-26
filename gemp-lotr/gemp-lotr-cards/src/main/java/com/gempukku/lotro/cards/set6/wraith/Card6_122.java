package com.gempukku.lotro.cards.set6.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. Each time a companion is killed in a skirmish involving a Nazgul, wound an ally twice
 * or exert a companion.
 */
public class Card6_122 extends AbstractMinion {
    public Card6_122() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.witchKing, "Deathless Lord", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.forEachKilledInASkirmish(game, effectResult, Race.NAZGUL, CardType.COMPANION)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<>();
            if (Filters.canSpot(game,
                    CardType.ALLY, Filters.canTakeWounds(self, 2))) {
                possibleEffects.add(
                        new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 2, CardType.ALLY) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Wound an ally twice";
                    }
                });
            } else if (!Filters.canSpot(game,
                    CardType.COMPANION, Filters.canExert(self))) {
                possibleEffects.add(
                        new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 1, CardType.ALLY) {
                    @Override
                    protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                        for (PhysicalCard card : cards)
                            action.appendEffect(new WoundCharactersEffect(self, card));
                    }
                });
            }
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a companion";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
