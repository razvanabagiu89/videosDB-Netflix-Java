package myinput;

import fileio.*;

import java.util.ArrayList;
import java.util.List;

public class MyInput {

    private List<ActorInputData> actorsData;
    private List<UserInputData> usersData;
    private List<ActionInputData> commandsData;

    private List<MovieInputData> moviesData;
    private List<SerialInputData> serialsData;
    /**
     * used for manipulating the input
     */
    public MyInput(final List<ActorInputData> actors, final List<UserInputData> users,
                   final List<ActionInputData> commands,
                   final List<MovieInputData> movies,
                   final List<SerialInputData> serials) {
        this.actorsData = new ArrayList<>(actors);
        this.usersData = new ArrayList<>(users);
        this.commandsData = new ArrayList<>(commands);
        this.moviesData = new ArrayList<>(movies);
        this.serialsData = new ArrayList<>(serials);
    }
    /**
     * getActors
     */
    public List<ActorInputData> getActors() {
        return actorsData;
    }
    /**
     * setActors
     */
    public void setActors(final List<ActorInputData> actorsData1) {
        this.actorsData = actorsData1;
    }
    /**
     * getUsers
     */
    public List<UserInputData> getUsers() {
        return usersData;
    }
    /**
     * setUsers
     */
    public void setUsers(final List<UserInputData> usersData1) {
        this.usersData = usersData1;
    }
    /**
     * getCommands
     */
    public List<ActionInputData> getCommands() {
        return commandsData;
    }
    /**
     * setCommands
     */
    public void setCommands(final List<ActionInputData> commandsData1) {
        this.commandsData = commandsData1;
    }
    /**
     * removeCommand
     */
    public void removeCommand(final ActionInputData command) {
        this.commandsData.remove(command);
    }
    /**
     * getMovies
     */
    public List<MovieInputData> getMovies() {
        return moviesData;
    }
    /**
     * setMovies
     */
    public void setMovies(final List<MovieInputData> moviesData1) {
        this.moviesData = moviesData1;
    }
    /**
     * getSerials
     */
    public List<SerialInputData> getSerials() {
        return serialsData;
    }
    /**
     * setSerials
     */
    public void setSerials(final List<SerialInputData> serialsData1) {
        this.serialsData = serialsData1;
    }
}
