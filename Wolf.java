import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Wolf.
 * Wolves age, move, eat rabbits/foxes, and die.
 * This is an Alpha predator. The specieis starts slow but overtime consumes the fox and
 * is ontop of the food chain.
 *
 * @author Erick Rubio
 * @version 2020.11.16
 */
public class Wolf extends Animal
{
    // Characteristics shared by all wolves (class variables).
    
    // The age at which a Wolf can start to breed.
    private static final int BREEDING_AGE = 20;
    // The age to which a Wolf can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a Wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.015;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a Wolf can go before it has to eat again.
    private static final int WOLF_FOOD_VALUE = 11;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The Wolf's age.
    private int age;
    // The Wolf's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a Wolf. A Wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(WOLF_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = WOLF_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the Wolf does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newWolfes A list to return newly born Wolfes.
     */
    public void act(List<Animal> newWolfes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newWolfes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Make this Wolf more hungry. This could result in the Wolf's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit ) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = WOLF_FOOD_VALUE;
                    return where;
                }
            }
            if(animal instanceof Fox ) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = WOLF_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this Wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newWolfes A list to return newly born Wolfes.
     */
    private void giveBirth(List<Animal> newWolfes)
    {
        // New Wolfes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, field, loc);
            newWolfes.add(young);
        }
    }
        
    /**
     * Use the field here to determine the age of the Wolf to breed
     * @Return the age of a Wolf can start breeding
       */
    public int getBreedingAge(){
        return BREEDING_AGE;
    }
    /**
     * Use the field here to determine the max age of the Wolf
     * @Return the max age of a Wolf can start breeding
       */
    public int getMaxAge(){
        return MAX_AGE;
    }
    
    /**
     * Use the field here to determine the breeding probability of the Wolf
     * @Return the breeding probability of a Wolf can start breeding
       */
    public double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    /**
     * Use the field here to determine the max litter of the Wolf
     * @Return the max litter of a Wolf can start breeding
       */
    public int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
}
