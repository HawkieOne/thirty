package se.umu.hali0151.thirtygame;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;

/**
 * A class containing the information of one dice
 * @author id18hll
 * @version 1.0
 */
public class Dice implements Parcelable, Cloneable{
    private int number;
    private int backgroundIndex;
    private boolean diceFinished;
    private int originPosition;
    private ImageButton imageButton;


    /**
     * Constructor for Dice
     */
    public Dice(int number, boolean diceFinished, int backgroundIndex, ImageButton imageButton) {
        this.number = number;
        this.diceFinished = diceFinished;
        this.backgroundIndex = backgroundIndex;
        this.imageButton = imageButton;
        this.originPosition = number - 1;
    }

    /**
     * Constructor for Dice
     */
    public Dice(){
        this.number = 0;
        this.backgroundIndex = 0;
        this.diceFinished = false;
        this.imageButton = null;
        this.originPosition = 0;
    }

    /*----------- Setters and getters for the variables ----------*/

    /**
     * @return number the number of the dice
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number of the dice
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return diceFinished a boolean which shows if the dice is finished
     */
    public boolean isDiceFinished() {
        return diceFinished;
    }

    /**
     * @return originPosition number of the dice position before sorted
     */
    public int getOriginPosition() {
        return originPosition;
    }

    /**
     * @param originPosition number of the dice position before sorted
     */
    public void setOriginPosition(int originPosition) {
        this.originPosition = originPosition;
    }

    /**
     * @param diceFinished a boolean which shows if the dice is finished
     */
    public void setDiceFinished(boolean diceFinished) {
        this.diceFinished = diceFinished;
    }

    /**
     * @return backgroundIndex the index for the dice background
     */
    public int getBackgroundIndex() {
        return backgroundIndex;
    }

    /**
     * @param backgroundIndex the index for the dice background
     */
    public void setBackgroundIndex(int backgroundIndex) {
        this.backgroundIndex = backgroundIndex;
    }

    /**
     * @return imageButton connected to the dice
     */
    public ImageButton getImageButton() {
        return imageButton;
    }

    /**
     * @param imageButton connected to the dice
     */
    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    /**
     * Returns a deep copy of the instance
     * @return super.clone() a deep copy of the instance
     * @throws CloneNotSupportedException if cloning isn't possible
     */
    public Object clone()throws CloneNotSupportedException{
        return super.clone();
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
        dest.writeInt(this.number);
        dest.writeInt(this.backgroundIndex);
        dest.writeByte(this.diceFinished ? (byte) 1 : (byte) 0);
    }

    /**
     * Reads data from parcel
     * @param in parcel to read from
     */
    protected Dice(Parcel in) {
        this.number = in.readInt();
        this.backgroundIndex = in.readInt();
        this.diceFinished = in.readByte() != 0;
    }

    /**
     * Generates instances of the parcelable class from a parcel
     */
    public static final Parcelable.Creator<Dice> CREATOR = new Parcelable.Creator<Dice>() {
        @Override
        public Dice createFromParcel(Parcel source) {
            return new Dice(source);
        }

        @Override
        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };
}