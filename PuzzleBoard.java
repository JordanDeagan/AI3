import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinWorkerThread;

class PuzzleBoard {
    private ArrayList[][] init;
    private ArrayList<ArrayList<Integer>> unfinished;
    private int[] check;
    private BufferedReader reader;
    private int noChanges;
    private Boards full;
    PuzzleBoard(String path){
        unfinished = new ArrayList<>();
        init = new ArrayList[9][9];
        check = new int[9];
        for (int i=0; i<9;i++){
            check[i] = i+1;
        }
//        for (String value: check){
//            System.out.print(value + " ");
//        }
        File rows = new File(path);
        try {
            reader = new BufferedReader(new FileReader(rows));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<9;i++){
            String[] row = new String[9];
            try {
                assert reader != null;
                row = reader.readLine().split(" ", 9);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int j=0; j<9;j++){
                if(row[j].length()>1){
                    row[j] = row[j].substring(0,1);
                }
                if(!row[j].equals("0")){
                    init[i][j] = new ArrayList<>(List.of(Integer.parseInt(row[j])));
                } else {
                    init[i][j] = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
                    unfinished.add(new ArrayList<>(List.of(i, j)));

                }
            }
//            System.out.println(board[i][0] + " " + board[i][1] + " " + board[i][2] + " " + board[i][3] + " " +
//                    board[i][4] + " " + board[i][5] + " " + board[i][6] + " " + board[i][7] + " " + board[i][8]);
        }
        noChanges = 0;
        full = new Boards(init, unfinished);
        full = fillConnections(full);
        System.out.print("\n");
        for(int i=0;i<9;i++){
            for (int j = 0; j<9; j++){
                if(full.getBoard()[i][j].size()==1) {
                    System.out.print(full.getBoard()[i][j].get(0) + " ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        Boards temp = solve(full);
        if(temp != null){
            full.setBoard(temp.getBoard());
            System.out.print("\n");
            for(int i=0;i<9;i++){
                for (int j = 0; j<9; j++){
                    if(full.getBoard()[i][j].size()==1) {
                        System.out.print(full.getBoard()[i][j].get(0) + " ");
                    } else {
                        System.out.print("[" + full.getBoard()[i][j].size() + "] ");
                    }
                }
                System.out.print("\n");
            }
        } else {
            System.out.println("No Solution found");
        }
    }

    private boolean checkRow(Boards board){
        for(int i=0;i<9;i++){
            for(int val: check){
                int count = 0;
                for (int j=0;j<9;j++){
                    if(checkSquare(board, i,j,val)){
                        count++;
                        if(count>1){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCol(Boards board){
        for(int i=0;i<9;i++){
            for(int val: check){
                int count = 0;
                for (int j=0;j<9;j++){
                    if(checkSquare(board, j,i,val)){
                        count++;
                        if(count>1){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkBox(Boards board){
        int[] box = new int[]{0, 3, 6, 9};
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++) {
                for (int val : check) {
                    int count = 0;
                    for (int r = box[i]; r < box[i + 1]; r++) {
                        for (int c = box[j]; c < box[j + 1]; c++) {
                            if(checkSquare(board, r,c,val)){
                                count++;
                                if(count>1){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isFilled(Boards board){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++) {
                if(board.getBoard()[i][j].size()!=1){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isCorrect(Boards board){
        if(isFilled(board)){
            return checkBox(board) && checkCol(board) && checkRow(board);
        } else {
            return false;
        }
    }

    boolean isCorrect(){
        if(isFilled(full)){
            return checkBox(full) && checkCol(full) && checkRow(full);
        } else {
            return false;
        }
    }

    private boolean checkSquare(Boards board, int i, int j, int val){
        if(board.getBoard()[i][j].size()!=1){
            return false;
        } else{
            return board.getBoard()[i][j].contains(val);
        }
    }

    private int boxNum(int x) {
        if(x<3) {
            return 0;
        } else if (x<6) {
            return 3;
        } else {
            return 6;
        }
    }

    private Boards fitConstraints(Boards board, int i, int j){
        Boards work = board;
        ArrayList<Integer> begin = work.getBoard()[i][j];
        work.getBoard()[i][j] = new ArrayList<Integer>(List.of(1,2,3,4,5,6,7,8,9));
//        System.out.println(i+ ", " + j + ": " + work.getBoard()[i][j]);
        for (int x = 0; x<9; x++){
            if(x!=j){
                if(work.getBoard()[i][x].size()==1){
                    work.getBoard()[i][j].remove(work.getBoard()[i][x].get(0));
//                    System.out.println("-"+work.getBoard()[i][x].get(0));
//                    System.out.println(i+ ", " + j + ": " + work.getBoard()[i][j]);
                }
            }
            if(x!=i){
                if(work.getBoard()[x][j].size()==1){
                    work.getBoard()[i][j].remove(work.getBoard()[x][j].get(0));
//                    System.out.println("-"+work.getBoard()[x][j].get(0));
//                    System.out.println(i+ ", " + j + ": " + work.getBoard()[i][j]);
                }
            }
        }
//        System.out.println("now for the box");
        int r, c;
        r = boxNum(i);
        c = boxNum(j);
//        System.out.println(r + " " + c);
        for (int x = r; x < r+3; x++) {
            for (int y = c; y < c + 3; y++) {
                if(work.getBoard()[x][y].size()==1 && !(x==i && y==j)){
                    work.getBoard()[i][j].remove(work.getBoard()[x][y].get(0));
//                    System.out.println("-"+work.getBoard()[x][y].get(0));
//                    System.out.println(i+ ", " + j + ": " + work.getBoard()[i][j]);
                }
            }
        }
//        if(work.getBoard()[i][j].size()==0){
//            return null;
//        }
        ArrayList <Integer> possible = work.getBoard()[i][j];
        Collections.sort(possible);
        Collections.sort(begin);
        if(possible.equals(begin)){
            noChanges++;
        } else {
            noChanges = 0;
        }

        return work;
    }

    private Boards fillConnections(Boards board){
        Boards work = board;
        for (ArrayList<Integer> integers : work.getUnfinished()) {
            int i = integers.get(0);
            int j = integers.get(1);
            int connected = 0;
            for (int x = 0; x < 9; x++) {
                if (x != j) {
                    if (work.getBoard()[i][x].size() > 1) {
                        connected++;
                    }
                }
                if (x != i) {
                    if (work.getBoard()[x][j].size() > 1) {
                        connected++;
                    }
                }
            }
            int r, c;
            r = boxNum(i);
            c = boxNum(j);
            for (int x = r; x < r + 3; x++) {
                for (int y = c; y < c + 3; y++) {
                    if ((work.getBoard()[x][y].size() > 1) && !(x == i && y == j)) {
                        connected++;
                    }
                }
            }
            if(integers.size()==2) {
                integers.add(connected);
            } else {
                integers.set(2,connected);
            }
        }
        return work;
    }

    private Boards fill(Boards board){
        noChanges = 0;
        Boards temp, work;
        work = board;
        while(work.getUnfinished().size()>0 && noChanges<work.getUnfinished().size()) {
            ArrayList<ArrayList<Integer>> still = new ArrayList<>();
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++) {
                    if(work.getBoard()[i][j].size()>1) {
                        still.add(new ArrayList<Integer>(List.of(i, j)));
                    }
                }
            }
            work.setUnfinished(still);
            for (int i=0;i<work.getUnfinished().size();i++) {
                temp = fitConstraints(work, work.getUnfinished().get(i).get(0), work.getUnfinished().get(i).get(1));
                if(temp!=null) {
                    work = temp;
//                    System.out.println("assigned");
                } else {
                    return null;
                }
            }

//            work = fillConnections(work);
        }
        ArrayList<ArrayList<Integer>> still = new ArrayList<>();
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++) {
                if(work.getBoard()[i][j].size()>1) {
                    still.add(new ArrayList<Integer>(List.of(i, j)));
                }
            }
        }
        work.setUnfinished(still);
        return fillConnections(work);
    }

    private Boards solve(Boards board){
        Boards work = board;
        Boards temp = fill(work);
        if(temp!= null){
            work = temp;

        } else {
            return null;
        }
//        System.out.println(board.getUnfinished().size());
        if(!isFilled(work)&& isSolvable(work)){
            work = backChecking(work);
        }
        if(work != null && isSolvable(work) && isFilled(work)) {
            if (isCorrect(work)) {
                return work;
            } else {
                return null;
            }
        } else {
            return null;
        }
//        return board;
    }

    private Boards backChecking(Boards board){
        Boards work = board;
        ArrayList<ArrayList<Integer>> still = new ArrayList<>();
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++) {
                if(work.getBoard()[i][j].size()>1) {
                    still.add(new ArrayList<Integer>(List.of(i, j)));
                }
            }
        }
        work.setUnfinished(still);
        work = fillConnections(work);
        ArrayList<ArrayList<Integer>> unchecked = work.getUnfinished();
        while (unchecked.size()>0) {
            work = board;
            ArrayList<Integer> choice = null;
            for (ArrayList<Integer> spot : unchecked) {
                if (choice == null || choice.get(2) < spot.get(2)) {
                    choice = spot;
                }
            }
//            System.out.println(choice);
            ArrayList<ArrayList<Integer>> nextDone = work.getUnfinished();
            nextDone.remove(choice);
            ArrayList<Integer> checking = work.getBoard()[choice.get(0)][choice.get(1)];
            if(checking.size()==0){
                return null;
            }

            for (int possible : checking) {
                work = board;
                System.out.println(choice + ": " + checking + "| " + possible);
                ArrayList<Integer>[][] temp = work.getBoard();
                temp[choice.get(0)][choice.get(1)] = new ArrayList<Integer>(List.of(possible));
                Boards tempBoard = new Boards(temp, nextDone);
                work = solve(tempBoard);
                if (work != null && isSolvable(work)) {
                    return work;
                }
            }
            unchecked.remove(choice);
        }
        return null;
    }

    private boolean isSolvable(Boards board){
        for(ArrayList<Integer> spot : board.getUnfinished()){
            if(board.getBoard()[spot.get(0)][spot.get(1)].size()==0){
                return false;
            }
        }
        return true;
    }
}
