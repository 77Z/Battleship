import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiViewport;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;

enum ApplicationState {
    ShipPlacement,
    Battle,
    Ending
}

public class Main extends Application {

    ShipSelection shipSelection;
    Battle battle;
    boolean first;
    ApplicationState applicationState;

    public Main() {
        shipSelection = new ShipSelection();
        battle = new Battle();
        first = true;
        applicationState = ApplicationState.ShipPlacement;
    }

    @Override
    protected void configure(final Configuration config) {
        config.setTitle("Vince's Battleship");
        config.setWidth(450);
        config.setHeight(770);
    }

    @Override
    protected void initImGui(final Configuration config) {
        super.initImGui(config);

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);

        io.getFonts().addFontDefault();
    }

    @Override
    public void process() {
        final ImGuiViewport viewport = ImGui.getMainViewport();

        ImGui.setNextWindowPos(viewport.getCenterX(), viewport.getCenterY(), ImGuiCond.Always, 0.5f, 0.5f);
        if (ImGui.beginPopup("intro", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Welcome to Battleship.");
            ImGui.textWrapped("You will start by placing all your ships in water, once finished, the game will " +
                    "automatically move to the attack phase, where you will battle against a computer.");

            if (ImGui.button("Place Ships")) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }

        if (first) {
            ImGui.openPopup("intro");
            first = false;
        }

        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(viewport.getSizeX(), viewport.getSizeY());
        ImGui.begin("Vince's Battleship", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);

        // Methods here return booleans that control the state of the application
        switch (applicationState) {
            case ShipPlacement -> {
                if (shipSelection.drawSelectionScreen()) applicationState = ApplicationState.Battle;
            }
            case Battle -> {
                if (battle.drawBattleScreen()) applicationState = ApplicationState.Ending;
            }
            case Ending -> {
                ImGui.text("Game Ended");
                if (Battle.winner == Winner.Player) {
                    ImGui.text("You won!!");
                } else {
                    ImGui.text("You lost :(");
                }
            }
        }

        ImGui.end();
    }

    public static void main(String[] args) {
        launch(new Main());
        System.exit(0);
    }
}