package com.redditclone.service;

import java.util.List;

public class Test2 {

    public static int countTeams(List<Integer> skills, int minPlayers, int minLevel, int maxLevel) {
        int result=0;
        int playerSkill=0;
        int currTeamSize=0;
        for (int i = 0; i < skills.size()-1; i++) {
            playerSkill=skills.get(i).intValue();
            if( playerSkill<minPlayers || playerSkill>maxLevel){
                skills.remove(i);
            }
        }
        if(skills.isEmpty()){
            return 0;
        }
        currTeamSize=skills.size()-1;
        result=1;
        while (currTeamSize>=minLevel){

        }


        return result;
    }

    public static void main(String[] args) {
        List<Integer> skills=List.of(12,4,6,13,5,10);
        int minPlayers=3;
        int minLevel=4;
        int maxLevel=10;
    }
}
