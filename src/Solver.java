import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public abstract class Solver {

    private int level;

    // /////////////////////////////////////////////////////////////////////////
    // Init
    // /////////////////////////////////////////////////////////////////////////

    Solver(int level) {
        this.level = level;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public List<String> read(int sublevel) throws IOException {
        // resources/Level?/in/level?_?.in
        String path = String.format("resources/Level%d/in/level%d_%d.in", level, level, sublevel);
        return Files.readAllLines(Paths.get(path));
    }

    protected abstract List<String> solve(List<String> input);

    public void write(List<String> output, int sublevel) throws IOException {
        // resources/Level?/out/level?_?.out
        // out folder has to be created manually!
        String path = String.format("resources/Level%d/out/level%d_%d.out", level, level, sublevel);
        Files.write(Paths.get(path), output, StandardOpenOption.CREATE);
    }
}
