package com.gempukku.lotro.cards.set12.dwarven;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: For each artifact and possession Thrarin bears, he is damage +1.
 */
public class Card12_015 extends AbstractCompanion {
    public Card12_015() {
        super(3, 7, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Thrarin", "Smith of Erebor", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, self, null, Keyword.DAMAGE, new CountActiveEvaluator(Filters.or(CardType.POSSESSION, CardType.ARTIFACT), Filters.attachedTo(self))));
}
}
