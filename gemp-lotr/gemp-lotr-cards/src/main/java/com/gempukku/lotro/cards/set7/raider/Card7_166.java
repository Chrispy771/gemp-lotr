package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Archery: Spot 4 Free Peoples cultures and either exert this minion or remove a threat to wound
 * a companion (except the Ring-bearer).
 */
public class Card7_166 extends AbstractMinion {
    public Card7_166() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Southron Leader");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && new CountCulturesEvaluator(Side.FREE_PEOPLE).evaluateExpression(game, null) >= 4
                && (PlayConditions.canSelfExert(self, game) || PlayConditions.canRemoveThreat(game, self, 1))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<>();
            possibleCosts.add(
                    new SelfExertEffect(action, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert this minion";
                        }
                    });
            possibleCosts.add(
                    new RemoveThreatsEffect(self, 1) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a threat";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
