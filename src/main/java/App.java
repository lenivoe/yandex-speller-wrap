import speller.Speller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("input empty line to exit");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            var speller = new Speller();
            while (true) {
                var line = reader.readLine();
                if (line.length() == 0) {
                    break;
                }
                var errors = speller.checkText(line);
                System.out.println(Arrays.toString(errors));
            }
        }
    }
}
