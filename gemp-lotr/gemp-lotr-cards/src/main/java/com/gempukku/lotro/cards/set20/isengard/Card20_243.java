package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Wizard’s Wrath
 * Isengard	Event • Response
 * Spell.
 * If Saruman wins a skirmish, wound an unbound companion.
 */
public class Card20_243 extends AbstractResponseEvent {
    public Card20_243() {
        super(Side.SHADOW, 2, Culture.ISENGARD, "Wizard's Wrath");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.saruman)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
