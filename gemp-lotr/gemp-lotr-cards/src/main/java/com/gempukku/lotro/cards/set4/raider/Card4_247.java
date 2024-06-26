package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Possession • Ranged Weapon
 * Game Text: Bearer must be a [RAIDER] Man. Bearer is an archer. While you can spot another [RAIDER] Man, the minion
 * archery total is +1.
 */
public class Card4_247 extends AbstractAttachable {
    public Card4_247() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.RAIDER, PossessionClass.RANGED_WEAPON, "Southron Bow");
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.RAIDER, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
        modifiers.add(
                new ArcheryTotalModifier(self, Side.SHADOW,
                        new SpotCondition(Filters.not(Filters.hasAttached(self)), Culture.RAIDER, Race.MAN), 1));
        return modifiers;
    }
}
