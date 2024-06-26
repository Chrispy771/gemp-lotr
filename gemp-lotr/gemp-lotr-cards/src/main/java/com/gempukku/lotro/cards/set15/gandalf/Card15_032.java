package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Choose one: spot an Ent to take a [GANDALF] companion into hand from your draw deck; or spot 2 Ents
 * to take a [GANDALF] companion and a [GANDALF] condition into hand from your draw deck. Shuffle your draw deck.
 */
public class Card15_032 extends AbstractEvent {
    public Card15_032() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Momentous Gathering", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.ENT);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<>();
        possibleEffects.add(
                new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.COMPANION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Take a GANDALF companion into hand from your draw deck";
                    }
                });
        if (PlayConditions.canSpot(game, 2, Race.ENT)) {
            possibleEffects.add(
                    new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.COMPANION) {
                        @Override
                        protected FullEffectResult playEffectReturningResult(LotroGame game) {
                            final FullEffectResult fullEffectResult = super.playEffectReturningResult(game);
                            action.appendEffect(
                                    new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.CONDITION));
                            return fullEffectResult;
                        }

                        @Override
                        public String getText(LotroGame game) {
                            return "Take a GANDALF companion and condition into hand from your draw deck";
                        }
                    });
        }
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        action.appendEffect(
                new ShuffleDeckEffect(playerId));
        return action;
    }
}
