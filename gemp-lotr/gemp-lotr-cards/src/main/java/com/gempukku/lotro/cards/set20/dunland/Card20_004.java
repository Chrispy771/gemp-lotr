package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * 3
 * Dunlending Marauder
 * Dunland	Minion • Man
 * 9	1	3
 * Dunlending Marauder is strength +1 for each [Dunland] event in your discard pile.
 */
public class Card20_004 extends AbstractMinion {
    public Card20_004() {
        super(3, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Marauder");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                return Filters.filter(game.getGameState().getDiscard(self.getOwner()), game, Culture.DUNLAND, CardType.EVENT).size();
                            }
                        }));
        return modifiers;
    }
}
