package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Aragorn
 * Game Text: Ring-bearer (resistance 10). The cost of each artifact, possession, and [SHIRE] tale played on Frodo
 * is -1.
 */
public class Card2_102 extends AbstractCompanion {
    public Card2_102() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, Signet.ARAGORN, "Frodo", "Reluctant Adventurer", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "The cost of each artifact, possession, and [SHIRE] tale played on Frodo  is -1.", Filters.or(CardType.ARTIFACT, CardType.POSSESSION, Filters.and(Culture.SHIRE, Keyword.TALE)), ModifierEffect.TWILIGHT_COST_MODIFIER) {
                    @Override
                    public int getTwilightCostModifier(LotroGame game, PhysicalCard physicalCard, PhysicalCard target, boolean ignoreRoamingPenalty) {
                        if (target == self)
                            return -1;
                        return 0;
                    }
                });
    }
}
