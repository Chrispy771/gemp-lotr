package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: Spell. Exert Gandalf X times to wound a minion X times. If that minion is a Nazgul, wound it again.
 */
public class Card7_050 extends AbstractEvent {
    public Card7_050() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Terrible and Evil", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        int vitality = game.getModifiersQuerying().getVitality(game, Filters.findFirstActive(game, Filters.gandalf, Filters.canExert(self)));
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose how many times you wish to exert Gandalf", 1, vitality - 1) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int exertCount = getValidatedResult(result);
                                action.insertCost(
                                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, exertCount, Filters.gandalf));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, exertCount, CardType.MINION) {
                                            @Override
                                            protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                                                for (PhysicalCard card : cards)
                                                    if (card.getBlueprint().getRace() == Race.NAZGUL)
                                                        action.appendEffect(new WoundCharactersEffect(self, card));
                                            }
                                        });
                            }
                        }));
        return action;
    }
}
