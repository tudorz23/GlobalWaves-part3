package commands.statsCommands.personalStats.wrappedStrategy;

public interface IWrappedStrategy {
    /**
     * Applies a particular wrapped generating algorithm.
     * To be implemented by specialized classes for BasicUser, Artist and Host.
     */
    void wrapped();
}
