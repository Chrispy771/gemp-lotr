package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: To play, spot Galadriel or Celeborn. While Galadriel is the Ring-bearer, she is strength +2
 * and resistance +2. Each time you play an [ELVEN] skirmish event, you may reinforce an [ELVEN] token.
 */
public class Card18_011 extends AbstractPermanent {
    public Card18_011() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.ELVEN, "Galadriel's Silver Ewer", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.or(Filters.galadriel, Filters.name("Celeborn")));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.galadriel, Filters.ringBearer), 2));
        modifiers.add(
                new ResistanceModifier(self, Filters.and(Filters.galadriel, Filters.ringBearer), 2));
        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(playerId), Culture.ELVEN, Keyword.SKIRMISH, CardType.EVENT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.ELVEN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
