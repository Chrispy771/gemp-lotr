package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: Forest. Sanctuary. Each ally whose home is site 6 is strength +3.
 */
public class Card1_353 extends AbstractSite {
    public Card1_353() {
        super("Lothlorien Woods", 6, 3, Direction.LEFT);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(6)), 3);
    }
}
