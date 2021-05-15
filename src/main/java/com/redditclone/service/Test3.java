package com.redditclone.service;

import java.util.Arrays;

public class Test3 {


    public static int solution(int[] a){
        int result=0;
        boolean isZero=Arrays.stream(a).anyMatch((i->i==0));
        if(!isZero){
            long count= Arrays.stream(a).filter(i -> i < 0).count();
            if(count%2==0){
                result=1;
            }else{
                result=-1;
            }
        }
        return result;
    }

    public static void main(String[] args) {

        System.out.println(solution(new int[]{1,2,3,-5}));

    }

}
