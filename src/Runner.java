import java.util.stream.IntStream;

public class Runner {

    public static void main(String[] args) {
        // Change level here
        var level = new Level1();

        IntStream.range(1, 6).forEach(sublevel -> {
            try {
                var input = level.read(sublevel);
                var output = level.solve(input);
                level.write(output, sublevel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
