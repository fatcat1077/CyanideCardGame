/**
 * 專案總入口。
 * 這個主程式不包含任何遊戲邏輯，
 * 單純轉呼叫 com.game.GameSimulator 以跑完整模擬。
 */
public class Main {

    public static void main(String[] args) {
        // 直接呼叫模擬器
        com.game.GameSimulator.main(args);
    }
}
