package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public class PlayersCantUseCardPhaseSpecialAbilitiesModifier extends AbstractModifier {
    private final Phase _phase;
    private final Filter _sourceFilters;

    public PlayersCantUseCardPhaseSpecialAbilitiesModifier(PhysicalCard source, Phase phase, Filterable... sourceFilters) {
        this(source, null, phase, sourceFilters);
    }

    public PlayersCantUseCardPhaseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, Phase phase, Filterable... sourceFilters) {
        super(source, "Can't use " + phase.getHumanReadable() + " special abilities", null, condition, ModifierEffect.ACTION_MODIFIER);
        _phase = phase;
        _sourceFilters = Filters.and(sourceFilters);
    }

    @Override
    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && action.getActionTimeword() == _phase
                && _sourceFilters.accepts(game, action.getActionSource()))
            return false;
        return true;
    }
}
