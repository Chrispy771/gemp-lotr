package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Shadow: Remove (1) and exert Gollum to discard the top card of your draw deck. If that card is a [GOLLUM]
 * or [WRAITH] card, take it into hand.
 */
public class Card7_062 extends AbstractPermanent {
    public Card7_062() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.GOLLUM, "It's Mine");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 1)
                && PlayConditions.canExert(self, game, Filters.gollum)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gollum));
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, playerId, false) {
                        @Override
                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                final Culture culture = card.getBlueprint().getCulture();
                                if (culture == Culture.GOLLUM || culture == Culture.WRAITH)
                                    action.appendEffect(
                                            new PutCardFromDiscardIntoHandEffect(card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
