package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: While Eomer bears a mount, he is damage +1. While no other companion is assigned to a skirmish, Eomer is
 * strength +1 for each other [ROHAN] companion you can spot.
 */
public class Card13_123 extends AbstractCompanion {
    public Card13_123() {
        super(3, 7, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eomer, "Heir to Meduseld", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(self, Filters.mounted), Keyword.DAMAGE, 1));
        modifiers.add(
                new StrengthModifier(self, self,
                        new NotCondition(new SpotCondition(Filters.not(self), CardType.COMPANION, Filters.assignedToSkirmish)),
                        new CountActiveEvaluator(Filters.not(self), CardType.COMPANION, Culture.ROHAN)));
        return modifiers;
    }
}
