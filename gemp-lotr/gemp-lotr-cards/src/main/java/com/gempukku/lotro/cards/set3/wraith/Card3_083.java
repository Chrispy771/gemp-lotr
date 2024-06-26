package com.gempukku.lotro.cards.set3.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachBurdenEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time the fellowship moves, add (1) for each burden you can spot. At
 * the end of each of your Shadow phases, exert a Nazgul or discard this condition.
 */
public class Card3_083 extends AbstractPermanent {
    public Card3_083() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, "The Ring Draws Them", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, new ForEachBurdenEvaluator()));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.SHADOW)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<>();
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Race.NAZGUL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a Nazgul";
                        }
                    });
            possibleEffects.add(
                    new SelfDiscardEffect(self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this condition";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
