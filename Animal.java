import java.util.List;
import java.util.Random;


/**
 * A class representing shared characteristics of animals.
 * Some methods in this abstract class are abstract as well since they are never
 * directly used by the animal class.
 * 
 * Wolves are the Alpha predator in this scenerio that begin slow but with so many
 * rabbits and foxes it overtakes the map.
 * 
 * @author Erick Rubio
 * @version 2020.11.16 
 */
public abstract class Animal
{
    //age of animal
    private int age;
    //Random generator
    private static final Random rand = Randomizer.getRandom();
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        age = 0;
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    /**
     * Animal can breed if it has reached the breeding age.
     * @return true if the animal can breed
       */
    public boolean canBreed(){
        return age >= getBreedingAge();
    }
    
    /**
     * Return the breeding age of this animal
     * @return the breeding age of this animal
       */
     abstract protected int getBreedingAge();

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    public void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    
    }
    
    /**
     * Return the max age of this animal
     * @return the max age of this animal
       */
     abstract protected int getMaxAge();    
     
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    public int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    /**
     * Return the breeding probability
     * @return the breeding probability of this animal
       */
     abstract protected double getBreedingProbability(); 
    /**
     * Return the Max Litter Size
     * @return the Max Litter Size of this animal
       */
     abstract protected int getMaxLitterSize(); 
}
