package com.gempukku.lotro.cards.set12.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Spot a [ROHAN] Man to discard a possession from play. You may exert a [ROHAN] Man who has resistance 3
 * or more to play this event during the maneuver phase.
 */
public class Card12_114 extends AbstractEvent {
    public Card12_114() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "For the Mark", Phase.REGROUP, Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN)
                && (game.getGameState().getCurrentPhase() != Phase.MANEUVER || PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN, Filters.minResistance(3)));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        if (PlayConditions.isPhase(game, Phase.MANEUVER))
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, Race.MAN, Filters.minResistance(3)));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
        return action;
    }
}
