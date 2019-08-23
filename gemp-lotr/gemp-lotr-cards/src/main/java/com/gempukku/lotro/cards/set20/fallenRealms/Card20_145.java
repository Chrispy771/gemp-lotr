package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

/**
 * 5
 * Strike from the South
 * Fallen Realms	Event • Skirmish
 * Wound a companion skirmishing a Southron for each Free Peoples card that companion bears.
 */
public class Card20_145 extends AbstractEvent {
    public Card20_145() {
        super(Side.SHADOW, 5, Culture.FALLEN_REALMS, "Strike from the South", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.inSkirmishAgainst(Keyword.SOUTHRON), Filters.canTakeWounds(self, 1)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = Filters.countActive(game, Side.FREE_PEOPLE, Filters.attachedTo(card));
                        for (int i=0; i< count; i++)
                            action.appendEffect(
                                    new WoundCharactersEffect(self, card));
                    }
                });
        return action;
    }
}
