package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. When you play this, you may spot a companion who has resistance 0 to make this minion
 * twilight cost -2.
 */
public class Card13_168 extends AbstractMinion {
    public Card13_168() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Aggressor");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (Filters.canSpot(game, CardType.COMPANION, Filters.maxResistance(0)))
            return -2;
        return 0;
    }
}
