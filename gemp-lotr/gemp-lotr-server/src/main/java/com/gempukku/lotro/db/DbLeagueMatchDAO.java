package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.league.LeagueSerieData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbLeagueMatchDAO implements LeagueMatchDAO {
    private DbAccess _dbAccess;

    public DbLeagueMatchDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    @Override
    public Collection<LeagueMatch> getLeagueMatches(League league) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select winner, loser from league_match where league_type=?");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);

                            result.add(new LeagueMatch(winner, loser));
                        }
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public Collection<LeagueMatch> getLeagueSerieMatches(League league, LeagueSerieData leagueSerie) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select winner, loser from league_match where league_type=? and season_type=?");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, leagueSerie.getName());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);

                            result.add(new LeagueMatch(winner, loser));
                        }
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public void addPlayedMatch(League league, LeagueSerieData leagueSeason, LeagueMatch match) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_match (league_type, season_type, winner, loser) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, leagueSeason.getName());
                    statement.setString(3, match.getWinner());
                    statement.setString(4, match.getLoser());
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }
}