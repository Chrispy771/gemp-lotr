package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Possession • Mount
 * Strength: +4
 * Vitality: +2
 * Game Text: Bearer must be an [ORC] Orc with strength 9 or less. Bearer is fierce.
 */
public class Card17_092 extends AbstractAttachable {
    public Card17_092() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.ORC, PossessionClass.MOUNT, "Vicious Warg");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ORC, Race.ORC, Filters.lessStrengthThan(10));
    }

    @Override
    public int getStrength() {
        return 4;
    }

    @Override
    public int getVitality() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        return modifiers;
    }
}
