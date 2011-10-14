package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PreventEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Response: If your opponent is about to take control of a site, exert Ceorl and a villager to prevent this.
 */
public class Card4_264 extends AbstractCompanion {
    public Card4_264() {
        super(2, 6, 3, Culture.ROHAN, Race.MAN, null, "Ceorl", true);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.TAKE_CONTROL_OF_A_SITE) {
            TakeControlOfASiteEffect takeControlEffect = (TakeControlOfASiteEffect) effect;
            if (!takeControlEffect.isPrevented()
                    && PlayConditions.canExert(self, game, Filters.sameCard(self))
                    && PlayConditions.canExert(self, game, Filters.keyword(Keyword.VILLAGER))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.keyword(Keyword.VILLAGER)));
                action.appendEffect(
                        new PreventEffect(takeControlEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
