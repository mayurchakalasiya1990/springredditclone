package com.redditclone.service;

public class Test1 {




    public static String gameWinner(String colors) {
        // Write your code here
        String result="";
        String player="www";
        for (int i = 0; i < colors.length()-1; i++) {

            if(i%2==0){
                player="www";
            }else{
                player="bbb";
            }

            int removeId=colors.indexOf(player);
            if(removeId==-1){

                if(player.equals("www")){
                    result="Wendy";
                }else{
                    result="Bob";
                }
                break;
            }else{
                colors=colors.substring(0,removeId)+colors.substring(removeId+1);
            }
        }
        return result;
    }



    public static void main(String[] args) {
        String input="wwwbb";

        System.out.println(gameWinner(input));

    }

}
