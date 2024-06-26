package com.gempukku.lotro.cards.set10.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot a Dwarf. Each time you lose initiative (except during the fellowship phase), you may play
 * a [DWARVEN] condition from hand or from your discard pile.
 */
public class Card10_002 extends AbstractPermanent {
    public Card10_002() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, "Memories of Darkness", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.DWARF);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesInitiative(effectResult, Side.FREE_PEOPLE)
                && game.getGameState().getCurrentPhase() != Phase.FELLOWSHIP
                && (
                PlayConditions.canPlayFromHand(playerId, game, Culture.DWARVEN, CardType.CONDITION)
                        || PlayConditions.canPlayFromDiscard(playerId, game, Culture.DWARVEN, CardType.CONDITION))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.DWARVEN, CardType.CONDITION));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.DWARVEN, CardType.CONDITION));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
