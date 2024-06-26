package com.gempukku.lotro.cards.set5.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Spot a Dwarf to Draw a card or play a [DWARVEN] condition from your discard pile.
 */
public class Card5_006 extends AbstractEvent {
    public Card5_006() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Defending the Keep", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);

        List<Effect> possibleEffects = new LinkedList<>();
        possibleEffects.add(
                new DrawCardsEffect(action, playerId, 1));
        possibleEffects.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.DWARVEN, CardType.CONDITION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play a DWARVEN condition from your discard pile";
                    }
                });

        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
