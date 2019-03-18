import java.util.ArrayList;

class Boards {
    private ArrayList[][] board;
    private ArrayList<ArrayList<Integer>> unfinished;
    Boards(ArrayList[][] init, ArrayList<ArrayList<Integer>> unDone){
        board = init;
        unfinished = unDone;
    }

    ArrayList<Integer>[][] getBoard() {
        return board;
    }

    void setBoard(ArrayList[][] next) {
        board = next;
    }

    ArrayList<ArrayList<Integer>> getUnfinished() {
        return unfinished;
    }

    void setUnfinished(ArrayList<ArrayList<Integer>> undone) {
        unfinished = undone;
    }
}
