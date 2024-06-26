package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. While at a battleground, this minion is fierce. While you control a battleground, this minion
 * is strength +4.
 */
public class Card4_136 extends AbstractMinion {
    public Card4_136() {
        super(5, 10, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Advance Uruk Patrol");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.BATTLEGROUND);
                            }
                        }, Keyword.FIERCE, 1));
        modifiers.add(
                new StrengthModifier(self, self,
                        new SpotCondition(Filters.and(Filters.siteControlled(self.getOwner()), Keyword.BATTLEGROUND)), 4));
        return modifiers;
    }
}
