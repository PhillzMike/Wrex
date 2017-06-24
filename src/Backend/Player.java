/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import java.util.Objects;


/**
 *
 * @author Phillz Mike
 */
public class Player {
    private final Hand hand;
    private boolean turn;
    private final  Profile profile;
    private final int id;
    private int rating;
    /**
     * Creates a new instance of a player
     * @param profile the profile of the player to be created
     * @param id a string representation the player to be created
     */
    public Player(Profile profile, int id){
        this.profile = profile;
        this.id = id;
        hand = new Hand();
    }
    /**
     * 
     * @return the name of this player
     */
    public String getName(){
        return profile.getName();
    }
    /**
     * 
     * @return true if it is this player's turn to play and false if otherwise
     */
    public boolean isTurn(){
        return turn;
    }
    /**
     * sets the turn of this player to true
     */
    public void setTurn(){
        turn  = true;
    }
    /**
     * 
     * @param card the card to be played by the player
     * @return true if the player has the card and false if otherwise
     */
    public boolean playCard(Card card){
        if(hand.playCard(card)){
            turn = false;
            return true;
        }
        return false;
        
    }
    /**
     * 
     * @return the hand of this player 
     */
    public Hand getHand(){
        return hand;
    }
    /**
     * 
     * @return the string id of this player
     */
    public int getId() {
        return id;
    }
    /**
     * Compares 2 players
     * @param b an instance player
     * @return false if compared with an instance that is not player, if otherwise returns true if both instances are the same and false if otherwise
     * 
     */
    @Override
    public boolean equals(Object b){
        if(b instanceof Player)
            return ((Player) b).profile.equals(this.profile) && ((Player) b).rating==this.rating;
        return false;
    }
    /**
     * 
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.profile);
        hash = 59 * hash + this.rating;
        return hash;
    }
    /**
     * Sets the rating of this player
     * @param players an array of all the players that played the game
     */
    public void setRating(Player[] players){
        int[] diff= {-2000,-1000,-500,-300,-100,-50,-10,0,10,50,100,300,500,1000,2000};
        int[] winRatings = {0,0,1,5,10,20,30,35,50,70,100,150,175,195,200};
        int[] loseRatings = {1,3,7,12,25,50,70,85,90,110,125,170,195,220,250};
        int position = 0;
        for(int i = 0;i<players.length;i++){
            if(this.equals(players[i]))
                position = i;
            break;
        }
        double ratingsAdded = 0;
        for(int i = 0;i<players.length;i++){
            if(position==i)
                break;
            for(int j=0;j<diff.length-1;j++){
                if(diff[j]<players[i].profile.getRating() && 
                        diff[j+1] > players[i].profile.getRating()){
                    if(position < i)
                        ratingsAdded += interpolate(diff[j],diff[j+1],winRatings[j]
                            ,winRatings[j+1],players[i].profile.getRating());
                    else
                        ratingsAdded -= interpolate(diff[j],diff[j+1],loseRatings[j]
                        , loseRatings[j+1],players[i].profile.getRating());


                }
            }  
        }
        ratingsAdded /= players.length-1;
        this.profile.addRating((position==0) ? 1.5*ratingsAdded:ratingsAdded);
        
    }
    private double interpolate(int low,int high, int lowScore, int highScore, double find){
        return (highScore-lowScore)*(find-low)/(high-low) + lowScore;
    }
    //TODO statistics
}
