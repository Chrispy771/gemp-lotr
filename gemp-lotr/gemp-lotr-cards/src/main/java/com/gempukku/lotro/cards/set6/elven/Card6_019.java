package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.logic.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Spot an Elf to reveal the top card of your draw deck. You may discard it, return it to
 * the top of your draw deck, or take it into hand.
 */
public class Card6_019 extends AbstractEvent {
    public Card6_019() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Gift of Foresight", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        for (final PhysicalCard card : revealedCards) {
                            action.appendEffect(
                                    new PlayoutDecisionEffect(playerId,
                                            new MultipleChoiceAwaitingDecision(1, "Choose what do you want to do with " + GameUtils.getFullName(card), new String[]{"Discard it", "Return to top of deck", "Take into hand"}) {
                                                @Override
                                                protected void validDecisionMade(int index, String result) {
                                                    if (index == 0)
                                                        action.appendEffect(
                                                                new DiscardCardFromDeckEffect(card));
                                                    else if (index == 2)
                                                        action.appendEffect(
                                                                new PutCardFromDeckIntoHandOrDiscardEffect(card, false));
                                                }
                                            }));
                        }
                    }
                });
        return action;
    }
}
