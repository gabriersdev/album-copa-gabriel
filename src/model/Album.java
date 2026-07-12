package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Album {
    private final int id;
    private int numPlayersPerTeam;
    private List<Team> teams;
    private Map<String, Team> teamMap;

    public Album(int id) {
        this.id = id;
        this.numPlayersPerTeam = 0;
        this.teams = new ArrayList<>();
        this.teamMap = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public int getNumPlayersPerTeam() {
        return numPlayersPerTeam;
    }

    public void setNumPlayersPerTeam(int numPlayersPerTeam) {
        this.numPlayersPerTeam = numPlayersPerTeam;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
        this.teamMap.put(team.getName().toLowerCase(), team);
    }

    public Team getTeam(String name) {
        return teamMap.get(name.toLowerCase());
    }

    public Team getTeam(int index) {
        if (index >= 0 && index < teams.size()) return teams.get(index);
        return null;
    }

    public boolean hasTeam(String name) {
        return teamMap.containsKey(name.toLowerCase());
    }

    public int getNumTeams() {
        return teams.size();
    }
}
