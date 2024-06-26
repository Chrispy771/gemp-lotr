package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 7
 * Game Text: While Legolas is at a battleground or forest site, he is strength +2 and resistance +2.
 */
public class Card11_021 extends AbstractCompanion {
    public Card11_021() {
        super(2, 6, 3, 7, Culture.ELVEN, Race.ELF, null, "Legolas", "Companion of the Ring", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Filters.or(Keyword.FOREST, Keyword.BATTLEGROUND)), 2));
        modifiers.add(
                new ResistanceModifier(self, self, new LocationCondition(Filters.or(Keyword.FOREST, Keyword.BATTLEGROUND)), 2));
        return modifiers;
    }
}
