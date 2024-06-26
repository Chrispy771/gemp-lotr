package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: While you can spot a [GANDALF] companion, Theoden gains muster. (At the start of the regroup phase, you
 * may discard a card from hand to draw a card.) While Theoden is at a battleground or plains site each [ROHAN] Man
 * is strength +1.
 */
public class Card11_159 extends AbstractCompanion {
    public Card11_159() {
        super(3, 7, 3, 6, Culture.ROHAN, Race.MAN, null, Names.theoden, "King of the Eorlingas", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Culture.GANDALF, CardType.COMPANION), Keyword.MUSTER, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.ROHAN, Race.MAN), new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.PLAINS)), 1));
        return modifiers;
    }
}
