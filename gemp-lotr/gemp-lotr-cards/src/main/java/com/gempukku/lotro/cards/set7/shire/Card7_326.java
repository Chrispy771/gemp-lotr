package com.gempukku.lotro.cards.set7.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.MakeRingBearerEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Ring-bound. Fellowship: Play a Hobbit companion to remove a burden. Response: If Frodo is killed, make
 * Sam the Ring-bearer (resistance 5).
 */
public class Card7_326 extends AbstractCompanion {
    public Card7_326() {
        super(2, 3, 4, 5, Culture.SHIRE, Race.HOBBIT, Signet.ARAGORN, "Sam", "Needer of Vittles", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, CardType.COMPANION, Race.HOBBIT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, CardType.COMPANION, Race.HOBBIT));
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, Filters.frodo, Filters.ringBearer)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(new MakeRingBearerEffect(self));
            return Collections.singletonList(action);
        }

        return null;
    }
}
