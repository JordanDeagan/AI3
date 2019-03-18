public class project3 {
    public static void main(String[] args) {
        PuzzleBoard board = new PuzzleBoard("/home/jordan-d/Desktop/College/AI/3rd/Suduko/problems/35/1.sd");
//        if(board.isFilled()) {
//            System.out.println(board.checkRow());
//            System.out.println(board.checkCol());
//            System.out.println(board.checkBox());
//        } else {
//            System.out.println("Fill the rest of the spaces");
//        }
        if(board.isCorrect()){
            System.out.println("that is a correct solution, yay");
        } else {
            System.out.println("that is an incorrect solution, sorry");
        }
    }
}
