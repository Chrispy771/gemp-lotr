package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotCultureTokensCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [MEN] minion. While you can spot a culture token, bearer is strength +1 for each
 * companion assigned to a skirmish.
 */
public class Card13_102 extends AbstractAttachable {
    public Card13_102() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MEN, PossessionClass.HAND_WEAPON, "Worn Battleaxe");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, CardType.MINION);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), new CanSpotCultureTokensCondition(1),
                        new CountActiveEvaluator(CardType.COMPANION, Filters.assignedToSkirmish)));
        return modifiers;
    }
}
