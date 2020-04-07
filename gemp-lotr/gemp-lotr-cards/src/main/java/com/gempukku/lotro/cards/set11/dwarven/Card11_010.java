package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.PreventableCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: Response: If a [DWARVEN] companion is about to take a wound in a skirmish, discard 2 [DWARVEN] cards from
 * play (except characters) to prevent that.
 */
public class Card11_010 extends AbstractCompanion {
    public Card11_010() {
        super(3, 7, 3, 7, Culture.DWARVEN, Race.DWARF, null, "Grimir", "Dwarven Emissary", true);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.DWARF, CardType.COMPANION)
                && PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canDiscardFromPlay(self, game, 2, Culture.DWARVEN, Filters.not(Filters.character))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Culture.DWARVEN, Filters.not(Filters.character)));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (PreventableCardEffect) effect, playerId, "Choose a DWARVEN companion", Culture.DWARVEN, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
