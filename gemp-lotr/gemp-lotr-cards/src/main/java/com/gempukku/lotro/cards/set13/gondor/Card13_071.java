package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 1
 * Game Text: Knight. Isildur is resistance +1 for each card in your hand. At the end of any phase, if Isildur has
 * resistance 0, place him in your dead pile. Each time you reconcile, you may draw a card.
 */
public class Card13_071 extends AbstractCompanion {
    public Card13_071() {
        super(3, 7, 3, 1, Culture.GONDOR, Race.MAN, null, "Isildur", "Heir of Elendil", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new ResistanceModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return game.getGameState().getHand(self.getOwner()).size();
                    }
                }));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfPhase(game, effectResult, null)
                && game.getModifiersQuerying().getResistance(game, self) == 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new KillEffect(self, self, KillEffect.Cause.CARD_EFFECT));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.reconciles(game, effectResult, playerId)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
