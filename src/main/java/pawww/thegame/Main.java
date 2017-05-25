/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pawww.thegame;

import pawww.thegame.view.Game;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import pawww.thegame.view.MenuView;
import pawww.thegame.view.ScoreView;

/**
 *
 * @author Daniel
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        // Tworzymy se terminal
        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        // A tutaj screen który przysłania terminal
        Screen screen = new Screen(terminal);

        // Pomocniczy obiekt do pisania na screenie
        ScreenWriter screenWriter = new ScreenWriter(screen);

        // Odpalamy
        screen.startScreen();

        // Chowam ten domyślny biały kursor 
        screen.setCursorPosition(null);

        // Pobieram szerokość i wysokość terminala; domyślnie 100x30
        int maxX = screen.getTerminalSize().getColumns();
        int maxY = screen.getTerminalSize().getRows();

        State state = State.MENU;

        MenuView menu = new MenuView(state, screen, screenWriter, maxX, maxY);
        Game game;
        ScoreView scoreView;
        while (state != State.EXIT) {

            switch (state) {
                case MENU:
                    menu.drawMenu();
                    menu.updateCursor();
                    state = menu.getState();
                    break;
                case PLAY:
                    game = new Game(screen, state);
                    game.play();

                    state = game.getState();
                    break;
                case SCORE:
                    scoreView = new ScoreView(screen, state);
                    scoreView.drawTable();
                    scoreView.update();
                    state = scoreView.getState();
                    break;
            }

        }
        screen.stopScreen();
        System.exit(0);
    }

}
