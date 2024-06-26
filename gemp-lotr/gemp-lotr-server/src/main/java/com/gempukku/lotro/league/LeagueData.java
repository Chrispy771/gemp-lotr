package com.gempukku.lotro.league;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.draft2.SoloDraft;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.util.List;

public interface LeagueData {
    boolean isSoloDraftLeague();

    List<LeagueSerieData> getSeries();

    SoloDraft getSoloDraft();

    CardCollection joinLeague(CollectionsManager collecionsManager, Player player, int currentTime);

    int process(CollectionsManager collectionsManager, List<PlayerStanding> leagueStandings, int oldStatus, int currentTime);

    default int getMaxRepeatMatchesPerSerie() {
        return 1;
    }
}
