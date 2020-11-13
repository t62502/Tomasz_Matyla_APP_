package com.example.tomaszmatyla;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneScore, playerTWoScore, playerStatus; // zmienne typu private TextView obsługujące pola layout'u.
    private Button[] buttons = new Button[9];  // zmienna typu private Button(tablica) obsługująca 9 przycisków layout'u.
    private Button resetGame; // zmienna typu private Button obsługująca przycisk reset.

    private int playerOneScoreCount, playerTwoScoreCount, roundCount; // zmienne typu private int obsługujące liczbe pukntów graczy oraz liczbe rund.
    boolean activePlayer; // zmienna logiczna obsługująca zmiane gracza

    // playerOne -> 0
    // playerTwo -> 1
    // empty -> 2

    int [] gameState = {2,2,2,2,2,2,2,2,2}; // stan gry zeby kontrolować(sledzic) gre.

    int [][] winningPositions = {      //tablica dwówymiarowa zawierająca wszystkie wygrane pozycje
            {0,1,2}, {3,4,5}, {6,7,8}, // rows
            {0,3,6}, {1,4,7}, {2,5,8}, // columns
            {0,4,8}, {2,4,6} // cross
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // połaczenie zmiennych z polami .xml zgodnie z ID
        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTWoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        // pętla for do połączenia klikniętych pól(przycisków) gry, tak ,zeby nie robic tego 9 razy takim samym fragmentem kodu.
        for(int i=0; i< buttons.length; i++){
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        roundCount = 0; // liczba rund na początku gry
        playerOneScoreCount = 0; // liczba punktów gracza na początku gry
        playerTwoScoreCount = 0; // -||-
        activePlayer = true; // zmienna aktywnego gracza ustawiona na true.
    }

    //metoda onClick nie pozwala kliknąc tego samego przycisku w nastepnej rundzie.
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        //sprawdzanie ktory przycisk został klikniety i wziecie jego ID
        String buttonID = v.getResources().getResourceEntryName(v.getId()); // bnt_2
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length())); // 2
        // Przycisk przyciśnięty przez gracza pierwszego zmienia sie w X
        if (activePlayer) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0; // zmiana stanu gry zgodnie z wcisnietym przycskiem przez gracza
        } else {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }      //analogicznie dla gracza drugiego przycisk zmienia sie w O
        roundCount++; // zwiekszenie liczby rund o 1 po wykonanym ruchu.

        if (checkWinner()) {
            if (activePlayer) {
                playerOneScoreCount++;
                updatePlayerSCore();            // update punktacji w momencie wygranej przez gracza pierwszego
                Toast.makeText(this, "Player One Won!", Toast.LENGTH_SHORT).show(); // alert o wygranej rundzie
                playAgain(); // rozpoczecie nowej rundy z czystym stanem gry
            } else {
                playerTwoScoreCount++;
                updatePlayerSCore();           // analogicznie to samo co wyzej dla gracza drugiego
                Toast.makeText(this, "Player Two Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
        } else if (roundCount == 9) {       // jesli liczba rund wynosla 9 i nie bylo zwyciezcy gra rozpoczyna sie od nowa
            playAgain();
            Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT).show();
        } else {
            activePlayer = !activePlayer;
        }

        if (playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("Player One is Winning!");// jesli liczba punktow gracza pierwszego jest wieksza, informacja o prowadzeniu
        }else if (playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.setText("Player Two is Winning!");// gdy drugi gracz prowadzi
        }else{
            playerStatus.setText(""); // kiedy ich wynik jest rowny brak informacji o prowadzacym
        }

        resetGame.setOnClickListener(new View.OnClickListener() { // klikniecie przycisku reset
            @Override
            public void onClick(View v) {  //
                playAgain(); // rozpoczyna gre z czystym stanem gry i z 0 iloscia rund
                playerOneScoreCount = 0; // czysci punktacje gracza pierwszego
                playerTwoScoreCount = 0; // czysci punktacje gracza drugiego
                playerStatus.setText("");
                updatePlayerSCore(); // wywaolanie funkcji aktualizacji punktow tak zeby wszystkie zmienne zostaly zresetowane.
            }
        });
}

            //funkcja logiczna sprawdzająca zwycięzce
    public boolean checkWinner(){
        boolean winnerResult = false; // wynik domysl ustawiony na false

        // pętla sprawdzająca pozycje wygrane ze stanem gry
        for(int [] winningPositon : winningPositions){
            if (gameState[winningPositon[0]] == gameState[winningPositon[1]] &&
                    gameState[winningPositon[1]] == gameState[winningPositon[2]] &&
                        gameState[winningPositon[0]] !=2){
                winnerResult = true; // jesli pozycje ze stanu gry odpowiadają pozycją zwycięzkim wynik zostaje ustawiony na true
            }
        }
        return winnerResult; // zwrócony wynik zywciezki
    }

            // funkcja update pukntacji graczy
        public void updatePlayerSCore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTWoScore.setText(Integer.toString(playerTwoScoreCount));
    }

        // funkcja rozpoczynajaca runde od nowa z domyslnym stanem gry
    public void playAgain(){
        roundCount = 0;
        activePlayer = true;
            // pętla for ustawiająca stan gry w pozycji pustych przycisków
        for(int i = 0; i < buttons.length; i++){
            gameState[i] = 2;
            buttons[i].setText("");

        }
    }

}