package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Companion • Elf
 * Strength: 9
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot 3 Elf companions. While Naith Warband bears a ranged weapon, it takes no more than 1 wound
 * during each skirmish phase and does not add to the fellowship archery total.
 */
public class Card6_023 extends AbstractCompanion {
    public Card6_023() {
        super(4, 9, 3, 6, Culture.ELVEN, Race.ELF, null, "Naith Warband", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Race.ELF, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new DoesNotAddToArcheryTotalModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.countActive(game, self, Filters.hasAttached(PossessionClass.RANGED_WEAPON)) > 0;
                            }
                        }));
        modifiers.add(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, self, Filters.hasAttached(PossessionClass.RANGED_WEAPON)));
        return modifiers;
    }
}
