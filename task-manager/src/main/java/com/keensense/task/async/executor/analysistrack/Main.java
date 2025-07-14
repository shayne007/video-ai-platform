package com.keensense.task.async.executor.analysistrack;

import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine();

        // find the largest substring that is pa
        int max = 0;
        for(int i=0;i< str.length();i++){
            for(int j= str.length();j>i;j--){
                String toBeJudged = str.substring(i,j);
                if(isPa(toBeJudged)){
                    max =  Math.max(max,toBeJudged.length());
                }
            }
        }
        System.out.print(max);
    }

    private static boolean isPa(String toBeJudged) {
        StringBuilder sb = new StringBuilder(toBeJudged);
        return toBeJudged.equals(sb.reverse().toString());
    }
}