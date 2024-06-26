package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 9
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: While you cannot spot 3 companions of the same culture, Gandalf is strength -2.
 */
public class Card7_036 extends AbstractCompanion {
    public Card7_036() {
        super(4, 9, 4, 6, Culture.GANDALF, Race.WIZARD, Signet.GANDALF, "Gandalf", "Defender of the West", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(final LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                Set<Culture> companionCultures = new HashSet<>();
                                for (PhysicalCard companion : Filters.filterActive(game, CardType.COMPANION))
                                    companionCultures.add(companion.getBlueprint().getCulture());

                                for (Culture companionCulture : companionCultures) {
                                    if (PlayConditions.canSpot(game, 3, CardType.COMPANION, companionCulture))
                                        return false;
                                }

                                return true;
                            }
                        }, -2));
    }
}
