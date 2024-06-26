package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 10
 * Type: Minion • Uruk-Hai
 * Strength: 17
 * Vitality: 4
 * Site: 5
 * Game Text: Damage +1 (or damage +2 if at a battleground). To play, spot an Uruk-hai. While at a battleground, this
 * minion is fierce.
 */
public class Card4_200 extends AbstractMinion {
    public Card4_200() {
        super(10, 17, 4, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Vanguard");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.URUK_HAI);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), Keyword.DAMAGE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), Keyword.FIERCE, 1));
        return modifiers;
    }
}
