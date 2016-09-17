# burlap_ale

A BURLAP library extension for interacting with the Arcade Learning Environment (ALE) by creating a BURLAP `Environment` that transfers `Action` and `State` information through FIFO pipes to and from ALE.

## Dependencies

ALE is not included with this library extension and will need to be installed separately.

The current version of ALE does not give the number of lives data over the PIPE interface.
So, if you want the ALEEnvironment to create a terminal state after the end of each life (as is done in Deepmind's DQN results),
you will need to clone [our fork of ALE](https://github.com/h2r/Arcade-Learning-Environment).

If you don't need this feature, vanilla ALE found on the [ALE downloads page](http://www.arcadelearningenvironment.org/downloads/) or on their [GitHub repo](https://github.com/mgbellemare/Arcade-Learning-Environment).

Clone the code to your machine and follow the compilation code on their GitHub page (copied here):

Install main dependencies:
```
sudo apt-get install libsdl1.2-dev libsdl-gfx1.2-dev libsdl-image1.2-dev cmake
```

Compilation:

```
mkdir build && cd build
cmake -DUSE_SDL=ON -DUSE_RLGLUE=OFF -DBUILD_EXAMPLES=ON ..
make -j 4
```

## Compiling

Compiling and management is performed with Maven. 

Install the jar into your local repository with

```
mvn install
```

Link to burlap_ale from a project by adding the following to the `<dependencies>` section of your project's pom.xml file.

```
<dependency>
  <groupId>edu.brown.cs.burlap</groupId>
  <artifactId>burlap_ale</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```


## Example code
We provide two sets of example code, one for running a human agent and one for running a random agent.

### Example 1: Human Agent
```
public class HumanAgent {
    public static void main(String[] args) {
        String alePath = "/path/to/ale/executable";
        String romPath = "/path/to/atari/2600/rom/file";

        // Create the ALE domain
        ALEDomainGenerator domGen = new ALEDomainGenerator();
        SADomain domain = domGen.generateDomain();

        // Create the environment
        ALEEnvironment env = new ALEEnvironment(alePath, romPath);

        // Create and initialize the visualizer with keyboard input (human=true)
        ALEVisualExplorer exp = new ALEVisualExplorer(domain, env, ALEVisualizer.create(), true);
        exp.initGUI();
    }
}
```

### Example 2: Random Agent
```
public class RandomAgent {
    public static void main(String[] args) {
        String alePath = "/path/to/ale/executable";
        String romPath = "/path/to/atari/2600/rom/file";

        // Create the ALE domain
        ALEDomainGenerator domGen = new ALEDomainGenerator();
        SADomain domain = domGen.generateDomain();

        // Create the environment
        ALEEnvironment env = new ALEEnvironment(alePath, romPath, 4, PoolingMethod.POOLING_METHOD_MAX);

        // Create and initialize the visualizer
        ALEVisualExplorer exp = new ALEVisualExplorer(domain, env, ALEVisualizer.create());
        exp.initGUI();

        // Refresh the visualizer at 60Hz
        exp.startLiveStatePolling(1000/60);

        // Initialize the policy
        Policy p = new RandomPolicy(domain);

        // Run the policy indefinitely
        while(true) {
            PolicyUtils.rollout(p, env);
            env.resetEnvironment();
        }
    }
}
```
