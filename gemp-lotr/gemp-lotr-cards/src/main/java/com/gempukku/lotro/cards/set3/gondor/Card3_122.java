package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Ranger. Fellowship: Add (2) to heal Boromir.
 */
public class Card3_122 extends AbstractCompanion {
    public Card3_122() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, Signet.GANDALF, "Boromir", "Defender of Minas Tirith", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
