package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotBurdensCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Ambush (1). While you can spot 4 burdens, this minion is strength +5. While you can spot
 * 5 burdens, this minion is fierce. While you can spot 6 burdens, this minion is damage +1.
 */
public class Card4_218 extends AbstractMinion {
    public Card4_218() {
        super(6, 13, 3, 4, Race.MAN, Culture.RAIDER, "Desert Legion", null, true);
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotBurdensCondition(4), 5));
        modifiers.add(
                new KeywordModifier(self, self, new SpotBurdensCondition(5), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotBurdensCondition(6), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
