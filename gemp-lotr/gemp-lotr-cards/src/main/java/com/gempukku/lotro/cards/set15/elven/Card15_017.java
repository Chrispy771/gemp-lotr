package com.gempukku.lotro.cards.set15.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot an Elf. While the fellowship is at a battleground site, Haldir is strength +2.
 * While the fellowship is at a forest site, Haldir is an archer.
 */
public class Card15_017 extends AbstractCompanion {
    public Card15_017() {
        super(2, 5, 3, 6, Culture.ELVEN, Race.ELF, null, "Haldir", "Sentry of the Golden Wood", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), 2));
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.FOREST), Keyword.ARCHER, 1));
        return modifiers;
    }
}
