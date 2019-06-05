package com.sudo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author chensc
 * @date 2021/3/7 13:58
 */
public class Sudoku {
    public static void main(String[] args) {
//        Sudoku sudoku = new Sudoku();
//        int[][] arr = sudoku.init();
//        arr = sudoku.digui(arr);
//        System.out.println(Arrays.deepToString(arr));
        int d = 2;
        System.out.println(Math.pow(2,10));
    }

    //数独初始化
    public int[][] init(){
        int[][] arr = {
                {7,0,0 ,0,0,4 ,0,2,0},
                {0,9,0 ,0,0,0 ,3,0,0},
                {0,0,0 ,0,0,6 ,0,0,8},

                {0,8,0 ,9,0,0 ,0,0,0},
                {0,3,5 ,0,0,0 ,0,0,9},
                {0,0,0 ,0,7,2 ,0,4,0},

                {0,0,9 ,5,2,0 ,0,0,0},
                {0,0,0 ,0,0,0 ,8,6,7},
                {1,0,0 ,3,0,0 ,0,0,0}
        };
        return arr;
    }

    //判断全填完
    public boolean is_full(int[][] arr){
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                if(arr[x][y] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    //获取当前值有可能的值
    public ArrayList<Integer> possible(int x,int y,int[][] arr){
        ArrayList<Integer> possible_arr = new ArrayList<Integer>();
        if(x>9 || x<0 || y>9 || y<0){
            return possible_arr;
        }


        //行判断
        ArrayList<Integer> col_arr = new ArrayList<>();
        for(int col=0 ; col<9; col++){
            if(arr[x][col] != 0){
                col_arr.add(arr[x][col]);
            }
        }

        //列判断
        ArrayList<Integer> row_arr = new ArrayList<>();
        for(int row=0 ; row<9; row++){
            if(arr[row][y] != 0) row_arr.add(arr[row][y]);
        }

        //圈判断
        ArrayList<Integer> circle_arr = new ArrayList<>();
        int circle_x = (int) Math.floor(x/3);
        int circle_y = (int) Math.floor(y/3);
        for(int c_x=3*circle_x;c_x<3+3*circle_x; c_x++){
            for(int c_y=3*circle_y;c_y<3+3*circle_y; c_y++){
                if(arr[c_x][c_y] != 0) circle_arr.add(arr[c_x][c_y]);
            }
        }


        int[] full_arr = {1,2,3,4,5,6,7,8,9};
        for(int full=0;full<full_arr.length;full++){
            if(!col_arr.contains(full_arr[full]) && !row_arr.contains(full_arr[full]) && !circle_arr.contains(full_arr[full])){
                possible_arr.add(full_arr[full]);
            }
        }
        return possible_arr;
    }

    //所以可能的值
    public ArrayList<Ge> getPossible(int[][] arr){
        ArrayList<Ge> ge_arr = new ArrayList<>();
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                if(arr[x][y] == 0){
                    ArrayList<Integer> possible = this.possible(x,y,arr);
                    if(possible == null || possible.size()==0){
                        return null;
                    }
                    Ge ge = new Ge();
                    ge.setX(x);
                    ge.setY(y);
                    ge.setPossible_arr(possible);
                    ge.setCount(possible.size());
                    ge_arr.add(ge);
                }
            }
        }
        ge_arr.sort(new Comparator<Ge>() {
            @Override
            public int compare(Ge o1, Ge o2) {
                if(o1.getCount() > o2.getCount()){
                    return 1;
                }else if(o1.getCount() < o2.getCount()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        return ge_arr;
    }

    //递归找到解
    public int[][] digui(int[][] arr){
        ArrayList<Ge> geList = this.getPossible(arr);
        if(geList == null) {
            //失败
            return null;
        }else if(geList.size() == 0){
            //成功
            return arr;
        }else{
            //递归查找
            Ge firstGe = geList.get(0);
            int[][] new_arr = new int[9][9];
            for(int s=0;s<firstGe.getCount();s++){
                arr[firstGe.getX()][firstGe.getY()] = firstGe.getPossible_arr().get(s);
                if(this.is_full(arr)){
                    return arr;
                }
                new_arr = this.digui(arr);
                if(new_arr == null){
                    arr[firstGe.getX()][firstGe.getY()] = 0;
                }else if(this.is_full(new_arr)){
                    return new_arr;
                }
            }
            return null;
        }
    }

}

class Ge{
    private int x;
    private int y;
    private int count;
    private ArrayList<Integer> possible_arr;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Integer> getPossible_arr() {
        return possible_arr;
    }

    public void setPossible_arr(ArrayList<Integer> possible_arr) {
        this.possible_arr = possible_arr;
    }


    @Override
    public String toString() {
        return "ge{" +
                "x=" + x +
                ", y=" + y +
                ", count=" + count +
                ", possible_arr=" + possible_arr +
                '}';
    }
}
