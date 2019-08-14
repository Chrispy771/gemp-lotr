package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.CommonEffects;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Fortress of Rohan
 * Rohan	Possession • Support Area
 * Fortification.
 * To play spot Theoden.
 * At the start of each skirmish involving a [Rohan] Man, each minion must exert.
 * Discard this condition at the start of the regroup phase.
 */
public class Card20_326 extends AbstractPermanent {
    public Card20_326() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.ROHAN, "Fortress of Rohan");
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.name(Names.theoden));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.isActive(game, Culture.ROHAN, Race.MAN, Filters.inSkirmish)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.MINION));
            return Collections.singletonList(action);
        }
        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
