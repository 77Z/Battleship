import imgui.ImGui;
import imgui.flag.ImGuiCol;

public class Battle {

    private final int[][] enemyBoard;
    public static Winner winner;

    public Battle() {

        winner = Winner.None;

        // Construct the enemy board
        // Three horizontal, two vertical
        // This should probably change to be random orientations in the future
        enemyBoard = new int[10][10];

        // Carrier, len. 5
        // Placements allowed from x 0-5, y 0-9
        {
            int x = (int)(Math.random() * 5 - 1);
            int y = (int)(Math.random() * 9 - 1);

            for (int i = 0; i < 5; i++)
                enemyBoard[y][x + i] = 1;
        }

        // Battleship, len. 4
        // Placements allowed from x 0-6, y 0-9
        {
            boolean overlappingCheck = true;
            while (overlappingCheck) {
                int x = (int)(Math.random() * 6 - 1);
                int y = (int)(Math.random() * 9 - 1);

                // Check if overlapping
                overlappingCheck = false;
                for (int i = 0; i < 4; i++)
                    if (enemyBoard[y][x + i] == 1) {
                        overlappingCheck = true;
                        break;
                    }

                for (int i = 0; i < 4; i++)
                    enemyBoard[y][x + i] = 1;
            }
        }

        // Cruiser, len. 3
        // Placements allowed from x 0-7, y 0-9
        {
            boolean overlappingCheck = true;
            while (overlappingCheck) {
                int x = (int)(Math.random() * 7 - 1);
                int y = (int)(Math.random() * 9 - 1);

                // Check if overlapping
                overlappingCheck = false;
                for (int i = 0; i < 3; i++)
                    if (enemyBoard[y][x + i] == 1) {
                        overlappingCheck = true;
                        break;
                    }

                for (int i = 0; i < 3; i++)
                    enemyBoard[y][x + i] = 1;
            }
        }

        // Submarine, len. 3
        // Placements allowed from x 0-9, y 0-7
        {
            boolean overlappingCheck = true;
            while (overlappingCheck) {
                int x = (int)(Math.random() * 9 - 1);
                int y = (int)(Math.random() * 7 - 1);

                // Check if overlapping
                overlappingCheck = false;
                for (int i = 0; i < 3; i++)
                    if (enemyBoard[y + i][x] == 1) {
                        overlappingCheck = true;
                        break;
                    }

                for (int i = 0; i < 3; i++)
                    enemyBoard[y + i][x] = 1;
            }
        }

        // Destroyer, len. 2
        // Placements allowed from x 0-9, y 0-8
        {
            boolean overlappingCheck = true;
            while (overlappingCheck) {
                int x = (int)(Math.random() * 9 - 1);
                int y = (int)(Math.random() * 8 - 1);

                // Check if overlapping
                overlappingCheck = false;
                for (int i = 0; i < 2; i++)
                    if (enemyBoard[y + i][x] == 1) {
                        overlappingCheck = true;
                        break;
                    }

                for (int i = 0; i < 2; i++)
                    enemyBoard[y + i][x] = 1;
            }
        }

        System.out.println("Enemy board visual:");
        for (int[] ints : enemyBoard) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println();
        }

    }

    public boolean drawBattleScreen() {
        // Enemy board
        ImGui.text("Enemy board (Computer)");
        ImGui.text("Click to torpedo!");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int colorPop = 0;
                if (enemyBoard[i][j] == 2) {
                    // Hit color
                    ImGui.pushStyleColor(ImGuiCol.Button, 255, 0, 0, 255);
                    colorPop++;
                } else if (enemyBoard[i][j] == 3) {
                    // Miss color
                    ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 255, 255);
                    colorPop++;
                }

                if (ImGui.button("##enemyButton" + i + j, 30, 30)) {
                    if (torpedoPosition(i, j)) return true;
                }
                
                for (int k = 0; k < colorPop; k++) ImGui.popStyleColor();
                
                if (j != 9) ImGui.sameLine();
            }
        }

        // Player board
        ImGui.text("Your board");
        ImGui.text("No need to click down here anymore, fire torpedo's instead!");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int colorPop = 0;
                if (ShipSelection.playerBoard[i][j] == 1) {
                    // Highlight players own ships
                    ImGui.pushStyleColor(ImGuiCol.Button, 0, 255, 0, 255);
                    colorPop++;
                } else if (ShipSelection.playerBoard[i][j] == 2) {
                    // Hit color
                    ImGui.pushStyleColor(ImGuiCol.Button, 255, 0, 0, 255);
                    colorPop++;
                } else if (ShipSelection.playerBoard[i][j] == 3) {
                    // Miss color
                    ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 255, 255);
                    colorPop++;
                }

                ImGui.button("##playerButton" + i + j, 30, 30);

                for (int k = 0; k < colorPop; k++) ImGui.popStyleColor();

                if (j != 9) ImGui.sameLine();
            }
        }
        
        return false;
    }


    private boolean torpedoPosition(int i, int j) {
        if (enemyBoard[i][j] == 0) {
            // Player missed
            enemyBoard[i][j] = 3;
        } else if (enemyBoard[i][j] == 1) {
            // Player hit ship
            enemyBoard[i][j] = 2;
        }

        // Did the player win??
        if (checkForWinner()) return true;

        // If not, time for the computer to strike back

        // This is pure random. It sucks.
        boolean guessAgain = true; // While condition to see if randomly picked has already been selected
        while (guessAgain) {
            int x = (int) (Math.random() * 9 - 1);
            int y = (int) (Math.random() * 9 - 1);

            guessAgain = ShipSelection.playerBoard[y][x] == 3 || ShipSelection.playerBoard[y][x] == 2;

            if (ShipSelection.playerBoard[y][x] == 0) {
                // Computer missed
                ShipSelection.playerBoard[y][x] = 3;
            } else if (ShipSelection.playerBoard[y][x] == 1) {
                // Computer hit ship
                ShipSelection.playerBoard[y][x] = 2;
            }
        }

        // Did the computer win??
        if (checkForWinner()) return true;

        return false;
    }

    private boolean checkForWinner() {

        // Did the player win?
        // Check to see if computer has any 1's left
        {
            boolean spottedOne = false;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (enemyBoard[i][j] == 1) {
                        spottedOne = true;
                        break;
                    }
                }
            }
            if (!spottedOne) {
                winner = Winner.Player;
                return true;
            }
        }

        // Did the computer win?
        // Check to see if player has any 1's left
        {
            boolean spottedOne = false;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (ShipSelection.playerBoard[i][j] == 1) {
                        spottedOne = true;
                        break;
                    }
                }
            }
            if (!spottedOne) {
                winner = Winner.Computer;
                return true;
            }
        }

        return false;
    }

}
