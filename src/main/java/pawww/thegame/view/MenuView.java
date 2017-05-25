/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame.view;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import static java.lang.Thread.sleep;
import pawww.thegame.State;
import static pawww.thegame.State.EXIT;
import static java.lang.Thread.sleep;

/**
 *
 * @author Daniel
 */
public class MenuView {

    private ScreenWriter screenWriter;
    private Screen screen;
    private int maxX = 100;
    private int maxY = 30;
    int currentCursorX = maxX / 2 - 11;
    int currentCursorY = maxY / 2 + 1;

    String cursorChar = "→";
    State state = State.MENU;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum Direction {
        DOWN, UP
    }
    // Zapisuję sobię numer aktualnej opcji
    int cursorPosition = 1;
    int numberOfOptions = 3;

    public MenuView(State state, Screen s, ScreenWriter sw) {
        this.screen = s;
        this.screenWriter = sw;
        this.state = state;
    }

    public MenuView(State state, Screen s, ScreenWriter sw, int terminalSizeX, int terminalSizeY) {
        this.screen = s;
        this.screenWriter = sw;
        this.maxX = terminalSizeX;
        this.maxY = terminalSizeY;
        this.state = state;
    }

    public void moveCursor(Direction direction) {
        screenWriter.drawString(currentCursorX, currentCursorY, " ");
        if (direction == Direction.UP) {
            cursorPosition--;
            currentCursorY -= 1;
        } else if (direction == Direction.DOWN) {
            cursorPosition++;
            currentCursorY += 1;
        }

        screenWriter.drawString(currentCursorX, currentCursorY, cursorChar);
        screen.refresh();

    }

    public void drawMenu() throws InterruptedException {
        screen.clear();
        screen.refresh();
        screenWriter.drawString(maxX / 2 - 32, maxY / 2 - 10, "████████╗██╗  ██╗███████╗     ██████╗  █████╗ ███╗   ███╗███████╗");
        sleep(50);
        screen.refresh();
        screenWriter.drawString(maxX / 2 - 32, maxY / 2 - 9, "╚══██╔══╝██║  ██║██╔════╝    ██╔════╝ ██╔══██╗████╗ ████║██╔════╝");
        sleep(50);
        screen.refresh();
        screenWriter.drawString(maxX / 2 - 32, maxY / 2 - 8, "   ██║   ███████║█████╗      ██║  ███╗███████║██╔████╔██║█████╗");
        sleep(50);
        screen.refresh();
        screenWriter.drawString(maxX / 2 - 32, maxY / 2 - 7, "   ██║   ██╔══██║██╔══╝      ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝");
        sleep(50);
        screen.refresh();
        screenWriter.drawString(maxX / 2 - 32, maxY / 2 - 6, "   ██║   ██║  ██║███████╗    ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗");
        sleep(50);
        screen.refresh();
        screenWriter.drawString(maxX / 2 - 32, maxY / 2 - 5, "   ╚═╝   ╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝");
        sleep(50);
        screen.refresh();
        // Wypisuję menu
        screen.putString(maxX / 2 - 7, maxY / 2, "MENU GŁÓWNE:", Terminal.Color.WHITE, Terminal.Color.BLACK);
        screen.putString(maxX / 2 - 9, maxY / 2 + 1, "1 - Start", Terminal.Color.WHITE, Terminal.Color.BLACK);
        screen.putString(maxX / 2 - 9, maxY / 2 + 2, "2 - Wyniki", Terminal.Color.WHITE, Terminal.Color.BLACK);
        screen.putString(maxX / 2 - 9, maxY / 2 + 3, "3 - Wyjście", Terminal.Color.WHITE, Terminal.Color.BLACK);

        screenWriter.drawString(currentCursorX, currentCursorY, cursorChar);
        screen.refresh();
    }

    public void updateCursor() throws InterruptedException {
        boolean stop = false;

        while (!stop) {
            // Wczytuje wciśnięty klawisz
            Key key = screen.readInput();
            // Dopóki klawisz nie zostanie wciśnięty działa ta pętla
            while (key == null) {
                key = screen.readInput();
            }

            // Po wciśnięciu klawisza sprawdza jaki to klawisz
            switch (key.getKind()) {
                case ArrowDown:

                    // Jeśli nie jest na ostatniej pozycji to zwiększa numer pozycji
                    if (cursorPosition != numberOfOptions) {
                        moveCursor(Direction.DOWN);
                    }

                    break;
                case ArrowUp:
                    // Podobnie jak ze strzałką w dół tyle że wstawiamy znacznik pole wyżej
                    if (cursorPosition > 1) {
                        moveCursor(Direction.UP);
                    }
                    break;
                case Enter:
                    // Przy naciśnięciu entera, jako że mamy tylko jedną działającą opcję
                    // tylko przy pozycji 1 coś się dzieje
                    switch (cursorPosition) {
                        case 1:
                            state = State.PLAY;
                            screen.clear();
                            stop = true;
                            break;
                        case 2:
                            state = State.SCORE;
                            screen.clear();
                            stop = true;
                            break;
                        case 3: // Jeśli naciśnięto Enter podczas gdy znacznik był na pozycji opcji exit,
                            // wychodzimy z pierwszego while'a i kończymy program
                            state = State.EXIT;
                            stop = true;
                            break;
                    }
                default:
                    break;
            }
        }
    }
}
