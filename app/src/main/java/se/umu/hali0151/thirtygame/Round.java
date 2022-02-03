
package se.umu.hali0151.thirtygame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * A class containing the information of one round
 * @author id18hll
 * @version 1.0
 */
public class Round implements Parcelable {
    private ArrayList<Dice> dices;
    private String typeOfPoints;
    private int points;
    private boolean finishedRound;
    private boolean startedRound;

    /**
     * Constructor for Round
     */
    public Round(){
        dices = new ArrayList<Dice>(){
            {
                add(new Dice(1, false, R.color.darkgrey, null));
                add(new Dice(2, false,  R.color.darkgrey, null));
                add(new Dice(3, false, R.color.darkgrey, null));
                add(new Dice(4, false,  R.color.darkgrey, null));
                add(new Dice(5, false, R.color.darkgrey, null));
                add(new Dice(6, false, R.color.darkgrey, null));
            }
        };
        typeOfPoints = null;
        points = 0;
        finishedRound = false;
        startedRound = false;
    }


    /*----------- Setters and getters for the variables ----------*/

    /**
     * @return dices ArrayList of dices in the Round
     */
    public ArrayList<Dice> getDices() {
        return dices;
    }

    /**
     * @param dices ArrayList of dices in the Round
     */
    public void setDices(ArrayList<Dice> dices) {
        this.dices = dices;
    }

    /**
     * @return typeOfPoints the type of points to count this round
     */
    public String getTypeOfPoints() {
        return typeOfPoints;
    }

    /**
     * @param typeOfPoints type of points to count this round
     */
    public void setTypeOfPoints(String typeOfPoints) {
        this.typeOfPoints = typeOfPoints;
    }

    /**
     * @return points the result for this round
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the result for this round
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return finishedRound a boolean which shows if the round is finished
     */
    public boolean isFinishedRound() {
        return finishedRound;
    }

    /**
     * @param finishedRound a boolean which shows if the round is finished
     */
    public void setFinishedRound(boolean finishedRound) {
        this.finishedRound = finishedRound;
    }

    /**
     * @return startedRound a boolean which shows if the round is started
     */
    public boolean isStartedRound() {
        return startedRound;
    }

    /**
     * @param startedRound a boolean which shows if the round is started
     */
    public void setStartedRound(boolean startedRound) {
        this.startedRound = startedRound;
    }

    /*----------- These methods make the class parcelable ----------*/

    /**
     * If the parcelable class has child classes this is used then
     * @return an int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes data to a parcel
     * @param dest parcel to write to
     * @param flags is only used in certain situations
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.dices);
        dest.writeString(this.typeOfPoints);
        dest.writeInt(this.points);
        dest.writeInt(this.finishedRound ? 1 : 0);
        dest.writeInt(this.startedRound ? 1 : 0);
    }

    /**
     * Reads data from parcel
     * @param in parcel to read from
     */
    protected Round(Parcel in) {
        this.dices = new ArrayList<>();
        in.readList(this.dices, Dice.class.getClassLoader());
        this.typeOfPoints = in.readString();
        this.points = in.readInt();
        this.finishedRound = in.readInt() == 1;
        this.startedRound = in.readInt() == 1;
    }

    /**
     * Generates instances of the parcelable class from a parcel
     */
    public static final Parcelable.Creator<Round> CREATOR = new Parcelable.Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel source) {
            return new Round(source);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
