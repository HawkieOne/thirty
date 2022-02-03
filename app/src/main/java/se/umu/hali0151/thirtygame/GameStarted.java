
package se.umu.hali0151.thirtygame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The activity which controls the entire game process, which is the dices and how the
 * points is calculated.
 *
 * @author id18hll
 * @version 1.0
 */

public class GameStarted extends AppCompatActivity {

    private  final int GREEN = R.drawable.border_bg;
    private  final int DARKGREY = R.color.darkgrey;

    private int[] diceDrawables;
    private ImageButton[] diceImagesButtons;
    private ArrayList<Round> roundsPlayed;
    private int[] colors;

    private Button nextRoundButton;
    private Button throwDiceButton;
    private Button getResultButton;

    private Spinner choicePointsSpinner;
    private ArrayList<String> arrayListSpinner;
    private ArrayAdapter<String> spinnerAdapter;

    private TextView roundTextView;
    private TextView throwsLeftTextView;
    private TextView pointsThisRound;

    private int throwsThisRound;
    private int roundNumber;
    private boolean nextRoundButtonVisible;
    private boolean spinnerEnabled;
    private final int maxThrowsPerRound = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_game_started);

        roundsPlayed = new ArrayList<>();

        //Setup for dice images
        diceDrawables = new int[]{
                R.drawable.white1,
                R.drawable.white2,
                R.drawable.white3,
                R.drawable.white4,
                R.drawable.white5,
                R.drawable.white6
        };

        //Setup for Image Buttons(Dices)
        diceImagesButtons = new ImageButton[]{
                (ImageButton) findViewById(R.id.dice1),
                (ImageButton) findViewById(R.id.dice2),
                (ImageButton) findViewById(R.id.dice3),
                (ImageButton) findViewById(R.id.dice4),
                (ImageButton) findViewById(R.id.dice5),
                (ImageButton) findViewById(R.id.dice6)
        };

        // Colors for showing how the points is calculated
        colors = new int[]{
                R.drawable.blue_dice_background,
                R.drawable.green_dice_background,
                R.drawable.orange_dice_background,
                R.drawable.purple_dice_background,
                R.drawable.red_dice_background,
                R.drawable.yellow_dice_background
        };

        // Setup for buttons and Text Views
        ImageButton helpImageButton = (ImageButton) findViewById(R.id.help);
        throwDiceButton = (Button) findViewById(R.id.throwDice);
        getResultButton = (Button) findViewById(R.id.result);
        nextRoundButton = (Button) findViewById(R.id.nextRound);
        pointsThisRound = (TextView) findViewById(R.id.pointsThisRound);

        // Controls the app after rotation or other occasions when the app is resumed
        if (savedInstanceState != null) {
            loadData(savedInstanceState);
        } else {
            roundNumber = 0;
            roundsPlayed.add(new Round());
            throwsThisRound = 0;
            roundsPlayed.get(roundNumber).setFinishedRound(false);
            roundsPlayed.get(roundNumber).setStartedRound(false);
            nextRoundButtonVisible = false;
            spinnerEnabled = true;
            for (int i = 0; i < diceImagesButtons.length; i++) {
                roundsPlayed.get(roundNumber).getDices().get(i).setImageButton(diceImagesButtons[i]);
            }
            arrayListSpinner = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.combinations)));
        }
        setupDiceListeners();
        showStats();

        // Setup for spinner
        choicePointsSpinner = (Spinner) findViewById(R.id.combinations1);
        spinnerAdapter = new ArrayAdapter<String>(GameStarted.this,
                R.layout.color_spinner_layout,
                arrayListSpinner) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the disable item text color
                    SpannableString content = new SpannableString(tv.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    tv.setText(content);
                }
                tv.setTextColor(getResources().getColor(R.color.cyan));
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_menu);
        choicePointsSpinner.setAdapter(spinnerAdapter);
        if(!spinnerEnabled){
            choicePointsSpinner.setEnabled(false);
        }


        // Help(Question Mark) button listener
        helpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameStarted.this, R.style.alertDialog);
                alertDialogBuilder.setTitle(R.string.rules);
                alertDialogBuilder.setMessage(R.string.howToPlay);
                alertDialogBuilder.setPositiveButton("OK", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setIcon(R.drawable.white6);
                alertDialog.show();
            }
        });

        // Throw button listener
        throwDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkGameState(throwDiceButton)) {
                    Round round = roundsPlayed.get(roundNumber);
                    if (throwsThisRound < 3) {
                        Random random = new Random();
                        //Randomizes the dices numbers
                        for (int j = 0; j < round.getDices().size(); j++) {
                            Dice dice = roundsPlayed.get(roundNumber).getDices().get(j);
                            if (!dice.isDiceFinished()) {
                                int diceNumber = random.nextInt(diceDrawables.length);
                                dice.setNumber(diceNumber + 1);
                                dice.getImageButton().setImageResource(diceDrawables[diceNumber]);
                            }
                        }
                        roundsPlayed.get(roundNumber).setStartedRound(true);
                        throwsThisRound++;
                        throwsLeftTextView.setText((getResources().getString(R.string.throwsLeft) + " " + (maxThrowsPerRound - throwsThisRound)));
                    } else if (round.getTypeOfPoints() == null ||  round.getTypeOfPoints().equals("Choose type of points")) {
                        animateElement(choicePointsSpinner);
                    } else if (throwsThisRound >= 3) {
                        animateElement(getResultButton);
                    }
                }
            }
        });

        // Drop down men listener for type of points
        choicePointsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (!selectedItem.equals("Choose type of points")) {
                    roundsPlayed.get(roundNumber).setTypeOfPoints(selectedItem);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Result button listener
        getResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkGameState(getResultButton)){
                    Round round = roundsPlayed.get(roundNumber);
                    if (round.getTypeOfPoints() == null ||
                            round.getTypeOfPoints().equals("Choose type of points")) {
                        animateElement(choicePointsSpinner);
                    } else {
                        int result = 0;
                        if (round.getDices().get(0).getNumber() == 0) {
                            animateElement(throwDiceButton);
                        } else {
                            roundsPlayed.get(roundNumber).setFinishedRound(true);
                            choicePointsSpinner.setEnabled(false);
                            spinnerEnabled = false;

                            // Removes the backgrounds which indicates a saved dice
                            for (int i = 0; i < diceImagesButtons.length; i++) {
                                roundsPlayed.get(roundNumber).getDices().get(i).getImageButton().setBackgroundResource(DARKGREY);
                                roundsPlayed.get(roundNumber).getDices().get(i).setBackgroundIndex(DARKGREY);
                            }

                            ArrayList<Dice> dices = roundsPlayed.get(roundNumber).getDices();
                            ArrayList<Dice> dicesClone = new ArrayList<>();
                            for(Dice dice : dices) {
                                try {
                                    dicesClone.add((Dice) dice.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }


                            if (round.getTypeOfPoints().equals("Choose type of points")) {
                                animateElement(choicePointsSpinner);
                            } else if (round.getTypeOfPoints().equals("Low")) {
                                result = calculatePointsLow(dices, round, colors);
                                round.setPoints(result);
                            } else {
                                result = calculatePoints(dices, dicesClone, round, colors);
                                round.setPoints(result);
                                round.getDices().clear();
                                round.setDices(dicesClone);
                            }

                            pointsThisRound.setVisibility(View.VISIBLE);
                            pointsThisRound.setText(MessageFormat.format("You got {0} {1}", String.valueOf(result), getResources().getString(R.string.pointsThisRound)));

                            nextRoundButton.setVisibility(View.VISIBLE);
                            nextRoundButtonVisible = true;
                        }
                    }
                }
            }
        });

        // Next round button listener
        nextRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roundNumber == 8) {
                    nextRoundButton.setText(R.string.finishGame);
                } else if (roundNumber == 9) {
                    Intent gameResults = new Intent(GameStarted.this, GameResults.class);
                    gameResults.putParcelableArrayListExtra("rounds", roundsPlayed);
                    startActivity(gameResults);
                    finish();
                }

                roundsPlayed.get(roundNumber).setFinishedRound(true);
                roundsPlayed.get(roundNumber).setStartedRound(false);
                throwsThisRound = 0;
                roundNumber++;
                roundsPlayed.add(new Round());

                // Resets the buttons
                for (int i = 0; i < diceImagesButtons.length; i++) {
                    Dice dice = roundsPlayed.get(roundNumber).getDices().get(i);
                    dice.setImageButton(diceImagesButtons[i]);
                    dice.getImageButton().setImageResource(diceDrawables[i]);

                    dice.getImageButton().setBackgroundResource(DARKGREY);
                    roundsPlayed.get(roundNumber).getDices().get(i).setBackgroundIndex(DARKGREY);
                    dice.setDiceFinished(false);
                }

                // Updates the drop-down menu
                int index = choicePointsSpinner.getSelectedItemPosition();
                arrayListSpinner.remove(index);
                spinnerAdapter.notifyDataSetChanged();
                choicePointsSpinner.setEnabled(true);
                spinnerEnabled = true;
                choicePointsSpinner.setSelection(0);

                nextRoundButton.setVisibility(View.INVISIBLE);
                nextRoundButtonVisible = false;
                pointsThisRound.setVisibility(View.INVISIBLE);

                roundTextView.setText(MessageFormat.format("{0} {1}",getResources().getString(R.string.roundNumber), (roundNumber + 1)));
                throwsLeftTextView.setText(MessageFormat.format("{0} {1}", getResources().getString(R.string.throwsLeft), (maxThrowsPerRound - throwsThisRound)));
            }
        });
    }

    /*----------- METHODS ----------*/

    /**
     * If a dice isn't finished set it to finished and put a background on the dice
     * If it is finished do the opposite
     * @param diceNumber The number of the dice
     * @param imageButton The ImageButton of the dice
     */
    private void setDiceToFinished(int diceNumber, ImageButton imageButton) {
        if (roundsPlayed.get(roundNumber).isStartedRound()) {
            if (!roundsPlayed.get(roundNumber).getDices().get(diceNumber).isDiceFinished()) {
                imageButton.setBackgroundResource(GREEN);
                roundsPlayed.get(roundNumber).getDices().get(diceNumber).setBackgroundIndex(GREEN);
                roundsPlayed.get(roundNumber).getDices().get(diceNumber).setDiceFinished(true);
            } else {
                imageButton.setBackgroundResource(DARKGREY);
                roundsPlayed.get(roundNumber).getDices().get(diceNumber).setBackgroundIndex(DARKGREY);
                roundsPlayed.get(roundNumber).getDices().get(diceNumber).setDiceFinished(false);
            }
        }
        else{
            animateElement(throwDiceButton);
        }
    }

    /**
     * Show the current round and how many throws the user has left
     */
    private void showStats() {
        roundTextView = (TextView) findViewById(R.id.roundNumber);
        roundTextView.setText(MessageFormat.format("{0} {1}",getResources().getString(R.string.roundNumber), (roundNumber + 1)));
        throwsLeftTextView = (TextView) findViewById(R.id.throwsLeft);
        throwsLeftTextView.setText((MessageFormat.format("{0} {1}", getResources().getString(R.string.throwsLeft), (maxThrowsPerRound - throwsThisRound))));
    }

    /**
     * Sets listeners for the dices
     */
    private void setupDiceListeners() {
        for (int index = 0; index < roundsPlayed.get(roundNumber).getDices().size(); index++) {
            final int finalIndex = index;
            roundsPlayed.get(roundNumber).getDices().get(finalIndex).getImageButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!roundsPlayed.get(roundNumber).isFinishedRound()) {
                        setDiceToFinished(finalIndex, roundsPlayed.get(roundNumber).getDices().get(finalIndex).getImageButton());
                    } else {
                        animateElement(nextRoundButton);
                    }
                }
            });
        }
    }

    /**
     * Animates a view to show where to press next to continue
     * @param view the view to animate
     */
    private void animateElement(View view){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(310);
        scaleDown.setRepeatCount(3);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }

    /**
     * Checks which state the game is in to help the player where to press next
     * @param button the button which the method is called from
     * @return state of the game depending on button pressed
     */
    private boolean checkGameState(Button button){
        if(!roundsPlayed.get(roundNumber).isFinishedRound()){
            if(!button.equals(throwDiceButton)){
                if(roundsPlayed.get(roundNumber).isStartedRound()){
                    return true;
                }
                else {
                    animateElement(throwDiceButton);
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else{
            animateElement(nextRoundButton);
            return false;
        }
    }

    /**
     * Loads saved data from a bundle. This is used when the activity is restarted
     * @param savedInstanceState the saved data to be loaded
     */
    private void loadData(Bundle savedInstanceState){
        roundsPlayed= savedInstanceState.getParcelableArrayList("rounds");
        roundNumber = savedInstanceState.getInt("roundNumber");
        throwsThisRound = savedInstanceState.getInt("throwsThisRound");
        nextRoundButtonVisible = savedInstanceState.getBoolean("nextRoundButton");
        spinnerEnabled = savedInstanceState.getBoolean("spinnerEnabled");
        arrayListSpinner = savedInstanceState.getStringArrayList("arrayListSpinner");

        for (int i = 0; i < diceImagesButtons.length; i++) {
            if(roundsPlayed.get(roundNumber).getDices().get(i).getImageButton() != null){
                Bitmap bitmap = ((BitmapDrawable)roundsPlayed.get(roundNumber).getDices().get(i).getImageButton().getDrawable()).getBitmap();
                diceImagesButtons[i].setImageBitmap(bitmap);
                Drawable diceBackground = roundsPlayed.get(roundNumber).getDices().get(i).getImageButton().getBackground();
                diceImagesButtons[i].setBackground(diceBackground);
            }
            else{
                diceImagesButtons[i].setImageResource(diceDrawables[roundsPlayed.get(roundNumber).getDices().get(i).getNumber() - 1]);
                if(roundsPlayed.get(roundNumber).getDices().get(i).getBackgroundIndex() == DARKGREY){
                    diceImagesButtons[i].setBackgroundResource(DARKGREY);
                    roundsPlayed.get(roundNumber).getDices().get(i).setBackgroundIndex(DARKGREY);
                }
                else if(roundsPlayed.get(roundNumber).getDices().get(i).getBackgroundIndex() == GREEN){
                    diceImagesButtons[i].setBackgroundResource(GREEN);
                    roundsPlayed.get(roundNumber).getDices().get(i).setBackgroundIndex(GREEN);
                }
                else{
                    diceImagesButtons[i].setBackgroundResource(roundsPlayed.get(roundNumber).getDices().get(i).getBackgroundIndex());
                    roundsPlayed.get(roundNumber).getDices().get(i).setBackgroundIndex(roundsPlayed.get(roundNumber).getDices().get(i).getBackgroundIndex());
                }
            }
            roundsPlayed.get(roundNumber).getDices().get(i).setImageButton(diceImagesButtons[i]);
        }
        if(nextRoundButtonVisible){
            nextRoundButton.setVisibility(View.VISIBLE);
        }
        if(roundsPlayed.get(roundNumber).isFinishedRound()){
            pointsThisRound.setVisibility(View.VISIBLE);
            pointsThisRound.setText(MessageFormat.format("You got {0} {1}", roundsPlayed.get(roundNumber).getPoints(), getResources().getString(R.string.pointsThisRound)));
        }
    }

    /**
     * Calculates points for the dices
     * @param dices the dices to calculate the points for
     * @param round the round which the game is at
     * @param colors an array of colors to illustrate how the points were calculated
     * @return result the calculated result
     */
    private Integer calculatePoints(ArrayList<Dice> dices, ArrayList<Dice> clonedDices, Round round, int[] colors){
        int sum = Integer.parseInt(round.getTypeOfPoints());
        int result = 0;

//        for(int i = 0; i < dices.size(); i++){
//            dices.get(i).setOriginPosition(dices.get());
//        }
        // Sort ArrayList dices with bubble sort
        sortArrayList(dices);

        int sumTemp = sum;
        int colorCount = 0;
        ArrayList<Integer> posToRemove = new ArrayList<>();

        for (int i = 0; i < dices.size(); i++) {
            int diceNumber = dices.get(i).getNumber();
            if (diceNumber > sum) {
                dices.remove(i);
                i--;
            } else if (diceNumber == sum) {
                result += sum;
                round.getDices().get(i).getImageButton().setBackgroundResource(colors[colorCount]);
                int originPosition = round.getDices().get(i).getOriginPosition();
                clonedDices.get(originPosition).setBackgroundIndex(colors[colorCount]);
                dices.remove(i);
                i--;
            } else {
                sumTemp -= diceNumber;
                posToRemove.add(i);
                for (int j = i + 1; j < dices.size(); j++) {
                    int nextDiceNumber = dices.get(j).getNumber();
                    if (nextDiceNumber <= sumTemp) {
                        sumTemp -= nextDiceNumber;
                        posToRemove.add(j);
                    }
                    if (sumTemp == 0) {
                        result += sum;
                        j = dices.size();
                    }
                }
                if (sumTemp != 0) {
                    posToRemove.clear();
                }
                if (!posToRemove.isEmpty()) {
                    i--;
                }
                for (int k = posToRemove.size() - 1; k >= 0; k--) {
                    int indexToRemove = posToRemove.get(k);
                    round.getDices().get(indexToRemove).getImageButton().setBackgroundResource(colors[colorCount]);
                    int originPosition = round.getDices().get(indexToRemove).getOriginPosition();
                    clonedDices.get(originPosition).setBackgroundIndex(colors[colorCount]);
                    dices.remove(indexToRemove);
                }
                posToRemove.clear();
                sumTemp = sum;
            }
            colorCount++;
            if (colorCount == 6) {
                colorCount = 0;
            }
        }
        return result;
    }

    /**
     * Calculates points for the dices
     * @param dices the dices to calculate the points for
     * @param round the round which the game is at
     * @param colors an array of colors to illustrate how the points were calculated
     * @return result the calculated result
     */
    private Integer calculatePointsLow(ArrayList<Dice> dices, Round round, int[] colors){
        int result = 0;
        for (int i = 0; i < dices.size(); i++) {
            int diceNumber = dices.get(i).getNumber();
            if (diceNumber <= 3) {
                result += diceNumber;
                round.getDices().get(i).getImageButton().setBackgroundResource(colors[i]);
                roundsPlayed.get(roundNumber).getDices().get(i).setBackgroundIndex(colors[i]);
            }
        }
        return result;
    }

    /**
     * Sorts an ArrayList though bubble sort
     * @param diceArrayList ArrayList to sort
     */
    private void sortArrayList(ArrayList<Dice> diceArrayList){
        for (int i = 0; i < diceArrayList.size() - 1; i++) {
            for (int j = 0; j < diceArrayList.size() - i - 1; j++){
                Dice dice = diceArrayList.get(j);
                Dice nextDice = diceArrayList.get(j + 1);
                if (dice.getNumber() < nextDice.getNumber()) {
                    int tempNumber = dice.getNumber();
                    int tempBackgroundIndex = dice.getBackgroundIndex();
                    boolean tempBoolean = dice.isDiceFinished();
                    int tempOriginPosition = dice.getOriginPosition();
                    ImageButton tempImageButton = dice.getImageButton();

                    dice.setNumber(nextDice.getNumber());
                    dice.setBackgroundIndex(nextDice.getBackgroundIndex());
                    dice.setDiceFinished(nextDice.isDiceFinished());
                    dice.setOriginPosition(nextDice.getOriginPosition());
                    dice.setImageButton(nextDice.getImageButton());

                    nextDice.setNumber(tempNumber);
                    nextDice.setBackgroundIndex(tempBackgroundIndex);
                    nextDice.setDiceFinished(tempBoolean);
                    nextDice.setOriginPosition(tempOriginPosition);
                    nextDice.setImageButton(tempImageButton);
                }
            }
        }
    }

    /**
     * Checks if back button is pressed
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameStarted.this, R.style.alertDialog);
        alertDialogBuilder.setTitle("Exiting the current game");
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Saves the state of the game in a Bundle
     * @param savedInstanceState the saved Bundle
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("rounds", roundsPlayed);
        savedInstanceState.putInt("roundNumber", roundNumber);
        savedInstanceState.putInt("throwsThisRound", throwsThisRound);
        savedInstanceState.putBoolean("nextRoundButton", nextRoundButtonVisible);
        savedInstanceState.putBoolean("spinnerEnabled", spinnerEnabled);
        savedInstanceState.putStringArrayList("arrayListSpinner", arrayListSpinner);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Restores a saved Bundle
     * @param savedInstanceState the saved Bundle
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundsPlayed= savedInstanceState.getParcelableArrayList("rounds");
        roundNumber = savedInstanceState.getInt("roundNumber");
        throwsThisRound = savedInstanceState.getInt("throwsThisRound");
        nextRoundButtonVisible = savedInstanceState.getBoolean("nextRoundButton");
        spinnerEnabled = savedInstanceState.getBoolean("spinnerEnabled");
        arrayListSpinner = savedInstanceState.getStringArrayList("arrayListSpinner");
    }
}

