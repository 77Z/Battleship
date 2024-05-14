import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImInt;

public class ShipSelection {

    private final Ships ships;
    private final ImInt orientation;
    public static int[][] playerBoard;

    public ShipSelection() {
        ships = new Ships();
        orientation = new ImInt(0);
        playerBoard = new int[10][10];
    }

    public boolean drawSelectionScreen() {

        if (ships.currentShipToPlace > 4) {
            return true;
        }

        ImGui.text("Place your ships down");
        ImGui.text("You are going to place the " + ships.ships[ships.currentShipToPlace] + " next");
        ImGui.text("It has a length of " + ships.shipLengths[ships.currentShipToPlace]);
        ImGui.radioButton("Horizontal", orientation, 1);
        ImGui.sameLine();
        ImGui.radioButton("Vertical", orientation, 0);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int colorPop = 0;
                if (playerBoard[i][j] == 1) {
                    ImGui.pushStyleColor(ImGuiCol.Button, 0, 255, 0, 255);
                    colorPop++;
                }

                if (ImGui.button("##ssbutton" + i + j, 30, 30)) {
                    placeShip(i, j);
                }

                for (int k = 0; k < colorPop; k++) ImGui.popStyleColor();

                if (j != 9) ImGui.sameLine();
            }
        }

        return false;
    }

    private void placeShip(int i, int j) {
        int shipLenToPlace = ships.shipLengths[ships.currentShipToPlace];
        if (orientation.equals(new ImInt(0))) {
            // Vertical

            // Check to see if ship is too large to fit vertically
            if (i + shipLenToPlace > 10) return;

            // Check to see if this placement will overlap existing ships
            for (int k = 0; k < shipLenToPlace; k++)
                if (playerBoard[i + k][j] == 1) return;

            // Made it. Place the ship.
            for (int k = 0; k < shipLenToPlace; k++) {
                playerBoard[i + k][j] = 1;
            }
        } else {
            // Horizontal

            // Check to see if ship is too large to fit horizontally
            if (j + shipLenToPlace > 10) return;

            // Check to see if this placement will overlap existing ships
            for (int k = 0; k < shipLenToPlace; k++)
                if (playerBoard[i][j + k] == 1) return;

            // Made it. Place the ship.
            for (int k = 0; k < shipLenToPlace; k++) {
                playerBoard[i][j + k] = 1;
            }
        }
        ships.currentShipToPlace++;
    }

}
