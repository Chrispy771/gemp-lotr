package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.cards.build.field.effect.appender.resolver.TimeResolver;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilModifierEffect extends UnrespondableEffect {
    private Modifier _modifier;
    private TimeResolver.Time until;

    public AddUntilModifierEffect(Modifier modifier, TimeResolver.Time until) {
        _modifier = modifier;
        this.until = until;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        Phase phase = until.getPhase();
        if (phase == null)
            phase = game.getGameState().getCurrentPhase();

        if (until.isStart())
            game.getModifiersEnvironment().addUntilStartOfPhaseModifier(_modifier, phase);
        else
            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(_modifier, phase);
    }
}
