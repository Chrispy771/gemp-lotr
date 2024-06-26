package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Spot your [ORC] minion to play an [ORC] possession from your discard pile (or, if that minion is at
 * a battleground site, from your draw deck or discard pile).
 */
public class Card11_106 extends AbstractEvent {
    public Card11_106() {
        super(Side.SHADOW, 0, Culture.ORC, "Armed for Battle", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC, CardType.MINION)
                && (PlayConditions.location(game, Keyword.BATTLEGROUND) || PlayConditions.canPlayFromDiscard(self.getOwner(), game, Culture.ORC, CardType.POSSESSION));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<>();
        possibleEffects.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ORC, CardType.POSSESSION));
        if (PlayConditions.location(game, Keyword.BATTLEGROUND))
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.ORC, CardType.POSSESSION));
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
